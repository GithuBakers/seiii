package cn.edu.nju.tagmakers.countsnju.data.dao;

import cn.edu.nju.tagmakers.countsnju.exception.PermissionDeniedException;
import cn.edu.nju.tagmakers.countsnju.filter.SecurityUserFilter;
import cn.edu.nju.tagmakers.countsnju.security.SecurityUser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecurityUserDAO extends DAO<SecurityUser,SecurityUserFilter>{
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
    protected void setChanges(SecurityUser ori, SecurityUser cur) {
        //仅仅负责更新密码的操作,默认这里的都是合法的密码
        ori.setSecurityPassword(cur.getSecurityPassword());
    }

    public boolean updatePassword(String userID,String oriPassword,String newPassword){
        String actualOriPassword = findByID(userID).getPassword();
        //验证输入的原有密码是否正确
        if(!actualOriPassword.equals(oriPassword)){
            throw new PermissionDeniedException("密码和原有的密码不符，请重新输入");
        }
        SecurityUser user = new SecurityUser();
        user.setSecurityUserName(userID);
        user.setSecurityPassword(newPassword);
        update(user);
        return true;
    }
}
