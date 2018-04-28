package cn.edu.nju.tagmakers.countsnju.algorithm.entity;

import cn.edu.nju.tagmakers.countsnju.entity.pic.Edge;

import java.util.List;

/**
 * Description:
 *
 * @author xxz
 * Created on 04/28/2018
 * @see ClusterMeasurement
 */
public class EdgeClusterMeasurement extends ClusterMeasurement {
    private List<Edge> clusters;

    public List<Edge> getClusters() {
        return clusters;
    }

    public void setClusters(List<Edge> clusters) {
        this.clusters = clusters;
    }
}
