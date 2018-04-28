package cn.edu.nju.tagmakers.countsnju.data.dao;

import cn.edu.nju.tagmakers.countsnju.entity.user.Worker;
import cn.edu.nju.tagmakers.countsnju.filter.WorkerFilter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * 工人类的DAO
 *
 * @author wym
 * Created on 04/06/2018
 * <p>
 * Update:
 * @author wym
 * Last modified on
 */
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
        //todo:
        List<Worker> workerList;
        if(filter == null){
            workerList = new ArrayList<>(map.values());
            return workerList;
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
    protected Worker setChanges(Worker ori, Worker cur) {
        if(cur.getAvatar() != null)ori.setAvatar(cur.getAvatar());
        if(cur.getNickName() != null)ori.setNickName(cur.getNickName());
        if(cur.getPassword() != null)ori.setPassword(cur.getPassword());
        if (cur.getBareIDs().size() > 0) ori.setBareIDs(new ArrayList<>(cur.getBareIDs()));
        if (cur.getTaskIDs().size() > 0) ori.setTaskIDs(new ArrayList<>(cur.getTaskIDs()));
        if(cur.getCredit() > 0)ori.setCredit(cur.getCredit());
        if(cur.getRank() > 0)ori.setRank(cur.getRank());
        if (cur.getDependencies().size() > 0) ori.setDependencies(cur.getDependencies());
        if (cur.getTestHistory().size() > 0) ori.setTestHistory(cur.getTestHistory());
        if (cur.getSex() != null) ori.setSex(cur.getSex());
        return ori;
    }
}
