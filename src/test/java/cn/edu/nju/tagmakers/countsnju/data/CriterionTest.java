package cn.edu.nju.tagmakers.countsnju.data;

import cn.edu.nju.tagmakers.countsnju.CountsnjuApplication;
import cn.edu.nju.tagmakers.countsnju.data.dao.CriterionDAO;
import cn.edu.nju.tagmakers.countsnju.entity.Criterion.Criterion;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.BareState;
import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Tag;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.testng.Assert.assertEquals;

@ContextConfiguration(classes = CountsnjuApplication.class)
@SpringBootTest
public class CriterionTest extends AbstractTestNGSpringContextTests {
    private Criterion criterion1;

    @Autowired
    private CriterionDAO dao;

    @BeforeSuite
    public void setUp() {
        criterion1 = new Criterion();
        criterion1.setCover("no cover");
        criterion1.setAim(10);
        criterion1.setCriterionID("test1");
        criterion1.setCriterionName("测试用标准集");
        criterion1.setRequirement("用来做测试");
        criterion1.setType(MarkType.DESC);

        Bare bare1 = new Bare();
        Bare bare2 = new Bare();
        bare1.setId("bare1");
        bare1.setMarkType(MarkType.DESC);
        bare1.setName("bare1的名字");
        bare1.setState(BareState.UNMARKED);
        bare1.setRaw("111.url");
        bare2.setId("bare2");
        bare2.setMarkType(MarkType.DESC);
        bare2.setName("bare2的名字");
        bare2.setState(BareState.UNMARKED);
        bare2.setRaw("222.url");
        ArrayList<Bare> bareList = new ArrayList<>();
        bareList.add(bare1);
        bareList.add(bare2);
        criterion1.setDataSet(bareList);

        String keyword1 = "烤冷面";
        String keyword2 = "鸡排";
        ArrayList<String> keywordsList = new ArrayList<>();
        keywordsList.add(keyword1);
        keywordsList.add(keyword2);
        criterion1.setKeywords(keywordsList);

        HashMap<String, List<Tag>> result = new HashMap<>();
        criterion1.setResult(result);
    }

    @Test
    public void addTest1() {
        dao.add(criterion1);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    public void addTest2() {
        dao.add(null);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    public void addTest3() {
        Criterion nothing = new Criterion();
        dao.add(nothing);
    }

    @Test(dependsOnMethods = "addTest1")
    public void findTest1() {
        Criterion res = dao.findByID("test1");
        assertEquals(res.getAim(), 10);
        assertEquals(res.getKeywords().get(0), "烤冷面");
        assertEquals(res.getKeywords().get(1), "鸡排");
    }

}
