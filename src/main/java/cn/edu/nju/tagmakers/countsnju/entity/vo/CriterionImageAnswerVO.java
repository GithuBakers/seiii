package cn.edu.nju.tagmakers.countsnju.entity.vo;

import cn.edu.nju.tagmakers.countsnju.entity.pic.Image;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Description:
 * 返回给工人的标准集的答案
 *
 * @author xxz
 * Created on 04/21/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CriterionImageAnswerVO {
    @JsonUnwrapped
    Image answer;

    @JsonProperty(value = "correct")
    boolean correct;

    public CriterionImageAnswerVO(Image answer, boolean correct) {
        this.answer = answer;
        this.correct = correct;
    }
}
