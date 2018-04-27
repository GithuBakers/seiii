package cn.edu.nju.tagmakers.countsnju.entity.pic;

import cn.edu.nju.tagmakers.countsnju.entity.Mark;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

/**
 * Description:
 * 举行标记
 *
 * @author xxz
 * Created on 03/17/2018
 * <p>
 * update: 用枚举类型来表示mark类型
 * @author xxz
 * modified on 03/21/2018
 */
@JsonAutoDetect(fieldVisibility = ANY)
public class Rect extends Mark implements Serializable {
    private static final long serialVersionUID = 6L;
    /* * * * * * * * * * * * * * * * * * * * * * *
     *
     * 坐标
     *
     * * * * * * * * * * * * * * * * * * * * * * *
     */
    @JsonProperty(value = "x")
    private Integer x;
    @JsonProperty(value = "y")
    private Integer y;
    /**
     * 矩形宽度
     */
    @JsonProperty(value = "width")
    private Integer width;
    /**
     * 矩形高度
     */
    @JsonProperty(value = "height")
    private Integer height;
    /**
     * 填充颜色
     */
    @JsonProperty(value = "fill")
    private String fill;
    /**
     * 我也不知道这是啥
     */
    @JsonProperty(value = "stroke")
    private String stroke;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Rect() {
        type = MarkType.RECT;
    }

    @Override
    public String toString() {
        return x + " " + y + " " + height + " " + fill;
    }
}
