package cn.edu.nju.tagmakers.countsnju.data;

import cn.edu.nju.tagmakers.countsnju.data.dao.InitiatorDAO;
import cn.edu.nju.tagmakers.countsnju.entity.user.Initiator;
import cn.edu.nju.tagmakers.countsnju.entity.user.Role;
import cn.edu.nju.tagmakers.countsnju.entity.user.Sex;
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

@ContextConfiguration(classes = InitiatorDAO.class)
@SpringBootTest
public class InitiatorDAOTest extends AbstractTestNGSpringContextTests {
    private Initiator initiator1;

    @Autowired
    private InitiatorDAO dao;

    @BeforeSuite
    public void setUp() {
        initiator1 = new Initiator();
        initiator1.setUserID("testInitiator");
        initiator1.setRole(Role.WORKER);
        initiator1.setPassword("123456");
        initiator1.setNickName("hello initiator");
        initiator1.setBirthdayString("1998-02-24");
        initiator1.setSex(Sex.FEMALE);
    }

    @AfterSuite
    public void End() {
        System.out.println(dao.find(null));
    }

    @Test
    //正常添加对象
    public void addTest1() {
        dao.add(initiator1);
        assertEquals(dao.findByID(initiator1.getPrimeKey()).getNickName(), "hello initiator");
    }

    @Test(expectedExceptions = InvalidInputException.class)
    //添加空对象
    public void addTest2() {
        dao.add(null);
    }

    @Test(dependsOnMethods = "updateTest1")
    //addTest1成功之后执行删除并查询结果
    public void deleteTest1() {
        assertEquals(dao.delete(initiator1.getPrimeKey()), true);
        assertEquals(dao.findByID(initiator1.getPrimeKey()), null);
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
    //正常更新initiator1
    public void updateTest1() {
        initiator1.setNickName("I change the name !");
        dao.update(initiator1);
        assertEquals(dao.findByID(initiator1.getPrimeKey()).getNickName(), "I change the name !");
    }

    @Test(expectedExceptions = InvalidInputException.class)
    //更新空对象
    public void updateTest2() {
        dao.update(null);
    }

    @Test(expectedExceptions = NotFoundException.class)
    //更新原本不存在的对象
    public void updateTest3() {
        Initiator temp = new Initiator();
        temp.setUserID("不存在的ID");
        dao.update(temp);
    }
}
