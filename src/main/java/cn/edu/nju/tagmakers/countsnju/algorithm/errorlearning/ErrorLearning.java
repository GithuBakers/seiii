package cn.edu.nju.tagmakers.countsnju.algorithm.errorlearning;

import cn.edu.nju.tagmakers.countsnju.algorithm.Integration;

import java.util.List;

/**
 * Description:
 * 用于错误学习的算法类
 *
 * @author wym
 * Created on
 * <p>
 * Update:
 * @author wym
 * Last modified on
 */
public class ErrorLearning {
    /**
     * 计算工人的能力
     *
     * @return
     */
    public static int countErrorLearningAbility(double value, List<Double> trend) {
        double ret = 0;
        //根据边际递减效应，当正确率（价值）越大的时候上升的价值比下降的价值大
        //ret应该属于-1.0到1.0
        for (int i = 0; i < trend.size() - 1; i++) {
            double change = trend.get(i + 1) - trend.get(i);
            if (change >= 0 && ret < 1.0) {
                ret += change * value;
                if (ret > 1.0) {
                    ret = 1.0;
                }
            } else if (change < 0 && ret > -1.0) {
                ret += change * (1 - value);
                if (ret < -1.0) {
                    ret = -1.0;
                }
            }
        }
        //映射到0-100的整数并返回
        return (int) (ret * 50 + 50);
    }

    /**
     * 假设正态分布，计算所有通过该标准集的工人的正确率分布并求出该工人正确率的价值
     */
    public static double countValue(List<Double> accuracyOfWorkers, double accuracyOfSomeWorker) {
        if (accuracyOfWorkers.size() <= 1) {
            return 100;
        } else {
            double Ex = BasicAlgorithm.countEx(accuracyOfWorkers);
            double VarX = BasicAlgorithm.countVar(accuracyOfWorkers, Ex);
            //变换，化成标准正态分布
            double realUpper = (accuracyOfSomeWorker - Ex) / Math.sqrt(VarX);
            return Integration.stdGaussValue(realUpper);
        }
    }

}
