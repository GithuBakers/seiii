package cn.edu.nju.tagmakers.countsnju.entity.vo;

import cn.edu.nju.tagmakers.countsnju.entity.Task;
import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Description:
 * 任务的基本信息
 *
 * @author xxz
 * Created on 04/06/2018
 * <p>
 * Update:
 * 增加了字段"keywords"
 * @author xxz
 * Created on 04/14/2018
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

    @JsonProperty(value = "keywords")
    private List<String> keywords;

    public TaskVOBasicInformation(Task task) {
        taskID = task.getTaskID();
        taskName = task.getTaskName();
        cover = task.getCover();
        type = task.getType();
        keywords = task.getKeywords();
    }
}
