package cn.edu.nju.tagmakers.countsnju.logic.service;

import cn.edu.nju.tagmakers.countsnju.algorithm.Cluster;
import cn.edu.nju.tagmakers.countsnju.algorithm.entity.EdgeClusterMeasurement;
import cn.edu.nju.tagmakers.countsnju.algorithm.entity.RectClusterMeasurement;
import cn.edu.nju.tagmakers.countsnju.data.controller.TaskController;
import cn.edu.nju.tagmakers.countsnju.data.controller.WorkerController;
import cn.edu.nju.tagmakers.countsnju.entity.Task;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Edge;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Rect;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Tag;
import cn.edu.nju.tagmakers.countsnju.entity.user.Sex;
import cn.edu.nju.tagmakers.countsnju.entity.user.Worker;
import cn.edu.nju.tagmakers.countsnju.entity.vo.diagram.BareAndCluster;
import cn.edu.nju.tagmakers.countsnju.entity.vo.diagram.SexAndAge;
import cn.edu.nju.tagmakers.countsnju.exception.FileIOException;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import util.FileCreator;
import util.OSSWriter;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
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

    private WorkerController workerController;

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
        if (checkFailure()) {
            throw new InvalidInputException("任务结果计算器暂时无法工作，因为未满足条件");
        } else {
            analysisDistribution();
            File file = writeFile();
            task.setResult(OSSWriter.upload(file));
            task.setFinished(true);
            taskController.update(task);
        }
    }

    private File writeFile() {
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
        } catch (FileNotFoundException e) {
            throw new FileIOException("创建结果集时发生文件异常");
        } catch (IOException e) {
            throw new FileIOException("File IO ERROR in task Service, when try to generate result set");
        }
        return file;
    }

    private boolean checkFailure() {
        return task == null || tags == null || taskController == null || workerController == null;
    }

    private void analysisDistribution() {
        List<Worker> workers = task.getUserMarked().keySet().parallelStream()
                .map(workerController::findByID)
                .collect(Collectors.toList());
        Sex sex = Sex.MALE;
        SexAndAge male = getSexDistribution(workers, sex);
        sex = Sex.FEMALE;
        SexAndAge female = getSexDistribution(workers, sex);
        sex = Sex.NA;
        SexAndAge others = getSexDistribution(workers, sex);
        List<SexAndAge> list = task.getUserDistribution();
        list.addAll(Arrays.asList(male, female, others));
        task.setUserDistribution(list);
    }

    private SexAndAge getSexDistribution(List<Worker> workers, Sex sex) {
        long TWENTY = 20 * 365 * 3600 * 1000L;
        long THIRTY = 30 * 365 * 3600 * 1000L;
        long FORTY = 2 * TWENTY;
        long NOW = new Date().getTime();
        List<Worker> maleWorkers = workers.stream().filter(worker -> worker.getSex() == sex).collect(Collectors.toList());
        List<Long> ages = maleWorkers.stream().map(worker -> NOW - worker.getBirthday()).collect(Collectors.toList());
        SexAndAge male = new SexAndAge();
        int under20 = (int) ages.stream().filter(age -> age < TWENTY).count();
        int under30 = (int) ages.stream().filter(age -> age < THIRTY).count() - under20;
        int under40 = (int) ages.stream().filter(age -> age <= FORTY).count() - under30;
        int above = (int) ages.stream().filter(age -> age > FORTY).count();
        male.setUnder20(under20);
        male.setBetween20And30(under30);
        male.setBetween30And40(under40);
        male.setAbove(above);
        return male;
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
        List<BareAndCluster> list = task.getBareAndClusters();
        List<String> bareIDs = task.getDataSet().stream().map(Bare::getId).collect(Collectors.toList());
        for (String bareID : bareIDs) {
            List<Tag> validTags = tags.stream()
                    .filter(tag -> tag.getBareID().equals(bareID))
                    .collect(Collectors.toList());
            validTags.forEach(tag -> {
                try {
                    writer.write(tag.toString());
                } catch (IOException e) {
                    throw new FileIOException("创建结果集时发生文件异常");
                }
            });
            BareAndCluster bareAndCluster = new BareAndCluster();
            bareAndCluster.setNumber((double) validTags.size());
            bareAndCluster.setKurtosis(1000.0);
            list.add(bareAndCluster);
        }
        task.setBareAndClusters(list);
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

    public WorkerController getWorkerController() {
        return workerController;
    }

    public void setWorkerController(WorkerController workerController) {
        this.workerController = workerController;
    }
}
