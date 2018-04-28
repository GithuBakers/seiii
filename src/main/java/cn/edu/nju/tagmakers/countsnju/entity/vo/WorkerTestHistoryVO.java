package cn.edu.nju.tagmakers.countsnju.entity.vo;

import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;

import java.io.Serializable;

public class WorkerTestHistoryVO implements Serializable {
    private static final long serialVersionUID = 888L;

    private long submitTime;
    private MarkType type;

    public WorkerTestHistoryVO() {

    }

    public WorkerTestHistoryVO(MarkType type, long submitTime) {
        this.type = type;
        this.submitTime = submitTime;

    }

    public long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Long submitTime) {
        this.submitTime = submitTime;
    }

    public MarkType getType() {
        return type;
    }

    public void setType(MarkType type) {
        this.type = type;
    }
}
