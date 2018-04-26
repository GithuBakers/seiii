package cn.edu.nju.tagmakers.countsnju.logic.service;

import cn.edu.nju.tagmakers.countsnju.algorithm.ResultJudger;
import cn.edu.nju.tagmakers.countsnju.data.controller.WorkerAndCriterionController;
import cn.edu.nju.tagmakers.countsnju.data.controller.WorkerController;
import cn.edu.nju.tagmakers.countsnju.entity.Criterion.Criterion;
import cn.edu.nju.tagmakers.countsnju.entity.Criterion.Result;
import cn.edu.nju.tagmakers.countsnju.entity.Criterion.WorkerAndCriterion;
import cn.edu.nju.tagmakers.countsnju.entity.Task;
import cn.edu.nju.tagmakers.countsnju.entity.pic.*;
import cn.edu.nju.tagmakers.countsnju.entity.user.Worker;
import cn.edu.nju.tagmakers.countsnju.entity.vo.*;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import cn.edu.nju.tagmakers.countsnju.exception.NotFoundException;
import cn.edu.nju.tagmakers.countsnju.exception.PermissionDeniedException;
import cn.edu.nju.tagmakers.countsnju.filter.TaskFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import util.SecurityUtility;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:
 * 众包工人的逻辑对象
 *
 * @author xxz
 * Created on 04/06/2018
 *
 * @update wym
 * 增加WorkerAndCriterionController和标准集的相关方法
 * Updated on 4/22
 */
@Component
public class WorkerService {

    private TaskService taskService;

    private WorkerController workerController;

    private WorkerAndCriterionController workerAndCriterionController;

    private TagService tagService;

    private CriterionService criterionService;

    @Autowired
    public WorkerService(TaskService taskService, WorkerController workerController,
                         TagService tagService, CriterionService criterionService, WorkerAndCriterionController workerAndCriterionController) {
        this.taskService = taskService;
        this.workerController = workerController;
        this.tagService = tagService;
        this.criterionService = criterionService;
        this.workerAndCriterionController = workerAndCriterionController;
    }


    /**
     * 按ID查找个人信息
     */
    public Worker findWorkerByName(String workerName) {
        return workerController.findByID(workerName);
    }

    /**
     * 更新个人信息
     */
    public boolean update(Worker worker) {
        workerController.update(worker);
        return true;
    }

