package cn.edu.nju.tagmakers.countsnju.entity.vo;

import cn.edu.nju.tagmakers.countsnju.entity.user.Task;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.Map;

/**
 * Description:
 * 返回给发起者的任务列表中的表项
 *
 * @author xxz
 * Created on 04/06/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class InitiatorTaskVO {

    @JsonUnwrapped
    private TaskVOBasicInformation taskVOBasicInformation;

    /**
     * 任务当前已达标图片的比例
     */
    @JsonProperty(value = "completeness")
    private Float completeness;

    /**
     * 是否已完成
     */
    @JsonProperty(value = "finished")
    private Boolean isFinished;

    public InitiatorTaskVO(Task task) {
        taskVOBasicInformation = new TaskVOBasicInformation(task);
        Map<String, Integer> userMarked = task.getUserMarked();

        //累加所有的标注量之后进行换算
        int curAim = 0;
        for (Integer num : userMarked.values()) {
            curAim += num;
        }
        completeness = (float) (int) ((double) curAim * 100 / task.getAim());

        //是否结束
        isFinished = task.getFinished();
    }
}
