package cn.edu.nju.tagmakers.countsnju.logic.service;

import cn.edu.nju.tagmakers.countsnju.data.controller.CriterionController;
import cn.edu.nju.tagmakers.countsnju.entity.Criterion.Criterion;
import cn.edu.nju.tagmakers.countsnju.filter.CriterionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component
@Scope(
        value = SCOPE_PROTOTYPE,
        proxyMode = ScopedProxyMode.TARGET_CLASS
)
public class CriterionService {
    private final CriterionController criterionController;

    @Autowired
    public CriterionService(CriterionController criterionController) {
        this.criterionController = criterionController;
    }

    public List<Criterion> find(CriterionFilter filter) {
        return criterionController.find(filter);
    }

    public Criterion findByID(String criterionID) {
        return criterionController.findByID(criterionID);
    }

    /**
     * 用于判断某工人是否通过了某个标准集
     *
     * @param criterionID 标准集ID
     * @param workerID    工人ID
     * @return 是否通过
     */
    public boolean isPassed(String criterionID, String workerID) {
        Criterion criterion = findByID(criterionID);
        boolean pass = criterion.getWorkerPassed().contains(workerID);
        return pass;
    }

    public void update(Criterion criterion) {
        criterionController.update(criterion);
    }

}
