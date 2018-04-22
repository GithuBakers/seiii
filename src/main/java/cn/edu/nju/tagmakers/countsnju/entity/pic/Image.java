package cn.edu.nju.tagmakers.countsnju.entity.pic;

import cn.edu.nju.tagmakers.countsnju.entity.Entity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * 带有标注的图像（返回给前端的）
 *
 * @author xxz
 * Created on 03/17/2018
 * <p>
 * Update:
 * @author xxz
 * Last modified on
 * <p>
 * update: 增加了 标记类型 这个筛选条件
 * @author xxz
 * modified on 03/22/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Image extends Entity<Image> implements Serializable {
    private static final long serialVersionUID = 5L;
    @JsonUnwrapped
    private Bare bare;
    @JsonUnwrapped
    private List<Tag> tags;
    @JsonIgnore
    private MarkType type;

    public Bare getBare() {
        return bare;
    }

    public void setBare(Bare bare) {
        this.bare = bare;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public MarkType getType() {
        return type;
    }

    public void setType(MarkType type) {
        this.type = type;
    }


    /**
     * 获取实体对象的主键
     *
     * @return id
     */
    @Override
    public String getPrimeKey() {
        return null;
    }

    /**
     * 为了不与clone冲突产生这个方法，调用构造器实现
     *
     * @return 新的对象
     */
    @Override
    public Image copy() {
        return null;
    }
}
