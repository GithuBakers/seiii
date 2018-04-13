package cn.edu.nju.tagmakers.countsnju.logic;

import cn.edu.nju.tagmakers.countsnju.CountsnjuApplication;
import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;
import cn.edu.nju.tagmakers.countsnju.entity.user.Task;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import cn.edu.nju.tagmakers.countsnju.exception.NotFoundException;
import cn.edu.nju.tagmakers.countsnju.logic.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.Random;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Description:TaskServiceDAO的测试类
 *
 * @author wym
 * Created on 03/19/2018
 */
//@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CountsnjuApplication.class)
@SpringBootTest
public class TaskServiceTest extends AbstractTestNGSpringContextTests {
    private Task task1;
    private Task task2;

    @Autowired
    private TaskService service;

    @BeforeSuite
    public void setUp() {
        task1 = new Task();
        task2 = new Task();
        task1.setTaskID(new Random(System.currentTimeMillis()).nextInt() + "");
        task2.setTaskID(new Random(System.currentTimeMillis() + 10086).nextInt() + "");
        task1.setTaskName("TaskService for test");
        task1.setType(MarkType.DESC);
        task1.setAim(100);
        task1.setFinished(false);
        task1.setLimit(2);
        task1.setCover("no cover");
        task1.setReward(10);
        task1.setInitiatorName("xxz");
        task1.setRequirement("打倒辣鸡翔哲");
        task1.setResult("no result");
    }

    @AfterSuite
    public void End() {
        System.out.println(service.findTask(null));
    }

    @Test
    //正常添加对象
    public void addTest1() {
        service.addTask(task1);
        Task temp = new Task(service.findByID(task1.getPrimeKey()));
        assertEquals(temp.getAim(), 100);
        assertEquals(temp.getCover(), "no cover");
    }

    @Test(expectedExceptions = InvalidInputException.class)
    //添加空对象
    public void addTest2() {
        service.addTask(null);
    }

    @Test
    //正常更新task1
    public void updateTest1() {
        task1.setCover("我是一个面包机");
        task1.setRequirement("(╯‵□′)╯︵┻━┻");
        service.updateTask(task1);
        Task temp = new Task(service.findByID(task1.getPrimeKey()));
        assertEquals(temp.getCover(), "我是一个面包机");
        assertEquals(temp.getRequirement(), "(╯‵□′)╯︵┻━┻");
        assertEquals(temp.getAim(), 100);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    //更新空对象
    public void updateTest2() {
        service.updateTask(null);
    }

    @Test(expectedExceptions = NotFoundException.class)
    //更新原本不存在的对象
    public void updateTest3() {
        Task temp = new Task();
        temp.setTaskID(new Random(System.currentTimeMillis()).nextInt() + "");
        temp.setTaskName("不存在的对象");
        service.updateTask(temp);
    }


}
