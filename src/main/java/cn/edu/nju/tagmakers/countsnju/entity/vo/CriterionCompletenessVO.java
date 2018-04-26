package cn.edu.nju.tagmakers.countsnju.entity.vo;

import cn.edu.nju.tagmakers.countsnju.entity.Criterion.Criterion;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Description:
 * 工人获取任务详情的时候查看到的VO，包括依赖的数据集和是否完成该数据集
 *
 * @author wym
 * Created on
 * <p>
 * Update:
 * @author wym
 * Last modified on
 */

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CriterionCompletenessVO {
    @JsonUnwrapped
    private CriterionVOBasicInformation criterion;

    @JsonProperty(value = "qualified")
    private boolean qualified;

    public CriterionCompletenessVO(Criterion criterion, String workerID) {
        this.criterion = new CriterionVOBasicInformation(criterion);
        qualified = criterion.getWorkerPassed().contains(workerID);
    }
}
