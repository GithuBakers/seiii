package cn.edu.nju.tagmakers.countsnju.entity.Criterion;

import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;

import java.util.Calendar;

public class Result {
    Bare bare;
    Calendar submitTime;
    boolean passed;
    boolean hasTested;

    Result(Bare bare) {
        this.bare = bare;
    }

    public Bare getBare() {
        return bare;
    }

    public void setBare(Bare bare) {
        this.bare = bare;
    }

    public Calendar getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Calendar submitTime) {
        this.submitTime = submitTime;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public boolean isHasTested() {
        return hasTested;
    }

    public void setHasTested(boolean hasTested) {
        this.hasTested = hasTested;
    }
}
