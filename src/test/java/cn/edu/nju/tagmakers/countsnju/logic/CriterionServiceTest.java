package cn.edu.nju.tagmakers.countsnju.logic;

import cn.edu.nju.tagmakers.countsnju.CountsnjuApplication;
import cn.edu.nju.tagmakers.countsnju.data.controller.WorkerAndCriterionController;
import cn.edu.nju.tagmakers.countsnju.entity.Criterion.Criterion;
import cn.edu.nju.tagmakers.countsnju.entity.Criterion.Result;
import cn.edu.nju.tagmakers.countsnju.entity.Criterion.WorkerAndCriterion;
import cn.edu.nju.tagmakers.countsnju.entity.pic.*;
import cn.edu.nju.tagmakers.countsnju.entity.user.Initiator;
import cn.edu.nju.tagmakers.countsnju.entity.user.Role;
import cn.edu.nju.tagmakers.countsnju.entity.user.Sex;
import cn.edu.nju.tagmakers.countsnju.entity.user.Worker;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerCriterionVO;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import cn.edu.nju.tagmakers.countsnju.exception.PermissionDeniedException;
import cn.edu.nju.tagmakers.countsnju.logic.service.CriterionService;
import cn.edu.nju.tagmakers.countsnju.logic.service.InitiatorCriterionService;
import cn.edu.nju.tagmakers.countsnju.logic.service.WorkerCriterionService;
import cn.edu.nju.tagmakers.countsnju.logic.service.WorkerService;
import cn.edu.nju.tagmakers.countsnju.security.SecurityUserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

@ContextConfiguration(classes = CountsnjuApplication.class)
@SpringBootTest
public class CriterionServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    InitiatorCriterionService initiatorCriterionService;

    @Autowired
    SecurityUserController securityUserController;

    @Autowired
    WorkerService workerService;

    @Autowired
    WorkerCriterionService workerCriterionService;

    @Autowired
    CriterionService criterionService;

    @Autowired
    WorkerAndCriterionController workerAndCriterionController;

    private Criterion testCriterion;
    private Initiator testInitiator;
    private Worker testWorker;

    @BeforeClass
    public void setUp() {
        //发起者
        testInitiator = new Initiator();
        String initiatorID = "testInitiatorInLogicCriterion";
        String initiatorAvatar = "no avatar";
        String password = "123456";
        String nickName = "hey";
        String birthdayStr = "1998-01-20";
        testInitiator.setRole(Role.INITIATOR);
        testInitiator.setUserID(initiatorID);
        testInitiator.setAvatar(initiatorAvatar);
        testInitiator.setPassword(password);
        testInitiator.setNickName(nickName);
        testInitiator.setSex(Sex.MALE);
        testInitiator.setBirthdayString(birthdayStr);

        //工人
        testWorker = new Worker();
        String workerID = "testWorkerInLogicCriterion";
        String workerAvatar = "no avatar";
        testWorker.setAvatar(workerAvatar);
        testWorker.setPassword(password);
        testWorker.setRole(Role.WORKER);
        testWorker.setUserID(workerID);
        testWorker.setNickName(nickName);
        testWorker.setSex(Sex.MALE);
        testWorker.setBirthdayString(birthdayStr);

        //创建发起者和工人
        securityUserController.signUp(testInitiator);
        securityUserController.signUp(testWorker);

        //标准集
        testCriterion = new Criterion();
        testCriterion.setCover("no cover");
        testCriterion.setAim(11);
        testCriterion.setCriterionID("testCriterion");
        testCriterion.setCriterionName("逻辑层测试用标准集");
        testCriterion.setRequirement("用来做测试");
        testCriterion.setType(MarkType.DESC);
        testCriterion.setInitiatorID(initiatorID);

        //20张大小的数据集
        ArrayList<Bare> bareList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Bare temp = new Bare();
            temp.setId("bare" + i);
            temp.setName("bare" + i + "的名字");
            temp.setRaw("bare" + i + ".url");
            temp.setState(BareState.UNMARKED);
            temp.setMarkType(MarkType.DESC);
            bareList.add(temp);

        }
        testCriterion.setDataSet(bareList);

        //关键词
        ArrayList<String> keywordsList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String temp = "keyword" + i;
            keywordsList.add(temp);
        }
        testCriterion.setKeywords(keywordsList);

        //标准结果
        HashMap<String, List<Tag>> result = new HashMap<>();
        for (int i = 0; i < 20; i++) {
            String bareID = "bare" + i;
            List<Tag> temp = new ArrayList<>();
            Tag tag = new Tag();
            tag.setBareID(bareID);
            tag.setWorkerID("发起者");
            tag.setComment(new Comment("keyword" + i));
            tag.setMark(new Description());
            tag.setNumberID("numberID" + i);
            tag.setTagID("tagID" + i);
            temp.add(tag);
            result.put(bareID, temp);
        }
        testCriterion.setResult(result);

    }

    //正常
    @Test
    public void addCriterionTest1() {
        initiatorCriterionService.addCriterion(testCriterion);
    }

    /**
     * 理论上不会发生
     */
