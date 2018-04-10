package cn.edu.nju.tagmakers.countsnju.entity.vo;

import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;
import cn.edu.nju.tagmakers.countsnju.entity.user.Task;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Description:
 * 任务的基本信息
 *
 * @author xxz
 * Created on 04/06/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class TaskVOBasicInformation {
    /**
     * 任务名
     */
    @JsonProperty(value = "task_name")
    private String taskName;

    /**
     * 任务封面
     */
    @JsonProperty(value = "cover")
    private String cover;

    /**
     * 任务类型
     */
    @JsonProperty(value = "type")
    private MarkType type;

    @JsonProperty(value = "task_id")
    private String taskID;

    public TaskVOBasicInformation(Task task) {
        taskID = task.getTaskID();
        taskName = task.getTaskName();
        cover = task.getCover();
        type = task.getType();
    }
}
