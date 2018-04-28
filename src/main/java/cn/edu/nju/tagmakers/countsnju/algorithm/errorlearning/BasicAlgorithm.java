package cn.edu.nju.tagmakers.countsnju.algorithm.errorlearning;

import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;

import java.util.List;

/**
 * Description:
 * 计算期望和方差
 * @author wym
 * Created on
 * <p>
 * Update:
 * @author wym
 * Last modified on
 */
public class BasicAlgorithm {
    /**
     * 计算期望
     */
    public static double countEx(List<Double> nums) {
        if (nums == null || (nums.size() == 0)) {
            throw new InvalidInputException("计算期望的时候非法输入");
        }
        double sum = 0.0;
        for (Double n : nums) {
            sum += n;
        }
        return sum / nums.size();
    }

    /**
     * 计算方差（无偏方差）
     */
    public static double countVar(List<Double> nums, double Ex) {
        if (nums == null) {
            throw new InvalidInputException("计算方差的时候非法输入");
        }
        if (nums.size() > 1) {
            double sum = 0;
            for (Double num : nums) {
                sum += Math.pow(num - Ex, 2);
            }
            int n = nums.size();
            return sum / (n - 1);
        }
        return 0;
    }
}
