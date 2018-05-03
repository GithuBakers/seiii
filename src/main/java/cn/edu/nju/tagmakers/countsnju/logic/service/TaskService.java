package cn.edu.nju.tagmakers.countsnju.logic.service;

import cn.edu.nju.tagmakers.countsnju.data.controller.TaskController;
import cn.edu.nju.tagmakers.countsnju.data.controller.WorkerController;
import cn.edu.nju.tagmakers.countsnju.entity.Task;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.user.Worker;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import cn.edu.nju.tagmakers.countsnju.filter.TagFilter;
import cn.edu.nju.tagmakers.countsnju.filter.TaskFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * 众包任务的逻辑对象
 *
 * @author xxz
 * Created on 04/06/2018
 * <p>
 * Update:
 * 增加了发放奖励的逻辑
 * @author xxz
 * Created on 04/07/2018
 * <p>
 * Update:
 * 将生成结果集的方法改为异步方法
 * @author xxz
 * Created on 04/28/2018
 */

@Component
public class TaskService {

    private TaskController taskController;

    private WorkerController workerController;

    private BareService bareService;

    private CriterionService criterionService;

    private TagService tagService;

    @Autowired
    public TaskService(TaskController taskController, WorkerController workerController, BareService bareService, CriterionService criterionService, TagService tagService) {
        this.taskController = taskController;
        this.workerController = workerController;
        this.bareService = bareService;
        this.criterionService = criterionService;
        this.tagService = tagService;
    }

    /**
     * 添加一项新任务
     * <p>
     * 将数据集的每一项增加至bare
     *
     * @param task 需要添加的任务
     */
    public void addTask(Task task) {
        if (task == null || task.getDataSet() == null) {
            throw new InvalidInputException("此任务没有上传数据集");
        }
        //设置finished
        task.setFinished(false);
        taskController.add(task);
        for (Bare bare : task.getDataSet()) {
            bareService.addBare(bare);
        }
//        for (Criterion criterion : task.getDependencies()) {
//            criterionService.addCriterion(criterion);
//        }


    }


    /**
     * 更新Task信息
     *
     * @param task 新的task信息
     */
    public void updateTask(Task task) {
        taskController.update(task);
    }

    /**
     * 查找任务列表
     *
     * @param filter 筛选条件
     */
    public List<Task> findTask(TaskFilter filter) {
        return taskController.find(filter);
    }

    /**
     * 按任务名查找任务
     *
     * @param id 任务名
     */
    public Task findByID(String id) {
        return taskController.findByID(id);
    }

    /**
     * 结束某项任务
     * <p>
     * 更改状态为已完成
     * <p>
     * 制作结果集
     * <p>
     * 发放奖励（60%）
     * <p>
     * 在数据层更新
     *
     * @param taskID 要结束的任务名
     * @return 结束后任务信息
     */
    public Task finishTask(String taskID) {
        Task toFinish = findByID(taskID);
        //更改状态为已完成
        toFinish.setFinished(true);
        //发放奖励
        reward(toFinish);
        //在数据层更新任务信息
        updateTask(toFinish);

        //制作结果集
        //编程异步方法
        makeResult(toFinish);
//        toFinish.setResult(makeResult(toFinish));
        return toFinish;
    }

    private void reward(Task task) {
        if (!task.getFinished()) {
            throw new InvalidInputException("任务尚未结束，请结束任务之后再发放奖励");
        }
        task.getUserMarked().entrySet().parallelStream()
                .forEach(entry -> {
                    Worker worker = workerController.findByID(entry.getKey());
                    int oriCredits = worker.getCredit();
                    oriCredits += Math.round(entry.getValue() / 10 * task.getReward() * 0.6);
                    worker.setCredit(oriCredits);
                    workerController.update(worker);
                });
    }

    /**
     * 为某个已结束任务创建结果
     *
     * @param task 已结束任务
     */
    private void makeResult(Task task) {
        if (!task.getFinished()) {
            throw new InvalidInputException("任务尚未结束，请结束任务之后再查看任务结果");
        }
        TaskResultCalculator calculator = new TaskResultCalculator();
        calculator.setTask(task);
        calculator.setTaskController(taskController);
        calculator.setWorkerController(workerController);
        calculator.setTags(task.getDataSet().stream().map(Bare::getId)
                .flatMap(id -> {
                    TagFilter filter = new TagFilter();
                    filter.setBareID(id);
                    return tagService.findTag(filter).stream();
                }).collect(Collectors.toList()));
        new Thread(calculator).start();
    }
}
