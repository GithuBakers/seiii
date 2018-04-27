package cn.edu.nju.tagmakers.countsnju.logic.service;

import cn.edu.nju.tagmakers.countsnju.data.controller.CriterionController;
import cn.edu.nju.tagmakers.countsnju.entity.Criterion.Criterion;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Image;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Tag;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import cn.edu.nju.tagmakers.countsnju.exception.NotFoundException;
import cn.edu.nju.tagmakers.countsnju.exception.PermissionDeniedException;
import cn.edu.nju.tagmakers.countsnju.filter.CriterionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author xxz
 * Created on 04/22/2018
 */
@Component
public class InitiatorCriterionService {
    private InitiatorService initiatorService;

    private CriterionController criterionController;

    @Autowired
    public InitiatorCriterionService(InitiatorService initiatorService, CriterionController criterionController) {
        this.initiatorService = initiatorService;
        this.criterionController = criterionController;
    }

    /**
     * 添加一个标准集（已经添加过发起者ID）
     */
    public boolean addCriterion(Criterion criterion) {
        if (criterion == null) {
            throw new InvalidInputException("在 增加标准集 时参数为空");
        }
        if (criterion.getCriterionID() == null) {
            throw new InvalidInputException("标准集ID为null");
        }
        if (criterion.getDataSet().size() == 0) {
            throw new InvalidInputException("数据集为空");
        }

        criterionController.add(criterion);
        return true;
    }

    /**
     * 获取某一发起者自己发起的所有标准集
     */
    public List<Criterion> getMyCriterion(String initiatorID) {
        if (initiatorID == null) {
            throw new InvalidInputException("在 发起者获取自己的结果集 时参数为空");
        }
        CriterionFilter filter = new CriterionFilter();
        filter.setInitiatorID(initiatorID);
        return criterionController.find(filter);
    }

    /**
     * 获取所有的标准集
     */
    public List<Criterion> getAllCriterion() {
        return criterionController.find(null);
    }

    /**
     * 发起者做自己的标准集-获取图片
     * 权限检查
     */
    public List<Bare> getCriterionBare(String criterionID, String initiatorID) {
        if (criterionID == null || initiatorID == null) {
            throw new InvalidInputException("在 获取标准集图片 时参数为空");
        } else {
            Criterion criterion = criterionController.findByID(criterionID);
            if (criterion == null) {
                throw new NotFoundException("没有此标准集");
            }
            if (criterion.getInitiatorID() == null || !criterion.getInitiatorID().equals(initiatorID)) {
                throw new PermissionDeniedException("这不是你发起的标准集");
            } else {
                return getBares(criterion);
            }
        }
    }

    /**
     * 发起者向自己的标准集提交答案
     * 权限检查
     */
    public boolean submitImage(String criterionID, String initiatorID, Image image) {
        if (criterionID == null || initiatorID == null || image == null) {
            throw new InvalidInputException("在 向标准集提交图片 时参数为空");
        } else {
            Criterion criterion = criterionController.findByID(criterionID);
            if (criterion == null) {
                throw new NotFoundException("没有此标准集");
            }

            if (!criterion.getInitiatorID().equals(initiatorID)) {
                throw new PermissionDeniedException("这不是你发起的标准集");
            } else {
                Map<String, List<Tag>> answer = criterion.getResult();

                if (image.getBare() == null || image.getBare().getId() == null) {
                    throw new InvalidInputException("在 向标准集提交图片 时,图片信息不完整");
                }

                answer.put(image.getBare().getId(), image.getTags());
                criterion.setResult(answer);
                if (answer.size() == criterion.getDataSet().size()) {
                    //如果发起者已经完成了标准集中的所有图片
                    criterion.setHasFinished(true);
                }
                criterionController.update(criterion);
            }
        }
        return true;
    }

    /**
     * 选取图片的算法
     */
    private List<Bare> getBares(Criterion criterion) {
        List<Bare> ret = criterion.getDataSet();
        Map<String, List<Tag>> answer = criterion.getResult();
        return ret.stream()
                .filter(bare -> !answer.keySet().contains(bare.getId()))
                .collect(Collectors.toList());

    }
}