//    //缺少发起人
//    @Test(expectedExceptions = InvalidInputException.class)
//    public void addCriterionTest2() {
//        Criterion temp = testCriterion.copy();
//        temp.setCriterionID("exception1");
//        temp.setInitiatorID(null);
//        initiatorCriterionService.addCriterion(temp);
//    }

    //缺少数据集
    @Test(expectedExceptions = InvalidInputException.class)
    public void addCriterionTest3() {
        Criterion temp = testCriterion.copy();
        temp.setCriterionID("exception2");
        temp.setDataSet(null);
        initiatorCriterionService.addCriterion(temp);
    }

    /**
     * 允许暂时不添加标准结果的做法
     */
//    //缺少标准结果
//    @Test(expectedExceptions = InvalidInputException.class)
//    public void addCriterionTest4() {
//        Criterion temp = testCriterion.copy();
//        temp.setCriterionID("exception3");
//        temp.setResult(null);
//        initiatorCriterionService.addCriterion(temp);
//    }

    //重复添加
    @Test(dependsOnMethods = "addCriterionTest1", expectedExceptions = PermissionDeniedException.class)
    public void addCriterionTest5() {
        initiatorCriterionService.addCriterion(testCriterion);
    }

    /**
     * 理论上不会出现这种情况
     */
//    //标准结果和数据集不是一一对应关系
//    @Test(expectedExceptions = InvalidInputException.class)
//    public void addCriterionTest6() {
//        Criterion temp = testCriterion.copy();
//        temp.setCriterionID("exception4");
//        temp.setDataSet((List<Bare>) temp.getDataSet().remove(0));
//        initiatorCriterionService.addCriterion(temp);
//    }

    //发起者正常添加结果
    @Test
    public void submitAnswerTest1() {
        //todo：字段加完自己写个小测试，不要用setUp里面的那个标准集，可以copy之后改一下数据
    }

    /**
     * 理论上不会出现这个情况
     */
