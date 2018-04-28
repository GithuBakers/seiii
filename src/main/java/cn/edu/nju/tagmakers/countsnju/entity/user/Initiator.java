package cn.edu.nju.tagmakers.countsnju.entity.user;

import cn.edu.nju.tagmakers.countsnju.entity.vo.diagram.InitiatorRecentTaskVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
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
public class Initiator extends User implements Serializable {
    private static final long serialVersionUID = 40L;

    public Initiator() {

    }

    public Initiator(User user) {
        //公共字段
        this.userID = user.getUserID();
        this.avatar = user.getAvatar();
        this.password = user.getPassword();
        this.nickName = user.getNickName();
        this.role = user.getRole();
        this.birthday = user.getBirthday();
        this.sex = user.sex;
        this.birthdayString = user.birthdayString;
    }

    private Initiator(Initiator toCopy) {
        //公共字段
        this.userID = toCopy.getUserID();
        this.avatar = toCopy.getAvatar();
        this.password = toCopy.getPassword();
        this.nickName = toCopy.getNickName();
        this.role = toCopy.getRole();
        this.birthdayString = toCopy.birthdayString;
        this.sex = toCopy.sex;
        this.birthday = toCopy.getBirthday();
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

    /**
     * 近期任务完成情况
     * 不需要持久化
     */
    @JsonProperty("tasks")
    private List<InitiatorRecentTaskVO> recentTasks;

    public List<String> getTaskNames() {
        return Optional.ofNullable(taskNames).orElse(new ArrayList<>());
    }

    public void setTaskNames(List<String> taskNames) {
        this.taskNames = taskNames;
    }

    public List<InitiatorRecentTaskVO> getRecentTasks() {
        return recentTasks;
    }

    public void setRecentTasks(List<InitiatorRecentTaskVO> recentTasks) {
        this.recentTasks = recentTasks;
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
