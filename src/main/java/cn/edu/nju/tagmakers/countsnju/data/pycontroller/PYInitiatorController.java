package cn.edu.nju.tagmakers.countsnju.data.pycontroller;

import cn.edu.nju.tagmakers.countsnju.data.dao.InitiatorDAO;
import cn.edu.nju.tagmakers.countsnju.entity.user.Initiator;
import cn.edu.nju.tagmakers.countsnju.filter.InitiatorFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

public class PYInitiatorController {
    @Autowired
    InitiatorDAO dao;

    @RequestMapping(value = "/Initiator/add",method = RequestMethod.POST)
    public Initiator add(@RequestBody Initiator bare){
        return dao.add(bare);
    }

    @RequestMapping(value = "/Initiator/find",method = RequestMethod.GET)
    public Initiator find(@RequestBody String id){
        return dao.findByID(id);
    }

    @RequestMapping(value = "/Initiator/delete",method = RequestMethod.DELETE)
    public boolean delete(@RequestBody String id){
        return dao.delete(id);
    }

    @RequestMapping(value = "/Initiator/update",method = RequestMethod.PUT)
    public Initiator update(@RequestBody Initiator bare){
        return dao.update(bare);
    }

    @RequestMapping(value = "/Initiator/findAll",method = RequestMethod.GET)
    public List<Initiator> findAll(@RequestBody InitiatorFilter filter){
        return dao.find(filter);
    }
}
