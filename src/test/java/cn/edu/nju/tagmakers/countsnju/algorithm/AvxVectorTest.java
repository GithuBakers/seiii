package cn.edu.nju.tagmakers.countsnju.algorithm;

import org.testng.TestNG;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;

/**
 * Description:
 *
 * @author xxz
 * Created on 04/22/2018
 */
public class AvxVectorTest extends TestNG {
    private static final int N = 10000000;

    private static Integer[] a;

    private static Integer[] b;

    @BeforeSuite
    public void setUp() {
        a = new Integer[N];

        b = new Integer[N];

        for (int i = 0; i < N; i++) {
            a[i] = 1;
            b[i] = 1;
        }
    }

    @Test
    public void testAdd() {
        AvxVector vector = new AvxVector(a);
        AvxVector vectorb = new AvxVector(b);
        System.out.println(Arrays.toString(vector.add(vectorb).getData()));
    }

    @Test
    public void testMul() {
        AvxVector vector = new AvxVector(a);
        AvxVector vectorb = new AvxVector(b);
        System.out.println(Arrays.toString(vector.mul(vectorb).getData()));
    }

    @Test
    public void testDiv() {
        AvxVector vector = new AvxVector(a);
        AvxVector vectorb = new AvxVector(b);
        AvxVector a = vector.add(vectorb);

        System.out.println(Arrays.toString(a.div(vectorb).getData()));
    }

    @Test
    public void testScala() {
        AvxVector vector = new AvxVector(a);
        AvxVector vectorb = new AvxVector(b);


        System.out.println(Arrays.toString(vector.scale(2.5).getData()));
    }

    @Test
    public void testDis() {
        int TIME = 100;
        AvxVector vector = new AvxVector(a);
        AvxVector vectorb = new AvxVector(b);
        for (int i = 0; i < TIME; i++) {
            vector.distance(vectorb);
        }
        System.out.println(vector.distance(vectorb));
//        assertEquals(vector.distance(vectorb), vectorb.distance(vector));
//        assertEquals(vectorb.distance(vector), 0.0);
//        AvxVector a = vector.add(vectorb);
//        assertEquals(vector.distance(a), a.distance(vector));
//        System.out.println(a.sum());

    }


    @Test
    public void tesSum() {
        AvxVector vector = new AvxVector(a);
        assertEquals(vector.sum(), N);

    }
}
