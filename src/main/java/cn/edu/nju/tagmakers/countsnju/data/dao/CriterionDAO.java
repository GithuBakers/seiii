package cn.edu.nju.tagmakers.countsnju.data.dao;

import cn.edu.nju.tagmakers.countsnju.entity.Criterion.Criterion;
import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;
import cn.edu.nju.tagmakers.countsnju.filter.CriterionFilter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Description:
 * 标准集的DAO
 *
 * @author wym
 * Created on
 * <p>
 * Update:增加workerPassed字段
 * @author wym
 * Last modified on 4/22
 */

@Component
public class CriterionDAO extends DAO<Criterion, CriterionFilter> {
    /**
     * 复合查找符合条件的对象
     *
     * @param filter 查询条件
     * @return 查询结果
     */
    @Override
    public List<Criterion> find(CriterionFilter filter) {
        return null;
    }

    /**
     * 将两个对象的不同的地方统一
     *
     * @param ori map中原来储存的对象
     * @param cur 更新的对象
     */
    @Override
    protected Criterion setChanges(Criterion ori, Criterion cur) {
        if (cur.getCriterionID() != null) ori.setCriterionID(cur.getCriterionID());
        if (cur.getCriterionName() != null) ori.setCriterionName(cur.getCriterionName());
        if (cur.getCover() != null) ori.setCover(cur.getCover());
        if (cur.getRequirement() != null) ori.setRequirement(cur.getRequirement());
        if (cur.getType() != MarkType.DEFAULT) ori.setType(cur.getType());
        if (cur.getAim() != 0) ori.setAim(cur.getAim());
        if (cur.getDataSet().size() > 0) ori.setDataSet(new ArrayList<>(cur.getDataSet()));
        if (cur.getKeywords().size() > 0) ori.setKeywords(new ArrayList<>(cur.getKeywords()));
        if (cur.getWorkerPassed().size() > 0) ori.setWorkerPassed(new HashSet<>(cur.getWorkerPassed()));
        return ori;
    }
}
