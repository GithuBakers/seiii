package cn.edu.nju.tagmakers.countsnju.entity;

import cn.edu.nju.tagmakers.countsnju.entity.Criterion.Criterion;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;
import cn.edu.nju.tagmakers.countsnju.entity.vo.diagram.BareAndCluster;
import cn.edu.nju.tagmakers.countsnju.entity.vo.diagram.SexAndAge;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * 一个众包任务
 *
 * @author xxz
 * Created on 04/06/2018
 * <p>
 * Update:
 * 增加了任务状态（是否已完成
 * @author xxz
 * Created on 04/07/2018
 * <p>
 * Update:增加了拷贝和构造
 * @author wym
 * Modified on 04/07/2018
 * <p>
 * Update:数据结构nullptr异常
 * @author wym
 * Modified on 04/07/2018
 * <p>
 * Update:
 * 增加keywords关键字
 * @author xxz
 * Created on 04/14/2018
 * <p>
 * Update:
 * 增加字段 结果集是否计算完成(has result)
 * 用户标注数量和用户标注聚集程度(bare and cluster)
 * @author xxz
 * Created on 04/28/2018
 * <p>
 * Update:
 * 增加字段：标记工人的性别/年龄分布
 * @author xxz
 * Created on 04/28/2018
 */

public class Task extends Entity<Task> implements Serializable {
    private static final long serialVersionUID = 80L;
    /**
     * 任务的唯一标识
     */
    @JsonProperty(value = "task_id")
    private String taskID;
    /**
     * 任务的名称
     */
    @JsonProperty(value = "task_name")
    private String taskName;
    /**
     * 发起者名字
     */
    @JsonProperty(value = "initiator_name")
    private String initiatorName;
    /**
     * 任务封面图
     */
    @JsonProperty(value = "cover")
    private String cover;
    /**
     * 任务类型
     */
    @JsonProperty(value = "type")
    private MarkType type;
    /**
     * 数据集
     */
    @JsonProperty(value = "data_set")
    private List<Bare> dataSet;
    /**
     * 目标标注人数
     */
    @JsonProperty(value = "aim")
    private int aim;
    /**
     * 每个用户最多标注的图片数量
     */
    @JsonProperty(value = "limit")
    private int limit;
    /**
     * 奖励规则
     */
    @JsonProperty(value = "reward")
    private int reward;
    /**
     * 任务的统计结果所在地
     */
    @JsonProperty(value = "result")
    private String result;
    /**
     * 任务要求
     */
    @JsonProperty(value = "requirement")
    private String requirement;
    /**
     * 是否已完成
     */
    @JsonProperty(value = "finished")
    private Boolean isFinished;
    /**
     * 任务中可用的关键字
     */
    @JsonProperty("keywords")
    private List<String> keywords;

    /**
     * 依赖的标准集对象
     */
    @JsonProperty(value = "dependencies")
    private List<Criterion> dependencies;

    /**
     * 是否已经计算完成结果集
     */
    @JsonProperty("has_result")
    private Boolean hasResult;

    /**
     * user和已经标注的数量
     */
    @JsonIgnore
    private Map<String, Integer> userMarked; //<USER_ID, MARKED_NUMBER>
    /**
     * bare和已经标注的数量
     */
    @JsonIgnore
    private Map<String, Integer> bareMarked; //<BARE_ID, MARKED_NUMBER>

    /**
     * 用户的 性别-年龄 分布
     */
    @JsonProperty("sex_age")
    private List<SexAndAge> userDistribution;
    /**
     * 用户标注数量-用户标注聚集程度
     */
    @JsonProperty("hive")
    private List<BareAndCluster> bareAndClusters;

    public Task() {

    }

    public Task(Task toCopy) {
        this.taskID = toCopy.taskID;
        this.taskName = toCopy.taskName;
        this.initiatorName = toCopy.initiatorName;
        this.cover = toCopy.cover;
        this.type = toCopy.type;
        this.dataSet = toCopy.dataSet;
        this.aim = toCopy.aim;
        this.limit = toCopy.limit;
        this.reward = toCopy.reward;
        this.result = toCopy.result;
        this.requirement = toCopy.requirement;
        this.isFinished = toCopy.isFinished;
        if (toCopy.keywords != null) {
            this.keywords = new ArrayList<>(toCopy.keywords);
        }
        if (toCopy.userMarked != null) {
            this.userMarked = new HashMap<>(toCopy.userMarked);
        }
        if (toCopy.bareMarked != null) {
            this.bareMarked = new HashMap<>(toCopy.bareMarked);
        }
        if (toCopy.dependencies != null) {
            this.dependencies = new ArrayList<>(toCopy.dependencies);
        }
        this.hasResult = toCopy.hasResult;
        if (toCopy.bareAndClusters != null) {
            this.bareAndClusters = new ArrayList<>(toCopy.bareAndClusters);
        }
        if (toCopy.userDistribution != null) {
            this.userDistribution = new ArrayList<>(toCopy.userDistribution);
        }
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public void setInitiatorName(String initiatorName) {
        this.initiatorName = initiatorName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public MarkType getType() {
        return type;
    }

    public void setType(MarkType type) {
        this.type = type;
    }

    public List<Bare> getDataSet() {
        return Optional.ofNullable(dataSet).orElse(new ArrayList<>());
    }

    public void setDataSet(List<Bare> dataSet) {
        this.dataSet = dataSet;
    }

    public int getAim() {
        return aim;
    }

    public void setAim(int aim) {
        this.aim = aim;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public Map<String, Integer> getUserMarked() {

        return Optional.ofNullable(userMarked).orElse(new ConcurrentHashMap<>());
    }

    public void setUserMarked(Map<String, Integer> userMarked) {
        this.userMarked = userMarked;
    }

    public Map<String, Integer> getBareMarked() {
        return Optional.ofNullable(bareMarked).orElse(new ConcurrentHashMap<>());
    }

    public void setBareMarked(Map<String, Integer> bareMarked) {
        this.bareMarked = bareMarked;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public List<String> getKeywords() {
        return Optional.ofNullable(keywords).orElse(new ArrayList<>());
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<Criterion> getDependencies() {
        return Optional.ofNullable(dependencies).orElse(new ArrayList<>());
    }

    public void setDependencies(List<Criterion> dependencies) {
        this.dependencies = dependencies;
    }

    public List<BareAndCluster> getBareAndClusters() {
        return Optional.ofNullable(bareAndClusters).orElse(new LinkedList<>());
    }

    public void setBareAndClusters(List<BareAndCluster> bareAndClusters) {
        this.bareAndClusters = bareAndClusters;
    }

    public Boolean getHasResult() {
        return hasResult;
    }

    public void setHasResult(Boolean hasResult) {
        this.hasResult = hasResult;
    }

    public List<SexAndAge> getUserDistribution() {
        return Optional.ofNullable(userDistribution).orElse(new LinkedList<>());
    }

    public void setUserDistribution(List<SexAndAge> userDistribution) {
        this.userDistribution = userDistribution;
    }

    /**
     * 获取实体对象的主键
     *
     * @return id
     */
    @Override
    public String getPrimeKey() {
        return taskID;
    }

    /**
     * 为了不与clone冲突产生这个方法，调用构造器实现
     *
     * @return 新的对象
     */
    @Override
    public Task copy() {
        return new Task(this);
    }
}
