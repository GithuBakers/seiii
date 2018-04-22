package cn.edu.nju.tagmakers.countsnju.entity.Criterion;

import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;

import java.util.Calendar;

public class Result {
    Bare bare;
    Calendar submitTime;
    boolean correct;
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

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public boolean isHasTested() {
        return hasTested;
    }

    public void setHasTested(boolean hasTested) {
        this.hasTested = hasTested;
    }
}
