package cn.edu.nju.tagmakers.countsnju.entity.vo.diagram;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Description:
 * 发起者任务近期情况
 *
 * @author xxz
 * Created on 04/28/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class InitiatorRecentTaskVO {
    /**
     * 任务名
     */
    @JsonProperty("task_name")
    private String taskName;

    /**
     * 任务ID
     */
    @JsonProperty("task_id")
    private String taskID;

    /**
     * 任务完成情况
     */
    @JsonProperty("completeness")
    private int completeness;
    /**
     * 近期完成状况
     */
    @JsonProperty("recent")
    private List<TimeAndValue> recent;


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public int getCompleteness() {
        return completeness;
    }

    public void setCompleteness(int completeness) {
        this.completeness = completeness;
    }

    public List<TimeAndValue> getRecent() {
        return recent;
    }

    public void setRecent(List<TimeAndValue> recent) {
        this.recent = recent;
    }
}
