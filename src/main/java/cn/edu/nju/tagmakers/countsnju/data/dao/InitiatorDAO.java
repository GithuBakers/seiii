package cn.edu.nju.tagmakers.countsnju.data.dao;

import cn.edu.nju.tagmakers.countsnju.entity.user.Initiator;
import cn.edu.nju.tagmakers.countsnju.filter.InitiatorFilter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InitiatorDAO extends DAO<Initiator,InitiatorFilter>{
    public InitiatorDAO(){
        map = new ConcurrentHashMap<>();
        filePath = "data" + File.separator + "Initiator.txt";
        read();
    }

    /**
     * 复合查找符合条件的对象
     *
     * @param filter 查询条件
     * @return 查询结果
     */
    @Override
    public List<Initiator> find(InitiatorFilter filter) {
        List<Initiator> InitiatorList;
        if(filter == null){
            InitiatorList = new ArrayList<>(map.values());
            return InitiatorList;
        }
        throw new UnsupportedOperationException("小温来写代码啦！！！！");

    }

    /**
     * 将两个对象的不同的地方统一
     *
     * @param ori map中原来储存的对象
     * @param cur 更新的对象
     */
    @Override
    protected void setChanges(Initiator ori, Initiator cur) {
        if(cur.getAvatar() != null)ori.setAvatar(cur.getAvatar());
        if(cur.getNickName() != null)ori.setNickName(cur.getNickName());
        if(cur.getPassword() != null)ori.setPassword(cur.getPassword());
//        if(cur.getRole() != Role.DEFAULT)ori.setRole(cur.getRole());
    }
}
