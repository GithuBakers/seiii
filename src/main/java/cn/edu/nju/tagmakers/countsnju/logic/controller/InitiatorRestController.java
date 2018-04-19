package cn.edu.nju.tagmakers.countsnju.logic.controller;

import cn.edu.nju.tagmakers.countsnju.entity.Task;
import cn.edu.nju.tagmakers.countsnju.entity.user.Initiator;
import cn.edu.nju.tagmakers.countsnju.entity.vo.InitiatorTaskVO;
import cn.edu.nju.tagmakers.countsnju.exception.PermissionDeniedException;
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
     * 获取个人信息
     *
     * @param username 用户名
     */
    @RequestMapping(value = "/information/{user_name}", method = RequestMethod.GET)
    public Initiator getInfo(@PathVariable(value = "user_name") String username) {
        String initiatorName = SecurityUtility.getUserName(SecurityContextHolder.getContext());

        if (!initiatorName.equals(username)) {
            throw new PermissionDeniedException("无权限访问别人的信息！");
        }
        return initiatorService.findInitiatorByName(username);

    }

    /**
     * 更新个人信息
     */
    @RequestMapping(value = "/information/{user_name}", method = RequestMethod.POST)
    public boolean updateInfo(@PathVariable(value = "user_name") String username, @RequestBody Initiator initiator) {
        Initiator toUpdate = new Initiator();
        //允许修改的字段
        toUpdate.setUserID(SecurityUtility.getUserName(SecurityContextHolder.getContext()));
        toUpdate.setAvatar(initiator.getAvatar());
        toUpdate.setNickName(initiator.getNickName());
        return initiatorService.update(toUpdate);
    }
    /**
     * 新增（新开始一个任务)
     *
     * @param task 新开始的任务
     */
    @RequestMapping(value = "/task/running_task", method = RequestMethod.POST)
    public boolean addTask(@RequestBody Task task) {
        //后来甩锅给前端啦
//        task.setCover(URLUtil.processURL(task.getCover()));
        String initiatorName = SecurityUtility.getUserName(SecurityContextHolder.getContext());
        task.setInitiatorName(initiatorName);
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
     * @param taskID 任务名
     */
    @RequestMapping(value = "/task/{task_id}", method = RequestMethod.GET)
    public Task getTaskByName(@PathVariable(value = "task_id") String taskID) {
        String initiatorName = SecurityUtility.getUserName(SecurityContextHolder.getContext());
        return initiatorService.findTaskByName(taskID, initiatorName);
    }

    /**
     * @param task 结束某一任务
     */
    @RequestMapping(value = "/task/finished_task", method = RequestMethod.POST)
    public Task finishTask(@RequestBody Task task) {
        String initiatorName = SecurityUtility.getUserName(SecurityContextHolder.getContext());
        return initiatorService.finishTask(task.getTaskID(), initiatorName);
    }


}
