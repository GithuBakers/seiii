package cn.edu.nju.tagmakers.countsnju.logic.service;

import cn.edu.nju.tagmakers.countsnju.data.controller.WorkerAndCriterionController;
import cn.edu.nju.tagmakers.countsnju.data.controller.WorkerController;
import cn.edu.nju.tagmakers.countsnju.entity.Criterion;
import cn.edu.nju.tagmakers.countsnju.entity.Task;
import cn.edu.nju.tagmakers.countsnju.entity.WorkerAndCriterion;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Image;
import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Tag;
import cn.edu.nju.tagmakers.countsnju.entity.user.Worker;
import cn.edu.nju.tagmakers.countsnju.entity.vo.*;
import cn.edu.nju.tagmakers.countsnju.exception.NotFoundException;
import cn.edu.nju.tagmakers.countsnju.exception.PermissionDeniedException;
import cn.edu.nju.tagmakers.countsnju.filter.CriterionFilter;
import cn.edu.nju.tagmakers.countsnju.filter.TaskFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
        if (detail.getUserMarked().get(workerName) == null
                || detail.getUserMarked().get(workerName) < detail.getLimit()) {
            return new WorkerTaskDetailVO(detail);
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
        //查看是否有权限
        if (toReceive.getUserMarked().get(workerName) == null
                || toReceive.getUserMarked().get(workerName) < toReceive.getLimit()) {

            //将任务加入工人的任务列表
            Worker receiver = workerController.findByID(workerName);
            if (receiver == null) {
                throw new NotFoundException("没有此任务");
            }
            List<String> taskList = receiver.getTaskIDs();
            if (!taskList.contains(taskID)) {
                taskList.add(taskID);
            } else {
                throw new PermissionDeniedException("重复接受某一任务");
            }
            Worker worker = workerController.findByID(workerName);
            worker.setTaskIDs(taskList);
            workerController.update(worker);

            //在任务的userMarked中添加此工人
            toReceive.getUserMarked().putIfAbsent(workerName, 0);
            taskService.updateTask(toReceive);

            return true;
        }
        throw new PermissionDeniedException("你已经达到此任务上限");
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
                return new WorkerTaskDetailVO(task);
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
        if (!workerAndCriterionController.existed(workerID, criterionID)) {
            //这个工人没有做过这个标准集,先添加，再查上来
            Criterion criterion = criterionService.findByID(criterionID);
            workerAndCriterionController.add(workerID, criterion);
        }
        WorkerAndCriterion workerAndCriterion = workerAndCriterionController.findByID(workerID, criterionID);


        return null;
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
