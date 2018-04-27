package cn.edu.nju.tagmakers.countsnju.algorithm.errorlearning;

import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;

import java.util.List;

/**
 * Description:
 *
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
        if (nums == null && (nums.size() == 0)) {
            throw new InvalidInputException("计算期望的时候非法输入");
        }
        double sum = 0.0;
        for (Double n : nums) {
            sum += n;
        }
        return sum / nums.size();
    }


}
