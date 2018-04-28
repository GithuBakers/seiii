package cn.edu.nju.tagmakers.countsnju.algorithm.entity;

/**
 * Description:
 * 聚类之后同时返回聚类结果和集中程度
 * <p>
 * 集中程度是一个相对水平，因此只要能满足如下性质即可：
 * 假设度量函数 double f(List)
 * <p>
 * - 如果结果A与结果B在聚类之后都没有留下离散点，那么f(A)=f(B)=MAX
 * 也就是说，凡是能被聚类算法聚合在一起的部分，都认为是聚合的
 * <p>
 * - 如果结果A与结果B在聚类之后有离散点，如果A的离散点 占比(精确到1/1000) 比B的离散点多，则f(A)<f(B)
 * 也就是说，在任何情况下，离散点的数量始终是最重要的考虑因素。理由如下：
 * 在聚类算法可以保证 足够多的 足够靠近的结果 都可以被聚集起来，因此剩下的离散点是因为
 * 无法进行聚类而排除的，这部分点剩的越多，代表这些点约分散，即使这些点相对集中，从全局角度来看
 * 依然属于离散点。
 * <p>
 * - 如果结果A与结果B在聚类之后有离散点，并且离散点 占比(1/1000) 一致，那么计算这些离散点的峰度，如果A的离散点峰度更高，
 * 则 f(A)>f(B)
 * <p>
 * - 如果峰度计算结果完全一致，则f(A)=f(B)
 * <p>
 * 这个类由算法逻辑生成，图表逻辑收集，图表逻辑转换处理后记录在任务类中
 *
 * @author xxz
 * Created on 04/28/2018
 */
public class ClusterMeasurement {
    private double kurtosis;

    public double getKurtosis() {
        return kurtosis;
    }

    public void setKurtosis(double kurtosis) {
        this.kurtosis = kurtosis;
    }
}
