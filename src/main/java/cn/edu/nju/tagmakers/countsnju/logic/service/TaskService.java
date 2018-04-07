package cn.edu.nju.tagmakers.countsnju.logic.service;

import cn.edu.nju.tagmakers.countsnju.data.controller.TaskController;
import cn.edu.nju.tagmakers.countsnju.entity.Task;
import cn.edu.nju.tagmakers.countsnju.exception.FileIOException;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import cn.edu.nju.tagmakers.countsnju.filter.TaskFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.FileCreator;
import util.OSSWriter;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
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

    /**
     * 结束某项任务
     *
     * @param taskName 要结束的任务名
     * @return 结束后任务信息
     */
    public Task finishTask(String taskName) {
        Task toFinish = findByID(taskName);
        //更改状态为已完成
        toFinish.setFinished(true);
        //制作结果集
        toFinish.setResult(makeResult(toFinish));
        //在数据层更新任务信息
        updateTask(toFinish);
        return toFinish;
    }

    /**
     * 为某个已结束任务创建结果
     *
     * @param task 已结束任务
     * @return 结果所在的URL
     */
    private String makeResult(Task task) {
        if (!task.getFinished()) {
            throw new InvalidInputException("任务尚未结束，请结束任务之后再查看任务结果");
        }
        String filePath = "task_result_" + task.getTaskName();
        FileCreator.createFile(filePath);
        File file = new File(filePath);

        //生成结果的逻辑
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
            writer.write("this is the result set of task" + file.getName());
            writer.newLine();
            writer.write("请联系支付宝：18251830730 以查看所有数据");
            writer.newLine();
            writer.write("皮这一下非常开心");
            writer.newLine();
            writer.write(new Date(System.currentTimeMillis()).toString());
        } catch (FileNotFoundException e) {
            throw new FileIOException(Arrays.toString(e.getStackTrace()));
        } catch (IOException e) {
            throw new FileIOException("File IO ERROR in task Service, when try to generate result set");
        }

        //上传到OSS
        return OSSWriter.upload(file);
    }
}
