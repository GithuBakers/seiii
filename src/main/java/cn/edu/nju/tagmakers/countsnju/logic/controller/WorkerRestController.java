package cn.edu.nju.tagmakers.countsnju.logic.controller;

import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerTaskDetailVO;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerTaskVO;
import cn.edu.nju.tagmakers.countsnju.logic.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping("/task_list")
    public List<WorkerTaskVO> getTaskList() {
        //TODO: 查询还在继续的
        return null;
    }


    @RequestMapping("/task/{task_name}")
    public WorkerTaskDetailVO getTaskDetail(@PathVariable(value = "task_name") String taskName) {
        return workerService.getTaskDetail(taskName);
    }


}
