package cn.edu.nju.tagmakers.countsnju.data;


import cn.edu.nju.tagmakers.countsnju.data.dao.TagDAO;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Comment;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Tag;
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
 * Description:tagDAO的测试类
 * @author wym
 * Created on 03/21/2018
 */
//@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TagDAO.class)
@SpringBootTest
public class TagDAOTest extends AbstractTestNGSpringContextTests {
    private Tag tag1;
    private Tag tag2;
    private Comment comment1;
    private Comment comment2;
    @Autowired
    private TagDAO dao;

    @BeforeSuite
    public void setUp(){
        comment1 = new Comment("一条评论");
        comment2 = new Comment("更新后的评论");
        tag1 = new Tag();
        tag1.setTagID("tag1");
        tag1.setWorkerID("拔丝地瓜");
        tag1.setComment(comment1);
        tag2 = new Tag();
    }

    @AfterSuite
    public void delete() {
        dao.delete(tag1.getTagID());
        dao.delete(tag2.getTagID());
    }
    @Test
    //正常添加
    public void addTest1(){
        dao.add(tag1);
        assertEquals(dao.findByID("tag1").getComment().getCmt(),comment1.getCmt());
    }

    @Test(expectedExceptions = InvalidInputException.class)
    //id为null添加之后返回nul
    public void addTest2(){
        dao.add(tag2);
    }

    @Test(dependsOnMethods = "addTest1")
    //删除tag1返回true
    public void deleteTest1(){
        assertEquals(dao.delete(tag1.getTagID()), true);
        assertEquals(dao.findByID(tag1.getTagID()), null);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    //删除null值
    public void deleteTest2(){
        dao.delete(null);
    }

    @Test(expectedExceptions = NotFoundException.class)
    //删除不存在的
    public void deleteTest3(){
        dao.delete("不存在");
    }

    @Test
    //正常更新tag1
    public void updateTest1(){
        tag1.setComment(comment2);
        assertEquals(dao.update(tag1).getComment().getCmt(),comment2.getCmt());
    }

    @Test(expectedExceptions = InvalidInputException.class)
    //更新空对象
    public void updateTest2(){
        dao.update(null);
    }

    @Test(expectedExceptions = NotFoundException.class)
    //更新id不存在的
    public void updateTest3() {
        tag2.setTagID(" ");
        dao.update(tag2);
    }



}
