package cn.edu.nju.tagmakers.countsnju.algorithm;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Integration {

    /**
     * @param realUpper 真实积分上限
     * @return 正态分布的积分函数
     */
    public static double stdGaussValue(double realUpper) {
        double upper = 1.0;
        double lower = 0.0;
        int n = 200;

        double result =
                simpsonRule(upper, lower, n, realUpper);
        result /= Math.pow(2 * Math.PI, 0.5);
        //保留小数点后6位 四舍五入
        result = new BigDecimal(result).
                setScale(6, RoundingMode.HALF_UP).doubleValue();
        return result;
    }

    /**
     * 辛普森积分法
     *
     * @param upper     积分上限
     * @param lower     积分下限
     * @param n         分段个数
     * @param realUpper 真实积分上限
     * @return 积分
     */
    private static double simpsonRule(double upper, double lower, int n, double realUpper) {
        double result = 0;

        double unit = (upper - lower) / n;
        double factor1 = unit / 3;
        double[] x = new double[n + 1];

        for (int i = 0; i < x.length; i++) {
            x[i] = lower + unit * i;
        }
        for (int i = 0; i < x.length; i++) {
            if (i == 0 || i == x.length - 1) {
                result += func(realUpper, x[i]);
            } else if (i % 2 == 0) { // if i is even num.
                result += 2 * func(realUpper, x[i]);
            } else { // if i is odd num.
                result += 4 * func(realUpper, x[i]);
            }
        }

        result *= factor1;
        return result;
    }

    /**
     * 计算y
     *
     * @param realUpper 真实积分上限
     * @param x         自变量
     * @return 结果
     */
    private static double func(double realUpper, double x) {
        if (x == 0) {
            return 0;
        }
        double t = realUpper - (1 - x) / x;
        return Math.pow(Math.E, -0.5 * t * t) / (x * x);
    }
}
