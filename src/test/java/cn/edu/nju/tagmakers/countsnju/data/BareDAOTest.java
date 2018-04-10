package cn.edu.nju.tagmakers.countsnju.data;


import cn.edu.nju.tagmakers.countsnju.data.dao.BareDAO;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
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

/**
 * Description:BareDAO的测试类
 * @author wym
 * Created on 03/19/2018
 */
//@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BareDAO.class)
@SpringBootTest
public class BareDAOTest extends AbstractTestNGSpringContextTests{
    private Bare bare1;
    private Bare bare2;

    @Autowired
    private BareDAO dao;

    @BeforeSuite
    public void setUp(){
        bare1 = new Bare();
        bare1.setName("name_1");
        bare1.setId("1");
        bare2 = new Bare();
    }

    @AfterSuite
    public void End(){
        System.out.println(dao.find(null));
    }

    @Test
    //正常添加对象
    public void addTest1(){
        dao.add(bare1);
        assertEquals(dao.findByID(bare1.getId()).getName(),"name_1");
    }

    @Test(expectedExceptions = InvalidInputException.class)
    //添加空对象
    public void addTest2(){
        dao.add(null);
    }

    @Test(dependsOnMethods = "addTest1")
    //addTest1成功之后执行删除并查询结果
    public void deleteTest1(){
        assertEquals(dao.delete(bare1.getId()),true);
        assertEquals(dao.findByID(bare1.getId()),null);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    //删除空id
    public void deleteTest2(){
        dao.delete(null);
    }

    @Test(expectedExceptions = NotFoundException.class)
    //删除不存在的对象
    public void deleteTest3(){
        dao.delete("不存在的id");
    }

    @Test
    //正常更新bare1
    public void updateTest1(){
        bare1.setName("更新后的bare1");
        assertEquals(dao.update(bare1).getName(),"更新后的bare1");
    }

    @Test(expectedExceptions = InvalidInputException.class)
    //更新空对象
    public void updateTest2(){
        dao.update(null);
    }

    @Test(expectedExceptions = NotFoundException.class)
    //更新原本不存在的对象
    public void updateTest3(){
        Bare temp = new Bare();
        temp.setId("不存在的ID");
        dao.update(temp);
    }


}
