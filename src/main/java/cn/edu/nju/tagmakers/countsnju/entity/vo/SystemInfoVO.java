package cn.edu.nju.tagmakers.countsnju.entity.vo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Description:
 * 用于向管理员展示的系统基本信息VO
 *
 * @author xxz
 * Created on 04/14/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SystemInfoVO {

    /**
     * 发起者数量
     */
    @JsonProperty("initiator_number")
    private int initiatorNumber;
    /**
     * 工人数量
     */
    @JsonProperty("worker_number")
    private int workerNumber;
    /**
     * 总用户数
     */
    @JsonProperty("total_user_number")
    private int totalUserNumber;
    /**
     * 未结束任务数
     */
    @JsonProperty("unfinished_number")
    private int unfinishedNumber;
    /**
     * 已结束任务数
     */
    @JsonProperty("finished_number")
    private int finishedNumber;
    /**
     * 总任务数
     */
    @JsonProperty("total_task_number")
    private int totalTaskNumber;


    public int getInitiatorNumber() {
        return initiatorNumber;
    }

    public void setInitiatorNumber(int initiatorNumber) {
        this.initiatorNumber = initiatorNumber;
    }

    public int getWorkerNumber() {
        return workerNumber;
    }

    public void setWorkerNumber(int workerNumber) {
        this.workerNumber = workerNumber;
    }

    public int getTotalUserNumber() {
        return totalUserNumber;
    }

    public void setTotalUserNumber(int totalUserNumber) {
        this.totalUserNumber = totalUserNumber;
    }

    public int getUnfinishedNumber() {
        return unfinishedNumber;
    }

    public void setUnfinishedNumber(int unfinishedNumber) {
        this.unfinishedNumber = unfinishedNumber;
    }

    public int getFinishedNumber() {
        return finishedNumber;
    }

    public void setFinishedNumber(int finishedNumber) {
        this.finishedNumber = finishedNumber;
    }

    public int getTotalTaskNumber() {
        return totalTaskNumber;
    }

    public void setTotalTaskNumber(int totalTaskNumber) {
        this.totalTaskNumber = totalTaskNumber;
    }
}
