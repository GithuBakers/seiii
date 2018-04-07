package cn.edu.nju.tagmakers.countsnju.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Description:
 * 发起者
 *
 * @author xxz
 * Created on 04/06/2018
 * <p>
 * Update:
 * 增加从user到initiator的构造器（用于注册）
 * @author xxz
 * Created on 04/07/2018
 */
public class Initiator extends User {
    public Initiator() {

    }

    public Initiator(User user) {
        //公共字段
        this.userID = user.getUserID();
        this.avatar = user.getAvatar();
        this.password = user.getPassword();
        this.nickName = user.getNickName();
        this.role = user.getRole();
    }

    private Initiator(Initiator toCopy) {
        //公共字段
        this.userID = toCopy.getUserID();
        this.avatar = toCopy.getAvatar();
        this.password = toCopy.getPassword();
        this.nickName = toCopy.getNickName();
        this.role = toCopy.getRole();

        //发起者字段
        if (toCopy.taskNames != null) {
            this.taskNames = new LinkedList<>(toCopy.taskNames);
        }
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
