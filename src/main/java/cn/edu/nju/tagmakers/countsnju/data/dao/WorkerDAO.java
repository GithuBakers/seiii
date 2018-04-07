package cn.edu.nju.tagmakers.countsnju.data.dao;

import cn.edu.nju.tagmakers.countsnju.entity.user.Role;
import cn.edu.nju.tagmakers.countsnju.entity.user.Worker;
import cn.edu.nju.tagmakers.countsnju.filter.WorkerFilter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WorkerDAO extends DAO<Worker,WorkerFilter>{
    public WorkerDAO(){
        map = new ConcurrentHashMap<>();
        filePath = "data" + File.separator + "Worker.txt";
        read();
    }

    /**
     * 复合查找符合条件的对象
     *
     * @param filter 查询条件
     * @return 查询结果
     */
    @Override
    public List<Worker> find(WorkerFilter filter) {
        List<Worker> workerList;
        if(filter == null){
            workerList = new ArrayList<>(map.values());
            return workerList;
        }
        return null;
    }

    /**
     * 将两个对象的不同的地方统一
     *
     * @param ori map中原来储存的对象
     * @param cur 更新的对象
     */
    @Override
    protected void setChanges(Worker ori, Worker cur) {
        if(cur.getAvatar()!=null)ori.setAvatar(cur.getAvatar());
        if(cur.getNickName()!=null)ori.setNickName(cur.getNickName());
        if(cur.getPassword()!=null)ori.setPassword(cur.getPassword());
        if(cur.getRole()!=Role.DEFAULT)ori.setRole(cur.getRole());
    }
}
