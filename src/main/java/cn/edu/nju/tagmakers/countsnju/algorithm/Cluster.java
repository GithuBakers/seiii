package cn.edu.nju.tagmakers.countsnju.algorithm;

import cn.edu.nju.tagmakers.countsnju.algorithm.entity.AlgoEdge;
import cn.edu.nju.tagmakers.countsnju.algorithm.entity.AlgoRect;
import cn.edu.nju.tagmakers.countsnju.algorithm.entity.EdgeClusterMeasurement;
import cn.edu.nju.tagmakers.countsnju.algorithm.entity.RectClusterMeasurement;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Edge;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Rect;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

/**
 * Description:
 * 将结果聚集为
 *
 * @author xxz
 * Created on 04/22/2018
 */
public class Cluster {
    public List<Rect> clusterRect(List<Rect> src) {
        List<Set<AvxVector>> clusters = rectCluster(src);


        return processAfterClusterRect(clusters);
    }

    private List<Rect> processAfterClusterRect(List<Set<AvxVector>> clusters) {
        return clusters.parallelStream()
                .filter(avxVectors -> avxVectors.size() != 0)
                //每个簇取平均
                .map(avxVectors -> avxVectors.parallelStream()
                        .reduce(AvxVector::add)
                        .orElse(avxVectors.iterator().next())
                        .scale(1.0 / avxVectors.size()))

                .map(AvxVector::getData)
                .map(AlgoRect::new)
                .map(AlgoRect::getRect)
                .collect(Collectors.toList());
    }

    public List<Edge> clusterEdge(List<Edge> src) {
        List<Set<AvxVector>> clusters = edgeCluster(src);

        return processAfterClusterEdge(clusters);
    }

    private List<Edge> processAfterClusterEdge(List<Set<AvxVector>> clusters) {
        return clusters.parallelStream()
                .filter(avxVectors -> avxVectors.size() != 0)
                .map(avxVectors -> avxVectors.parallelStream()
                        .reduce(AvxVector::add)
                        .orElse(avxVectors.iterator().next())
                        .scale(1.0 / avxVectors.size()))
                .map(AvxVector::getData)
                .map(AlgoEdge::new)
                .map(AlgoEdge::getEdge)
                .collect(Collectors.toList());
    }

