package cn.edu.nju.tagmakers.countsnju.entity.vo.diagram;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Description:
 * <p>
 * 起始时间 和 时间段内完成量
 * <p>
 * Created on 04/27/2018
 *
 * @author xxz
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class TimeAndValue {
    /**
     * 时间段起始时间
     */
    @JsonProperty("x")
    private long time;

    /**
     * 这段时间完成的任务量
     */
    @JsonProperty("y1")
    private long workload;

    @JsonProperty("y2")
    private long credits;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getWorkload() {
        return workload;
    }

    public void setWorkload(long workload) {
        this.workload = workload;
    }

    public long getCredits() {
        return credits;
    }

    public void setCredits(long credits) {
        this.credits = credits;
    }
}