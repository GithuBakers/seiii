package cn.edu.nju.tagmakers.countsnju.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Description:
 *
 * @author xxz
 * Created on 04/06/2018
 */
public class Worker extends User {
    /**
     * 已接受的任务列表
     */
    @JsonIgnore
    private List<String> taskNames;

    @JsonProperty
    private int credit;

    @JsonProperty
    private int rank;
}