    public RectClusterMeasurement rectClusterMeasurement(List<Rect> src) {
        int MAX = 1000;
        List<Set<AvxVector>> clusters = rectCluster(src);
        RectClusterMeasurement clusterMeasurementVO = new RectClusterMeasurement();
        clusterMeasurementVO.setClusters(processAfterClusterRect(clusters));
        long discrete = src.size() - clusters.stream().mapToLong(Collection::size).sum();
        if (discrete == 0) {
            clusterMeasurementVO.setKurtosis(MAX);
            return clusterMeasurementVO;
        }
        double disPercentage = ((double) discrete) / src.size();
        double metric = MAX - disPercentage * 1000;
        //去除聚类的点
        List<AvxVector> remains = src.stream()
                .map(AlgoRect::new)
                .map(AlgoRect::getAvxVector)
                .collect(Collectors.toList());

        remains.removeAll(clusters.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
        //计算峰度
        double kurtosis = calKurtosis(remains);
        //饱和计算
        metric += kurtosis;
        if (metric > MAX) {
            clusterMeasurementVO.setKurtosis(MAX);
        } else {
            clusterMeasurementVO.setKurtosis(metric);
        }
        return clusterMeasurementVO;

    }

    public EdgeClusterMeasurement edgeClusterMeasurement(List<Edge> src) {
        int MAX = 1000;
        List<Set<AvxVector>> clusters = edgeCluster(src);
        EdgeClusterMeasurement clusterMeasurementVO = new EdgeClusterMeasurement();
        clusterMeasurementVO.setClusters(processAfterClusterEdge(clusters));
        long discrete = src.size() - clusters.stream().mapToLong(Collection::size).sum();
        if (discrete == 0) {
            clusterMeasurementVO.setKurtosis(MAX);
            return clusterMeasurementVO;
        }
        double disPercentage = ((double) discrete) / src.size();
        double metric = MAX - disPercentage * 1000;
        //去除聚类的点
        List<AvxVector> remains = EdgeNormalize.normalize(src).stream()
                .map(AlgoEdge::getAvxVector)
                .collect(Collectors.toList());

        remains.removeAll(clusters.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
        //计算峰度
        double kurtosis = calKurtosis(remains);
        //饱和计算
        metric += kurtosis;
        if (metric > MAX) {
            clusterMeasurementVO.setKurtosis(MAX);
        } else {
            clusterMeasurementVO.setKurtosis(metric);
        }
        return clusterMeasurementVO;

    }

    private double calKurtosis(List<AvxVector> src) {
        double MAX = 9.9;
        //将向量按距离规格化
        if (src.size() <= 1) {
            return MAX;
        }
        AvxVector base = src.get(0);
        src.remove(0);
        List<Double> data = src.stream()
                .map(avxVector -> avxVector.distance(base))
                .collect(Collectors.toList());
        //将基准加入数据中
        data.add(0.0);

        double mean = data.stream()
                .reduce((aDouble, aDouble2) -> aDouble + aDouble2)
                .orElse(data.get(0))
                / data.size();
        List<Double> bias = data.stream()
                .map(num -> num - mean)
                .collect(Collectors.toList());
        double b4 = bias.stream()
                .map(num -> num * num)
                .map(factor2 -> factor2 * factor2)
                .reduce((aDouble, aDouble2) -> aDouble + aDouble2)
                .orElse(bias.get(0) * bias.get(0) * bias.get(0) * bias.get(0));

        double b2 = bias.stream()
                .map(num -> num * num)
                .reduce((aDouble, aDouble2) -> aDouble + aDouble2)
                .orElse(bias.get(0) * bias.get(0));

        double kurtosis = b4 / (b2 * b2);
        if (kurtosis > MAX || Double.isNaN(kurtosis)) {
            kurtosis = MAX;
        }
        return kurtosis;
    }


    private List<Set<AvxVector>> rectCluster(List<Rect> src) {
        //临域半径 最小数
        int R = 10, MIN = 5;

        List<AlgoRect> algoRects = src.parallelStream().map(AlgoRect::new).collect(Collectors.toList());
        AvxVector[] vectors = new AvxVector[src.size()];
        algoRects.parallelStream()
                .map(AlgoRect::getAvxVector)
                .collect(Collectors.toList())
                .toArray(vectors);


        //计算距离
        double distance[][] = new double[src.size()][src.size()];


        return cluster(R, MIN, vectors, distance);
    }

    private List<Set<AvxVector>> edgeCluster(List<Edge> src) {
        int R = 10, MIN = 5;
        List<AlgoEdge> algoEdges = EdgeNormalize.normalize(src);
        AvxVector[] vectors = new AvxVector[algoEdges.size()];
        algoEdges.stream()
                .map(AlgoEdge::getAvxVector)
                .collect(Collectors.toList())
                .toArray(vectors);
        double[][] distance = new double[vectors.length][vectors.length];

        return cluster(R, MIN, vectors, distance);
    }

    /**
     * 聚类算法（参考西瓜书）
     *
     * @param r        邻域半径
     * @param MIN      聚类最小值
     * @param vectors  要聚类的向量数组
     * @param distance 用于储存距离的数组（这样同时还能告诉这个方法数据规模有多大
     */
    private List<Set<AvxVector>> cluster(int r, int MIN, AvxVector[] vectors, double[][] distance) {

        Map<AvxVector, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < distance.length; i++) {
            indexMap.put(vectors[i], i);
        }

        distance(vectors, distance);
        for (int i = 0; i < distance.length; i++) {
            for (int j = 0; j < i; j++) {
                distance[i][j] = distance[j][i];
            }
        }


        //计算核心对象
        Set<AvxVector> cores = new HashSet<>();
        for (int i = 0; i < distance.length; i++) {
            int cnt = 0;
            for (int k = 0; k < distance.length; k++) {
                if (distance[i][k] < r) {
                    cnt++;
                }
            }
            if (cnt >= MIN) {
                cores.add(vectors[i]);
            }
        }

        List<Set<AvxVector>> clusters = new LinkedList<>();
        //全局必须只有一份avxVector的引用
        Set<AvxVector> unvisited = Arrays.stream(vectors).collect(Collectors.toSet());

        while (!cores.isEmpty()) {
            Set<AvxVector> oldUnvisited = new HashSet<>(unvisited);
            ArrayList<AvxVector> q = new ArrayList<>();
            AvxVector next = cores.iterator().next();
            q.add(next);
            cores.remove(next);
            unvisited.remove(next);
            //是否是一个类
            boolean cluster = false;
            while (!q.isEmpty()) {
                AvxVector cur = q.get(0);
                q.remove(cur);
                LinkedList<AvxVector> neighbour = new LinkedList<>();
                int i = indexMap.get(cur);
                for (int j = 0; j < distance.length; j++) {
                    if (distance[i][j] <= r && unvisited.contains(vectors[j])) {
                        neighbour.add(vectors[j]);
                    }
                }

                if (neighbour.size() >= MIN) {
                    cluster = true;
                    q.addAll(neighbour.stream()
                            .filter(unvisited::contains)
                            .collect(Collectors.toSet()));
                    unvisited.removeAll(neighbour);
                }

            }
            if (cluster) {
                Set<AvxVector> newCluster = oldUnvisited.parallelStream()
                        .filter(vector -> !unvisited.contains(vector))
                        .collect(Collectors.toSet());
                clusters.add(newCluster);
                cores.removeAll(newCluster);
            } else {
                cores.removeAll(oldUnvisited);
            }
        }
        return clusters;
    }

    private void distance(AvxVector[] vectors, double[][] distance) {
        DistanceCalculator distanceCalculator = new DistanceCalculator(distance, 0, vectors.length, vectors);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.submit(distanceCalculator);
        try {
            distanceCalculator.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
