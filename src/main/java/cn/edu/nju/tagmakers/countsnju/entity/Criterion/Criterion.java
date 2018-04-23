package cn.edu.nju.tagmakers.countsnju.entity.Criterion;

import cn.edu.nju.tagmakers.countsnju.entity.Entity;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Tag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.*;

/**
 * Update:
 * 增加 发起者ID 字段
 * 增加 答案 字段
 *
 * @author xxz
 * Created on 04/22/2018
 */

/**
 * Description:
 * 标准集的实体对象
 *
 * @author wym
 * Created on
 * <p>
 * Update:增加workerPassed
 * @author wym
 * Last modified on 4/22
 */
public class Criterion extends Entity<Criterion> implements Serializable {
    /**
     * 由发起者提供的答案
     * Map[BareID, Tags]
     */
    @JsonIgnore
    private Map<String, List<Tag>> result;

    private static final long serialVersionUID = 86L;

    @JsonIgnore
    private final int numOfPerGet = 10;

    @JsonProperty(value = "criterion_id")
    private String criterionID;

    @JsonProperty(value = "criterion_name")
    private String criterionName;
    @JsonProperty(value = "cover")
    private String cover;

    @JsonProperty(value = "type")
    private MarkType type;

    @JsonProperty(value = "data_set")
    private List<Bare> dataSet;

    @JsonProperty(value = "aim")
    private int aim;

    @JsonProperty(value = "requirement")
    private String requirement;

    @JsonProperty(value = "keywords")
    private List<String> keywords;

    @JsonIgnore
    private Set<String> workerPassed;

    @JsonIgnore
    private String initiatorID;


    public Criterion(Criterion toCopy) {
        this.criterionID = toCopy.criterionID;
        this.criterionName = toCopy.criterionName;
        this.cover = toCopy.cover;
        this.type = toCopy.type;
        this.dataSet = toCopy.dataSet;
        this.aim = toCopy.aim;
        this.requirement = toCopy.requirement;
        this.initiatorID = toCopy.initiatorID;
        if (toCopy.dataSet != null) {
            this.dataSet = new ArrayList<>(toCopy.dataSet);
        }
        if (toCopy.keywords != null) {
            this.keywords = new ArrayList<>(toCopy.keywords);
        }
        if (toCopy.getWorkerPassed() != null) {
            this.workerPassed = new HashSet<>(toCopy.getWorkerPassed());
        }
        if (toCopy.result != null) {
            this.result = new ConcurrentHashMap<>(toCopy.result);
        }
    }

    public Criterion() {
    }

    public int getNumOfPerGet() {
        return numOfPerGet;
    }

    public String getCriterionID() {
        return criterionID;
    }

    public void setCriterionID(String criterionID) {
        this.criterionID = criterionID;
    }

    public String getCriterionName() {
        return criterionName;
    }

    public void setCriterionName(String criterionName) {
        this.criterionName = criterionName;
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
        return java.util.Optional.ofNullable(dataSet).orElse(new ArrayList<>());
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

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public List<String> getKeywords() {
        return java.util.Optional.ofNullable(keywords).orElse(new ArrayList<>());
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public Set<String> getWorkerPassed() {
        return Optional.ofNullable(workerPassed).orElse(new HashSet<>());
    }

    public void setWorkerPassed(Set<String> workerPassed) {
        this.workerPassed = workerPassed;
    }

    public String getInitiatorID() {
        return initiatorID;
    }

    public void setInitiatorID(String initiatorID) {
        this.initiatorID = initiatorID;
    }

    public Map<String, List<Tag>> getResult() {
        return Optional.ofNullable(result)
                .map(ConcurrentHashMap::new)
                .orElse(new ConcurrentHashMap<>());
    }

    public void setResult(Map<String, List<Tag>> result) {
        this.result = result;
    }

    /**
     * 获取实体对象的主键
     *
     * @return id
     */
    @Override
    public String getPrimeKey() {
        return criterionID;
    }

    /**
     * 为了不与clone冲突产生这个方法，调用构造器实现
     *
     * @return 新的对象
     */
    @Override
    public Criterion copy() {
        return new Criterion(this);
    }
}
