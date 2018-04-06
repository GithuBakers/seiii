package cn.edu.nju.tagmakers.countsnju.data.controller;

import cn.edu.nju.tagmakers.countsnju.data.dao.TagDAO;
import cn.edu.nju.tagmakers.countsnju.entity.Tag;
import cn.edu.nju.tagmakers.countsnju.filter.TagFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 * 标记的Controller
 *
 * @author xxz
 * Created on 03/17/2018
 * <p>
 * Update: 填充了方法
 * @author xxz
 * Last modified on 21/03/2018
 */
@Component
public class TagController implements cn.edu.nju.tagmakers.countsnju.data.controller.DataController<Tag, TagFilter> {
    private final TagDAO tagDAO;


    @Autowired
    public TagController(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    /**
     * 增加一个对象
     *
     * @param obj 需要增加的对象
     * @return 增加的对象
     */
    @Override
    public Tag add(Tag obj) {
        return tagDAO.add(obj);
    }

    /**
     * 更新对象的信息
     *
     * @param obj 需要更新的对象
     * @return 更新后的对象
     */
    @Override
    public Tag update(Tag obj) {
        return tagDAO.update(obj);
    }

    /**
     * 按条件查找
     *
     * @param filter 查找条件
     * @return 查找结果
     */
    @Override
    public List<Tag> find(TagFilter filter) {
        return tagDAO.find(filter);
    }

    /**
     * 删除某一对象（按ID）
     *
     * @param id 要删除的对象ID
     * @return 是否删除成功
     */
    @Override
    public boolean delete(String id) {
        return tagDAO.delete(id);
    }

    /**
     * 按ID查找对象
     *
     * @param id
     */
    @Override
    public Tag findByID(String id) {
        return tagDAO.findByID(id);
    }
}
