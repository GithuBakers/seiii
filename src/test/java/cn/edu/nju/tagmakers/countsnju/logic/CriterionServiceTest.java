package cn.edu.nju.tagmakers.countsnju.logic;

import cn.edu.nju.tagmakers.countsnju.CountsnjuApplication;
import cn.edu.nju.tagmakers.countsnju.entity.Criterion.Criterion;
import cn.edu.nju.tagmakers.countsnju.entity.pic.*;
import cn.edu.nju.tagmakers.countsnju.entity.user.Initiator;
import cn.edu.nju.tagmakers.countsnju.entity.user.Role;
import cn.edu.nju.tagmakers.countsnju.entity.user.Worker;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import cn.edu.nju.tagmakers.countsnju.logic.service.CriterionService;
import cn.edu.nju.tagmakers.countsnju.logic.service.InitiatorCriterionService;
import cn.edu.nju.tagmakers.countsnju.logic.service.InitiatorService;
import cn.edu.nju.tagmakers.countsnju.logic.service.WorkerService;
import cn.edu.nju.tagmakers.countsnju.security.SecurityUserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ContextConfiguration(classes = CountsnjuApplication.class)
@SpringBootTest
public class CriterionServiceTest {

    @Autowired
    InitiatorCriterionService initiatorCriterionService;

    @Autowired
    SecurityUserController securityUserController;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private CriterionService criterionService;

    private Criterion testCriterion;
    private Initiator testInitiator;
    private Worker testWorker;

    @BeforeSuite
    public void setUp() {
        //发起者
        testInitiator = new Initiator();
        String initiatorID = "testInitiatorInLogicCriterion";
        String initiatorAvatar = "no avatar";
        String password = "123456";
        String nickName = "hey";
        testInitiator.setRole(Role.INITIATOR);
        testInitiator.setUserID(initiatorID);
        testInitiator.setAvatar(initiatorAvatar);
        testInitiator.setPassword(password);
        testInitiator.setNickName(nickName);

        //工人
        testWorker = new Worker();
        String workerID = "testWorkerInLogicCriterion";
        String workerAvatar = "no avatar";
        testWorker.setAvatar(workerAvatar);
        testWorker.setPassword(password);
        testWorker.setRole(Role.WORKER);
        testWorker.setUserID(workerID);
        testWorker.setNickName(nickName);

        //创建发起者和工人
        securityUserController.signUp(testInitiator);
        securityUserController.signUp(testWorker);

        //标准集
        testCriterion = new Criterion();
        testCriterion.setCover("no cover");
        testCriterion.setAim(10);
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

    @Test
    public void addCriterionTest1() {
        initiatorCriterionService.addCriterion(testCriterion);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    public void addCriterionTest2() {
        Criterion temp = testCriterion.copy();
        temp.setInitiatorID(null);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    public void addCriterionTest3() {
        Criterion temp = testCriterion.copy();
        temp.setDataSet(null);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    public void addCriterionTest4() {
        Criterion temp = testCriterion.copy();
        temp.setResult(null);
    }

}
