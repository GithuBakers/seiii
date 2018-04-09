package cn.edu.nju.tagmakers.countsnju.entity.user;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

/**
 * Description:
 *
 * @author xxz
 * Created on 04/07/2018
 */
public class RoleWorker implements GrantedAuthority, Serializable {
    private static final long serialVersionUID = 90L;
    @Override
    public String getAuthority() {
        return "ROLE_WORKER";
    }
}
