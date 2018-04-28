package cn.edu.nju.tagmakers.countsnju.entity.vo.diagram;

import cn.edu.nju.tagmakers.countsnju.entity.user.Sex;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Description:
 * 对于某一任务
 * 用户的性别和年龄分布
 *
 * @author xxz
 * Created on 04/28/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SexAndAge implements Serializable {
    private static final long serialVersionUID = 71L;

    @JsonProperty("name")
    private Sex sex;
    @JsonProperty("under20")
    private double under20;
    @JsonProperty("under30")
    private double between20And30;
    @JsonProperty("under40")
    private double between30And40;
    @JsonProperty("above")
    private double above;

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public double getUnder20() {
        return under20;
    }

    public void setUnder20(double under20) {
        this.under20 = under20;
    }

    public double getBetween20And30() {
        return between20And30;
    }

    public void setBetween20And30(double between20And30) {
        this.between20And30 = between20And30;
    }

    public double getBetween30And40() {
        return between30And40;
    }

    public void setBetween30And40(double between30And40) {
        this.between30And40 = between30And40;
    }

    public double getAbove() {
        return above;
    }

    public void setAbove(double above) {
        this.above = above;
    }
}