    /**
     * 工人查看的任务列表(还未结束 && 还未接受
     *
     * @param taskFilter 筛选条件
     */
    public List<WorkerTaskVO> findWorkerTask(TaskFilter taskFilter, String workerName) {

        return taskService.findTask(taskFilter).stream()
                .filter(task -> {
                    Worker worker = workerController.findByID(workerName);
                    //还未接受任务
                    return !worker.getTaskIDs().contains(task.getPrimeKey());

                })
                .map(WorkerTaskVO::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取某一任务的详情
     *
     * @param taskID 任务名
     */
    public WorkerTaskDetailVO getTaskDetail(String taskID, String workerName) {
        Task detail = taskService.findByID(taskID);
        //检查是否可接受此任务
        if (check(workerName, detail)) {
            return new WorkerTaskDetailVO(detail,workerName);
        } else {
            throw new PermissionDeniedException("你暂时无法接受此任务");
        }
    }

    /**
     * 接受某一任务
     * <p>
     * 查看工人是否有权限加入此任务
     * <p>
     * 将任务加入工人的任务列表
     * <p>
     * 在任务的userMarked中添加此工人
     *
     * @param workerName 参与者名
     * @param taskID     接受的任务名
     */
    public boolean receiveTask(String taskID, String workerName) {
        Task toReceive = taskService.findByID(taskID);
        if (toReceive == null) {
            throw new NotFoundException("没有此任务");
        }
        //查看是否可接受此任务
        if (check(workerName, toReceive)) {

            //将任务加入工人的任务列表, 并记录下其添加时间
            Worker receiver = workerController.findByID(workerName);
            if (receiver == null) {
                throw new NotFoundException("没有此任务");
            }
            List<String> taskList = receiver.getTaskIDs();
            if (!taskList.contains(taskID)) {
                taskList.add(taskID);
                receiver.setTaskIDs(taskList);
                Map<String, Long> receiveTime = receiver.getReceivedTime();
                receiveTime.put(taskID, new Date().getTime());
                receiver.setReceivedTime(receiveTime);
            } else {
                throw new PermissionDeniedException("重复接受某一任务");
            }

//            Worker worker = workerController.findByID(workerName);
            workerController.update(receiver);

            //在任务的userMarked中添加此工人
            toReceive.getUserMarked().putIfAbsent(workerName, 0);
            taskService.updateTask(toReceive);

            return true;
        }
        throw new PermissionDeniedException("你已经达到此任务上限");
    }

    private boolean check(String workerName, Task toReceive) {
        return toReceive.getUserMarked().get(workerName) == null
                || toReceive.getUserMarked().get(workerName) < toReceive.getLimit();
    }


    /**
     * 返回工人需要完成的图片
     *
     * @param taskID     任务名
     * @param workerName 工人名
     */
    public List<Bare> getBares(String taskID, String workerName) {
        return calculateBares(taskService.findByID(taskID),
                workerController.findByID(workerName),
                10);
    }


    /**
     * 工人提交一个标注
     * <p>
     * 在任务的图片列表中加入此图片，并将标注次数加一
     * <p>
     * 将任务列表中工人的标注次数加一
     * <p>
     * 判断是否要增加奖励
     * <p>
     * 将工人的图片列表中加入此图片
     * <p>
     * 为标记加上工人Name
     * <p>
     * 将标记加入数据层
     *
     * @param image      图片
     * @param taskID     任务名
     * @param workerName 工人名
     */
    public boolean submitTag(Image image, String taskID, String workerName) {
        Task toUpdate = taskService.findByID(taskID);
        Worker worker = workerController.findByID(workerName);
        //在任务的图片列表中将标注次数加一
        //如果缺失，则这是第一次标注
        updateBareMarked(image, toUpdate);

        //将任务列表中工人的标注次数加一
        //如果缺失，则这是第一个标注
        updateUserMarked(image, workerName, toUpdate, worker);

        taskService.updateTask(toUpdate);
        workerController.update(worker);

        List<Tag> tagList = image.getTags();
        tagList.parallelStream().forEach(tag -> tag.setWorkerID(workerName));
        tagList.parallelStream().forEach(tagService::addTag);
        return true;
    }

    private void updateUserMarked(Image image, String workerName, Task toUpdate, Worker worker) {
        Map<String, Integer> userMarked = toUpdate.getUserMarked();
        userMarked.putIfAbsent(workerName, 0);
        //否则增加1
        int tmp1 = userMarked.get(workerName);
        tmp1++;
        userMarked.put(workerName, tmp1);
        //是否要获得40%的奖励
        if (tmp1 % 10 == 0) {
            int reward = worker.getCredit();
            reward += Math.round(toUpdate.getReward() * 0.4);
            worker.setCredit(reward);
        }
        //将工人的图片列表中加入此图片
        List<String> bareIDs = worker.getBareIDs();
        bareIDs.add(image.getBare().getId());
        worker.setBareIDs(bareIDs);
        toUpdate.setUserMarked(userMarked);
    }

    private void updateBareMarked(Image image, Task toUpdate) {
        Map<String, Integer> bareMarked = toUpdate.getBareMarked();
        bareMarked.putIfAbsent(image.getBare().getId(), 0);
        int tmp = bareMarked.get(image.getBare().getId());
        tmp++;
        if (tmp > toUpdate.getLimit()) {
            throw new PermissionDeniedException("你已经做了足够多了！换个任务试一试吧");
        }
        bareMarked.put(image.getBare().getId(), tmp);
        toUpdate.setBareMarked(bareMarked);
    }

    public List<WorkerReceivedTaskVO> getReceivedTasks(String workerName) {
        Worker worker = workerController.findByID(workerName);
        return workerController.findByID(workerName).getTaskIDs().parallelStream()
                .map(taskService::findByID)
                .map(task -> new WorkerReceivedTaskVO(task, worker))
                .collect(Collectors.toList());
    }


    public WorkerReceivedTaskDetailVO getReceivedTaskDetails(String taskID, String workerName) {
        Task task = taskService.findByID(taskID);
        Worker worker = workerController.findByID(workerName);
        if (!worker.getTaskIDs().contains(taskID)) {
            throw new PermissionDeniedException("这不是你的任务！");
        }
        return new WorkerReceivedTaskDetailVO(task, worker);
    }

    /**
     * 获取推荐的任务
     * 确保是工人可以接受的任务
     *
     * @param type       种类
     * @param workerName 工人名
     */
    public WorkerTaskDetailVO getRecommendTask(MarkType type, String workerName) {
        TaskFilter filter = new TaskFilter();
        filter.setMarkType(type);
        filter.setFinished(false);
        List<Task> taskList = taskService.findTask(filter);
        for (Task task : taskList) {
            if (!task.getUserMarked().keySet().contains(workerName)) {
                return new WorkerTaskDetailVO(task, workerName);
            }
        }
        throw new NotFoundException("暂时没有推荐给你的任务喔");
    }

    /**
     * 工人查看所有的标准集
     *
     * @return 所有的标准集
     */
    public List<WorkerCriterionVO> getAllCriterion() {
        List<Criterion> criterionList = criterionService.find(null);
        List<WorkerCriterionVO> ret = new ArrayList<>();
        for (Criterion temp : criterionList) {
            String criterionID = temp.getCriterionID();
            String workerID = SecurityUtility.getUserName(SecurityContextHolder.getContext());
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
    public List<Bare> getCriterionBares(String criterionID) {
        String workerID = SecurityUtility.getUserName(SecurityContextHolder.getContext());
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
        for (Bare bare : latestBares) {
            for (Result result : resultList) {
                if (result.getBare().equals(bare)) {
                    if (result.isHasTested()) latestBares.remove(bare);
                }
            }
        }
        return latestBares;
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
        trig(workerAndCriterion, ret);
        return ret;
    }

    /**
     * 由于返回了固定数量的要标注的图片
     * 更新用户的最新做的固定张数图片
     * 更新用户的上一次的正确率
     */
    private void trig(WorkerAndCriterion workerAndCriterion, List<Bare> bareList) {
        List<Result> resultList = workerAndCriterion.getResults();
        List<Bare> latestBares = workerAndCriterion.getLatestBares();
        List<Double> accuracy = workerAndCriterion.getAccuracy();
        //更新正确率
        if (latestBares.size() > 0) {
            int totalPassed = 0;
            for (Bare bare : latestBares) {
                for (Result result : resultList) {
                    if (result.getBare().equals(bare)) {
                        totalPassed += result.isCorrect() ? 1 : 0;
                    }
                    break;
                }
            }
            accuracy.add((double) totalPassed / latestBares.size());
        }
        //更新最后在做的固定张数的图片
        latestBares.addAll(bareList);
        //设置回去
        workerAndCriterion.setLatestBares(latestBares);
        workerAndCriterion.setAccuracy(accuracy);
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
    public CriterionImageAnswerVO submitCriterionResult(String criterionID, Image image) {
        boolean isCorrect = judgeCorrect(criterionID, image);
        String bareID = image.getBare().getId();
        String workerID = SecurityUtility.getUserName(SecurityContextHolder.getContext());
        WorkerAndCriterion workerAndCriterion = workerAndCriterionController.findByID(workerID, criterionID);
        List<Result> resultList = workerAndCriterion.getResults();
        for (Result result : resultList) {
            if (result.getBare().getId().equals(bareID)) {
                result.setHasTested(true);
                result.setCorrect(isCorrect);
                result.setSubmitTime(Calendar.getInstance());
            }
        }
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

    private Image getAnswer(String criterionID, Image image) {
        String bareID = image.getBare().getId();
        Criterion criterion = criterionService.findByID(criterionID);
        Image ret = image.copy();
        List<Tag> answer = criterion.getResult().get(bareID);
        ret.setTags(answer);
        return ret;
    }

    /**
     * 计算工人需要完成的图片
     * 与工人已有的不重复，并且返回的列表不重复
     *
     * @param task   任务
     * @param worker 工人
     * @param number 获取的图片数量上限
     */
    private List<Bare> calculateBares(Task task, Worker worker, int number) {
        List<Bare> ret = new LinkedList<>();
        //"随机算法"
        for (int i = 0; i < number; i++) {
            int random = Math.abs(new Random(System.currentTimeMillis()).nextInt());
            random += i;
            random = random % task.getDataSet().size();
            Bare bare = task.getDataSet().get(random);
            if (!ret.contains(bare) && !worker.getBareIDs().contains(bare.getId())) {
                ret.add(bare);
            }
        }
        return ret;
    }


}
