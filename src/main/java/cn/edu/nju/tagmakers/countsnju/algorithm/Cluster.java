package cn.edu.nju.tagmakers.countsnju.algorithm;

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
        //临域半径 最小数
        int R = 10, MIN = 5;

        List<AlgoRect> algoRects = src.parallelStream().map(AlgoRect::new).collect(Collectors.toList());
        AvxVector[] vectors = new AvxVector[src.size()];
        algoRects.parallelStream()
                .map(AlgoRect::getAvxVector)
                .collect(Collectors.toList())
                .toArray(vectors);

        Map<AvxVector, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < src.size(); i++) {
            indexMap.put(vectors[i], i);
        }

        //计算距离
        double distance[][] = new double[src.size()][src.size()];

        //used to benchmark

        distance(vectors, distance);
        for (int i = 0; i < src.size(); i++) {
            for (int j = 0; j < i; j++) {
                distance[i][j] = distance[j][i];
            }
        }


        //计算核心对象
        Set<AvxVector> cores = new HashSet<>();
        for (int i = 0; i < src.size(); i++) {
            int cnt = 0;
            for (int k = 0; k < src.size(); k++) {
                if (distance[i][k] < R) {
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
            while (!q.isEmpty()) {
                AvxVector cur = q.get(0);
                q.remove(cur);
                LinkedList<AvxVector> neighbour = new LinkedList<>();
                int i = indexMap.get(cur);
                for (int j = 0; j < src.size(); j++) {
                    if (distance[i][j] <= R && unvisited.contains(vectors[j])) {
                        neighbour.add(vectors[j]);
                    }
                }

                if (neighbour.size() >= MIN) {
                    q.addAll(neighbour.stream()
                            .filter(unvisited::contains)
                            .collect(Collectors.toSet()));
                    unvisited.removeAll(neighbour);
                }

            }
            Set<AvxVector> newCluster = oldUnvisited.parallelStream()
                    .filter(vector -> !unvisited.contains(vector))
                    .collect(Collectors.toSet());
            clusters.add(newCluster);
            cores.removeAll(newCluster);
        }

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
