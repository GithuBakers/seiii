package cn.edu.nju.tagmakers.countsnju.logic.service;

import cn.edu.nju.tagmakers.countsnju.entity.Task;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Tag;
import cn.edu.nju.tagmakers.countsnju.entity.user.Initiator;
import cn.edu.nju.tagmakers.countsnju.entity.user.Worker;
import cn.edu.nju.tagmakers.countsnju.entity.vo.diagram.InitiatorRecentTaskVO;
import cn.edu.nju.tagmakers.countsnju.entity.vo.diagram.TimeAndValue;
import cn.edu.nju.tagmakers.countsnju.entity.vo.diagram.WorkerRecentTaskVO;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import cn.edu.nju.tagmakers.countsnju.filter.TagFilter;
import cn.edu.nju.tagmakers.countsnju.filter.TaskFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * 用于生成各种统计图表的Service
 *
 * @author xxz
 * Created on 04/27/2018
 */
@Component
public class DiagramService {
    private TagService tagService;
    private TaskService taskService;


    @Autowired
    public DiagramService(TagService tagService, TaskService taskService) {
        this.tagService = tagService;
        this.taskService = taskService;
    }

    public List<WorkerRecentTaskVO> getWorkerRecentActivity(Worker worker) {
        if (worker == null) {
            throw new InvalidInputException("在 图表生成逻辑模块 出现 工人为null");
        }
        TagFilter tagFilter = new TagFilter();
        tagFilter.setWorkerID(worker.getPrimeKey());
        List<Tag> tags = tagService.findTag(tagFilter);
        return worker.getReceivedTime().keySet().stream()
                .map(taskService::findByID)
                .map(task -> generateWorkerRecentTask(worker, task, tags))
                .collect(Collectors.toList());
    }

    public List<InitiatorRecentTaskVO> getInitiatorRecentActivity(Initiator initiator) {
        if (initiator == null) {
            throw new InvalidInputException("在 图表生成逻辑模块 出现 发起者为null");
        }
        TaskFilter taskFilter = new TaskFilter();
        taskFilter.setFinished(false);
        taskFilter.setInitiatorName(initiator.getPrimeKey());
        List<Task> tasks = taskService.findTask(taskFilter);
        return tasks.stream()
                .map(this::generateInitiatorRecentTask)
                .collect(Collectors.toList());

    }

    private InitiatorRecentTaskVO generateInitiatorRecentTask(Task task) {
        List<Tag> tags = new LinkedList<>();
        List<String> bareIDs = task.getDataSet().stream().map(Bare::getId).collect(Collectors.toList());
        int finish = 0;
        for (String bareID : bareIDs) {
            TagFilter tagFilter = new TagFilter();
            tagFilter.setBareID(bareID);
            List<Tag> tag = tagService.findTag(tagFilter);
            tags.addAll(tag);
            if (tag.size() > task.getAim()) {
                finish++;
            }
        }
        InitiatorRecentTaskVO recentTaskVO = new InitiatorRecentTaskVO();
        recentTaskVO.setCompleteness(finish * 100 / task.getDataSet().size());
        recentTaskVO.setRecent(getTimeAndValues(tags));
        recentTaskVO.setTaskID(task.getTaskID());
        recentTaskVO.setTaskName(task.getTaskName());
        return recentTaskVO;
    }

    private WorkerRecentTaskVO generateWorkerRecentTask(Worker worker, Task task, List<Tag> allTags) {
        List<Tag> tags = allTags.stream()
                .filter(tag ->//是否是此任务的tag
                        task.getDataSet().stream().map(Bare::getId).collect(Collectors.toList())
                                .contains(tag.getBareID()))
                .collect(Collectors.toList());
        //增加随机访问性能
        tags = new ArrayList<>(tags);

        //下次就不用再算一次这些了
        allTags.removeAll(tags);
        WorkerRecentTaskVO workerRecentTaskVO = new WorkerRecentTaskVO();
        workerRecentTaskVO.setTaskID(task.getTaskID());
        workerRecentTaskVO.setTaskName(task.getTaskName());
        //工人完成情况
        int complete;
        if (task.getUserMarked() == null || task.getUserMarked().get(worker.getPrimeKey()) == null) {
            complete = 0;
        } else {
            complete = task.getUserMarked().get(worker.getPrimeKey()) * 100 / task.getLimit();
        }
        workerRecentTaskVO.setCompleteness(complete);


        if (tags.size() == 0) {
            workerRecentTaskVO.setRecent(new LinkedList<>());
        } else {
            LinkedList<TimeAndValue> list = getTimeAndValues(tags);

            workerRecentTaskVO.setRecent(list);
        }//end of tag-size else

        return workerRecentTaskVO;
    }

    private LinkedList<TimeAndValue> getTimeAndValues(List<Tag> tags) {
        //先取出对于这个任务所有的
        LinkedList<TimeAndValue> list = new LinkedList<>();
        tags.sort((tag1, tag2) -> (int) (tag1.getSubmitTime() - tag2.getSubmitTime()) % 2);
        long min = tags.get(0).getSubmitTime();
        long max = new Date().getTime();
        int index = 0;
        //一天的毫秒数
        long oneDay = 24 * 3600 * 1000;
        for (long i = min; i < max; i += oneDay) {
            TimeAndValue timeUnit = new TimeAndValue();
            timeUnit.setTime(i);
            int cnt = 0;
            for (; ; index++) {
                Long submitTime = tags.get(index).getSubmitTime();
                if (submitTime >= i && submitTime < i + oneDay) {
                    cnt++;
                } else {
                    break;
                }
            }
            timeUnit.setWorkload(cnt);
            list.add(timeUnit);
        }//end of time loop
        return list;
    }
}
