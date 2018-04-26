package cn.edu.nju.tagmakers.countsnju.logic;

import cn.edu.nju.tagmakers.countsnju.CountsnjuApplication;
import cn.edu.nju.tagmakers.countsnju.entity.pic.*;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import cn.edu.nju.tagmakers.countsnju.filter.ImageFilter;
import cn.edu.nju.tagmakers.countsnju.logic.service.BareService;
import cn.edu.nju.tagmakers.countsnju.logic.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.testng.AssertJUnit.assertEquals;


/**
 * Description:
 * imageService测试
 *
 * @author wym
 * Created on 03/23/2018
 * <p>
 * update: 通过测试
 * @author wym
 * modified on 03/23/2018
 */
@ContextConfiguration(classes = CountsnjuApplication.class)
@SpringBootTest
public class ImageServiceTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private ImageService service;

    @Autowired
    private BareService bareService;

    @BeforeSuite
    public void setUp() {
    }

    @Test
    //正常情况
    public void addImageTest1() {
        Image image1 = new Image();
        Bare bare1 = new Bare();
        bare1.setId("test");
        bare1.setState(BareState.MARKED);
        ArrayList tagList = new ArrayList();
        Tag tag1 = new Tag();
        tagList.add(tag1);
        image1.setBare(bare1);
        image1.setType(MarkType.DESC);
        image1.setTags(tagList);
        image1.setBare(bare1);
        bareService.addBare(bare1);
        service.addImage(image1);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    //空id
    public void addImageTest2() {
        service.addImage(null);
    }

    //空id
    @Test(expectedExceptions = InvalidInputException.class)
    public void addImageTest3() {
        Image image1 = new Image();
        Bare bare1 = new Bare();
        ArrayList tagList = new ArrayList();
        Tag tag1 = new Tag();
        tagList.add(tag1);
        image1.setBare(bare1);
        image1.setType(MarkType.DESC);
        image1.setTags(tagList);
        image1.setBare(bare1);
        bareService.addBare(bare1);
        service.addImage(image1);
    }

    @Test(dependsOnMethods = "addImageTest1")
    public void findImageTest1() {
        ImageFilter filter = new ImageFilter();
        filter.setBareState(BareState.MARKED);
        filter.setMarkType(MarkType.DESC);
        assertEquals(false, service.findImage(filter).size() > 0);
    }

    @Test(dependsOnMethods = "addImageTest1")
    public void findImageTest2() {
        Image image = service.findImageByID("test");
        assertEquals(image.getTags().size() > 0, true);
    }
}
