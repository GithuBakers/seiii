package cn.edu.nju.tagmakers.countsnju.logic.controller;

import cn.edu.nju.tagmakers.countsnju.entity.Criterion.Criterion;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Image;
import cn.edu.nju.tagmakers.countsnju.exception.PermissionDeniedException;
import cn.edu.nju.tagmakers.countsnju.logic.service.InitiatorCriterionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import util.SecurityUtility;

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
    private InitiatorCriterionService criterionService;

    @Autowired
    public InitiatorCriterionRestController(InitiatorCriterionService criterionService) {
        this.criterionService = criterionService;
    }

    /**
     * 发起者创建标准集，记得要在里面加上发起者id
     */
    @RequestMapping(value = "/new_criterion", method = RequestMethod.POST)
    public boolean addCriterion(@RequestBody Criterion criterion) {
        criterion.setInitiatorID(SecurityUtility.getUserName(SecurityContextHolder.getContext()));
        return criterionService.addCriterion(criterion);
    }

    /**
     * 发起者查看自己创建的所有标准集
     */
    @RequestMapping(value = "/myself", method = RequestMethod.GET)
    public List<Criterion> getCriterion(@RequestParam("initiator") String initiator) {
        //以后定接口就不要这种指代自己的ID了吧
        if (!SecurityUtility.getUserName(SecurityContextHolder.getContext()).equals(initiator)) {
            throw new PermissionDeniedException("身份与你的登录身份不符");
        } else {
            return criterionService.getMyCriterion(initiator);
        }
    }

    /**
     * 发起者查看所有结果集
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Criterion> getAllCriterion() {
        return criterionService.getAllCriterion();
    }

    /**
     * 获取某一标准的图片
     * （用于让发起者做自己的标准）
     * 记得要检查身份是否是自己的标准
     */
    @RequestMapping(value = "/img", method = RequestMethod.GET)
    public List<Bare> getCriterionBare(@RequestParam("criterion_id") String criterion_id) {
        return criterionService.getCriterionBare(criterion_id,
                SecurityUtility.getUserName(SecurityContextHolder.getContext()));
    }

    /**
     * 发起者提交标准集中某一图片的标注结果
     * 记得要检查一下身份，是否是自己的标准集
     *
     * @param criterion_id 标准集ID
     */
    @RequestMapping(value = "img", method = RequestMethod.POST)
    public boolean submitImage(@RequestParam String criterion_id, @RequestBody Image image) {
        return criterionService.submitImage(criterion_id,
                SecurityUtility.getUserName(SecurityContextHolder.getContext()),
                image);
    }


}
