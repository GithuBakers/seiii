package cn.edu.nju.tagmakers.countsnju.algorithm;

import java.util.concurrent.RecursiveTask;

/**
 * Description:
 *
 * @author xxz
 * Created on 04/22/2018
 */
public class DistanceCalculator extends RecursiveTask<Integer> {
    private double[][] distance;
    private int beginRow;
    private int taskNumber;
    private AvxVector[] data;
    private int THRESHOLD = 10;

    public DistanceCalculator(double[][] distance, int beginRow, int taskNumber, AvxVector[] data) {
        this.distance = distance;
        this.beginRow = beginRow;
        this.data = data;
        this.taskNumber = taskNumber;
    }


    /**
     * The main computation performed by this task.
     *
     * @return the result of the computation
     */
    @Override
    protected Integer compute() {
//        System.out.println("from " + beginRow + " " + (beginRow + taskNumber));
        if (taskNumber < THRESHOLD) {
            for (int i = beginRow; i < beginRow + taskNumber; i++) {
//                System.out.println("line "+i);
                for (int j = i; j < data.length; j++) {
                    distance[i][j] = data[i].distance(data[j]);
//                    System.out.println("distance," + i + "," + j + "," + distance[i][j]);
                }
            }
//            System.out.println(distance + " in here");
//            System.out.println("here");
        } else {
            //分成两份
            DistanceCalculator part1 = new DistanceCalculator(distance, beginRow, taskNumber >> 1, data);
            DistanceCalculator part2 = new DistanceCalculator(distance, beginRow + (taskNumber >> 1) - 1, (taskNumber >> 1) + 2, data);
            part1.fork();
            part2.fork();
            part1.join();
            part2.join();
        }
        //不需要返回值
        return null;
    }
}
