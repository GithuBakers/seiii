package cn.edu.nju.tagmakers.countsnju.logic.controller;

import cn.edu.nju.tagmakers.countsnju.entity.Criterion;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Image;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description:
 * 发起者标准集的controller
 *
 * @author xxz
 * Created on 04/19/2018
 */
@RestController
@RequestMapping("initiator/criterion")
public class InitiatorCriterionRestController {

    /**
     * 发起者创建标准集，记得要在里面加上发起者id
     */
    @RequestMapping(value = "/new_criterion", method = RequestMethod.POST)
    public boolean addCriterion(@RequestBody Criterion criterion) {
        throw new UnsupportedOperationException();
    }

    /**
     * 发起者查看自己创建的所有标准集
     */
    @RequestMapping(value = "/myself", method = RequestMethod.GET)
    public List<Criterion> getCriterion(@RequestParam("initiator") String initiator) {
        throw new UnsupportedOperationException();

    }

    /**
     * 发起者查看所有结果集
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Criterion> getAllCriterion() {
        throw new UnsupportedOperationException();

    }

    /**
     * 获取某一标准的图片
     * （用于让发起者做自己的标准）
     * 记得要检查身份是否是自己的标准
     */
    @RequestMapping(value = "/img", method = RequestMethod.GET)
    public List<Bare> getCriterionBare(@RequestParam("criterion_id") String criterion_id) {
        throw new UnsupportedOperationException();
    }

    /**
     * 发起者提交标准集中某一图片的标注结果
     * 记得要检查一下身份，是否是自己的标准集
     *
     * @param criterion_id 标准集ID
     */
    @RequestMapping(value = "img", method = RequestMethod.POST)
    public boolean submitImage(@RequestParam String criterion_id, @RequestBody Image image) {
        throw new UnsupportedOperationException();
    }


}
