package cn.edu.nju.tagmakers.countsnju.data.dao;

import cn.edu.nju.tagmakers.countsnju.entity.Criterion.Criterion;
import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;
import cn.edu.nju.tagmakers.countsnju.filter.CriterionFilter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
 * <p>
 * Update:
 * 增加字段 发起者是否完成
 * @author xxz
 * Created on 04/27/2018
 */
@Component
public class CriterionDAO extends DAO<Criterion, CriterionFilter> {
    public CriterionDAO() {
        map = new ConcurrentHashMap<>();
        filePath = "data" + File.separator + "Criterion.txt";
        read();
    }

    /**
     * 复合查找符合条件的对象
     *
     * @param filter 查询条件
     * @return 查询结果
     */
    @Override
    public List<Criterion> find(CriterionFilter filter) {
        if (filter == null) {
            return new ArrayList<>(map.values());
        }

        Stream<Criterion> criterionStream = map.values().stream();
        if (filter.getInitiatorID() != null) {
            criterionStream = criterionStream.filter(criterion -> criterion.getInitiatorID().equals(filter.getInitiatorID()));
        }
        if (filter.getFinished() != null) {
            criterionStream = criterionStream.filter(criterion -> {
                Boolean hasFinished = criterion.isHasFinished();
                if (hasFinished == null) {
                    return false;
                }
                return hasFinished.equals(filter.getFinished());
            });
        }
        return criterionStream.collect(Collectors.toList());
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
        if (cur.getInitiatorID() != null) ori.setInitiatorID(cur.getInitiatorID());
        if (cur.isHasFinished() != null) ori.setHasFinished(cur.isHasFinished());
        if (cur.getResult() != null) ori.setResult(cur.getResult());
        return ori;
    }
}
