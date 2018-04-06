package cn.edu.nju.tagmakers.countsnju.logic.service;

import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerTaskDetailVO;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerTaskVO;
import cn.edu.nju.tagmakers.countsnju.filter.TaskFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * 众包工人的逻辑对象
 *
 * @author xxz
 * Created on 04/06/2018
 */
@Component
public class WorkerService {

    private TaskService service;


    @Autowired
    public WorkerService(TaskService service) {
        this.service = service;
    }

    /**
     * 工人查看的任务列表
     *
     * @param taskFilter 筛选条件
     */
    public List<WorkerTaskVO> findWorkerTask(TaskFilter taskFilter) {

        return service.findTask(taskFilter).stream()
                .map(WorkerTaskVO::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取某一任务的详情
     *
     * @param taskName 任务名
     */
    public WorkerTaskDetailVO getTaskDetail(String taskName) {
        return new WorkerTaskDetailVO(service.findByID(taskName));
    }

    /**
     * 接受某一任务
     *
     * @param taskName 接受的任务名
     */
    public boolean receiveTask(String taskName) {
        //TODO:
        return false;
    }


}
