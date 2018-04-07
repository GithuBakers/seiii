package cn.edu.nju.tagmakers.countsnju.entity.vo;

import cn.edu.nju.tagmakers.countsnju.entity.user.Task;
import cn.edu.nju.tagmakers.countsnju.entity.user.Worker;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.Map;

/**
 * Description:
 * 用户查看已完成任务列表的列表VO
 *
 * @author xxz
 * Created on 04/06/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class WorkerReceivedTaskVO {

    @JsonUnwrapped
    private TaskVOBasicInformation basicInformation;

    /**
     * 当前用户已经标注的数量
     */
    @JsonProperty(value = "actual_number")
    private int markNumber;

    /**
     * 当前用户已获得的标注总量
     */
    @JsonProperty(value = "total_reward")
    private int totalReward;

    /**
     * 用户标记次数是否已经达到任务最大值 || 任务是否已经被发起者结束
     */
    @JsonProperty(value = "finished")
    private boolean isFinished;

    public WorkerReceivedTaskVO(Task task, Worker worker) {
        basicInformation = new TaskVOBasicInformation(task);

        Map<String, Integer> userMarked = task.getUserMarked();
        for (int num : userMarked.values()) {
            markNumber += num;
        }

        totalReward = markNumber / 10 * task.getReward();

        if (task.getFinished()) {
            isFinished = true;
        } else {
            String userID = worker.getUserID();
            if (userMarked.get(userID) == task.getLimit()) {
                isFinished = true;
            }
        }
        //TODO;
    }
}
