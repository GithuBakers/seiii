package cn.edu.nju.tagmakers.countsnju.algorithm;

import cn.edu.nju.tagmakers.countsnju.entity.pic.Rect;
import org.testng.TestNG;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Description:
 *
 * @author xxz
 * Created on 04/22/2018
 */
public class ClusterTest extends TestNG {
    List<Rect> rects = new LinkedList<>();

    @BeforeSuite
    public void setUp() {
        int[][] init = {
                {5, 5, 10, 10},
                {5, 5, 10, 10},
                {5, 5, 10, 10},
                {5, 5, 10, 10},
                {5, 5, 10, 10},

                {8, 8, 10, 10},
                {8, 8, 10, 10},
                {8, 8, 10, 10},
                {8, 8, 10, 10},
                {8, 8, 10, 10},

                {0, 0, 10, 10},
                {0, 0, 10, 10},
                {2, 2, 10, 10},
                {1, 3, 10, 10},
                {0, 0, 10, 10},

        };
        int N = 10;
        for (int i = 0; i < N; i++) {
            for (int[] anInit : init) {
                rects.add(build(anInit[0], anInit[1], anInit[2], anInit[3]));
            }
        }
    }

    private Rect build(int x, int y, int height, int width) {
        Rect rect = new Rect();
        rect.setX(x);
        rect.setY(y);
        rect.setHeight(height);
        rect.setWidth(width);
        return rect;
    }

    @Test
    public void testCluster() {
        Cluster cluster = new Cluster();
//        System.out.println(cluster.clusterRect(rects).stream().map(rect ->
//                rect.getX() + " #### " + rect.getY() + " #### " + rect.getHeight() + " #### " + rect.getWidth()
//        ).collect(Collectors.toList()));
        System.out.println(cluster.clusterRect(rects));
    }

}
