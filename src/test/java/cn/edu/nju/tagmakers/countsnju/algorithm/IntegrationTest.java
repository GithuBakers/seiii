package cn.edu.nju.tagmakers.countsnju.algorithm;

import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * Description:
 * 正态分布积分算法的测试，手动查表检验了
 *
 * @author wym
 * Created on
 * <p>
 * Update:
 * @author wym
 * Last modified on
 */
public class IntegrationTest extends AbstractTestNGSpringContextTests {
    @Test
    public void test1() {
        System.out.println(Integration.stdGaussValue(1.35));
    }
}
