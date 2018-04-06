package cn.edu.nju.tagmakers.countsnju.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * 一个众包任务
 *
 * @author xxz
 * Created on 04/06/2018
 */

public class Task {
    /**
     * 任务的唯一标识
     */
    @JsonProperty(value = "task_name")
    private String taskName;

    /**
     * 发起者名字
     */
    @JsonProperty(value = "initiator_name")
    private String initiatorName;

    /**
     * 任务封面图
     */
    @JsonProperty(value = "cover")
    private String cover;

    /**
     * 任务类型
     */
    @JsonProperty(value = "type")
    private MarkType type;

    /**
     * 数据集
     */
    @JsonProperty(value = "data_set")
    private List<Bare> dataSet;

    /**
     * 目标标注人数
     */
    @JsonProperty(value = "aim")
    private int aim;

    /**
     * 每个用户最多标注的图片数量
     */
    @JsonProperty(value = "limit")
    private int limit;

    /**
     * 奖励规则
     */
    @JsonProperty(value = "reward")
    private int reward;

    /**
     * 任务的统计结果所在地
     */
    @JsonProperty(value = "result")
    private String result;

    /**
     * 任务要求
     */
    @JsonProperty(value = "requirement")
    private String requirement;

    /**
     * user和已经标注的数量
     */
    @JsonIgnore
    private Map<String, Integer> userMarked; //<USER_ID, MARKED_NUMBER>

    /**
     * bare和已经标注的数量
     */
    @JsonIgnore
    private Map<String, Integer> bareMarked; //<BARE_ID, MARKED_NUMBER>


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public void setInitiatorName(String initiatorName) {
        this.initiatorName = initiatorName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public MarkType getType() {
        return type;
    }

    public void setType(MarkType type) {
        this.type = type;
    }

    public List<Bare> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<Bare> dataSet) {
        this.dataSet = dataSet;
    }

    public int getAim() {
        return aim;
    }

    public void setAim(int aim) {
        this.aim = aim;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public Map<String, Integer> getUserMarked() {
        return userMarked;
    }

    public void setUserMarked(Map<String, Integer> userMarked) {
        this.userMarked = userMarked;
    }

    public Map<String, Integer> getBareMarked() {
        return bareMarked;
    }

    public void setBareMarked(Map<String, Integer> bareMarked) {
        this.bareMarked = bareMarked;
    }
}
