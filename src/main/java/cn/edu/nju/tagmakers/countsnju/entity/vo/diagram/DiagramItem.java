package cn.edu.nju.tagmakers.countsnju.entity.vo.diagram;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Description:
 * 饼图和雷达图的项
 *
 * @author xxz
 * Created on 04/28/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DiagramItem {
    //项名
    @JsonProperty("item")
    private String itemName;

    //项值
    @JsonProperty("number")
    private int number;

    public DiagramItem(String itemName, int number) {
        this.itemName = itemName;
        this.number = number;
    }

    public DiagramItem() {
    }
}
