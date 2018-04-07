package cn.edu.nju.tagmakers.countsnju.data.dao;

import cn.edu.nju.tagmakers.countsnju.entity.MarkType;
import cn.edu.nju.tagmakers.countsnju.entity.Task;
import cn.edu.nju.tagmakers.countsnju.filter.TaskFilter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TaskDAO extends DAO<Task,TaskFilter>{
    public TaskDAO(){
        map = new ConcurrentHashMap<>();
        filePath = "data" + File.separator + "Task.txt";
        read();
    }

    /**
     * 复合查找符合条件的对象
     *
     * @param filter 查询条件
     * @return 查询结果
     */
    @Override
    public List<Task> find(TaskFilter filter) {
        List<Task> taskList;
        if(filter == null){
            taskList = new ArrayList<>(map.values());
            return taskList;
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
    protected void setChanges(Task ori, Task cur) {
        //todo:这里是否要做int类型的默认值
        if(cur.getTaskName() != null)ori.setTaskName(cur.getTaskName());
        if(cur.getInitiatorName() != null)ori.setInitiatorName(cur.getInitiatorName());
        if(cur.getCover() != null)ori.setCover(cur.getCover());
        if(cur.getType() != MarkType.DEFAULT)ori.setType(cur.getType());
        if(cur.getDataSet().size() > 0)ori.setDataSet(cur.getDataSet());
        if(cur.getAim() != 0)ori.setAim(cur.getAim());
        if(cur.getLimit() != 0)ori.setLimit(cur.getLimit());
        if(cur.getReward() != 0)ori.setReward(cur.getReward());
        if(cur.getResult() != null)ori.setResult(cur.getResult());
        if(cur.getRequirement() != null)ori.setRequirement(cur.getRequirement());
        if(cur.getFinished())ori.setFinished(true);
        if(cur.getUserMarked().size() > 0)ori.setUserMarked(cur.getUserMarked());
        if(cur.getBareMarked().size() > 0)ori.setBareMarked(cur.getBareMarked());
    }
}
