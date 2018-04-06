package cn.edu.nju.tagmakers.countsnju.data.controller;

import cn.edu.nju.tagmakers.countsnju.data.dao.BareDAO;
import cn.edu.nju.tagmakers.countsnju.entity.Bare;
import cn.edu.nju.tagmakers.countsnju.filter.BareFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 * 不含标注的图片的数据层Controller
 *
 * @author xxz
 * Created on 03/17/2018
 * <p>
 * update: 填充了controller的方法
 * @author xuxiangzhe
 * modified on 03/21/2018
 */

/**
 * update: sss
 * @author xxz
 * modified on 03/21/2018
 */
@Component
public class BareController implements cn.edu.nju.tagmakers.countsnju.data.controller.DataController<Bare, BareFilter> {
    private final BareDAO bareDAO;

    @Autowired
    public BareController(BareDAO bareDAO) {
        this.bareDAO = bareDAO;
    }


    /**
     * 增加一个对象
     *
     * @param obj 需要增加的对象
     * @return 增加的对象
     */
    @Override
    public Bare add(Bare obj) {
        return bareDAO.add(obj);
    }

    /**
     * 更新对象的信息
     *
     * @param obj 需要更新的对象
     * @return 更新后的对象
     */
    @Override
    public Bare update(Bare obj) {
        return bareDAO.update(obj);
    }

    /**
     * 按条件查找
     *
     * @param filter 查找条件
     * @return 查找结果
     */
    @Override
    public List<Bare> find(BareFilter filter) {
        return bareDAO.find(filter);
    }

    /**
     * 删除某一对象（按ID）
     *
     * @param id 要删除的对象ID
     * @return 是否删除成功
     */
    @Override
    public boolean delete(String id) {
        return bareDAO.delete(id);
    }

    /**
     * 按ID查找对象
     *
     * @param id
     */
    @Override
    public Bare findByID(String id) {
        return bareDAO.findByID(id);
    }
}
