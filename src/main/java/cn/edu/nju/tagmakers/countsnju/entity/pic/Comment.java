package cn.edu.nju.tagmakers.countsnju.entity.pic;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

/**
 * Description:
 * 用户对图片的注释
 *
 * @author xxz
 * Created on 03/17/2018
 * <p>
 * Update:增加setter和getter方法
 * @author wym
 * Last modified on 03/21/2018
 */
@JsonAutoDetect(fieldVisibility = ANY)
public class Comment implements Serializable {
    private static final long serialVersionUID = 2L;
    @JsonProperty(value = "comment")
    private String cmt;

    public Comment(String cmt) {
        this.cmt = cmt;
    }

    public Comment() {
    }

    public String getCmt() {
        return cmt;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
    }
}
