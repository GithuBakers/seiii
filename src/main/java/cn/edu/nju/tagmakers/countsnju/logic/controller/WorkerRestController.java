package cn.edu.nju.tagmakers.countsnju.logic.controller;

import cn.edu.nju.tagmakers.countsnju.entity.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.Image;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerReceivedTaskDetailVO;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerReceivedTaskVO;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerTaskDetailVO;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerTaskVO;
import cn.edu.nju.tagmakers.countsnju.filter.TaskFilter;
import cn.edu.nju.tagmakers.countsnju.logic.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import util.SecurityUtility;

import java.util.List;

/**
 * Description:
 * 众包工人的restController
 *
 * @author xxz
 * Created on 04/06/2018
 */

@RestController
@RequestMapping("/worker")
public class WorkerRestController {

    private WorkerService workerService;



    @Autowired
    public WorkerRestController(WorkerService workerService) {
        this.workerService = workerService;
    }

    /**
     * 查看所有能参加的任务列表
     */
    @RequestMapping(value = "/task_list", method = RequestMethod.GET)
    public List<WorkerTaskVO> getTaskList() {
        String workerName = SecurityUtility.getUserName(SecurityContextHolder.getContext());
        TaskFilter filter = new TaskFilter();
        filter.setFinished(false);
        return workerService.findWorkerTask(filter, workerName);
    }


    /**
     * 查看某一任务详情
     *
     * @param taskName 任务名
     */
    @RequestMapping(value = "/task/{task_name}", method = RequestMethod.GET)
    public WorkerTaskDetailVO getTaskDetail(@PathVariable(value = "task_name") String taskName) {
        String workerName = SecurityUtility.getUserName(SecurityContextHolder.getContext());
        return workerService.getTaskDetail(taskName, workerName);
    }


    /**
     * 接受某一任务
     *
     * @param taskName 任务名
     */
    @RequestMapping(value = "/task/received_task/{task_name}", method = RequestMethod.POST)
    public boolean receiveTask(@PathVariable(value = "task_name") String taskName) {
        String workerName = SecurityUtility.getUserName(SecurityContextHolder.getContext());
        return workerService.receiveTask(taskName, workerName);
    }

    /**
     * 获取某一任务的图片列表
     *
     * @param taskName 任务名
     */
    @RequestMapping(value = "/task/received_task/img/{task_name}", method = RequestMethod.GET)
    public List<Bare> getBareList(@PathVariable(value = "task_name") String taskName) {
        String workerName = SecurityUtility.getUserName(SecurityContextHolder.getContext());

        return workerService.getBares(taskName, workerName);
    }

    /**
     * 提交对某一张图片的标注
     *
     * @param taskName 任务名
     * @param imgID    图片名
     * @param image    （含标注的）图片
     */
    @RequestMapping(value = "/task/received_task/{task_name}/{img_id}", method = RequestMethod.POST)
    public boolean submitImage(@PathVariable(value = "task_name") String taskName,
                               @PathVariable(value = "img_id") String imgID,
                               @RequestBody Image image) {
        String workerName = SecurityUtility.getUserName(SecurityContextHolder.getContext());

        return workerService.submitTag(image, taskName, workerName);

    }

    /**
     * 查看已接受任务列表
     */
    @RequestMapping(value = "/task/received_task", method = RequestMethod.GET)
    public List<WorkerReceivedTaskVO> getReceivedTask() {
        String workerName = SecurityUtility.getUserName(SecurityContextHolder.getContext());

        return workerService.getReceivedTasks(workerName);
    }

    /**
     * 获取某一已接受任务的详细信息
     */
    @RequestMapping(value = "/task/received_task/{task_name}", method = RequestMethod.GET)
    public WorkerReceivedTaskDetailVO getReceivedTaskDetail(@PathVariable String taskName) {
        String workerName = SecurityUtility.getUserName(SecurityContextHolder.getContext());

        return workerService.getReceivedTaskDetails(taskName, workerName);
    }


}
