package cn.edu.nju.tagmakers.countsnju.logic.service;

import cn.edu.nju.tagmakers.countsnju.data.controller.TaskController;
import cn.edu.nju.tagmakers.countsnju.entity.Task;
import cn.edu.nju.tagmakers.countsnju.filter.TaskFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 * 众包任务的逻辑对象
 *
 * @author xxz
 * Created on 04/06/2018
 */


@Component
public class TaskService {

    private TaskController taskController;

    @Autowired
    public TaskService(TaskController taskController) {
        this.taskController = taskController;
    }

    /**
     * 添加一项新任务
     *
     * @param task 需要添加的任务
     */
    public void addTask(Task task) {
        taskController.add(task);
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

}
