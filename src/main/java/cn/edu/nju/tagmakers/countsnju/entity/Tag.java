package cn.edu.nju.tagmakers.countsnju.entity;

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
 */
@JsonAutoDetect(fieldVisibility = ANY)
public class Tag extends Entity implements Serializable {
    private static final long serialVersionUID = 7L;
    //全部tag的唯一性标识，tagID=bareID_id
    @JsonIgnore
    private String tagID;
    //编号（比如某一张图的第几个标签)
    @JsonProperty(value = "id")
    private String numberID;
    //原图id
    @JsonIgnore
    private String bareID;
    private Mark mark;
    @JsonUnwrapped
    private Comment comment;

    public Tag() {

    }
    /**
     * 用于代替clone方法的构造器
     *
     * @param toClone 要拷贝的对象
     */
    public Tag(Tag toClone) {
        this.comment = toClone.getComment();
        this.mark = toClone.getMark();
        this.numberID = toClone.getNumberID();
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

    /**
     * 获取实体对象的主键
     *
     * @return id
     */
    @Override
    public String getPrimeKey() {
        return tagID;
    }
}
