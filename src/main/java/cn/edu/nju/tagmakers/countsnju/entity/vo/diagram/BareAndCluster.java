package cn.edu.nju.tagmakers.countsnju.entity.vo.diagram;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Description:
 * 单张图片用户标记数量和标记的聚集程度
 *
 * @author xxz
 * Created on 04/28/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class BareAndCluster implements Serializable {
    private static final long serialVersionUID = 71L;

    /**
     * 标注人数
     */
    @JsonProperty("x")
    private Double number;
    /**
     * 标注聚集程度
     */
    @JsonProperty("y")
    private Double kurtosis;

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public Double getKurtosis() {
        return kurtosis;
    }

    public void setKurtosis(Double kurtosis) {
        this.kurtosis = kurtosis;
    }
}
