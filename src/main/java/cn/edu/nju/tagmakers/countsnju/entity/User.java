package cn.edu.nju.tagmakers.countsnju.entity;

import cn.edu.nju.tagmakers.countsnju.entity.user.Role;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Description:
 * 用户类
 * @author wym
 * Created on 2018.4.6
 * <p>
 * Update:
 * @author wym
 * Last modified on
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class User {

    @JsonProperty(value = "user_name")
    private String userName;

    @JsonProperty(value = "password")
    private String password;

    @JsonProperty(value = "nick_name")
    private String nickName;

    private String avator;

    private Role role;
}
