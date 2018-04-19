package cn.edu.nju.tagmakers.countsnju.entity;

import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Criterion extends Entity<Criterion> implements Serializable {
    public Criterion() {
    }

    public Criterion(Criterion toCopy) {
        this.criterionID = toCopy.criterionID;
        this.criterionName = toCopy.criterionName;
        this.cover = toCopy.cover;
        this.type = toCopy.type;
        this.dataSet = toCopy.dataSet;
        this.aim = toCopy.aim;
        this.requirement = toCopy.requirement;
        if (toCopy.dataSet != null) {
            this.dataSet = new ArrayList<>(toCopy.dataSet);
        }
        if (toCopy.keywords != null) {
            this.keywords = new ArrayList<>(toCopy.keywords);
        }

    }

    private static final long serialVersionUID = 86L;
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
