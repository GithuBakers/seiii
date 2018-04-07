package cn.edu.nju.tagmakers.countsnju.data.pycontroller;

import cn.edu.nju.tagmakers.countsnju.data.dao.TaskDAO;
import cn.edu.nju.tagmakers.countsnju.entity.user.Task;
import cn.edu.nju.tagmakers.countsnju.filter.TaskFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

public class PYTaskController {
    @Autowired
    TaskDAO dao;

    @RequestMapping(value = "/Task/add",method = RequestMethod.POST)
    public Task add(@RequestBody Task bare){
        return dao.add(bare);
    }

    @RequestMapping(value = "/Task/find",method = RequestMethod.GET)
    public Task find(@RequestBody String id){
        return dao.findByID(id);
    }

    @RequestMapping(value = "/Task/delete",method = RequestMethod.DELETE)
    public boolean delete(@RequestBody String id){
        return dao.delete(id);
    }

    @RequestMapping(value = "/Task/update",method = RequestMethod.PUT)
    public Task update(@RequestBody Task bare){
        return dao.update(bare);
    }

    @RequestMapping(value = "/Task/findAll",method = RequestMethod.GET)
    public List<Task> findAll(@RequestBody TaskFilter filter){
        return dao.find(filter);
    }
}
