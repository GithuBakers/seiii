package cn.edu.nju.tagmakers.countsnju.entity.vo.diagram;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Description:
 * 工人最近的标注都是由哪些部分组成的
 *
 * @author xxz
 * Created on 04/28/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class WorkerRecentTags {
    @JsonProperty("tags")
    private List<DiagramItem> list;

    public WorkerRecentTags(List<DiagramItem> list) {
        this.list = list;
    }

    public WorkerRecentTags() {
    }
}
