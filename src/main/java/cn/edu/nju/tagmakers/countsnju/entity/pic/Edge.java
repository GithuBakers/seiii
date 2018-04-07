package cn.edu.nju.tagmakers.countsnju.entity.pic;

import cn.edu.nju.tagmakers.countsnju.entity.Mark;
import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

/**
 * Description:
 * 边界标记
 *
 * @author xxz
 * Created on 03/17/2018
 * <p>
 * update: 用枚举类型表示mark类型
 * @author xxz
 * modified on 03/21/2018
 */
@JsonAutoDetect(fieldVisibility = ANY)

public class Edge extends Mark implements Serializable {
    private static final long serialVersionUID = 4L;
    /**
     * 轨迹
     */
    @JsonProperty(value = "points")
    private List<Integer> points;
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

    public Edge() {
        type = MarkType.EDGE;

    }
}
