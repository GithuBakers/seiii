package cn.edu.nju.tagmakers.countsnju.logic.controller;

import cn.edu.nju.tagmakers.countsnju.entity.Task;
import cn.edu.nju.tagmakers.countsnju.entity.vo.InitiatorTaskVO;
import cn.edu.nju.tagmakers.countsnju.logic.service.InitiatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public InitiatorRestController(InitiatorService initiatorService) {
        this.initiatorService = initiatorService;
    }

    /**
     * 新增（新开始一个任务)
     *
     * @param task 新开始的任务
     */
    @RequestMapping(value = "/task/running_task", method = RequestMethod.POST)
    public boolean addTask(@RequestBody Task task) {
        initiatorService.createTask(task);
        return false;
    }

    /**
     * 获取自己新建的任务列表
     *
     * @param isFinished 是否完成（筛选条件）
     */
    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public List<InitiatorTaskVO> getTasks(@PathParam(value = "finished") boolean isFinished) {
        //TODO
        return null;
    }

    /**
     * 获取某一任务的详情
     *
     * @param taskName 任务名
     */
    @RequestMapping(value = "/task/{task_name}", method = RequestMethod.GET)
    public Task getTaskByName(@PathVariable(value = "task_name") String taskName) {
        //TODO
        return null;
    }

    /**
     * @param taskName 结束某一任务
     */
    @RequestMapping(value = "/task/finished_task", method = RequestMethod.POST)
    public Task finishTask(@RequestBody String taskName) {
        return initiatorService.finishTask(taskName);
    }


}
