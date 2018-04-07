package cn.edu.nju.tagmakers.countsnju.entity.vo;

import cn.edu.nju.tagmakers.countsnju.entity.user.Task;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Description:
 * 众包工人查看的任务列表
 *
 * @author xxz
 * Created on 04/06/2018
 */

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class WorkerTaskVO {

    @JsonUnwrapped
    private TaskVOBasicInformation taskVOBasicInformation;

    /**
     * 奖励
     */
    @JsonProperty(value = "reward")
    private int reward;

    public WorkerTaskVO(Task task) {
        //TODO
    }
}
