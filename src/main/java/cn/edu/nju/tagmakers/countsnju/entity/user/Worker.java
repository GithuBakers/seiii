package cn.edu.nju.tagmakers.countsnju.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.corba.se.spi.orbutil.threadpool.Work;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Description:
 *
 * @author xxz
 * Created on 04/06/2018
 */
public class Worker extends User {
    public Worker(){

    }

    public Worker(Worker toCopy){
        //公共字段
        this.userID = toCopy.getUserID();
        this.avatar = toCopy.getAvatar();
        this.password = toCopy.getPassword();
        this.nickName = toCopy.getNickName();
        this.role = toCopy.getRole();

        //工人字段
        this.taskNames = toCopy.taskNames;
        this.bareNames = toCopy.bareNames;
        this.credit = toCopy.credit;
        this.rank = toCopy.rank;
    }

    /**
     * 已接受的任务列表
     */
    @JsonIgnore
    private List<String> taskNames;

    /**
     * 已标注的图片(ID)列表
     */
    private List<String> bareNames;

    @JsonProperty
    private int credit;

    @JsonProperty
    private int rank;

    public List<String> getTaskNames() {
        return Optional.ofNullable(taskNames).orElse(new ArrayList<>());
    }

    public void setTaskNames(List<String> taskNames) {
        this.taskNames = taskNames;
    }

    public List<String> getBareNames() {
        return Optional.ofNullable(bareNames).orElse(new ArrayList<>());
    }

    public void setBareNames(List<String> bareNames) {
        this.bareNames = bareNames;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public Worker copy(){
        return new Worker(this);
    }
}
