package cn.edu.nju.tagmakers.countsnju.entity.user;

import org.springframework.security.core.GrantedAuthority;

/**
 * Description:
 *
 * @author xxz
 * Created on 04/07/2018
 */
public class RoleAdmin implements GrantedAuthority {

    @Override
    public String getAuthority() {
        return "ROLE_ADMIN";
    }
}
