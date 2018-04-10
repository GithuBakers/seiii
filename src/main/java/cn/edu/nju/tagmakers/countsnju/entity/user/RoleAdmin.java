package cn.edu.nju.tagmakers.countsnju.entity.user;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

/**
 * Description:
 *
 * @author xxz
 * Created on 04/07/2018
 */
public class RoleAdmin implements GrantedAuthority, Serializable {
    private static final long serialVersionUID = 20L;
    @Override
    public String getAuthority() {
        return "ROLE_ADMIN";
    }
}
