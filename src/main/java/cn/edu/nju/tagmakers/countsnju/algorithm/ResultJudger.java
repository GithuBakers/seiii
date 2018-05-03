package cn.edu.nju.tagmakers.countsnju.algorithm;

import cn.edu.nju.tagmakers.countsnju.algorithm.entity.AlgoEdge;
import cn.edu.nju.tagmakers.countsnju.algorithm.entity.AlgoRect;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Description;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Edge;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Rect;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * 用于判断标注结果正误
 *
 * @author xxz
 * Created on 04/21/2018
 */

public class ResultJudger {

    private static double JUDGE_EDGE;

    /**
     * 在未来或添加 基本的语义分析机制
     */
    public static boolean judgeDesc(Description a, Description b) {
        return true;
    }


    public static boolean judgeRect(List<Rect> a, List<Rect> b) {
        double JUDGE_CRITERION = 100;
        if (a == null || b == null || a.size() != b.size()) {
            return false;
        } else {
            AvxVector[] algoRectsA = new AvxVector[a.size()];
            a.stream()
                    .map(AlgoRect::new)
                    .map(AlgoRect::getAvxVector)
                    .collect(Collectors.toList())
                    .toArray(algoRectsA);
            AvxVector[] algoRectsB = new AvxVector[b.size()];
            b.stream()
                    .map(AlgoRect::new)
                    .map(AlgoRect::getAvxVector)
                    .collect(Collectors.toList())
                    .toArray(algoRectsB);

            //每个矩形至少与其它一个矩形靠近
            double min = 9e9;
            for (int i = 0; i < a.size(); i++) {
                for (int j = 0; j < b.size(); j++) {
                    double tmp = algoRectsA[i].distance(algoRectsB[j]);
                    min = tmp < min ? tmp : min;
                }
                if (min > JUDGE_CRITERION) {
                    //不合要求，返回false
                    return false;
                } else {
                    //符合要求，算下一轮
                    min = 9e9;
                }
            }
            return true;
        }
    }

    public static boolean judgeEdge(List<Edge> a, List<Edge> b) {

        if (a.size() != b.size()) {
            return false;
        } else {
            double min = 9e9;
            for (Edge edge : a) {
                for (Edge another : b) {
                    double tmp = calDistance(edge, another);
                    min = min < tmp ? min : tmp;
                }
                if (min > JUDGE_EDGE) {
                    return false;
                } else {
                    min = 9e9;
                }
            }
            return true;
        }

    }

    private static double calDistance(Edge a, Edge b) {
        int aSize = a.getPoints().size();
        int bSize = b.getPoints().size();
        int min = aSize > bSize ? bSize : aSize;
        min >>= 1;
        JUDGE_EDGE = min * 50;
        AvxVector vectorA = new AlgoEdge(a, min).getAvxVector();
        AvxVector vectorB = new AlgoEdge(b, min).getAvxVector();
        return vectorA.distance(vectorB);
    }
}
