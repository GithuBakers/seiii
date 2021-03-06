package cn.edu.nju.tagmakers.countsnju.entity.pic;

import cn.edu.nju.tagmakers.countsnju.entity.Entity;
import cn.edu.nju.tagmakers.countsnju.entity.Mark;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

/**
 * Description:
 * 标注，包含标记和注释
 *
 * @author xxz
 * Created on 03/17/2018
 * <p>
 * Update:增加了一个id属性
 * @author wym
 * Last modified on 03/20/2018
 * <p>
 * Update:增加了一个bareID属性
 * @author wym
 * Last modified on 03/20/2018
 * <p>
 * update: 调整了属性的命名
 * @author xxz
 * modified on 03/21/2018
 * <p>
 * Update:
 * 增加了userID这个字段
 * @author xxz
 * Created on 04/07/2018
 * <p>
 * Update:
 * 增加字段：提交时间
 * @author xxz
 * Created on 04/27/2018
 */
@JsonAutoDetect(fieldVisibility = ANY)
public class Tag extends Entity<Tag> implements Serializable {
    private static final long serialVersionUID = 7L;
    /**
     * 全部tag的唯一性标识
     */
    @JsonProperty("id")
    private String tagID;
    /**
     * 编号（比如某一张图的第几个标签)
     */
    @JsonIgnore
    private String numberID;
    /**
     * 原图id
     */
    @JsonIgnore
    private String bareID;

    @JsonProperty(value = "mark")
    private Mark mark;

    @JsonUnwrapped
    private Comment comment;

    /**
     * 做标注的工人的ID
     */
    @JsonIgnore
    private String workerID;

    /**
     * 提交时间
     */
    @JsonIgnore
    private Long submitTime;

    public Tag() {

    }

    /**
     * 用于代替clone方法的构造器
     *
     * @param toCopy 要拷贝的对象
     */
    public Tag(Tag toCopy) {
        this.comment = toCopy.comment;
        this.mark = toCopy.mark;
        this.numberID = toCopy.numberID;
        this.workerID = toCopy.workerID;
        this.bareID = toCopy.bareID;
        this.tagID = toCopy.tagID;
        this.submitTime = toCopy.submitTime;
    }

    public String getTagID() {
        return tagID;
    }

    public void setTagID(String tagID) {
        this.tagID = tagID;
    }

    public String getNumberID() {
        return numberID;
    }

    public void setNumberID(String numberID) {
        this.numberID = numberID;
    }

    public Mark getMark() {
        return mark;
    }

    public void setMark(Mark mark) {
        this.mark = mark;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public String getBareID() {
        return bareID;
    }

    public void setBareID(String bareID) {
        this.bareID = bareID;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getWorkerID() {
        return workerID;
    }

    public void setWorkerID(String workerID) {
        this.workerID = workerID;
    }

    public Long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Long submitTime) {
        this.submitTime = submitTime;
    }

    @Override
    public String toString() {
        return bareID + "," + comment + "," + mark.toString();
    }

    /**
     * 获取实体对象的主键
     *
     * @return id
     */
    @Override
    public String getPrimeKey() {
        return tagID;
    }

    /**
     * 为了不与clone冲突产生这个方法，调用构造器实现
     *
     * @return 新的对象
     */
    @Override
    public Tag copy() {
        return new Tag(this);
    }
}
