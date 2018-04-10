package cn.edu.nju.tagmakers.countsnju.logic;

import cn.edu.nju.tagmakers.countsnju.CountsnjuApplication;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;
import cn.edu.nju.tagmakers.countsnju.entity.user.Initiator;
import cn.edu.nju.tagmakers.countsnju.entity.user.Role;
import cn.edu.nju.tagmakers.countsnju.entity.user.Task;
import cn.edu.nju.tagmakers.countsnju.entity.vo.InitiatorTaskVO;
import cn.edu.nju.tagmakers.countsnju.exception.NotFoundException;
import cn.edu.nju.tagmakers.countsnju.exception.PermissionDeniedException;
import cn.edu.nju.tagmakers.countsnju.filter.TaskFilter;
import cn.edu.nju.tagmakers.countsnju.logic.service.TaskService;
import cn.edu.nju.tagmakers.countsnju.logic.service.InitiatorService;
import cn.edu.nju.tagmakers.countsnju.security.SecurityUserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

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

    @BeforeSuite
    public void setUp() {
        testInitiator = new Initiator();
        testInitiator.setUserID("wym");
        testInitiator.setNickName("小温");
        testInitiator.setPassword("123");
        testInitiator.setAvatar("没有头像");
        testInitiator.setRole(Role.INITIATOR);

        Bare bare1 = new Bare();
        bare1.setId("图1");
        Bare bare2 = new Bare();
        bare2.setId("图2");
        List<Bare> dataSet = new ArrayList<>();
        dataSet.add(bare1);
        dataSet.add(bare2);

        testTask = new Task();
        testTask.setInitiatorName("wym");
        testTask.setTaskName("TaskService for test");
        testTask.setType(MarkType.DESC);
        testTask.setAim(100);
        testTask.setFinished(false);
        testTask.setLimit(2);
        testTask.setCover("no cover");
        testTask.setReward(10);
        testTask.setInitiatorName("xxz");
        testTask.setRequirement("打倒辣鸡翔哲");
        testTask.setResult("no result");
        testTask.setDataSet(dataSet);
        testTask.setType(MarkType.DESC);

        //先注册
        securityUserController.signUp(testInitiator);
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
        Initiator temp = initiatorService.findInitiatorByName(testInitiator.getUserID());
        assertEquals(temp.getNickName(), "巨无霸");
    }

    @Test(dependsOnMethods = "createTaskTest1")
    //查找任务
    public void findTaskTest1() {
        Task temp = initiatorService.findTaskByName(testTask.getPrimeKey(), testInitiator.getPrimeKey());
        assertEquals(temp.getResult(), testTask.getResult());
    }

    @Test
    //查找不存在的任务
    public void findTaskTest2() {
        Task temp = initiatorService.findTaskByName("不存在", testInitiator.getPrimeKey());
        assertNull(temp);
    }

    @Test
    //查找任务列表
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
        securityUserController.signUp(temp);
        initiatorService.finishTask(testTask.getPrimeKey(), temp.getPrimeKey());
    }

    @Test(dependsOnMethods = "createTaskTest1")
    //正确的发起者结束任务
    public void ownerTest2() {
        initiatorService.finishTask(testTask.getPrimeKey(), testInitiator.getPrimeKey());
        Task res = initiatorService.findTaskByName(testTask.getPrimeKey(), testInitiator.getPrimeKey());
        if (res == null) {
            throw new NotFoundException("在结束任务的测试中没有能找到任务");
        } else {
            assertEquals(java.util.Optional.ofNullable(res.getFinished()), true);
        }
    }
}
