package cn.edu.nju.tagmakers.countsnju.entity.vo;

import cn.edu.nju.tagmakers.countsnju.entity.Task;
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
public class WorkerReceivedTaskDetailVO {

    @JsonUnwrapped
    private WorkerReceivedTaskVO taskVO;

    /**
     * 单个用户任务限制
     */
    @JsonProperty(value = "limit")
    private int limit;

    /**
     * 奖励规则
     */
    @JsonProperty(value = "reward")
    private int reward;

    public WorkerReceivedTaskDetailVO(Task task, Worker worker) {
        //TODO:
    }
}