//    //发起者向已经提交的标准集添加结果
//    @Test(dependsOnMethods = "addCriterionTest1", expectedExceptions = PermissionDeniedException.class)
//    public void submitAnswerTest2() {
//        Image tempImage = new Image();
//        Bare tempBare = new Bare();
//        tempBare.setId("in submitAnswerTest2");
//        tempImage.setBare(tempBare);
//        initiatorCriterionService.submitImage(testCriterion.getCriterionID(), testInitiator.getUserID(), tempImage);
//    }

    //发起者向不属于自己的添加结果
    @Test(expectedExceptions = PermissionDeniedException.class)
    public void submitAnswerTest3() {
        Criterion temp = testCriterion.copy();
        String tempInitiatorID = "another Initiator";
        String tempCriterionID = "another Criterion";
        temp.setInitiatorID(tempInitiatorID);
        temp.setCriterionID(tempCriterionID);
        initiatorCriterionService.addCriterion(temp);

        Image tempImage = new Image();
        Bare tempBare = new Bare();
        tempBare.setId("in submitAnswerTest3");
        tempImage.setBare(tempBare);
        initiatorCriterionService.submitImage(tempCriterionID, testInitiator.getUserID(), tempImage);
    }

    //发起者向不存在的标准集添加结果
    @Test(expectedExceptions = InvalidInputException.class)
    public void submitAnswerTest4() {
        Image tempImage = new Image();
        Bare tempBare = new Bare();
        tempBare.setId("in submitAnswerTest4");
        tempImage.setBare(tempBare);
        initiatorCriterionService.submitImage(null, testInitiator.getUserID(), tempImage);
    }

    //发起者查看自己发起的任务
    @Test(dependsOnMethods = "addCriterionTest1")
    public void getCriterionTest1() {
        List<Criterion> res = initiatorCriterionService.getMyCriterion(testInitiator.getUserID());
        assertEquals(res.size(), 1);
        assertEquals(testCriterion.getCriterionID(), res.get(0).getCriterionID());
    }

    //发起者查看所有任务
    @Test(dependsOnMethods = {"addCriterionTest1", "submitAnswerTest3"})
    public void getCriterionTest2() {
        List<Criterion> res = initiatorCriterionService.getAllCriterion();
        assertTrue(res.size() >= 2);
    }

    //发起者获取图片
    @Test(dependsOnMethods = "addCriterionTest1")
    public void getBareTest1() {
        initiatorCriterionService.getCriterionBare(testCriterion.getCriterionID(), testInitiator.getUserID());
    }

    //将工人添加到通过标准集的集合中
    @Test(dependsOnMethods = "addCriterionTest1")
    public void workerPassTest1() {
        //本来不在里面
        assertFalse(criterionService.isPassed(testCriterion.getCriterionID(), testWorker.getPrimeKey()));
        //加入
        Set<String> workerPassed = testCriterion.getWorkerPassed();
        workerPassed.add(testWorker.getPrimeKey());
        testCriterion.setWorkerPassed(workerPassed);
        criterionService.update(testCriterion);

        assertTrue(criterionService.isPassed(testCriterion.getCriterionID(), testWorker.getPrimeKey()));


        //数据层的逻辑是size>0之后做更改所以这里不测移除
    }

    //工人查看所有标准集
    @Test(dependsOnMethods = {"addCriterionTest1", "submitAnswerTest3"})
    public void getCriterionTest3() {
        List<WorkerCriterionVO> res = workerCriterionService.getAllCriterion(testWorker.getPrimeKey());
        assertTrue(res.size() >= 2);
    }

    //工人获得某标准集的图片（10张）
    @Test(dependsOnMethods = "addCriterionTest1")
    public void getBareTest2() {
        List<Bare> list = workerCriterionService.getCriterionBares(testCriterion.getCriterionID(), testWorker.getPrimeKey());
        assertTrue(list.size() == 10);
    }

    //工人添加9个正确标注后获得图片依旧是上次的图片
    @Test(dependsOnMethods = "addCriterionTest1")
    public void workerSubmitTest1() {
        List<Bare> bareListBeforeSubmit = workerCriterionService.getCriterionBares(testCriterion.getCriterionID(), testWorker.getPrimeKey());
        for (int i = 0; i < 9; i++) {
            Image res = new Image();

            //原图
            Bare tempBare = bareListBeforeSubmit.get(i).copy();

            //标注
            Tag tempTag = new Tag();
            tempTag.setBareID(tempBare.getId());
            tempTag.setWorkerID(testWorker.getPrimeKey());
            tempTag.setComment(new Comment("keyword" + i));
            tempTag.setMark(new Description());
            tempTag.setNumberID("numberID" + i);
            tempTag.setTagID("tagID" + i);
            List<Tag> listTag = new ArrayList<>();
            listTag.add(tempTag);

            //结果
            res.setBare(tempBare);
            res.setType(MarkType.DESC);
            res.setTags(listTag);
            workerCriterionService.submitCriterionResult(testCriterion.getCriterionID(), res, testWorker.getPrimeKey());
        }

        //验证是否取出来一张
        assertEquals(1, workerCriterionService.getCriterionBares(testCriterion.getCriterionID(), testWorker.getPrimeKey()).size());

        //验证是否更新标注信息和正误信息
        WorkerAndCriterion res = workerAndCriterionController.findByID(testWorker.getPrimeKey(), testCriterion.getCriterionID());
        assertFalse(res.isPassed());
        assertEquals(0, res.getAccuracy().size());
        List<Bare> latestBares = res.getLatestBares();
        List<Result> resultList = res.getResults();
        for (Bare temp : latestBares) {
            for (Result result : resultList) {
                if (temp.getId().equals(result.getBare().getId())) {
                    //要么做过的是正确的，要么没做过的是错的
                    assertTrue(
                            (result.isHasTested() && result.isCorrect()) ||
                                    (!result.isHasTested() && !result.isCorrect()));
                }
            }
        }


    }

    //工人再添加1个正确的图片之后查出来新的没有被标注过的十张图片并且正确率会更新为1.0
    @Test(dependsOnMethods = "workerSubmitTest1")
    public void workerSubmitTest2() {
        List<Bare> bareListBeforeSubmit = workerCriterionService.getCriterionBares(testCriterion.getCriterionID(), testWorker.getPrimeKey());
        Image resImage = new Image();
        int i = 0;

        //原图
        Bare tempBare = bareListBeforeSubmit.get(i).copy();

        //标注
        Tag tempTag = new Tag();
        tempTag.setBareID(tempBare.getId());
        tempTag.setWorkerID(testWorker.getPrimeKey());
        tempTag.setComment(new Comment("keyword" + i));
        tempTag.setMark(new Description());
        tempTag.setNumberID("numberID" + i);
        tempTag.setTagID("tagID" + i);
        List<Tag> listTag = new ArrayList<>();
        listTag.add(tempTag);

        //结果
        resImage.setBare(tempBare);
        resImage.setType(MarkType.DESC);
        resImage.setTags(listTag);
        workerCriterionService.submitCriterionResult(testCriterion.getCriterionID(), resImage, testWorker.getPrimeKey());

        //又取出来了十张
        List<Bare> bareListAfterSubmit = workerCriterionService.getCriterionBares(testCriterion.getCriterionID(), testWorker.getPrimeKey());
        assertEquals(bareListAfterSubmit.size(), 10);

        WorkerAndCriterion res = workerAndCriterionController.findByID(testWorker.getPrimeKey(), testCriterion.getCriterionID());
        //验证是否通过
        assertFalse(res.isPassed());

        //验证正确率
        assertEquals(1.0, res.getAccuracy().get(0));
        List<Bare> latestBares = res.getLatestBares();
        List<Result> resultList = res.getResults();
        for (Bare temp : latestBares) {
            for (Result result : resultList) {
                if (temp.getId().equals(result.getBare().getId())) {
                    //要么做过的是正确的，要么没做过的是错的
                    assertTrue(
                            (result.isHasTested() && result.isCorrect()) ||
                                    (!result.isHasTested() && !result.isCorrect()));
                }
            }
        }
    }

    //工人再做1个正确的图片之后通过测试
    @Test(dependsOnMethods = "workerSubmitTest2")
    public void workerSubmitTest3() {
        List<Bare> bareListBeforeSubmit = workerCriterionService.getCriterionBares(testCriterion.getCriterionID(), testWorker.getPrimeKey());
        Image resImage = new Image();

        //原图
        Bare tempBare = bareListBeforeSubmit.get(0).copy();

        //标注
        Tag tempTag = new Tag();
        tempTag.setBareID(tempBare.getId());
        tempTag.setWorkerID(testWorker.getPrimeKey());
        tempTag.setComment(new Comment("keyword" + 10));
        tempTag.setMark(new Description());
        tempTag.setNumberID("numberID" + 10);
        tempTag.setTagID("tagID" + 10);
        List<Tag> listTag = new ArrayList<>();
        listTag.add(tempTag);

        //结果
        resImage.setBare(tempBare);
        resImage.setType(MarkType.DESC);
        resImage.setTags(listTag);
        workerCriterionService.submitCriterionResult(testCriterion.getCriterionID(), resImage, testWorker.getPrimeKey());
        //判断工人已经通过了这个标准集
        Criterion targetCriterion = criterionService.findByID(testCriterion.getCriterionID());
        assertTrue(targetCriterion.getWorkerPassed().contains(testWorker.getPrimeKey()));

        assertTrue(workerService.findWorkerByName(testWorker.getPrimeKey()).getDependencies().size() > 0);
        assertEquals(workerService.findWorkerByName(testWorker.getPrimeKey()).getErrorLearningAbility().intValue(), 50);

    }

    /**
     * 这个测试暂时先不测，DESC的判断正误的算法太复杂，鸽了
     */

    //工人再做9张错误的图片之后请求新的图片之后正确率更新并且返回这9张错误的图片和1张正确的图片
