package cn.edu.nju.tagmakers.countsnju.data.controller;

import cn.edu.nju.tagmakers.countsnju.data.dao.CriterionDAO;
import cn.edu.nju.tagmakers.countsnju.entity.Criterion;
import cn.edu.nju.tagmakers.countsnju.filter.CriterionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CriterionController implements DataController<Criterion, CriterionFilter> {
    private final CriterionDAO criterionDAO;

    @Autowired
    public CriterionController(CriterionDAO dao) {
        criterionDAO = dao;
    }

    /**
     * 增加一个对象
     *
     * @param obj 需要增加的对象
     * @return 增加的对象
     */
    @Override
    public Criterion add(Criterion obj) {
        return criterionDAO.add(obj);
    }

    /**
     * 更新对象的信息
     *
     * @param obj 需要更新的对象
     * @return 更新后的对象
     */
    @Override
    public Criterion update(Criterion obj) {
        return criterionDAO.add(obj);
    }

    /**
     * 按条件查找
     *
     * @param filter 查找条件
     * @return 查找结果
     */
    @Override
    public List<Criterion> find(CriterionFilter filter) {
        return criterionDAO.find(filter);
    }

    /**
     * 删除某一对象（按ID）
     *
     * @param id 要删除的对象ID
     * @return 是否删除成功
     */
    @Override
    public boolean delete(String id) {
        return criterionDAO.delete(id);
    }

    /**
     * 按ID查找对象
     *
     * @param id
     */
    @Override
    public Criterion findByID(String id) {
        return criterionDAO.findByID(id);
    }
}
