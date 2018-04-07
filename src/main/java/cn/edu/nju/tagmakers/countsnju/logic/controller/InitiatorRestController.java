package cn.edu.nju.tagmakers.countsnju.logic.controller;

import cn.edu.nju.tagmakers.countsnju.entity.user.Task;
import cn.edu.nju.tagmakers.countsnju.entity.vo.InitiatorTaskVO;
import cn.edu.nju.tagmakers.countsnju.filter.TaskFilter;
import cn.edu.nju.tagmakers.countsnju.logic.service.InitiatorService;
import cn.edu.nju.tagmakers.countsnju.logic.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import util.SecurityUtility;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * Description:
 * 众包发起者的restController
 *
 * @author xxz
 * Created on 04/06/2018
 */
@RestController
@RequestMapping("/initiator")
public class InitiatorRestController {

    private final InitiatorService initiatorService;


    @Autowired
    public InitiatorRestController(InitiatorService initiatorService, TaskService taskService) {
        this.initiatorService = initiatorService;
    }

    /**
     * 新增（新开始一个任务)
     *
     * @param task 新开始的任务
     */
    @RequestMapping(value = "/task/running_task", method = RequestMethod.POST)
    public boolean addTask(@RequestBody Task task) {
        return initiatorService.createTask(task);
    }

    /**
     * 获取自己新建的任务列表
     *
     * @param isFinished 是否完成（筛选条件）
     */
    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public List<InitiatorTaskVO> getTasks(@PathParam(value = "finished") boolean isFinished) {
        TaskFilter filter = new TaskFilter();
        filter.setFinished(isFinished);
        return initiatorService.findInitiatorTask(filter);
    }

    /**
     * 获取某一任务的详情
     *
     * @param taskName 任务名
     */
    @RequestMapping(value = "/task/{task_name}", method = RequestMethod.GET)
    public Task getTaskByName(@PathVariable(value = "task_name") String taskName) {
        String initiatorName = SecurityUtility.getUserName(SecurityContextHolder.getContext());
        return initiatorService.findTaskByName(taskName, initiatorName);
    }

    /**
     * @param taskName 结束某一任务
     */
    @RequestMapping(value = "/task/finished_task", method = RequestMethod.POST)
    public Task finishTask(@RequestBody String taskName) {
        String initiatorName = SecurityUtility.getUserName(SecurityContextHolder.getContext());
        return initiatorService.finishTask(taskName, initiatorName);
    }


}
