package cn.edu.nju.tagmakers.countsnju.algorithm;

import cn.edu.nju.tagmakers.countsnju.entity.pic.Edge;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * 因为Edge不能升维，所以必须要在
 * 信息丢失 和 可计算 之间选取一个权衡
 * 不能直接选取纬度最小值，那样会导致过度的信息丢失
 * 也不能选取纬度最大值，那样会使有效数据减少严重
 *
 * @author xxz
 * Created on 04/26/2018
 */
public class EdgeNormalize {
    public static List<AlgoEdge> normalize(List<Edge> src) {
        List<Edge> sorted = src.stream().sorted(Comparator.comparingInt(edge -> ((Edge) edge).getPoints().size()).reversed())
                .collect(Collectors.toList());

        int most = (int) (sorted.size() * 0.8);
        int dimension = sorted.get(most).getPoints().size() / 2;
        return sorted.subList(0, most - 1).stream()
                .map(edge -> new AlgoEdge(edge, dimension))
                .collect(Collectors.toList());

    }
}
