package cn.edu.nju.tagmakers.countsnju.logic.service;

import cn.edu.nju.tagmakers.countsnju.algorithm.ResultJudger;
import cn.edu.nju.tagmakers.countsnju.data.controller.CriterionController;
import cn.edu.nju.tagmakers.countsnju.data.controller.WorkerAndCriterionController;
import cn.edu.nju.tagmakers.countsnju.data.controller.WorkerController;
import cn.edu.nju.tagmakers.countsnju.entity.Criterion.Criterion;
import cn.edu.nju.tagmakers.countsnju.entity.Criterion.Result;
import cn.edu.nju.tagmakers.countsnju.entity.Criterion.WorkerAndCriterion;
import cn.edu.nju.tagmakers.countsnju.entity.pic.*;
import cn.edu.nju.tagmakers.countsnju.entity.user.Worker;
import cn.edu.nju.tagmakers.countsnju.entity.vo.CriterionImageAnswerVO;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerCriterionVO;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import cn.edu.nju.tagmakers.countsnju.filter.CriterionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description:
 * 工人与标准集服务的类（从原来工人的Service中抽取了出来）
 *
 * @author wym
 * Created on 4.26
 * <p>
 * Update:
 * @author wym
 * Last modified on
 * <p>
 * Update:
 * 工人获取标准集的时候 应该只获取发起者做完的部分
 * @author xxz
 * Created on 04/27/2018
 */
@Component
public class WorkerCriterionService {
    private CriterionService criterionService;

    private WorkerAndCriterionController workerAndCriterionController;

    private CriterionController criterionController;

    private WorkerController workerController;

    @Autowired
    public WorkerCriterionService(CriterionService criterionService, WorkerAndCriterionController workerAndCriterionController,
                                  CriterionController criterionController, WorkerController workerController) {
        this.criterionService = criterionService;
        this.workerAndCriterionController = workerAndCriterionController;
        this.criterionController = criterionController;
        this.workerController = workerController;
    }

    /**
     * 工人查看所有的标准集
     * <p>
     * 这些标准集只包括发起者提供完整答案的部分
     *
     * @return 所有的标准集
     */
    public List<WorkerCriterionVO> getAllCriterion(String workerID) {
        CriterionFilter filter = new CriterionFilter();
        filter.setFinished(true);
        List<Criterion> criterionList = criterionService.find(filter);
        List<WorkerCriterionVO> ret = new ArrayList<>();
        for (Criterion temp : criterionList) {
            String criterionID = temp.getCriterionID();
            Boolean passed = criterionService.isPassed(criterionID, workerID);
            WorkerCriterionVO vo = new WorkerCriterionVO(temp, passed);
            ret.add(vo);
        }
        return ret;
    }

    /**
     * 工人一次性获取十张标准集图片
     *
     * @param criterionID 标准集ID
     * @return 标准集图片
     */
    public List<Bare> getCriterionBares(String criterionID, String workerID) {
        //用户一次获得的图片张数,这里是防止硬编码的问题
        Criterion criterion = criterionService.findByID(criterionID);
        int numOfPerGet = criterion.getNumOfPerGet();
        if (!workerAndCriterionController.existed(workerID, criterionID)) {
            //这个工人没有做过这个标准集,先添加，再查上来
            workerAndCriterionController.add(workerID, criterion);
        }
        WorkerAndCriterion workerAndCriterion = workerAndCriterionController.findByID(workerID, criterionID);
        //上次如果有没做完的就继续做，否则返回新的并触发影响
        List<Bare> BaresRemain = countRemain(workerAndCriterion);
        if (BaresRemain.size() == 0) {
            return selectBares(workerAndCriterion, numOfPerGet);
        } else {
            return BaresRemain;
        }
    }

    /**
     * 计算上次是否完成全部
     */
    private List<Bare> countRemain(WorkerAndCriterion workerAndCriterion) {
        List<Result> resultList = workerAndCriterion.getResults();
        List<Bare> latestBares = workerAndCriterion.getLatestBares();
        List<Bare> remainBares = new ArrayList<>(latestBares);

        //在迭代的时候删除会出现问题，因此先记录要删除的内容
        List<Bare> toDelete = new ArrayList<>();
        for (Bare bare : latestBares) {
            for (Result result : resultList) {
                if (result.getBare().equals(bare)) {
                    //这里判断的是当前若干张图片里面的这张图片有没有被标注过
                    if (result.isFinishedInCurrentTest()) toDelete.add(bare);
                }
            }
        }
        for (Bare delete : toDelete) {
            remainBares.remove(delete);
        }
        return remainBares;
    }

