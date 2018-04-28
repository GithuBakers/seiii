package cn.edu.nju.tagmakers.countsnju.logic;

import cn.edu.nju.tagmakers.countsnju.CountsnjuApplication;
import cn.edu.nju.tagmakers.countsnju.entity.Task;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;
import cn.edu.nju.tagmakers.countsnju.entity.user.Initiator;
import cn.edu.nju.tagmakers.countsnju.entity.user.Role;
import cn.edu.nju.tagmakers.countsnju.entity.user.Sex;
import cn.edu.nju.tagmakers.countsnju.entity.vo.InitiatorTaskDetailVO;
import cn.edu.nju.tagmakers.countsnju.entity.vo.InitiatorTaskVO;
import cn.edu.nju.tagmakers.countsnju.exception.NotFoundException;
import cn.edu.nju.tagmakers.countsnju.exception.PermissionDeniedException;
import cn.edu.nju.tagmakers.countsnju.filter.TaskFilter;
import cn.edu.nju.tagmakers.countsnju.logic.service.InitiatorService;
import cn.edu.nju.tagmakers.countsnju.logic.service.TaskService;
import cn.edu.nju.tagmakers.countsnju.security.SecurityUserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.testng.Assert.*;

@ContextConfiguration(classes = CountsnjuApplication.class)
@SpringBootTest
public class InitiatorServiceTest extends AbstractTestNGSpringContextTests {
    @Autowired
    InitiatorService initiatorService;

    @Autowired
    SecurityUserController securityUserController;

    @Autowired
    TaskService taskService;

    private Initiator testInitiator;
    private Task testTask;

    @BeforeClass
    public void setUp() {
        testInitiator = new Initiator();
        testInitiator.setUserID("wym");
        testInitiator.setNickName("小温");
        testInitiator.setPassword("123");
        testInitiator.setAvatar("没有头像");
        testInitiator.setRole(Role.INITIATOR);
        testInitiator.setSex(Sex.NA);
        testInitiator.setBirthdayString("1998-02-27");

        Bare bare1 = new Bare();
        bare1.setId("图1" + new Random(System.currentTimeMillis()).nextInt());
        Bare bare2 = new Bare();
        bare2.setId("图2" + new Random(System.currentTimeMillis() + 10086).nextInt());
        List<Bare> dataSet = new ArrayList<>();
        dataSet.add(bare1);
        dataSet.add(bare2);

        testTask = new Task();
        testTask.setTaskID(String.valueOf(new Random(System.currentTimeMillis()).nextInt()));
        testTask.setInitiatorName("wym");
        testTask.setTaskName("TaskService for test");
        testTask.setType(MarkType.DESC);
        testTask.setAim(100);
        testTask.setFinished(false);
        testTask.setLimit(2);
        testTask.setCover("no cover");
        testTask.setReward(10);
        testTask.setRequirement("打倒辣鸡翔哲");
        testTask.setResult("no result");
        testTask.setDataSet(dataSet);
        testTask.setType(MarkType.DESC);

        //先注册
        try {
            securityUserController.signUp(testInitiator);
        } catch (PermissionDeniedException e) {
            //如果测试之前跑过了，那么用户重复注册可能会引起异常
            System.out.println("继续！");
        }
    }

    @Test
    //查找存在的发起者
    public void findTest1() {
        Initiator temp = initiatorService.findInitiatorByName(testInitiator.getUserID());
        assertEquals(temp.getAvatar(), "没有头像");
    }

    @Test
    //查找不存在的发起者
    public void findTest2() {
        Initiator temp = initiatorService.findInitiatorByName("不存在");
        assertNull(temp);
    }

    @Test
    //发起任务
    public void createTaskTest1() {
        assertTrue(initiatorService.createTask(testTask));

    }

    @Test(dependsOnMethods = "createTaskTest1", expectedExceptions = PermissionDeniedException.class)
    //重复发起任务
    public void createTaskTest2() {
        initiatorService.createTask(testTask);
    }

    @Test
    //正常更新用户
    public void updateInitiatorTest1() {
        testInitiator.setNickName("巨无霸");
        initiatorService.update(testInitiator);
        Initiator temp = initiatorService.findInitiatorByName(testInitiator.getUserID());
        assertEquals(temp.getNickName(), "巨无霸");
    }

//    @Test(dependsOnMethods = "createTaskTest1")
//    //查找任务
//    public void findTaskTest1() {
//        Task temp = initiatorService.findTaskByName(testTask.getPrimeKey(), testInitiator.getPrimeKey());
//        assertEquals(temp.getResult(), testTask.getResult());
//    }

//    @Test(expectedExceptions = PermissionDeniedException.class)
//    //查找不存在的任务
//    public void findTaskTest2() {
//        Task temp = initiatorService.findTaskByName("不存在", testInitiator.getPrimeKey());
//        assertNull(temp);
//    }

    //查找任务列表
    @Test(dependsOnMethods = "createTaskTest1")
    public void findTaskTest3() {
        TaskFilter filter = new TaskFilter();
        filter.setInitiatorName(testInitiator.getPrimeKey());
        List<InitiatorTaskVO> taskList = initiatorService.findInitiatorTask(filter);
        boolean flag = taskList.size() > 0;
        assertTrue(flag);
    }

    @Test(dependsOnMethods = "createTaskTest1", expectedExceptions = PermissionDeniedException.class)
    //通过不存在的发起者结束任务
    public void ownerTest1() {
        Initiator temp = new Initiator();
        temp.setUserID("fake owner");
        temp.setRole(Role.INITIATOR);
        temp.setBirthdayString("1998-12-23");
        securityUserController.signUp(temp);
        initiatorService.finishTask(testTask.getPrimeKey(), temp.getPrimeKey());
    }

    @Test(dependsOnMethods = "createTaskTest1")
    //正确的发起者结束任务
    public void ownerTest2() {
        initiatorService.finishTask(testTask.getPrimeKey(), testInitiator.getPrimeKey());
    }
}
