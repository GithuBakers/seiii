package cn.edu.nju.tagmakers.countsnju.entity.vo;

import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;

import java.io.Serializable;
import java.util.Calendar;

public class WorkerTestHistoryVO implements Serializable {
    private static final long serialVersionUID = 888L;

    private Calendar submitTime;
    private MarkType type;

    public WorkerTestHistoryVO() {

    }

    public WorkerTestHistoryVO(MarkType type, Calendar submitTime) {
        this.type = type;
        this.submitTime = submitTime;

    }

    public Calendar getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Calendar submitTime) {
        this.submitTime = submitTime;
    }

    public MarkType getType() {
        return type;
    }

    public void setType(MarkType type) {
        this.type = type;
    }
}
