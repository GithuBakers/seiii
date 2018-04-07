package cn.edu.nju.tagmakers.countsnju.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Description:
 *
 * @author xxz
 * Created on 04/06/2018
 */
public class Initiator extends User {
    public Initiator(){

    }

    public Initiator(Initiator toCopy){
        //公共字段
        this.userID = toCopy.getUserID();
        this.avatar = toCopy.getAvatar();
        this.password = toCopy.getPassword();
        this.nickName = toCopy.getNickName();
        this.role = toCopy.getRole();

        //发起者字段
        this.taskNames = toCopy.taskNames;
    }

    /**
     * 发起者发起的任务们
     */
    @JsonIgnore
    private List<String> taskNames;

    public List<String> getTaskNames() {
        return Optional.ofNullable(taskNames).orElse(new ArrayList<>());
    }

    public void setTaskNames(List<String> taskNames) {
        this.taskNames = taskNames;
    }

    /**
     * 为了不与clone冲突产生这个方法，调用构造器实现
     *
     * @return 新的对象
     */
    @Override
    public Initiator copy() {
        return new Initiator(this);
    }
}
