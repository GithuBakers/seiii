package cn.edu.nju.tagmakers.countsnju.entity;

import cn.edu.nju.tagmakers.countsnju.entity.pic.Description;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Edge;
import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Rect;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

/**
 * Description:
 * 标记的抽象公共类
 *
 * @author xxz
 * Created on 03/17/2018
 * <p>
 * Update: 增加了枚举类型表示的类型
 * @author xxz
 * Last modified on 21/03/2018
 */
@JsonAutoDetect(fieldVisibility = ANY)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Description.class, name = "DESC"),
        @JsonSubTypes.Type(value = Rect.class, name = "RECT"),
        @JsonSubTypes.Type(value = Edge.class, name = "EDGE")})
public abstract class Mark implements Serializable {
    @JsonProperty(value = "type")
    protected MarkType type;

    public MarkType getType() {
        return type;
    }

}
