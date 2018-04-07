package cn.edu.nju.tagmakers.countsnju.data.pycontroller;

import cn.edu.nju.tagmakers.countsnju.data.dao.WorkerDAO;
import cn.edu.nju.tagmakers.countsnju.entity.user.Worker;
import cn.edu.nju.tagmakers.countsnju.filter.WorkerFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

public class PYWorkerController {
    @Autowired
    WorkerDAO dao;

    @RequestMapping(value = "/Worker/add",method = RequestMethod.POST)
    public Worker add(@RequestBody Worker bare){
        return dao.add(bare);
    }

    @RequestMapping(value = "/Worker/find",method = RequestMethod.GET)
    public Worker find(@RequestBody String id){
        return dao.findByID(id);
    }

    @RequestMapping(value = "/Worker/delete",method = RequestMethod.DELETE)
    public boolean delete(@RequestBody String id){
        return dao.delete(id);
    }

    @RequestMapping(value = "/Worker/update",method = RequestMethod.PUT)
    public Worker update(@RequestBody Worker bare){
        return dao.update(bare);
    }

    @RequestMapping(value = "/Worker/findAll",method = RequestMethod.GET)
    public List<Worker> findAll(@RequestBody WorkerFilter filter){
        return dao.find(filter);
    }
}
