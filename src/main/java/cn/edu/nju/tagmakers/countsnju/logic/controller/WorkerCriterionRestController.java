package cn.edu.nju.tagmakers.countsnju.logic.controller;

import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Image;
import cn.edu.nju.tagmakers.countsnju.entity.vo.CriterionImageAnswerVO;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerCriterionVO;
import cn.edu.nju.tagmakers.countsnju.logic.service.WorkerCriterionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import util.SecurityUtility;

import java.util.List;

/**
 * Description:
 * 工人与标准集交互的controller
 *
 * @author xxz
 * Created on 04/21/2018
 */

@RestController
@RequestMapping("/worker/criterion")
public class WorkerCriterionRestController {
    private final WorkerCriterionService workerCriterionService;

    @Autowired
    public WorkerCriterionRestController(WorkerCriterionService workerCriterionService) {
        this.workerCriterionService = workerCriterionService;
    }

    /**
     * 工人查看所有的标准集
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<WorkerCriterionVO> getAllCriterion() {
        String workerID = SecurityUtility.getUserName(SecurityContextHolder.getContext());
        return workerCriterionService.getAllCriterion(workerID);
    }

    /**
     * 工人获取某一标准集的图片
     */
    @RequestMapping(value = "/img", method = RequestMethod.GET)
    public List<Bare> getBares(@RequestParam("criterion_id") String criterion_id) {
        String workerID = SecurityUtility.getUserName(SecurityContextHolder.getContext());
        return workerCriterionService.getCriterionBares(criterion_id, workerID);
    }

    /**
     * 工人提交某一张图片
     * 注意，不需要保存这个结果，仅需要保存对/错即可
     * 始终返回正确答案，并且在答案中指明对错
     */
    @RequestMapping(value = "/img", method = RequestMethod.POST)
    public CriterionImageAnswerVO submitImage(@RequestParam("criterion_id") String criterionID, @RequestBody Image image) {
        String workerID = SecurityUtility.getUserName(SecurityContextHolder.getContext());
        return workerCriterionService.submitCriterionResult(criterionID, image, workerID);
    }

}
