package cn.edu.nju.tagmakers.countsnju.entity.vo;

import cn.edu.nju.tagmakers.countsnju.entity.Criterion;
import cn.edu.nju.tagmakers.countsnju.entity.Task;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.Map;

import java.util.List;

/**
 * Description:
 * 发起者查看任务详情
 *
 * @author xxz
 * Created on 04/19/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class InitiatorTaskDetailVO {
    @JsonUnwrapped
    private Task task;

    @JsonProperty("total_reward")
    private int totalReward;

    /**
     * 任务当前已达标图片的比例
     */
    @JsonProperty(value = "completeness")
    private Float completeness;
    @JsonUnwrapped
    private List<Criterion> dependencies;

    public InitiatorTaskDetailVO(Task task) {
        this.task = task;
        totalReward = (int) (0.4 *
                task.getBareMarked()
                        .values().stream()
                        .reduce((integer, integer2) -> integer + integer2)
                        .orElse(0)
                * task.getReward());
        this.dependencies = task.getDependencies();

        Map<String, Integer> userMarked = task.getUserMarked();

        //累加所有的标注量之后进行换算
        int curAim = 0;
        for (Integer num : userMarked.values()) {
            if (num > task.getAim()) {
                curAim++;
            }
        }
        completeness = (float) (int) ((double) curAim * 100 / task.getAim());
    }
}
