package cn.edu.nju.tagmakers.countsnju.entity.pic;

import cn.edu.nju.tagmakers.countsnju.entity.Entity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Description:
 * 不含标注的图片
 *
 * @author xxz
 * Created on 03/17/2018
 * <p>
 * Update:增加了clone方法
 * @author wym
 * Last modified on 03/19/2018
 * <p>
 * update:增加了状态字段
 * @author xxz
 * modified on
 * Update:增加了获取主键方法
 * @author wym
 * Last modified on 04/06/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Bare extends Entity<Bare> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 图片ID
     */
    @JsonProperty(value = "id")
    private String id;
    /**
     * 图片名
     */
    @JsonProperty(value = "name")
    private String name;
    /**
     * 缩略图URL
     */
    @JsonProperty(value = "url_small")
    private String thumbnail;
    /**
     * 小缩略图URL
     */
    @JsonProperty(value = "url_ex_small")
    private String exThumbnail;
    /**
     * 原图URL
     */
    @JsonProperty(value = "url")
    private String raw;

    /**
     * 原图状态
     */
    @JsonIgnore
    private BareState state = BareState.UNMARKED;

    /**
     * 需要做的标注类型
     */
    @JsonProperty(value = "mark_type")
    private MarkType markType = MarkType.DEFAULT;

    public Bare() {

    }

    /**
     * 替换clone方法的构造器
     *
     * @param toCopy 要拷贝的对象
     */
    public Bare(Bare toCopy) {
        this.exThumbnail = toCopy.exThumbnail;
        this.id = toCopy.id;
        this.name = toCopy.name;
        this.raw = toCopy.raw;
        this.thumbnail = toCopy.thumbnail;
        this.state = toCopy.state;
        this.markType = toCopy.markType;
    }

    public BareState getState() {
        return state;
    }

    public void setState(BareState state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getExThumbnail() {
        return exThumbnail;
    }

    public void setExThumbnail(String exThumbnail) {
        this.exThumbnail = exThumbnail;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public MarkType getMarkType() {
        return markType;
    }

    public void setMarkType(MarkType markType) {
        this.markType = markType;
    }


    /**
     * 获取实体对象的主键
     *
     * @return id
     */
    @Override
    public String getPrimeKey() {
        return id;
    }

    /**
     * 为了不与clone冲突产生这个方法，调用构造器实现
     *
     * @return 新的对象
     */
    @Override
    public Bare copy() {
        return new Bare(this);
    }
}
