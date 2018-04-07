package cn.edu.nju.tagmakers.countsnju.data.pycontroller;

import cn.edu.nju.tagmakers.countsnju.data.dao.BareDAO;
import cn.edu.nju.tagmakers.countsnju.entity.Bare;
import cn.edu.nju.tagmakers.countsnju.filter.BareFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/py")
public class PYBareController {
    @Autowired
    BareDAO dao;

    @RequestMapping(value = "/Bare/add",method = RequestMethod.POST)
    public Bare add(@RequestBody Bare bare){
        return dao.add(bare);
    }

    @RequestMapping(value = "/Bare/find",method = RequestMethod.GET)
    public Bare find(@RequestBody String id){
        return dao.findByID(id);
    }

    @RequestMapping(value = "/Bare/delete",method = RequestMethod.DELETE)
    public boolean delete(@RequestBody String id){
        return dao.delete(id);
    }

    @RequestMapping(value = "/Bare/update",method = RequestMethod.PUT)
    public Bare update(@RequestBody Bare bare){
        return dao.update(bare);
    }

    @RequestMapping(value = "/Bare/findAll",method = RequestMethod.GET)
    public List<Bare> findAll(@RequestBody BareFilter filter){
        return dao.find(filter);
    }
}
