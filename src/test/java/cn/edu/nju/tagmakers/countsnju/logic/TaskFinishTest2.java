package cn.edu.nju.tagmakers.countsnju.logic;

import cn.edu.nju.tagmakers.countsnju.CountsnjuApplication;
import cn.edu.nju.tagmakers.countsnju.data.controller.WorkerAndCriterionController;
import cn.edu.nju.tagmakers.countsnju.entity.Task;
import cn.edu.nju.tagmakers.countsnju.entity.pic.*;
import cn.edu.nju.tagmakers.countsnju.entity.user.Initiator;
import cn.edu.nju.tagmakers.countsnju.entity.user.Role;
import cn.edu.nju.tagmakers.countsnju.entity.user.Sex;
import cn.edu.nju.tagmakers.countsnju.entity.user.Worker;
import cn.edu.nju.tagmakers.countsnju.logic.service.CriterionService;
import cn.edu.nju.tagmakers.countsnju.logic.service.InitiatorService;
import cn.edu.nju.tagmakers.countsnju.logic.service.WorkerCriterionService;
import cn.edu.nju.tagmakers.countsnju.logic.service.WorkerService;
import cn.edu.nju.tagmakers.countsnju.security.SecurityUserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@ContextConfiguration(classes = CountsnjuApplication.class)
@SpringBootTest
public class TaskFinishTest2 extends AbstractTestNGSpringContextTests {
    @Autowired
    InitiatorService initiatorService;
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

    private Task testTask;
    private Initiator testInitiator;
    private Worker testWorker;

    @BeforeClass
    public void setUp() {
        //发起者
        testInitiator = new Initiator();
        String initiatorID = "testInitiatorInTaskFinish";
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
        String workerID = "testWorkerInTaskFinish";
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
        testTask = new Task();
        testTask.setCover("no cover");
        testTask.setAim(11);
        testTask.setTaskID("testCriterionInTaskFinish");
        testTask.setTaskName("逻辑层测试用标准集");
        testTask.setRequirement("用来做测试");
        testTask.setType(MarkType.DESC);
        testTask.setInitiatorName(initiatorID);
        testTask.setLimit(100);

        //20张大小的数据集
        ArrayList<Bare> bareList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Bare temp = new Bare();
            temp.setId("bareInTaskFinish" + i);
            temp.setName("bareInTaskFinish" + i + "的名字");
            temp.setRaw("bareInTaskFinish" + i + ".url");
            temp.setState(BareState.UNMARKED);
            temp.setMarkType(MarkType.DESC);
            bareList.add(temp);

        }
        testTask.setDataSet(bareList);

        initiatorService.createTask(testTask);
        workerService.receiveTask(testTask.getTaskID(), testWorker.getPrimeKey());
    }

    @Test
    public void submitAnswer() {
        for (int i = 0; i < 20; i++) {
            Image image = new Image();
            String bareID = "bareInTaskFinish" + i;
            Bare bare = new Bare();
            bare.setId(bareID);
            bare.setMarkType(MarkType.DESC);
            List<Tag> tagList = new ArrayList<>();
            Tag tag = new Tag();
            tag.setBareID(bareID);
            tag.setWorkerID(testWorker.getPrimeKey());
            tag.setComment(new Comment("keyword" + i));
            tag.setMark(new Description());
            tag.setNumberID("numberID" + i);
            tag.setTagID("tagID" + i);
            tagList.add(tag);
            image.setTags(tagList);
            image.setType(MarkType.DESC);
            image.setBare(bare);
            workerService.submitTag(image, testTask.getTaskID(), testWorker.getPrimeKey());
        }

        initiatorService.finishTask(testTask.getTaskID(), testInitiator.getPrimeKey());
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initiatorService.getTaskResult(testTask.getTaskID(), testInitiator.getPrimeKey());
    }

}
