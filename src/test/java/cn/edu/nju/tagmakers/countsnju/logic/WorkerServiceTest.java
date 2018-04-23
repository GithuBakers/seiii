package cn.edu.nju.tagmakers.countsnju.logic;

import cn.edu.nju.tagmakers.countsnju.CountsnjuApplication;
import cn.edu.nju.tagmakers.countsnju.entity.Task;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Image;
import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Tag;
import cn.edu.nju.tagmakers.countsnju.entity.user.Role;
import cn.edu.nju.tagmakers.countsnju.entity.user.Worker;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerReceivedTaskDetailVO;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerReceivedTaskVO;
import cn.edu.nju.tagmakers.countsnju.exception.NotFoundException;
import cn.edu.nju.tagmakers.countsnju.exception.PermissionDeniedException;
import cn.edu.nju.tagmakers.countsnju.filter.TaskFilter;
import cn.edu.nju.tagmakers.countsnju.logic.service.TaskService;
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
import java.util.Random;

import static org.testng.Assert.*;

@ContextConfiguration(classes = CountsnjuApplication.class)
@SpringBootTest
public class WorkerServiceTest extends AbstractTestNGSpringContextTests {
    @Autowired
    WorkerService workerService;

    @Autowired
    SecurityUserController securityUserController;

    @Autowired
    TaskService taskService;

    private Worker testWorker;
    private Task testTask;
    private Bare bare1;
    private Bare bare2;
    private String randomizedID;

    @BeforeClass
    public void setUp() {
        testWorker = new Worker();
        randomizedID = randomizedID + new Random(System.currentTimeMillis()).nextInt() + "";
        testWorker.setUserID(randomizedID);
        testWorker.setNickName("拔丝地瓜");
        testWorker.setRole(Role.WORKER);
        testWorker.setPassword("123");
        testWorker.setCredit(10);
        testWorker.setRank(1);

        bare1 = new Bare();
        bare1.setId("图1" + new Random(System.currentTimeMillis()).nextInt() + "");
        bare2 = new Bare();
        bare2.setId("图2" + new Random(System.currentTimeMillis()).nextInt() + "");
        List<Bare> dataSet = new ArrayList<>();
        dataSet.add(bare1);
        dataSet.add(bare2);

        testTask = new Task();
        testTask.setTaskID(randomizedID);
        testTask.setTaskName(randomizedID);
        testTask.setType(MarkType.DESC);
        testTask.setAim(100);
        testTask.setFinished(false);
        testTask.setLimit(10);
        testTask.setCover("no cover");
        testTask.setReward(10);
        testTask.setInitiatorName(randomizedID);
        testTask.setRequirement("打倒辣鸡翔哲");
        testTask.setResult("no result");
        testTask.setDataSet(dataSet);
        testTask.setType(MarkType.DESC);

        System.out.println(securityUserController == null);
        securityUserController.signUp(testWorker);
        taskService.addTask(testTask);
    }

    @Test
    //查找存在的工人
    public void findTest1() {
        Worker temp = workerService.findWorkerByName(randomizedID);
        assertEquals(temp.getNickName(), "拔丝地瓜");
    }

    @Test
    //查找不存在的工人
    public void findTest2() {
        assertNull(workerService.findWorkerByName("不存在"));
    }

    @Test(dependsOnMethods = "receiveTaskTest1")
    //根据任务查
    public void findTest3() {
        TaskFilter filter = new TaskFilter();
        filter.setFinished(false);
        filter.setInitiatorName("wym");
        assertTrue(workerService.findWorkerTask(filter, randomizedID) != null);
    }

    @Test
    //正常接受任务
    public void receiveTaskTest1() {
        workerService.receiveTask(randomizedID, randomizedID);
    }

    @Test(expectedExceptions = NotFoundException.class)
    //不存在的任务
    public void receiveTaskTest2() {
        workerService.receiveTask("不存在", randomizedID);
    }

    @Test(expectedExceptions = NotFoundException.class)
    //不存在的用户接受存在的任务
    public void receiveTaskTest3() {
        workerService.receiveTask(randomizedID, "不存在");
    }

    @Test(dependsOnMethods = "receiveTaskTest1", expectedExceptions = PermissionDeniedException.class)
    //同一个用户接受重复任务
    public void receiveTaskTest4() {
        workerService.receiveTask(randomizedID, randomizedID);
    }

    @Test
    //正常更新用户
    public void updateWorkerTest1() {
        testWorker.setNickName("巨无霸");
        workerService.update(testWorker);
        assertEquals(workerService.findWorkerByName(testWorker.getUserID()).getNickName(), "巨无霸");
    }

    @Test(dependsOnMethods = "receiveTaskTest1")
    public void submitTest1() {
        Image image1 = new Image();
        image1.setBare(bare1);
        image1.setType(MarkType.DESC);
        Tag tag1 = new Tag();
        tag1.setBareID("图1");
        tag1.setTagID("tag 1" + randomizedID);
        tag1.setWorkerID(randomizedID);
        tag1.setNumberID("01");
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag1);
        image1.setTags(tagList);
        boolean success = workerService.submitTag(image1, testTask.getTaskName(), testWorker.getUserID());
        assertTrue(success);
    }

    @Test(dependsOnMethods = "receiveTaskTest1")
    public void getDetailTest() {
        WorkerReceivedTaskDetailVO vo = workerService.getReceivedTaskDetails(testTask.getTaskName(), testWorker.getUserID());
        assertEquals(vo.getLimit(), 10);
    }

    @Test(dependsOnMethods = "submitTest1")
    //在工人提交修改后查询
    public void getBareTest() {
        List<Bare> list = workerService.getBares(testTask.getTaskName(), testWorker.getUserID());
        assertTrue(list != null);
    }

    @Test(dependsOnMethods = "receiveTaskTest1")
    //在工人接受任务之后查询
    public void getReceivedTaskTest() {
        List<WorkerReceivedTaskVO> list = workerService.getReceivedTasks(testWorker.getUserID());
        assertTrue(list.size() > 0);
    }
}
