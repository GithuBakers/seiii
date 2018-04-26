package cn.edu.nju.tagmakers.countsnju.algorithm;

import cn.edu.nju.tagmakers.countsnju.entity.pic.Edge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * 用于处理算法的Edge
 * 因为Edge数组实际上是变长的 (x,y) 点对，
 * 因此Edge数组要支持规格化。
 * 规格化只能单方向调整，也就是只能降维
 *
 * @author xxz
 * Created on 04/26/2018
 */
public class AlgoEdge {
    private Integer[] data;
    private Edge edge;

    public AlgoEdge(Integer[] data) {
        this.data = data;
        edge = new Edge();
        wrapEdge(data);
    }

    public AlgoEdge(Edge edge, int dimension) {
        this.edge = edge;
        dimension *= 2;   //x,y
        //为了提高随机访问性能
        List<Integer> rawData = new ArrayList<>(edge.getPoints());
        if (rawData.size() < dimension) {
            throw new UnsupportedOperationException("不可以给Edge升维");
        } else {
            int toDelete = rawData.size() - dimension;
            //平均每多少个要删除一个，这里始终向上舍入(即 导致更少的点被删除)，最终可能导致结尾的点不按密度被删除
            int density = (rawData.size() + toDelete - 1) / toDelete;
            data = new Integer[dimension];
            int deleteIndex = 0;
            for (int i = 0; i < dimension; i++) {
                if (deleteIndex % density == 0) {
                    deleteIndex++;
                }
                data[i] = rawData.get(deleteIndex++);
            }

        }

    }

    public AvxVector getAvxVector() {
        return new AvxVector(data);
    }

    public Edge getEdge() {
        wrapEdge(data);
        return edge;
    }

    private void wrapEdge(Integer[] data) {
        edge.setPoints(Arrays.stream(data).collect(Collectors.toList()));
    }
}
