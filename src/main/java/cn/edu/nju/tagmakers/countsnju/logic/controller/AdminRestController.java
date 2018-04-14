package cn.edu.nju.tagmakers.countsnju.logic.controller;

import cn.edu.nju.tagmakers.countsnju.data.controller.InitiatorController;
import cn.edu.nju.tagmakers.countsnju.data.controller.TaskController;
import cn.edu.nju.tagmakers.countsnju.data.controller.WorkerController;
import cn.edu.nju.tagmakers.countsnju.entity.Task;
import cn.edu.nju.tagmakers.countsnju.entity.vo.SystemInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * 管理员的rest接口
 *
 * @author xxz
 * Created on 04/14/2018
 */
@RestController
@RequestMapping("/admin")
public class AdminRestController {
    private final InitiatorController initiatorController;
    private final WorkerController workerController;
    private final TaskController taskController;

    @Autowired
    public AdminRestController(InitiatorController initiatorController, WorkerController workerController, TaskController taskController) {
        this.initiatorController = initiatorController;
        this.workerController = workerController;
        this.taskController = taskController;
    }

    @RequestMapping(value = "/sys_info", method = RequestMethod.GET)
    public SystemInfoVO getSystemInfo() {
        SystemInfoVO systemInfoVO = new SystemInfoVO();
        systemInfoVO.setFinishedNumber((int) taskController.find(null).stream()
                .filter(Task::getFinished)
                .count());
        systemInfoVO.setInitiatorNumber(initiatorController.count());
        systemInfoVO.setWorkerNumber(workerController.count());
        systemInfoVO.setTotalUserNumber(initiatorController.count() + workerController.count());
        systemInfoVO.setUnfinishedNumber((int) taskController.find(null).stream()
                .filter(task -> !task.getFinished())
                .count());
        systemInfoVO.setTotalTaskNumber(taskController.count());
        return systemInfoVO;
    }
}
