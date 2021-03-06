package cn.edu.nju.tagmakers.countsnju.data.controller;

import cn.edu.nju.tagmakers.countsnju.data.dao.WorkerDAO;
import cn.edu.nju.tagmakers.countsnju.entity.user.Worker;
import cn.edu.nju.tagmakers.countsnju.filter.WorkerFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 * 众包工人的数据层Controller
 *
 * @author xxz
 * Created on 04/07/2018
 */
@Component
public class WorkerController implements DataController<Worker, WorkerFilter> {
    private final WorkerDAO workerDAO;

    @Autowired
    public WorkerController(WorkerDAO dao){
        this.workerDAO = dao;
    }

    /**
     * 增加一个对象
     *
     * @param obj 需要增加的对象
     * @return 增加的对象
     */
    @Override
    public Worker add(Worker obj) {
        return workerDAO.add(obj);
    }

    /**
     * 更新对象的信息
     *
     * @param obj 需要更新的对象
     * @return 更新后的对象
     */
    @Override
    public Worker update(Worker obj) {
        return workerDAO.update(obj);
    }

    /**
     * 按条件查找
     *
     * @param filter 查找条件
     * @return 查找结果
     */
    @Override
    public List<Worker> find(WorkerFilter filter) {
        return workerDAO.find(filter);
    }

    /**
     * 删除某一对象（按ID）
     *
     * @param id 要删除的对象ID
     * @return 是否删除成功
     */
    @Override
    public boolean delete(String id) {
        return workerDAO.delete(id);
    }

    /**
     * 按ID查找对象
     *
     * @param id
     */
    @Override
    public Worker findByID(String id) {
        return workerDAO.findByID(id);
    }

    public int count() {
        return workerDAO.count();
    }
}
