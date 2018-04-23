package cn.edu.nju.tagmakers.countsnju.data.controller;

import cn.edu.nju.tagmakers.countsnju.data.dao.TaskDAO;
import cn.edu.nju.tagmakers.countsnju.entity.Task;
import cn.edu.nju.tagmakers.countsnju.filter.TaskFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 * 众包任务的数据层controller
 *
 * @author xxz
 * Created on 04/06/2018
 */
@Component
public class TaskController implements DataController<Task, TaskFilter> {
    private final TaskDAO taskDAO;

    @Autowired
    public TaskController(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }


    /**
     * 增加一个对象
     *
     * @param obj 需要增加的对象
     * @return 增加的对象
     */
    @Override
    public Task add(Task obj) {
        return taskDAO.add(obj);
    }

    /**
     * 更新对象的信息
     *
     * @param obj 需要更新的对象
     * @return 更新后的对象
     */
    @Override
    public Task update(Task obj) {
        return taskDAO.update(obj);
    }

    /**
     * 按条件查找
     *
     * @param filter 查找条件
     * @return 查找结果
     */
    @Override
    public List<Task> find(TaskFilter filter) {
        return taskDAO.find(filter);
    }

    /**
     * 删除某一对象（按ID）
     *
     * @param id 要删除的对象ID
     * @return 是否删除成功
     */
    @Override
    public boolean delete(String id) {
        return taskDAO.delete(id);
    }

    /**
     * 按ID查找对象
     */
    @Override
    public Task findByID(String id) {
        return taskDAO.findByID(id);
    }

    public int count() {
        return taskDAO.count();
    }
}
