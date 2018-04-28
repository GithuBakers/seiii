package cn.edu.nju.tagmakers.countsnju.data.dao;

import cn.edu.nju.tagmakers.countsnju.entity.user.Role;
import cn.edu.nju.tagmakers.countsnju.entity.user.User;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import cn.edu.nju.tagmakers.countsnju.exception.PermissionDeniedException;
import cn.edu.nju.tagmakers.countsnju.filter.SecurityUserFilter;
import cn.edu.nju.tagmakers.countsnju.security.SecurityUser;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Update:
 * 修改了密码中noop相关的部分
 *
 * @author WYM
 * Created on 04/20/2018
 */
@Component
public class SecurityUserDAO extends DAO<SecurityUser,SecurityUserFilter>{
    public SecurityUserDAO() {
        map = new ConcurrentHashMap<>();
        filePath = "data" + File.separator + "SecurityUser.txt";
        read();
        //添加管理员
        User admin = new User();
        admin.setUserID("admin");
        admin.setPassword("admin");
        admin.setRole(Role.ADMIN);
        SecurityUser admin2 = new SecurityUser(admin);
        add(admin2);

    }

    @Override
    public synchronized SecurityUser add(SecurityUser obj) {
        if (findByID(obj.getPrimeKey()) != null) {
            throw new InvalidInputException("对不起，该用户已注册");
        }
        return super.add(obj);
    }

    /**
     * 复合查找符合条件的对象
     *
     * @param filter 查询条件
     * @return 查询结果
     */
    @Override
    public List<SecurityUser> find(SecurityUserFilter filter) {
        //这个类中不需要filter
        return null;
    }

    /**
     * 将两个对象的不同的地方统一
     *
     * @param ori map中原来储存的对象
     * @param cur 更新的对象
     */
    @Override
    protected SecurityUser setChanges(SecurityUser ori, SecurityUser cur) {
        //仅仅负责更新密码的操作,默认这里的都是合法的密码
        ori = new SecurityUser(cur);
        return ori;
    }

    public boolean updatePassword(String userID,String oriPassword,String newPassword){
        SecurityUser oriSecurityUser = findByID(userID);
        String actualOriPassword = oriSecurityUser.getPassword();
        //验证输入的原有密码是否正确
        if (!actualOriPassword.equals(oriPassword)) {
            throw new PermissionDeniedException("密码和原有的密码不符，请重新输入");
        }

        SecurityUser user = new SecurityUser(userID, newPassword, oriSecurityUser.getAuthorities());
        update(user);
        return true;
    }
}
