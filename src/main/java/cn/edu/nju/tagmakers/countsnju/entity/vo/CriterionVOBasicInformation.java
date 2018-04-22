package cn.edu.nju.tagmakers.countsnju.entity.vo;

import cn.edu.nju.tagmakers.countsnju.entity.Criterion;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Description:
 * 标准集的基本信息
 *
 * @author wym
 * Created on
 * <p>
 * Update:
 * @author wym
 * Last modified on
 */

public class CriterionVOBasicInformation {
    @JsonProperty(value = "criterion_id")
    private String criterionID;

    @JsonProperty(value = "criterion_name")
    private String criterionName;

    @JsonProperty(value = "cover")
    private String cover;

    @JsonProperty(value = "type")
    private MarkType type;

    @JsonProperty(value = "aim")
    private int aim;

    @JsonProperty(value = "requirement")
    private String requirement;

    @JsonProperty(value = "keywords")
    private List<String> keywords;

    /**
     *
     */
    @JsonProperty(value = "data_set")
    private List<Bare> dataSet;

    public CriterionVOBasicInformation(Criterion criterion) {
        criterionID = criterion.getCriterionID();
        criterionName = criterion.getCriterionName();
        cover = criterion.getCover();
        type = criterion.getType();
        aim = criterion.getAim();
        requirement = criterion.getRequirement();
        keywords = criterion.getKeywords();
        dataSet = criterion.getDataSet();
    }
}
