package cn.edu.nju.tagmakers.countsnju.logic.controller;

import cn.edu.nju.tagmakers.countsnju.algorithm.ResultJudger;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Image;
import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;
import cn.edu.nju.tagmakers.countsnju.entity.vo.CriterionImageAnswerVO;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerCriterionVO;
import cn.edu.nju.tagmakers.countsnju.logic.service.CriterionService;
import cn.edu.nju.tagmakers.countsnju.logic.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType.DESC;

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
    private final WorkerService workerService;

    @Autowired
    public WorkerCriterionRestController(WorkerService workerService) {
        this.workerService = workerService;
    }

    /**
     * 工人查看所有的标准集
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<WorkerCriterionVO> getAllCriterion() {
        return workerService.getAllCriterion();
    }

    /**
     * 工人获取某一标准集的图片
     */
    @RequestMapping(value = "/img", method = RequestMethod.GET)
    public List<Bare> getBares(@RequestParam("criterion_id") String criterion_id) {
        return workerService.getCriterionBares(criterion_id);
    }

    /**
     * 工人提交某一张图片
     * 注意，不需要保存这个结果，仅需要保存对/错即可
     * 始终返回正确答案，并且在答案中指明对错
     */
    @RequestMapping(value = "/img", method = RequestMethod.POST)
    public CriterionImageAnswerVO submitImage(@RequestParam("criterion_id") String criterionID, @RequestBody Image image) {

        return workerService.submitCriterionResult(criterionID, image);
    }

}