    /**
     * 在进入这个方法之前应该要先判断用户上次的十张图片有没有做完
     * 优先选择没有做过的图片，然后是做错了的图片，最后是正确的图片
     */
    private List<Bare> selectBares(WorkerAndCriterion workerAndCriterion, int numOfPerGet) {
        List<Result> resultList = workerAndCriterion.getResults();
        List<Bare> ret = new ArrayList<>();
        int[] nums = countNums(resultList);
        int notTested = nums[0];
        int wrong = nums[1];
        int passed = nums[2];
        if (notTested + wrong + passed < numOfPerGet) throw new InvalidInputException("获取标准集图片的时候数量不够");
        if (notTested >= numOfPerGet) {
            List<Result> notTestList = resultList.stream()
                    .filter(result -> !result.isHasTested())
                    .collect(Collectors.toList());
            for (int i = 0; i < numOfPerGet; i++) {
                ret.add(notTestList.get(i).getBare());
            }
        } else {
            ret.addAll(resultList.stream()
                    .filter(result -> !result.isHasTested())
                    .map(Result::getBare)
                    .collect(Collectors.toList()));
            //把需要的图片减掉未测试图片数
            numOfPerGet -= notTested;
            if (wrong >= numOfPerGet) {
                List<Result> wrongList = resultList.stream()
                        .filter(Result::isHasTested)
                        .filter(result -> !result.isCorrect())
                        .collect(Collectors.toList());
                for (int i = 0; i < numOfPerGet; i++) {
                    ret.add(wrongList.get(i).getBare());
                }
            } else {
                ret.addAll(resultList.stream()
                        .filter(Result::isHasTested)
                        .filter(result -> !result.isCorrect())
                        .map(Result::getBare)
                        .collect(Collectors.toList()));
                //把需要的图片减掉错误图片数
                numOfPerGet -= wrong;
                List<Result> passedList = resultList.stream()
                        .filter(Result::isCorrect)
                        .collect(Collectors.toList());
                for (int i = 0; i < numOfPerGet; i++) {
                    ret.add(passedList.get(i).getBare());
                }
            }
        }
        //返回新的要标注的图片需要触发一系列的更改
        trig(workerAndCriterion, ret);
        return ret;
    }

    /**
     * 由于返回了固定数量的要标注的图片必须把返回的图片都标记成没有被标注过
     * 更新用户的最新做的固定张数图片
     * 更新用户的上一次的正确率
     */
    private void trig(WorkerAndCriterion workerAndCriterion, List<Bare> bareList) {
        List<Result> resultList = workerAndCriterion.getResults();
        List<Bare> latestBares = workerAndCriterion.getLatestBares();
        List<Double> accuracy = workerAndCriterion.getAccuracy();
        //更新正确率并把是否在当前测试中完成的字段设置为false以便下一次的重复调用
        if (latestBares.size() > 0) {
            int totalPassed = 0;
            for (Bare bare : latestBares) {
                for (Result result : resultList) {
                    if (result.getBare().equals(bare)) {
                        totalPassed += result.isCorrect() ? 1 : 0;
                        //修改是否在当前若干张测试中完成的状态
                        result.setFinishedInCurrentTest(false);
                        break;
                    }
                }
            }
            accuracy.add((double) totalPassed / latestBares.size());
        }
        //清除之前的图片，更新最后在做的固定张数的图片
        latestBares = new ArrayList<>();
        latestBares.addAll(bareList);
        //设置回去
        workerAndCriterion.setLatestBares(latestBares);
        workerAndCriterion.setAccuracy(accuracy);
        workerAndCriterionController.update(workerAndCriterion);
    }

    /**
     * 依次储存没有做过的图片数，做错了的图片数，正确的图片数
     *
     * @return
     */
    private int[] countNums(List<Result> resultList) {
        int[] nums = new int[3];
        int notTested = 0;
        int wrong = 0;
        int passed = 0;
        for (Result temp : resultList) {
            if (!temp.isHasTested()) {
                notTested++;
            } else {
                if (!temp.isCorrect()) wrong++;
                else passed++;
            }
        }
        nums[0] = notTested;
        nums[1] = wrong;
        nums[2] = passed;
        return nums;
    }

