package cn.edu.nju.tagmakers.countsnju.data.controller;

import cn.edu.nju.tagmakers.countsnju.data.dao.WorkerAndCriterionDAO;
import cn.edu.nju.tagmakers.countsnju.entity.Criterion.Criterion;
import cn.edu.nju.tagmakers.countsnju.entity.Criterion.WorkerAndCriterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkerAndCriterionController {
    private final WorkerAndCriterionDAO workerAndCriterionDAO;

    @Autowired
    public WorkerAndCriterionController(WorkerAndCriterionDAO workerAndCriterionDAO) {
        this.workerAndCriterionDAO = workerAndCriterionDAO;
    }

    /**
     * 用于判断是否工人已经做过某个标准集
     */
    public boolean existed(String workerID, String criterionID) {
        return workerAndCriterionDAO.existed(workerID, criterionID);
    }

    /**
     * 根据工人ID和标准集ID查找
     */
    public WorkerAndCriterion findByID(String workerID, String criterionID) {
        return workerAndCriterionDAO.findByID(workerID, criterionID);
    }

    /**
     * 检查过用户没有做过这个标准集之后添加这个结果
     */
    public void add(String workerID, Criterion criterion) {
        workerAndCriterionDAO.add(workerID, criterion);
    }
}
