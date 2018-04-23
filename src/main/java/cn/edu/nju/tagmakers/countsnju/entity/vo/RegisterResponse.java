package cn.edu.nju.tagmakers.countsnju.entity.vo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Description:
 * 前端要求返回必须要是对象
 *
 * @author xxz
 * Created on 04/17/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RegisterResponse {
    @JsonProperty("status")
    private String status;

    public RegisterResponse(String status) {
        this.status = status;
    }
}
