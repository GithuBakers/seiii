package cn.edu.nju.tagmakers.countsnju.data.dao;

import cn.edu.nju.tagmakers.countsnju.entity.User;
import cn.edu.nju.tagmakers.countsnju.filter.UserFilter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserDAO extends DAO<User,UserFilter>{

    public UserDAO() {
        map = new ConcurrentHashMap<>();
        filePath = "data" + File.separator + "User.txt";
        read();
    }

    @Override
    public List<User> find(UserFilter filter) {
        return null;
    }

    @Override
    public User findByID(String id) {
        return null;
    }

    @Override
    protected void setChanges(User ori, User cur) {

    }

}
