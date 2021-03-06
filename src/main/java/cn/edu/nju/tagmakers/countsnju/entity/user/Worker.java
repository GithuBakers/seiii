package cn.edu.nju.tagmakers.countsnju.entity.user;

import cn.edu.nju.tagmakers.countsnju.entity.Criterion.Criterion;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerTestHistoryVO;
import cn.edu.nju.tagmakers.countsnju.entity.vo.diagram.WorkerCapability;
import cn.edu.nju.tagmakers.countsnju.entity.vo.diagram.WorkerRecentTags;
import cn.edu.nju.tagmakers.countsnju.entity.vo.diagram.WorkerRecentTaskVO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

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
 * <p>
 * Update:
 * 增加Map记录下工人接受任务的时间
 * @author xxz
 * Created on 04/26/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
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
        this.sex = user.sex;
        this.birthdayString = user.birthdayString;
    }

    private Worker(Worker toCopy) {
        //公共字段
        this.userID = toCopy.getUserID();
        this.avatar = toCopy.getAvatar();
        this.password = toCopy.getPassword();
        this.nickName = toCopy.getNickName();
        this.role = toCopy.getRole();
        this.sex = toCopy.sex;
        this.birthdayString = toCopy.birthdayString;

        //工人字段
        if (toCopy.taskIDs != null) {
            this.taskIDs = new LinkedList<>(toCopy.taskIDs);
        }
        if (toCopy.bareIDs != null) {
            this.bareIDs = new LinkedList<>(toCopy.bareIDs);
        }
        this.credit = toCopy.credit;
        this.rank = toCopy.rank;
        this.dependencies = toCopy.dependencies;
        this.receivedTime = toCopy.receivedTime;
        this.testHistory = toCopy.testHistory;
        this.errorLearningAbility = toCopy.errorLearningAbility;
    }

    /**
     * 已标注的图片(ID)列表
     */
    private List<String> bareIDs;

    /**
     * 已接受的任务列表
     */
    @JsonIgnore
    private List<String> taskIDs;


    @JsonProperty(value = "credit")
    private int credit;

    @JsonProperty(value = "rank")
    private int rank;

    @JsonProperty(value = "dependencies")
    private List<Criterion> dependencies;

    /**
     * 错误学习能力值
     */
    @JsonIgnore
    private Integer errorLearningAbility;

    /**
     * 工人接受任务的时间(保留最近30项）
     * Map ( taskID, receiveTime )
     */
    @JsonIgnore
    private Map<String, Long> receivedTime;

    @JsonIgnore
    private List<WorkerTestHistoryVO> testHistory;
    /* * * * * * * * * * * * * * * * * * * *
     *
     * 不需要持久化的字段们
     *
     ** * * * * * * * * * * * * * * * * * * *
     */

    /**
     * 工人近期任务情况（不需要持久化，随用随算）
     */
    @JsonProperty("tasks")
    private List<WorkerRecentTaskVO> tasks;

    /**
     * 工人近期任务的组成
     */
    @JsonProperty("tags")
    @JsonUnwrapped
    private WorkerRecentTags recentTags;

    /**
     * 工人能力/可信程度 的五个维度
     */
    @JsonProperty("capability")
    @JsonUnwrapped
    private WorkerCapability capability;


    public List<WorkerTestHistoryVO> getTestHistory() {
        return Optional.ofNullable(testHistory).orElse(new ArrayList<>());
    }

    public void setTestHistory(List<WorkerTestHistoryVO> testHistory) {
        this.testHistory = testHistory;
    }

    public List<Criterion> getDependencies() {
        return Optional.ofNullable(dependencies).orElse(new LinkedList<>());
    }

    public void setDependencies(List<Criterion> dependencies) {
        this.dependencies = dependencies;
    }

    public Map<String, Long> getReceivedTime() {
        return Optional.ofNullable(receivedTime).orElse(new TreeMap<>());
    }

    public void setReceivedTime(Map<String, Long> receivedTime) {
        int MAX = 30;
        if (receivedTime != null && receivedTime.size() > MAX) {
            this.receivedTime = new HashMap<>();
            receivedTime.entrySet().stream()
                    .sorted(((o1, o2) -> {
                        if (o2.getValue().equals(o1.getValue())) {
                            return 0;
                        } else {
                            return o2.getValue() - o1.getValue() > 0 ? 1 : -1;
                        }
                    }))
                    .collect(Collectors.toList())
                    //外层if条件句已保证至少有MAX个元素
                    .subList(0, MAX)
                    .forEach(entry -> this.receivedTime.put(entry.getKey(), entry.getValue()));

        } else {
            this.receivedTime = receivedTime;
        }
    }
    //这部分已经放在公共字段里了
//    @JsonProperty(value = "sex")
//    private Sex sex;
//
//    @JsonProperty(value = "birthday")
//    private String birthday;


    public List<String> getTaskIDs() {
        return Optional.ofNullable(taskIDs).orElse(new ArrayList<>());
    }

    public void setTaskIDs(List<String> taskIDs) {
        this.taskIDs = taskIDs;
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

    public List<WorkerRecentTaskVO> getTasks() {
        return tasks;
    }

    public void setTasks(List<WorkerRecentTaskVO> tasks) {
        this.tasks = tasks;
    }

    public WorkerRecentTags getRecentTags() {
        return recentTags;
    }

    public void setRecentTags(WorkerRecentTags recentTags) {
        this.recentTags = recentTags;
    }

    public WorkerCapability getCapability() {
        return capability;
    }

    public void setCapability(WorkerCapability capability) {
        this.capability = capability;
    }

    public Integer getErrorLearningAbility() {
        return errorLearningAbility == null ? 0 : errorLearningAbility;
    }

    public void setErrorLearningAbility(int errorLearningAbility) {
        this.errorLearningAbility = errorLearningAbility;
    }

    @Override
    public Worker copy() {
        return new Worker(this);
    }
}
