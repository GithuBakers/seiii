package cn.edu.nju.tagmakers.countsnju.data.dao;

import cn.edu.nju.tagmakers.countsnju.entity.Criterion.Criterion;
import cn.edu.nju.tagmakers.countsnju.entity.Criterion.WorkerAndCriterion;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Description:
 * 专门用于记录工人做标准集相关的结果的DAO
 * * @author wym
 * Created on
 * <p>
 * Update:
 *
 * @author wym
 * Last modified on
 */
@Component
public class WorkerAndCriterionDAO {
    //todo
    private Set<WorkerAndCriterion> set;

    public WorkerAndCriterionDAO() {
        set = new HashSet<>();
    }

    /**
     * 用于判断是否工人已经做过某个标准集
     */
    public boolean existed(String workerID, String criterionID) {
        return findByID(workerID, criterionID) != null;
    }

    /**
     * 根据工人ID和标准集ID查找
     */
    public WorkerAndCriterion findByID(String workerID, String criterionID) {
        for (WorkerAndCriterion temp : set) {
            if (workerID.equals(temp.getWorkerID()) && criterionID.equals(temp.getCriterionID())) {
                return temp;
            }
        }
        return null;
    }

    /**
     * 检查过用户没有做过这个标准集之后添加这个结果
     *
     * @param workerID
     * @param criterion
     */
    public void add(String workerID, Criterion criterion) {
        if (criterion == null) throw new InvalidInputException("添加的标准集为空");
        WorkerAndCriterion temp = new WorkerAndCriterion(workerID, criterion);
        set.add(temp);
    }

    public void update(WorkerAndCriterion workerAndCriterion) {
        for (WorkerAndCriterion temp : set) {
            if (workerAndCriterion.getWorkerID().equals(temp.getWorkerID()) && workerAndCriterion.getCriterionID().equals(temp.getCriterionID())) {
                set.remove(temp);
                set.add(workerAndCriterion);
            }
        }
    }
}
