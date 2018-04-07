package cn.edu.nju.tagmakers.countsnju.entity.vo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Description:
 * 用于修改密码的VO
 *
 * @author xxz
 * Created on 04/07/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PasswordVO {

    @JsonProperty(value = "ori_password")
    private String oriPassword;

    @JsonProperty(value = "new_password")
    private String newPassword;

    public String getOriPassword() {
        return oriPassword;
    }

    public void setOriPassword(String oriPassword) {
        this.oriPassword = oriPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
