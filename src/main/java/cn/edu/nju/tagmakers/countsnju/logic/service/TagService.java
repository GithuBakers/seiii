package cn.edu.nju.tagmakers.countsnju.logic.service;

import cn.edu.nju.tagmakers.countsnju.data.controller.TagController;
import cn.edu.nju.tagmakers.countsnju.entity.Tag;
import cn.edu.nju.tagmakers.countsnju.filter.TagFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * Description:
 * 标注的逻辑对象
 *
 * @author xxz
 * Created on 03/17/2018
 * <p>
 * update: 填充了类内方法
 * @author xxz
 * modified on 03/21/2018
 */

@Component
@Scope(
        value = SCOPE_PROTOTYPE,
        proxyMode = ScopedProxyMode.TARGET_CLASS
)
public class TagService {
    private final TagController tagController;

    @Autowired
    public TagService(TagController controller) {
        this.tagController = controller;
    }

    public Tag addTag(Tag tag) {
        return tagController.add(tag);
    }

    public boolean deleteTag(String id) {
        return tagController.delete(id);
    }

    public List<Tag> findTag(TagFilter filter) {
        return tagController.find(filter);
    }

}
