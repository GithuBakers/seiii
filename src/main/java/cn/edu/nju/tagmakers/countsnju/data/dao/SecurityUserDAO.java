package cn.edu.nju.tagmakers.countsnju.data.dao;

import cn.edu.nju.tagmakers.countsnju.filter.SecurityUserFilter;
import cn.edu.nju.tagmakers.countsnju.security.SecurityUser;

import java.util.List;

public class SecurityUserDAO extends DAO<SecurityUser,SecurityUserFilter>{
    /**
     * 复合查找符合条件的对象
     *
     * @param filter 查询条件
     * @return 查询结果
     */
    @Override
    public List<SecurityUser> find(SecurityUserFilter filter) {
        return null;
    }

    /**
     * 将两个对象的不同的地方统一
     *
     * @param ori map中原来储存的对象
     * @param cur 更新的对象
     */
    @Override
    protected void setChanges(SecurityUser ori, SecurityUser cur) {

    }
}
