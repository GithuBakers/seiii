package cn.edu.nju.tagmakers.countsnju.entity.vo;

import cn.edu.nju.tagmakers.countsnju.entity.Criterion.Criterion;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * 工人要查看的标准集
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class WorkerCriterionVO {

    @JsonUnwrapped
    private CriterionVOBasicInformation criterionVOBasicInformation;

    @JsonProperty(value = "is_qualified")
    private boolean passed;

    public WorkerCriterionVO(Criterion criterion, boolean passed) {
        criterionVOBasicInformation = new CriterionVOBasicInformation(criterion);
        this.passed = passed;
    }
}
