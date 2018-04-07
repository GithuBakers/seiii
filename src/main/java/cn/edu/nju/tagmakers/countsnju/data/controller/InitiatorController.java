package cn.edu.nju.tagmakers.countsnju.data.controller;

import cn.edu.nju.tagmakers.countsnju.entity.user.Initiator;
import cn.edu.nju.tagmakers.countsnju.filter.InitiatorFilter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 * 众包发起者的数据层Controller
 *
 * @author xxz
 * Created on 04/07/2018
 */
@Component
public class InitiatorController implements DataController<Initiator, InitiatorFilter> {
    /**
     * 增加一个对象
     *
     * @param obj 需要增加的对象
     * @return 增加的对象
     */
    @Override
    public Initiator add(Initiator obj) {
        return null;
    }

    /**
     * 更新对象的信息
     *
     * @param obj 需要更新的对象
     * @return 更新后的对象
     */
    @Override
    public Initiator update(Initiator obj) {
        return null;
    }

    /**
     * 按条件查找
     *
     * @param filter 查找条件
     * @return 查找结果
     */
    @Override
    public List<Initiator> find(InitiatorFilter filter) {
        return null;
    }

    /**
     * 删除某一对象（按ID）
     *
     * @param id 要删除的对象ID
     * @return 是否删除成功
     */
    @Override
    public boolean delete(String id) {
        return false;
    }

    /**
     * 按ID查找对象
     *
     * @param id
     */
    @Override
    public Initiator findByID(String id) {
        return null;
    }
}
