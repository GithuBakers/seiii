package cn.edu.nju.tagmakers.countsnju.entity.Criterion;


import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * 工人和他做过的标准集
 *
 * @author wym
 * Created on
 * <p>
 * Update:
 * @author wym
 * Last modified on
 */
public class WorkerAndCriterion {
    private String workerID;
    private String criterionID;
    private boolean passed;
    private List<Result> results;
    private List<Bare> latestBares;
    /**
     * 每提交10张的正确率
     */
    private List<Double> accuracy;

    public WorkerAndCriterion(String workerID, Criterion criterion) {
        this.workerID = workerID;
        criterionID = criterion.getCriterionID();
        List<Bare> dataSet = criterion.getDataSet();
        for (Bare temp : dataSet) {
            results.add(new Result(temp));
        }
        accuracy = new ArrayList<>();
        latestBares = new ArrayList<>();
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getWorkerID() {
        return workerID;
    }

    public void setWorkerID(String workerID) {
        this.workerID = workerID;
    }

    public String getCriterionID() {
        return criterionID;
    }

    public void setCriterionID(String criterionID) {
        this.criterionID = criterionID;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public List<Double> getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(List<Double> accuracy) {
        this.accuracy = accuracy;
    }

    public List<Bare> getLatestBares() {
        return latestBares;
    }

    public void setLatestBares(List<Bare> latestBares) {
        this.latestBares = latestBares;
    }
}
