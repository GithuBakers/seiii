package cn.edu.nju.tagmakers.countsnju.data;

import cn.edu.nju.tagmakers.countsnju.data.dao.WorkerDAO;
import cn.edu.nju.tagmakers.countsnju.entity.user.Role;
import cn.edu.nju.tagmakers.countsnju.entity.user.Worker;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import cn.edu.nju.tagmakers.countsnju.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

@ContextConfiguration(classes = WorkerDAO.class)
@SpringBootTest
public class WorkerDAOTest extends AbstractTestNGSpringContextTests {
    private Worker worker1;

    @Autowired
    private WorkerDAO dao;

    @BeforeSuite
    public void setUp() {
        worker1 = new Worker();
        worker1.setUserID("testWorker");
        worker1.setRole(Role.WORKER);
        worker1.setPassword("123456");
        worker1.setNickName("hello worker");
        worker1.setCredit(100);
    }

    @AfterSuite
    public void End() {
        System.out.println(dao.find(null));
    }

    @Test
    //正常添加对象
    public void addTest1() {
        dao.add(worker1);
        assertEquals(dao.findByID(worker1.getPrimeKey()).getCredit(), 100);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    //添加空对象
    public void addTest2() {
        dao.add(null);
    }

    @Test(dependsOnMethods = "updateTest1")
    //addTest1成功之后执行删除并查询结果
    public void deleteTest1() {
        assertEquals(dao.delete(worker1.getPrimeKey()), true);
        assertEquals(dao.findByID(worker1.getPrimeKey()), null);
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
    //正常更新worker1
    public void updateTest1() {
        worker1.setCredit(90);
        dao.update(worker1);
        assertEquals(dao.findByID(worker1.getPrimeKey()).getCredit(), 90);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    //更新空对象
    public void updateTest2() {
        dao.update(null);
    }

    @Test(expectedExceptions = NotFoundException.class)
    //更新原本不存在的对象
    public void updateTest3() {
        Worker temp = new Worker();
        temp.setUserID("不存在的ID");
        dao.update(temp);
    }
}
