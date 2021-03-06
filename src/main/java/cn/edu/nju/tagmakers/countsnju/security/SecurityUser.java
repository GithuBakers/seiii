package cn.edu.nju.tagmakers.countsnju.security;

import cn.edu.nju.tagmakers.countsnju.entity.Entity;
import cn.edu.nju.tagmakers.countsnju.entity.user.RoleAdmin;
import cn.edu.nju.tagmakers.countsnju.entity.user.RoleInitiator;
import cn.edu.nju.tagmakers.countsnju.entity.user.RoleWorker;
import cn.edu.nju.tagmakers.countsnju.entity.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Update:
 * 创建类
 *
 * @author WYM
 * Created on 04/07/2018
 *
 * Update:
 * 去掉登录时加上的{noop}
 * @author xxz
 * Created on 04/17/2018
 */

public class SecurityUser extends Entity<SecurityUser> implements UserDetails {
    @JsonProperty(value = "user_name")
    private String securityUserName;
    @JsonProperty(value = "password")
    private String securityPassword;

    public SecurityUser(SecurityUser user){
        this.securityUserName = user.securityUserName;
        this.securityPassword = user.securityPassword;
        this.authorities = new ArrayList<>(user.authorities);
    }

    //用于add方法新建一个SecurityUser
    public SecurityUser(User user) {
        authorities = new ArrayList<>();
        securityPassword = user.getPassword();
        securityUserName = user.getUserID();

        switch (user.getRole()) {
            case ADMIN:
                authorities.add(new RoleAdmin());
                break;
            case WORKER:
                authorities.add(new RoleWorker());
                break;
            case INITIATOR:
                authorities.add(new RoleInitiator());
                break;
        }
    }

    public SecurityUser() {
    }

    /**********************************

     构造器

     *********************************/



    //下面两个用于更新秘密吗的方法
    public SecurityUser(String securityUserName, String securityPassword, Collection<? extends GrantedAuthority> authorities) {
        this.securityUserName = securityUserName;
        this.securityPassword = securityPassword;
        this.authorities = new ArrayList<>(authorities);
    }
    private List<GrantedAuthority> authorities;

    /**
     * 后端鉴权时用
     *
     * @param securityPassword
     */
    public void setSecurityPassword(String securityPassword) {
        this.securityPassword = securityPassword;
    }

    /**
     * 获取实体对象的主键
     *
     * @return id
     */
    @Override
    public String getPrimeKey() {
        return securityUserName;
    }

    /**
     * 为了不与clone冲突产生这个方法，调用构造器实现
     *
     * @return 新的对象
     */
    @Override
    public SecurityUser copy() {
        return new SecurityUser(this);
    }

    /**
     * Returns the authorities granted to the user. Cannot return <code>null</code>.
     *
     * @return the authorities, sorted by natural key (never <code>null</code>)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return the password
     */
    @Override
    public String getPassword() {
        return securityPassword;
    }

    /**
     * Returns the username used to authenticate the user. Cannot return <code>null</code>.
     *
     * @return the username (never <code>null</code>)
     */
    @Override
    public String getUsername() {
        return securityUserName;
    }

    /**
     * Indicates whether the user's account has expired. An expired account cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user's account is valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     *
     * @return <code>true</code> if the user's credentials are valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
