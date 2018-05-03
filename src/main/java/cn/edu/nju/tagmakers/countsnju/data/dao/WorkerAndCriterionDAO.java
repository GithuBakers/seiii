package cn.edu.nju.tagmakers.countsnju.data.dao;

import cn.edu.nju.tagmakers.countsnju.entity.Criterion.Criterion;
import cn.edu.nju.tagmakers.countsnju.entity.Criterion.WorkerAndCriterion;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import org.springframework.stereotype.Component;
import util.FileCreator;
import util.Log;
import util.LogPriority;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Description:
 * 专门用于记录工人做标准集相关的结果的DAO
 * * @author wym
 * Created on
 * <p>
 * Update:
 *
 * @author wym
 * Last modified on
 */
@Component
public class WorkerAndCriterionDAO {
    private Set<WorkerAndCriterion> set;
    private String filePath;

    public WorkerAndCriterionDAO() {
        set = new HashSet<>();
        filePath = "data" + File.separator + "WorkerAndCriterion.txt";
        read();
    }

    /**
     * 用于判断是否工人已经做过某个标准集
     */
    public boolean existed(String workerID, String criterionID) {
        return findByID(workerID, criterionID) != null;
    }

    /**
     * 根据工人ID和标准集ID查找
     */
    public WorkerAndCriterion findByID(String workerID, String criterionID) {
        for (WorkerAndCriterion temp : set) {
            if (workerID.equals(temp.getWorkerID()) && criterionID.equals(temp.getCriterionID())) {
                return temp;
            }
        }
        return null;
    }

    /**
     * 检查过用户没有做过这个标准集之后添加这个结果
     *
     */
    public void add(String workerID, Criterion criterion) {
        if (criterion == null) throw new InvalidInputException("添加的标准集为空");
        WorkerAndCriterion temp = new WorkerAndCriterion(workerID, criterion);
        set.add(temp);
        writeObject(set, filePath);
    }

    /**
     * 更新关系对象
     */
    public void update(WorkerAndCriterion workerAndCriterion) {
        Set<WorkerAndCriterion> tempSet = new HashSet<>(set);
        for (WorkerAndCriterion temp : tempSet) {
            if (workerAndCriterion.getWorkerID().equals(temp.getWorkerID()) && workerAndCriterion.getCriterionID().equals(temp.getCriterionID())) {
                set.remove(temp);
                set.add(workerAndCriterion);
            }
        }
        writeObject(set, filePath);
    }

    /**
     * 读对象
     */
    private Set<WorkerAndCriterion> readObj(String path) {
        FileCreator.createFile(path);
        Set<WorkerAndCriterion> objSet = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(path)))) {
            objSet = (Set<WorkerAndCriterion>) ois.readObject();
        } catch (EOFException e) {
            objSet = new HashSet<>();
        } catch (IOException | ClassNotFoundException e) {
            Log.log("读对象异常", LogPriority.ERROR);
            e.printStackTrace();
        }
        return objSet;
    }

    /**
     * 读文件
     */
    private void read() {
        Set<WorkerAndCriterion> objSet = readObj(filePath);
        for (WorkerAndCriterion temp : objSet) {
            set.add(temp);
        }
    }

    /**
     * 写文件
     */
    private void writeObject(Set<WorkerAndCriterion> objSet, String path) {
        FileCreator.createFile(path);
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File(path)))) {
            os.writeObject(objSet);
        } catch (IOException e) {
            Log.log("写对象异常", LogPriority.ERROR);
            e.printStackTrace();
        }
    }
}
