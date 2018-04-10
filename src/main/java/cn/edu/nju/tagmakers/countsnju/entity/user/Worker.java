package cn.edu.nju.tagmakers.countsnju.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Description:
 * 众包工人
 *
 * @author xxz
 * Created on 04/06/2018
 * <p>
 * Update:
 * 增加了从user到众包工人的构造方法（用于注册）
 * @author xxz
 * Created on 04/07/2018
 */
public class Worker extends User implements Serializable {
    private static final long serialVersionUID = 60L;

    public Worker() {

    }

    public Worker(User user) {
        this.userID = user.getUserID();
        this.avatar = user.getAvatar();
        this.password = user.getPassword();
        this.nickName = user.getNickName();
        this.role = user.getRole();
    }

    /**
     * 已标注的图片(ID)列表
     */
    private List<String> bareIDs;

    /**
     * 已接受的任务列表
     */
    @JsonIgnore
    private List<String> taskNames;

    private Worker(Worker toCopy) {
        //公共字段
        this.userID = toCopy.getUserID();
        this.avatar = toCopy.getAvatar();
        this.password = toCopy.getPassword();
        this.nickName = toCopy.getNickName();
        this.role = toCopy.getRole();

        //工人字段
        if (toCopy.taskNames != null) {
            this.taskNames = new LinkedList<>(toCopy.taskNames);
        }
        if (toCopy.bareIDs != null) {
            this.bareIDs = new LinkedList<>(toCopy.bareIDs);
        }
        this.credit = toCopy.credit;
        this.rank = toCopy.rank;
    }

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

    public List<String> getBareIDs() {
        return Optional.ofNullable(bareIDs).orElse(new ArrayList<>());
    }

    public void setBareIDs(List<String> bareIDs) {
        this.bareIDs = bareIDs;
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
    public Worker copy() {
        return new Worker(this);
    }
}