    /**
     * 修改对应图片的correct和hasTested，设置提交时间
     */
    public CriterionImageAnswerVO submitCriterionResult(String criterionID, Image image, String workerID) {
        boolean isCorrect = judgeCorrect(criterionID, image);
        String bareID = image.getBare().getId();
        WorkerAndCriterion workerAndCriterion = workerAndCriterionController.findByID(workerID, criterionID);
        List<Result> resultList = workerAndCriterion.getResults();
        for (Result result : resultList) {
            if (result.getBare().getId().equals(bareID)) {
                //这个值只设置一次别的地方不会再改成false
                result.setHasTested(true);
                result.setCorrect(isCorrect);
                result.setSubmitTime(Calendar.getInstance());
                //这个值是用于判断在当前若干张里面是否完成，可能被设置成false和true
                result.setFinishedInCurrentTest(true);
                break;
            }
        }
        //更新
        workerAndCriterion.setResults(resultList);
        workerAndCriterionController.update(workerAndCriterion);

        //判断是否通过
        int aim = workerAndCriterion.getAim();
        boolean pass = judgePass(resultList, aim);
        if (pass) {
            //标准集中加入通过标准的工人
            Criterion target = criterionController.findByID(criterionID);
            Set<String> workerPassed = target.getWorkerPassed();
            workerPassed.add(workerID);
            target.setWorkerPassed(workerPassed);
            criterionController.update(target);

            //关系类中设置pass
            workerAndCriterion.setPassed(true);
            workerAndCriterionController.update(workerAndCriterion);

            //工人自身字段里面添加通过的标准集
            Worker temp = workerController.findByID(workerID);
            List<Criterion> passedCriterion = temp.getDependencies();
            passedCriterion.add(target);
            temp.setDependencies(passedCriterion);
            workerController.update(temp);
        }

        //无论正确与否都返回正确答案
        Image answer = getAnswer(criterionID, image);
        return new CriterionImageAnswerVO(answer, isCorrect);
    }

    /**
     * 调用算法判断计算提交结果是否正确
     */
    private boolean judgeCorrect(String criterionID, Image image) {
        Criterion criterion = criterionService.findByID(criterionID);
        String bareID = image.getBare().getId();
        MarkType type = image.getType();
        List<Tag> resultOfWorker = image.getTags();
        List<Tag> answers = criterion.getResult().get(bareID);
        switch (type) {
            case DESC:
                Description descFromWorker = (Description) resultOfWorker.get(0).getMark();
                Description descFromAnswer = (Description) answers.get(0).getMark();
                return ResultJudger.judgeDesc(descFromAnswer, descFromWorker);
            case EDGE:
                List<Edge> edgeFromWorker = resultOfWorker.stream()
                        .map(tag -> (Edge) tag.getMark())
                        .collect(Collectors.toList());
                List<Edge> edgeFromAnswer = answers.stream()
                        .map(tag -> (Edge) tag.getMark())
                        .collect(Collectors.toList());
                return ResultJudger.judgeEdge(edgeFromAnswer, edgeFromWorker);
            case RECT:
                List<Rect> rectFromWorker = resultOfWorker.stream()
                        .map(tag -> (Rect) tag.getMark())
                        .collect(Collectors.toList());
                List<Rect> rectFromAnswer = answers.stream()
                        .map(tag -> (Rect) tag.getMark())
                        .collect(Collectors.toList());
                return ResultJudger.judgeRect(rectFromAnswer, rectFromWorker);
            case DEFAULT:
                return false;
        }
        return false;
    }

    /**
     * 获取图片答案
     */
    private Image getAnswer(String criterionID, Image image) {
        String bareID = image.getBare().getId();
        Criterion criterion = criterionService.findByID(criterionID);
        Image ret = image.copy();
        List<Tag> answer = criterion.getResult().get(bareID);
        ret.setTags(answer);
        return ret;
    }

    /**
     * 判断是否已经通过了该标准集
     */
    private boolean judgePass(List<Result> resultList, int aim) {
        int totalCorrect = 0;
        for (Result temp : resultList) {
            if (temp.isCorrect()) {
                totalCorrect++;
            }
        }
        if (totalCorrect >= aim) {
            return true;
        }
        return false;
    }
}
