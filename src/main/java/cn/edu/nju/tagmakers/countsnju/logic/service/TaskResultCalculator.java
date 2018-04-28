package cn.edu.nju.tagmakers.countsnju.logic.service;

import cn.edu.nju.tagmakers.countsnju.algorithm.Cluster;
import cn.edu.nju.tagmakers.countsnju.algorithm.entity.EdgeClusterMeasurement;
import cn.edu.nju.tagmakers.countsnju.algorithm.entity.RectClusterMeasurement;
import cn.edu.nju.tagmakers.countsnju.data.controller.TaskController;
import cn.edu.nju.tagmakers.countsnju.entity.Task;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Edge;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Rect;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Tag;
import cn.edu.nju.tagmakers.countsnju.entity.vo.diagram.BareAndCluster;
import cn.edu.nju.tagmakers.countsnju.exception.FileIOException;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import util.FileCreator;
import util.OSSWriter;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * 生成测试结果用的多线程程序
 * 记得要先设置tags 和 task 和 taskController！
 *
 * @author xxz
 * Created on 04/27/2018
 */
public class TaskResultCalculator implements Runnable {
    private Task task;

    private List<Tag> tags;

    private TaskController taskController;

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        if (task == null || tags == null || taskController == null) {
            throw new InvalidInputException("计算任务结果时 任务或tag为空");
        } else {
            String filePath = "ret" + File.separator + "task_result_" + task.getTaskName();
            FileCreator.createFile(filePath);
            File file = new File(filePath);
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
                writer.write("this is the result set of task" + file.getName());
                writer.newLine();
//                writer.write("请联系支付宝：18251830730 以查看所有数据");
//                writer.newLine();
//                writer.write("皮这一下非常开心");
//                writer.newLine();
//                writer.write(new Date(System.currentTimeMillis()).toString());
                Cluster cluster = new Cluster();
                switch (task.getType()) {
                    case DESC: {
                        writeTags(writer);
                        break;
                    }

                    case EDGE: {
                        writeClusterEdge(writer, cluster);
                        writeTags(writer);
                        break;
                    }

                    case RECT: {
                        writeClusterRect(writer, cluster);
                        writeTags(writer);
                        break;
                    }
                    case DEFAULT:
                }
                task.setFinished(true);
                task.setResult(OSSWriter.upload(file));
                taskController.update(task);
            } catch (FileNotFoundException e) {
                throw new FileIOException("创建结果集时发生文件异常");
            } catch (IOException e) {
                throw new FileIOException("File IO ERROR in task Service, when try to generate result set");
            }
        }
    }

    private void writeClusterRect(BufferedWriter writer, Cluster cluster) throws IOException {
        writer.write("The machine's result:");
//        writer.newLine();
        //获取所有图片ID，对每一张图片进行聚类操作
        List<String> bareIDs = task.getDataSet().stream().map(Bare::getId).collect(Collectors.toList());
        List<BareAndCluster> list = task.getBareAndClusters();
        for (String bareID : bareIDs) {
            //找到所有属于本图片的标注
            List<Rect> validTags = tags.stream()
                    .filter(tag -> tag.getBareID().equals(bareID))
                    .map(tag -> ((Rect) tag.getMark()))
                    .collect(Collectors.toList());
            RectClusterMeasurement measurement = cluster.rectClusterMeasurement(
                    validTags);
            List<Rect> rects = measurement.getClusters();
            //写文件
            rects.forEach(rect -> {
                try {
                    writer.newLine();
                    writer.write(bareID + ",");
                    writer.write(rect.toString());
                } catch (IOException e) {
                    throw new FileIOException("创建结果集时发生文件异常");
                }

            });
            BareAndCluster bareAndCluster = new BareAndCluster();
            //为了将格式转换成double
            bareAndCluster.setNumber(1.0 * validTags.size());
            bareAndCluster.setKurtosis(measurement.getKurtosis());
            list.add(bareAndCluster);
        }
        writer.write("The user's result:");
        writer.newLine();
    }

    private void writeClusterEdge(BufferedWriter writer, Cluster cluster) throws IOException {
        writer.write("The machine's result:");
        List<BareAndCluster> list = task.getBareAndClusters();
//        writer.newLine();
        List<String> bareIDs = task.getDataSet().stream().map(Bare::getId).collect(Collectors.toList());
        for (String bareID : bareIDs) {
            List<Edge> validTags = tags.stream()
                    .filter(tag -> tag.getBareID().equals(bareID))
                    .map(tag -> (Edge) tag.getMark())
                    .collect(Collectors.toList());
            EdgeClusterMeasurement measurement = cluster.edgeClusterMeasurement(validTags);
            List<Edge> edges = measurement.getClusters();

            edges.forEach(edge -> {
                try {
                    writer.newLine();
                    writer.write(bareID + ",");
                    writer.write(edge.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            BareAndCluster bareAndCluster = new BareAndCluster();
            //为了将格式转换成double
            bareAndCluster.setNumber(1.0 * validTags.size());
            bareAndCluster.setKurtosis(measurement.getKurtosis());
            list.add(bareAndCluster);
        }
        writer.write("The worker's result:");
        writer.newLine();
    }

    private void writeTags(BufferedWriter writer) {
        tags.forEach(tag -> {
            try {
                writer.write(tag.toString());
            } catch (IOException e) {
                throw new FileIOException("创建结果集时发生文件异常");
            }
        });
    }


    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public TaskController getTaskController() {
        return taskController;
    }

    public void setTaskController(TaskController taskController) {
        this.taskController = taskController;
    }
}
