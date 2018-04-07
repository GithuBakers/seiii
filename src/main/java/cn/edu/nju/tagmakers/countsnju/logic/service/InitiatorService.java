package cn.edu.nju.tagmakers.countsnju.logic.service;

import cn.edu.nju.tagmakers.countsnju.entity.Task;
import cn.edu.nju.tagmakers.countsnju.entity.vo.InitiatorTaskVO;
import cn.edu.nju.tagmakers.countsnju.exception.PermissionDeniedException;
import cn.edu.nju.tagmakers.countsnju.filter.TaskFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * 众包发起者的逻辑对象
 *
 * @author xxz
 * Created on 04/06/2018
 */
@Component
public class InitiatorService {

    private TaskService taskService;

    @Autowired
    public InitiatorService(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * 添加一项新任务
     *
     * @param task 需要添加的任务
     */
    public boolean createTask(Task task) {
        taskService.addTask(task);
        return true;
    }


    /**
     * 结束一项任务
     * @param taskName 任务名
     * @param initiatorName 企图结束任务的用户名
     */
    public Task finishTask(String taskName, String initiatorName) {
        if (!isOwner(taskName, initiatorName)) {
            throw new PermissionDeniedException("这不是你的任务");
        }
        return taskService.finishTask(taskName);
    }


    /**
     * 按条件查找任务列表
     *
     * @param taskFilter 筛选条件
     * @return 符合条件的任务
     */
    public List<InitiatorTaskVO> findInitiatorTask(TaskFilter taskFilter) {

        return taskService.findTask(taskFilter).stream()
                .map(InitiatorTaskVO::new)
                .collect(Collectors.toList());
    }

    /**
     * 查看自己发起的某一任务的详情
     *
     * @param taskName      任务名
     * @param initiatorName 发起者名
     */
    public Task findTaskByName(String taskName, String initiatorName) {

        if (!isOwner(taskName, initiatorName)) {
            throw new PermissionDeniedException("这不是你创建的任务！");
        }
        return taskService.findByID(taskName);
    }

    private boolean isOwner(String taskName, String initiatorName) {
        return taskService.findByID(taskName).getInitiatorName().equals(initiatorName);
    }

}
