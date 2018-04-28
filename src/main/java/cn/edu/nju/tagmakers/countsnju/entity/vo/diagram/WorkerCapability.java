package cn.edu.nju.tagmakers.countsnju.entity.vo.diagram;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Description:
 * 工人的能力
 *
 * @author xxz
 * Created on 04/28/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class WorkerCapability {
    @JsonProperty("capability")
    List<DiagramItem> capability;

    public WorkerCapability(List<DiagramItem> capability) {
        this.capability = capability;
    }
}
