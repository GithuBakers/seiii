package cn.edu.nju.tagmakers.countsnju.entity.vo.diagram;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Description:
 * 反应工人近期 某一任务 标注增加数量/完成情况 的VO
 *
 * @author xxz
 * Created on 04/27/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class WorkerRecentTaskVO {
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
    private int compeleteness;
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

    public int getCompeleteness() {
        return compeleteness;
    }

    public void setCompeleteness(int compeleteness) {
        this.compeleteness = compeleteness;
    }

    public List<TimeAndValue> getRecent() {
        return recent;
    }

    public void setRecent(List<TimeAndValue> recent) {
        this.recent = recent;
    }
}
