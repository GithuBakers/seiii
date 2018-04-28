package cn.edu.nju.tagmakers.countsnju.logic.service;

import cn.edu.nju.tagmakers.countsnju.data.controller.WorkerController;
import cn.edu.nju.tagmakers.countsnju.entity.Task;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Tag;
import cn.edu.nju.tagmakers.countsnju.entity.user.Initiator;
import cn.edu.nju.tagmakers.countsnju.entity.user.Worker;
import cn.edu.nju.tagmakers.countsnju.entity.vo.WorkerTestHistoryVO;
import cn.edu.nju.tagmakers.countsnju.entity.vo.diagram.*;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import cn.edu.nju.tagmakers.countsnju.filter.TagFilter;
import cn.edu.nju.tagmakers.countsnju.filter.TaskFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
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
    private WorkerController workerController;
    private WorkerCriterionService workerCriterionService;

    @Autowired
    public DiagramService(TagService tagService, TaskService taskService, WorkerController workerController, WorkerCriterionService workerCriterionService) {
        this.tagService = tagService;
        this.taskService = taskService;
        this.workerController = workerController;
        this.workerCriterionService = workerCriterionService;
    }

    public void getWorkerRecentActivity(Worker worker) {
        if (worker == null) {
            throw new InvalidInputException("在 图表生成逻辑模块 出现 工人为null");
        }
        TagFilter tagFilter = new TagFilter();
        tagFilter.setWorkerID(worker.getPrimeKey());
        List<Tag> tags = tagService.findTag(tagFilter);
        worker.setTasks(worker.getReceivedTime().keySet().stream()
                .map(taskService::findByID)
                .map(task -> generateWorkerRecentTask(worker, task, tags))
                .collect(Collectors.toList()));
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

    public void getWorkerCapability(Worker worker) {
        List<Worker> workers = workerController.find(null);
        workers.sort(Comparator.comparingInt(o -> o.getDependencies().size()));
        int mySize = worker.getDependencies().size();
        int cnt = 0;
        for (; ; cnt++) {
            if (mySize == workers.get(cnt).getDependencies().size()) {
                break;
            }
        }
        int criterion = 100 * cnt / workers.size();

        DiagramItem cri = new DiagramItem("完成标准集", criterion);

        int recentTask = worker.getTasks().size();
        int task;
        if (recentTask > 100) {
            task = 100;
        } else {
            task = recentTask;
        }
        DiagramItem recent = new DiagramItem("近期情况", task);
        //此功能将于之后添加
        DiagramItem acu = new DiagramItem("任务准确度", 70);

        DiagramItem taskNumber = new DiagramItem("任务完成量", worker.getTaskIDs().size());

        DiagramItem learning = new DiagramItem("错误学习能力",
                workerCriterionService.getErrorLearningAbility(worker.getPrimeKey()));

        List<DiagramItem> items = new LinkedList<>(Arrays.asList(cri, recent, acu, taskNumber, learning));
        worker.setCapability(new WorkerCapability(items));
    }

    public void getWorkerRecentTags(Worker worker) {
        //RECENT = 30天前
        long RECENT = new Date().getTime() - 30 * 3600 * 1000L;
        List<WorkerTestHistoryVO> workerTestHistory = workerCriterionService.getTestHistory(worker.getPrimeKey(), RECENT);
        DiagramItem criterionDesc = new DiagramItem("标准描述",
                (int) workerTestHistory.stream()
                        .filter(workerTestHistoryVO -> workerTestHistoryVO.getType() == MarkType.DESC)
                        .count());
        DiagramItem criterionRect = new DiagramItem("标准矩形",
                (int) workerTestHistory.stream()
                        .filter(workerTestHistoryVO -> workerTestHistoryVO.getType() == MarkType.RECT)
                        .count());
        DiagramItem criterionEdge = new DiagramItem("标准边界",
                (int) workerTestHistory.stream()
                        .filter(workerTestHistoryVO -> workerTestHistoryVO.getType() == MarkType.EDGE)
                        .count());

        TagFilter filter = new TagFilter();
        filter.setWorkerID(worker.getPrimeKey());
        List<MarkType> tags = tagService.findTag(filter).stream()
                .map(tag -> tag.getMark().getType())
                .collect(Collectors.toList());
        DiagramItem taskDesc = new DiagramItem("任务描述",
                (int) tags.stream().filter(tag -> tag == MarkType.DESC).count());
        DiagramItem taskRect = new DiagramItem("任务矩形",
                (int) tags.stream().filter(tag -> tag == MarkType.RECT).count());
        DiagramItem taskEdge = new DiagramItem("任务边界",
                (int) tags.stream().filter(tag -> tag == MarkType.EDGE).count());

        List<DiagramItem> diagramItems = new LinkedList<>();
        diagramItems.addAll(Arrays.asList(criterionDesc, criterionEdge, criterionRect, taskDesc, taskEdge, taskRect));
        worker.setRecentTags(new WorkerRecentTags(diagramItems));


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
            //从任务完成量换算成积分获取量
            list.forEach(timeAndValue ->
                    timeAndValue.setWorkload((long) (timeAndValue.getWorkload() * 0.4 * task.getReward())));
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
