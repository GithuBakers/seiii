package cn.edu.nju.tagmakers.countsnju.entity.vo;

import cn.edu.nju.tagmakers.countsnju.entity.user.Task;
import cn.edu.nju.tagmakers.countsnju.entity.user.Worker;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Description:
 *
 * @author xxz
 * Created on 04/06/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class WorkerTaskDetailVO {
    @JsonUnwrapped
    private TaskVOBasicInformation taskVOBasicInformation;

    /**
     * 单个用户标注的张数上限
     */
    @JsonProperty(value = "limit")
    private int limit;

    /**
     * 奖励
     */
    @JsonProperty(value = "reward")
    private int reward;

    /**
     * 对于某一任务的具体要求
     */
    @JsonProperty(value = "requirement")
    private String requirement;


    public WorkerTaskDetailVO(Task task) {
        taskVOBasicInformation = new TaskVOBasicInformation(task);
        limit = task.getLimit();
        reward = task.getReward();
        requirement = task.getRequirement();
        //TODO:
    }
}
