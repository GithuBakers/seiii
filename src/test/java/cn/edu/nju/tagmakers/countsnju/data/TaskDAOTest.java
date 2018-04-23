package cn.edu.nju.tagmakers.countsnju.data;


import cn.edu.nju.tagmakers.countsnju.data.dao.TaskDAO;
import cn.edu.nju.tagmakers.countsnju.entity.Task;
import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import cn.edu.nju.tagmakers.countsnju.exception.NotFoundException;
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
 * Description:TaskDAO的测试类
 *
 * @author wym
 * Created on 03/19/2018
 */
//@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TaskDAO.class)
@SpringBootTest
public class TaskDAOTest extends AbstractTestNGSpringContextTests {
    private Task task1;
    private Task task2;

    @Autowired
    private TaskDAO dao;
    private String randomizedID;

    @BeforeSuite
    public void setUp() {
        task1 = new Task();
        task2 = new Task();
        randomizedID = "" + new Random(System.currentTimeMillis()).nextInt();
        task1.setTaskID(randomizedID);
        task2.setTaskID(randomizedID + "123");
        task1.setTaskName("Task for test");
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
        System.out.println(dao.find(null));
    }

    @Test
    //正常添加对象
    public void addTest1() {
        dao.add(task1);
        Task temp = new Task(dao.findByID(task1.getPrimeKey()));
        assertEquals(temp.getAim(), 100);
        assertEquals(temp.getCover(), "no cover");
    }

    @Test(expectedExceptions = InvalidInputException.class)
    //添加空对象
    public void addTest2() {
        dao.add(null);
    }

    @Test(dependsOnMethods = "updateTest1")
    //updateTest1成功之后执行删除并查询结果
    public void deleteTest1() {
        dao.delete(task1.getPrimeKey());
        assertEquals(dao.findByID(task1.getPrimeKey()), null);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    //删除空id
    public void deleteTest2() {
        dao.delete(null);
    }

    @Test(expectedExceptions = NotFoundException.class)
    //删除不存在的对象
    public void deleteTest3() {
        dao.delete("不存在的id");
    }

    @Test
    //正常更新task1
    public void updateTest1() {
        task1.setCover("我是一个面包机");
        task1.setRequirement("(╯‵□′)╯︵┻━┻");
        dao.update(task1);
        Task temp = new Task(dao.findByID(task1.getPrimeKey()));
        assertEquals(temp.getCover(), "我是一个面包机");
        assertEquals(temp.getRequirement(), "(╯‵□′)╯︵┻━┻");
        assertEquals(temp.getAim(), 100);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    //更新空对象
    public void updateTest2() {
        dao.update(null);
    }

    @Test(expectedExceptions = NotFoundException.class)
    //更新原本不存在的对象
    public void updateTest3() {
        Task temp = new Task();
        temp.setTaskID("我也是一个面包机！！");
        dao.update(temp);
    }


}
