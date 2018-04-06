package cn.edu.nju.tagmakers.countsnju.entity.vo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Description:
 * 任务图片列表的VO
 *
 * @author xxz
 * Created on 04/06/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class BareVO {

    /**
     * 原图ID
     */
    @JsonProperty(value = "id")
    private String bareID;

    /**
     * 图片名
     */
    @JsonProperty(value = "name")
    private String name;

    /**
     * 原图的URL
     */
    @JsonProperty(value = "raw")
    private String rawURL;
}
