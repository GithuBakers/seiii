package cn.edu.nju.tagmakers.countsnju.entity.user;

import cn.edu.nju.tagmakers.countsnju.entity.Entity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Description:
 * 用户类
 * @author wym
 * Created on 04/06/2018
 * <p>
 * Update:增加获取主键方法
 * @author wym
 * Last modified on 04/06/2018
 *
 * Update:
 * ym!!!你为什么把password注释掉！！！！！
 * @author xxz
 * Created on 04/17/2018
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class User extends Entity<User> implements Serializable {
    private static final long serialVersionUID = 70L;

    @JsonProperty(value = "user_name")
    protected String userID;

    /**
     * 这里如果ignore的话无法解析前端发来的数据
     */
    //    @JsonIgnore
    @JsonProperty(value = "password")
    protected String password;

    @JsonProperty(value = "nick_name")
    protected String nickName;

    @JsonProperty(value = "avatar")
    protected String avatar;

    @JsonProperty(value = "role")
    protected Role role;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * 获取实体对象的主键
     *
     * @return id
     */
    @Override
    public String getPrimeKey() {
        return userID;
    }

    /**
     * 为了不与clone冲突产生这个方法，调用构造器实现
     *
     * @return 新的对象
     */
    @Override
    public User copy() {
        throw new UnsupportedOperationException();
    }
}
