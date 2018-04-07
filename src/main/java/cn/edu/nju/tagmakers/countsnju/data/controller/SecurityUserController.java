package cn.edu.nju.tagmakers.countsnju.data.controller;

import cn.edu.nju.tagmakers.countsnju.data.dao.SecurityUserDAO;
import cn.edu.nju.tagmakers.countsnju.filter.SecurityUserFilter;
import cn.edu.nju.tagmakers.countsnju.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SecurityUserController implements DataController<SecurityUser,SecurityUserFilter>{
    private final SecurityUserDAO securityUserDAO;

    @Autowired
    public SecurityUserController(SecurityUserDAO dao){
        this.securityUserDAO = dao;
    }

    /**
     * 增加一个对象
     *
     * @param obj 需要增加的对象
     * @return 增加的对象
     */
    @Override
    public SecurityUser add(SecurityUser obj) {
        return null;
    }

    /**
     * 更新对象的信息
     *
     * @param obj 需要更新的对象
     * @return 更新后的对象
     */
    @Override
    public SecurityUser update(SecurityUser obj) {
        return null;
    }

    /**
     * 按条件查找
     *
     * @param filter 查找条件
     * @return 查找结果
     */
    @Override
    public List<SecurityUser> find(SecurityUserFilter filter) {
        return null;
    }

    /**
     * 删除某一对象（按ID）
     *
     * @param id 要删除的对象ID
     * @return 是否删除成功
     */
    @Override
    public boolean delete(String id) {
        return false;
    }

    /**
     * 按ID查找对象
     *
     * @param id
     */
    @Override
    public SecurityUser findByID(String id) {
        return null;
    }
}
