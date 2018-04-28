package cn.edu.nju.tagmakers.countsnju.algorithm.entity;

import cn.edu.nju.tagmakers.countsnju.entity.pic.Rect;

import java.util.List;

/**
 * Description:
 *
 * @author xxz
 * Created on 04/28/2018
 * @see ClusterMeasurement
 */
public class RectClusterMeasurement extends ClusterMeasurement {
    private List<Rect> clusters;

    public List<Rect> getClusters() {
        return clusters;
    }

    public void setClusters(List<Rect> clusters) {
        this.clusters = clusters;
    }
}
