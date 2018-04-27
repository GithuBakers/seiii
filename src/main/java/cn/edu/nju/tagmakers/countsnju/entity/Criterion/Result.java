package cn.edu.nju.tagmakers.countsnju.entity.Criterion;

import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;

import java.io.Serializable;
import java.util.Calendar;

public class Result implements Serializable {
    private static final long serialVersionUID = 400L;

    Bare bare;
    Calendar submitTime;
    boolean correct;
    boolean hasTested;
    /**
     * 增加一个在当前的要标注的若干张图片里面是否被标注过，和整个是否被标注过区分开
     * 这个字段只用于判断当前若干张是否完成了标注，然后判断是否需要获取新的图片
     */
    boolean finishedInCurrentTest;

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

    public boolean isFinishedInCurrentTest() {
        return finishedInCurrentTest;
    }

    public void setFinishedInCurrentTest(boolean finishedInCurrentTest) {
        this.finishedInCurrentTest = finishedInCurrentTest;
    }
}