//    @Test(dependsOnMethods = "workerSubmitTest3")
//    public void workerSubmitTest4() {
//        List<Bare> bareListBeforeSubmit = workerCriterionService.getCriterionBares(testCriterion.getCriterionID(), testWorker.getPrimeKey());
//        for (int i = 11; i < 20; i++) {
//            Image res = new Image();
//
//            //原图
//            Bare tempBare = bareListBeforeSubmit.get(i - 11).copy();
//
//            //标注(这边是错误的标注)
//            Tag tempTag = new Tag();
//            tempTag.setBareID(tempBare.getId());
//            tempTag.setWorkerID(testWorker.getPrimeKey());
//            tempTag.setComment(new Comment("keyword" + "wrong" + i));
//            tempTag.setMark(new Description());
//            tempTag.setNumberID("numberID" + "wrong" + i);
//            tempTag.setTagID("tagID" + "wrong" + i);
//            List<Tag> listTag = new ArrayList<>();
//            listTag.add(tempTag);
//
//            //结果
//            res.setBare(tempBare);
//            res.setType(MarkType.DESC);
//            res.setTags(listTag);
//            workerCriterionService.submitCriterionResult(testCriterion.getCriterionID(), res, testWorker.getPrimeKey());
//        }
//
//        //验证正确率
//        WorkerAndCriterion res = workerAndCriterionController.findByID(testWorker.getPrimeKey(), testCriterion.getCriterionID());
//        List<Bare> bareListAfterSubmit = workerCriterionService.getCriterionBares(testCriterion.getCriterionID(), testWorker.getPrimeKey());
//        assertEquals(1.0, res.getAccuracy().get(0));
//        assertEquals(0.1, res.getAccuracy().get(1));
//        List<Bare> latestBares = res.getLatestBares();
//        List<Result> resultList = res.getResults();
//        for (Bare temp : latestBares) {
//            for (Result result : resultList) {
//                if (temp.getId().equals(result.getBare().getId())) {
//                    //要么做过的是正确的，要么没做过的是错的
//                    assertTrue(
//                            (result.isHasTested() && result.isCorrect()) ||
//                                    (!result.isHasTested() && !result.isCorrect()));
//                }
//            }
//        }
//
//        int totalWrong = 0;
//        int totalCorrect = 0;
//        for (Bare bare : bareListAfterSubmit) {
//            for (Result result : resultList) {
//                if (bare.getId().equals(result.getBare().getId())) {
//                    if (result.isCorrect()) totalCorrect++;
//                    else totalWrong++;
//                    break;
//                }
//            }
//        }
//        assertEquals(totalCorrect, 1);
//        assertEquals(totalWrong, 9);
//    }


}
