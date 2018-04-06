package cn.edu.nju.tagmakers.countsnju.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Description:
 *
 * @author xxz
 * Created on 04/06/2018
 */
public class Initiator extends User {

    /**
     * 发起者发起的任务们
     */
    @JsonIgnore
    private List<String> taskNames;


}
