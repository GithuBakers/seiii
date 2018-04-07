package cn.edu.nju.tagmakers.countsnju.logic.service;

import cn.edu.nju.tagmakers.countsnju.data.controller.WorkerController;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Image;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Tag;
import cn.edu.nju.tagmakers.countsnju.entity.user.Task;
import cn.edu.nju.tagmakers.countsnju.entity.user.Worker;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerReceivedTaskDetailVO;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerReceivedTaskVO;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerTaskDetailVO;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerTaskVO;
import cn.edu.nju.tagmakers.countsnju.exception.PermissionDeniedException;
import cn.edu.nju.tagmakers.countsnju.filter.TaskFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Description:
 * 众包工人的逻辑对象
 *
 * @author xxz
 * Created on 04/06/2018
 */
@Component
public class WorkerService {

    private TaskService taskService;

    private WorkerController workerController;

    private TagService tagService;


    @Autowired
    public WorkerService(TaskService taskService, WorkerController workerController, TagService tagService) {
        this.taskService = taskService;
        this.workerController = workerController;
        this.tagService = tagService;
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
     * 工人查看的任务列表(还未结束 && 还未达到上限
     *
     * @param taskFilter 筛选条件
     */
    public List<WorkerTaskVO> findWorkerTask(TaskFilter taskFilter, String workerName) {

        return taskService.findTask(taskFilter).stream()
                .filter(task -> Optional.ofNullable(
                        task.getUserMarked().get(workerName))
                        .orElse(0)
                        < task.getLimit())
                .map(WorkerTaskVO::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取某一任务的详情
     *
     * @param taskName 任务名
     */
    public WorkerTaskDetailVO getTaskDetail(String taskName, String workerName) {
        Task detail = taskService.findByID(taskName);
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
     * @param taskName   接受的任务名
     */
    public boolean receiveTask(String taskName, String workerName) {
        Task toReceive = taskService.findByID(taskName);

        //查看是否有权限
        if (toReceive.getUserMarked().get(workerName) == null
                || toReceive.getUserMarked().get(workerName) < toReceive.getLimit()) {

            //将任务加入工人的任务列表
            List<String> taskList = workerController.findByID(workerName).getTaskNames();
            if (!taskList.contains(taskName)) {
                taskList.add(taskName);
            }
            Worker worker = workerController.findByID(workerName);
            worker.setTaskNames(taskList);
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
     * @param taskName   任务名
     * @param workerName 工人名
     */
    public List<Bare> getBares(String taskName, String workerName) {
        return calculateBares(taskService.findByID(taskName),
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
     * 将工人的图片列表中加入此图片
     * <p>
     * 为标记加上工人Name
     * <p>
     * 将标记加入数据层
     *
     * @param image      图片
     * @param taskName   任务名
     * @param workerName 工人名
     */
    public boolean submitTag(Image image, String taskName, String workerName) {
        Task toUpdate = taskService.findByID(taskName);
        //在任务的图片列表中将标注次数加一
        //如果缺失，则这是第一次标注
        toUpdate.getBareMarked().putIfAbsent(image.getBare().getId(), 1);
        int tmp = toUpdate.getBareMarked().get(image.getBare().getId());
        tmp++;
        if (tmp > toUpdate.getLimit()) {
            throw new PermissionDeniedException("你已经做了足够多了！换个任务试一试吧");
        }
        toUpdate.getBareMarked().put(image.getBare().getId(), tmp);

        //将任务列表中工人的标注次数加一
        //如果缺失，则这是第一个标注
        toUpdate.getUserMarked().putIfAbsent(workerName, 1);
        //否则增加1
        int tmp1 = toUpdate.getUserMarked().get(workerName);
        toUpdate.getUserMarked().put(workerName, tmp1);

        taskService.updateTask(toUpdate);
        //将工人的图片列表中加入此图片
        Worker worker = workerController.findByID(workerName);
        List<String> bareIDs = worker.getBareIDs();
        bareIDs.add(image.getBare().getId());
        worker.setBareIDs(bareIDs);
        workerController.update(worker);

        List<Tag> tagList = image.getTags();
        tagList.parallelStream().forEach(tag -> tag.setWorkerID(workerName));
        tagList.parallelStream().forEach(tagService::addTag);
        return true;
    }

    public List<WorkerReceivedTaskVO> getReceivedTasks(String workerName) {
        Worker worker = workerController.findByID(workerName);
        return workerController.findByID(workerName).getTaskNames().parallelStream()
                .map(taskService::findByID)
                .map(task -> new WorkerReceivedTaskVO(task, worker))
                .collect(Collectors.toList());
    }


    public WorkerReceivedTaskDetailVO getReceivedTaskDetails(String taskName, String workerName) {
        Task task = taskService.findByID(taskName);
        Worker worker = workerController.findByID(workerName);
        if (!worker.getTaskNames().contains(taskName)) {
            throw new PermissionDeniedException("这不是你的任务！");
        }
        return new WorkerReceivedTaskDetailVO(task, worker);
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
            int random = new Random(System.currentTimeMillis()).nextInt() % task.getDataSet().size();
            Bare bare = task.getDataSet().get(random);
            if (!ret.contains(bare) && !worker.getBareIDs().contains(bare.getId())) {
                ret.add(bare);
            }
        }
        return ret;
    }


}
