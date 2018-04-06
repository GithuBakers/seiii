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
     * 任务要求
     */
    @JsonProperty(value = "requirement")
    private String requirement;

    @JsonIgnore
    private Map<String, Integer> userMarked; //<USER_ID, MARKED_NUMBER>
}
