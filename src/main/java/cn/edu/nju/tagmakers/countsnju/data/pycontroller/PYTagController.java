package cn.edu.nju.tagmakers.countsnju.data.pycontroller;

import cn.edu.nju.tagmakers.countsnju.data.dao.TagDAO;
import cn.edu.nju.tagmakers.countsnju.entity.Tag;
import cn.edu.nju.tagmakers.countsnju.filter.TagFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

public class PYTagController {
    @Autowired
    TagDAO dao;

    @RequestMapping(value = "/Tag/add",method = RequestMethod.POST)
    public Tag add(@RequestBody Tag bare){
        return dao.add(bare);
    }

    @RequestMapping(value = "/Tag/find",method = RequestMethod.GET)
    public Tag find(@RequestBody String id){
        return dao.findByID(id);
    }

    @RequestMapping(value = "/Tag/delete",method = RequestMethod.DELETE)
    public boolean delete(@RequestBody String id){
        return dao.delete(id);
    }

    @RequestMapping(value = "/Tag/update",method = RequestMethod.PUT)
    public Tag update(@RequestBody Tag bare){
        return dao.update(bare);
    }

    @RequestMapping(value = "/Tag/findAll",method = RequestMethod.GET)
    public List<Tag> findAll(@RequestBody TagFilter filter){
        return dao.find(filter);
    }
}
