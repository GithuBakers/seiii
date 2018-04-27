package cn.edu.nju.tagmakers.countsnju.data.dao;

import cn.edu.nju.tagmakers.countsnju.entity.pic.Tag;
import cn.edu.nju.tagmakers.countsnju.filter.TagFilter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Description:
 * 标注DAO
 *
 * @author wym
 * Created on 04/06/2018
 * <p>
 * Update:重构
 * @author wym
 * Last modified on 04/06/2018
 * <p>
 * Update:
 * 增加字段 提交时间
 * @author xxz
 * Created on 04/27/2018
 */
@Component
public class TagDAO extends DAO<Tag, TagFilter> {

    public TagDAO() {
        map = new ConcurrentHashMap<>();
        filePath = "data" + File.separator + "Tag.txt";
        read();
    }

    /**
     * 复合查找符合条件的对象
     *
     * @param filter 查询条件
     * @return 查询结果
     */
    @Override
    public List<Tag> find(TagFilter filter) {
        List<Tag> tagList = new ArrayList<>();
        if (filter == null) {
            tagList.addAll(map.values());
            return tagList;
        }
        tagList = map.values().stream()
                .filter(tag -> tag.getBareID().equals(filter.getBareID()))
                .collect(Collectors.toList());
        return tagList;
    }

    /**
     * 将原有的对象和要更新的对象统一
     *
     * @param ori 缓存中原来的对象
     * @param cur 要更新的对象
     */
    @Override
    protected Tag setChanges(Tag ori, Tag cur) {
        //暂时定为所有的String Mark默认值为null，更新的非默认值为更改值，不存在增量修改
        if (cur.getComment() != null) ori.setComment(cur.getComment());
        if (cur.getTagID() != null) ori.setTagID(cur.getTagID());
        if (cur.getMark() != null) ori.setMark(cur.getMark());
        if (cur.getBareID() != null) ori.setBareID(cur.getBareID());
        if (cur.getSubmitTime() != null) ori.setSubmitTime(cur.getSubmitTime());
        return ori;
    }

}
