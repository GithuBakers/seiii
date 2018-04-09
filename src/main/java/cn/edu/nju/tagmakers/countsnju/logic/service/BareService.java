package cn.edu.nju.tagmakers.countsnju.logic.service;

import cn.edu.nju.tagmakers.countsnju.data.controller.BareController;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.filter.BareFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import util.Counter;

import java.util.List;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * Description:
 * 不含标注的图片的逻辑对象
 *
 * @author xxz
 * Created on 03/17/2018
 * <p>
 * update: 填充了类内方法
 * @author xxz
 * modified on 03/21/2018
 * <p>
 * Update:
 * 自己写图片ID
 * @author xxz
 * Created on 04/06/2018
 */
@Component
@Scope(
        value = SCOPE_PROTOTYPE,
        proxyMode = ScopedProxyMode.TARGET_CLASS
)
public class BareService {

    private final BareController bareController;

    @Autowired
    public BareService(BareController bareController) {
        this.bareController = bareController;
    }

    public void addBare(Bare bare) {
        //todo:记得调用这个方法
        bare.setId(Counter.getBareCounter());
        bareController.add(bare);
    }

    public Bare updateBare(Bare bare) {
        return bareController.update(bare);
    }

    public List<Bare> findBare(BareFilter filter) {
        return bareController.find(filter);
    }

    public Bare findBareByID(String bareID) {
        return bareController.findByID(bareID);
    }

}
