package cn.edu.nju.tagmakers.countsnju.entity;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {
    @Override
    public String getAuthority() {
        return null;
    }
}
