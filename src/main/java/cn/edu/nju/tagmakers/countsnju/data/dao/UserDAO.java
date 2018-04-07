package cn.edu.nju.tagmakers.countsnju.data.dao;

import cn.edu.nju.tagmakers.countsnju.entity.user.Role;
import cn.edu.nju.tagmakers.countsnju.entity.user.User;
import cn.edu.nju.tagmakers.countsnju.filter.UserFilter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Deprecated
@Component
public class UserDAO extends DAO<User,UserFilter>{

    public UserDAO() {
        map = new ConcurrentHashMap<>();
        filePath = "data" + File.separator + "User.txt";
        read();
    }

    @Override
    public List<User> find(UserFilter filter) {
        List<User> userList;
        if(filter == null){
            userList = new ArrayList<>(map.values());
            return userList;
        }

        return null;
    }

    @Override
    protected void setChanges(User ori, User cur) {
        if(cur.getAvatar()!=null)ori.setAvatar(cur.getAvatar());
        if(cur.getNickName()!=null)ori.setNickName(cur.getNickName());
        if(cur.getPassword()!=null)ori.setPassword(cur.getPassword());
        if(cur.getRole()!=Role.DEFAULT)ori.setRole(cur.getRole());
    }

}
