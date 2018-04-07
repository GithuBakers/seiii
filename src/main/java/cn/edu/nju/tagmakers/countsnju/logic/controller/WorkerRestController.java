package cn.edu.nju.tagmakers.countsnju.logic.controller;

import cn.edu.nju.tagmakers.countsnju.entity.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.Image;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerReceivedTaskDetailVO;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerReceivedTaskVO;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerTaskDetailVO;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerTaskVO;
import cn.edu.nju.tagmakers.countsnju.logic.service.UserService;
import cn.edu.nju.tagmakers.countsnju.logic.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public WorkerRestController(WorkerService workerService, UserService userService) {
        this.workerService = workerService;

    }

    /**
     * 查看所有能参加的任务列表
     */
    @RequestMapping(value = "/task_list", method = RequestMethod.GET)
    public List<WorkerTaskVO> getTaskList() {
        //TODO: 查询还在继续的
        return null;
    }


    /**
     * 查看某一任务详情
     *
     * @param taskName 任务名
     */
    @RequestMapping(value = "/task/{task_name}", method = RequestMethod.GET)
    public WorkerTaskDetailVO getTaskDetail(@PathVariable(value = "task_name") String taskName) {
        return workerService.getTaskDetail(taskName);
    }


    /**
     * 接受某一任务
     *
     * @param taskName 任务名
     */
    @RequestMapping(value = "/task/received_task/{task_name}", method = RequestMethod.POST)
    public boolean receiveTask(@PathVariable(value = "task_name") String taskName) {
//        SecurityContextHolder.getContext().getAuthentication()
        //TODO:
        return false;
    }

    /**
     * 获取某一任务的图片列表
     *
     * @param taskName 任务名
     */
    @RequestMapping(value = "/task/received_task/img/{task_name}", method = RequestMethod.GET)
    public List<Bare> getBareList(@PathVariable(value = "task_name") String taskName) {
//        Worker worker= userService((XXXXUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getUsername()
        //TODO:
        return null;
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
        //TODO: add worker name

        return false;

    }

    /**
     * 查看已接受任务列表
     */
    @RequestMapping(value = "/task/received_task", method = RequestMethod.GET)
    public List<WorkerReceivedTaskVO> getReceivedTask() {
        //TODO:根据worker name筛选
        return null;
    }

    /**
     * 获取某一已接受任务的详细信息
     */
    @RequestMapping(value = "/task/received_task/{task_name}", method = RequestMethod.GET)
    public WorkerReceivedTaskDetailVO getReceivedTaskDetail() {
        return null;
    }


}
