package cn.edu.nju.tagmakers.countsnju.data.dao;

import cn.edu.nju.tagmakers.countsnju.entity.Tag;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import cn.edu.nju.tagmakers.countsnju.exception.NotFoundException;
import cn.edu.nju.tagmakers.countsnju.filter.TagFilter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Description:
 * 标注DAO
 *
 * @author xxz
 * Created on 03/17/2018
 * <p>
 * Update:完成了TagDAO
 * Update:delete，update，add方法添加同步修饰符
 * @author wym
 * Last modified on 03/20/2018
 * Update:delete，修复添加和更新操作之前判断id的空指针bug
 * @author wym
 * Last modified on 03/21/2018
 * Update:通过TagDAO测试
 * @author wym
 * Last modified on 03/21/2018
 * Update:完善了filter
 * @author wym
 * Last modified on 03/21/2018
 * update: 修复了用"=="比较string的错误
 * @author xxz
 * modified on 03/22/2018
 */
@Component
public class TagDAO extends DAO<Tag, TagFilter> {
    private static ConcurrentHashMap<String, Tag> tagMap = new ConcurrentHashMap<>();
    private final String FILE_PATH = "data" + File.separator + "Tag.txt";

    {
        read();
    }

    /**
     * 增加一个新的对象
     *
     * @param obj 新增的
     * @return 新增的对象
     */
    @Override
    public synchronized Tag add(Tag obj) {
        //检查输入
        if (checkObjEqualsNull(obj)) {
            throw new InvalidInputException("操作：添加标注 非法输入：空对象");
        }
        if (checkStringEqualsNull(obj.getTagID())) {
            throw new InvalidInputException("操作：添加标注 非法输入：空ID");
        }
        if (!checkExist(obj.getTagID())) {
            tagMap.put(obj.getTagID(), obj);
            writeObject(tagMap, FILE_PATH);
        }
        return obj;
    }

    /**
     * 删除指定ID的对象
     *
     * @param id 对象ID
     * @return 是否删除成功
     */
    @Override
    public synchronized boolean delete(String id) {
        //检查输入
        if (checkStringEqualsNull(id)) {
            throw new InvalidInputException("操作：删除标注 非法输入：空ID");
        }
        if (!checkExist(id)) {
            throw new NotFoundException("操作：删除标注 未能找到要删除的对象");
        }
        tagMap.remove(id);
        writeObject(tagMap, FILE_PATH);
        return true;
    }

    /**
     * 更新一个已存在的对象
     *
     * @param obj 需要更新的对象
     * @return 更新后的对象
     */
    @Override
    public synchronized Tag update(Tag obj) {
        //检查输入
        if (checkObjEqualsNull(obj)) {
            throw new InvalidInputException("操作：更新标注 非法输入：空对象");
        }
        if (checkStringEqualsNull(obj.getTagID())) {
            throw new InvalidInputException("操作：更新标注 非法输入：空ID");
        }
        if (!checkExist(obj.getTagID())) {
            throw new NotFoundException("操作：更新标注 未能找到要更新的对象");
        }
        Tag ori = new Tag(obj);
        setChanges(ori, obj);
        System.out.println(ori == null);
        tagMap.put(ori.getTagID(), ori);
        writeObject(tagMap, FILE_PATH);
        return ori;
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
            tagList.addAll(tagMap.values());
            return tagList;
        }
        tagList = tagMap.values().stream()
                .filter(tag -> tag.getBareID().equals(filter.getBareID()))
                .collect(Collectors.toList());
        return tagList;
    }

    /**
     * 按ID查找对象
     *
     * @param id 对象ID
     * @return 查找结果（如果不存在，返回null）
     */
    @Override
    public Tag findByID(String id) {
        if (checkStringEqualsNull(id)) {
            return null;
        }
        if (tagMap.containsKey(id))
            return new Tag(tagMap.get(id));
        return null;
    }

    /**
     * 在添加对象之前检查是否存在重复添加
     *
     * @param id 对象ID
     * @return 是否存在重复添加
     */
    private boolean checkExist(String id) {
        return tagMap.containsKey(id);
    }

    /**
     * 将原有的对象和要更新的对象统一
     *
     * @param ori 缓存中原来的对象
     * @param cur 要更新的对象
     */
    private void setChanges(Tag ori, Tag cur) {
        //暂时定为所有的String Mark默认值为null，更新的非默认值为更改值，不存在增量修改
        if (cur.getComment() != null) ori.setComment(cur.getComment());
        if (cur.getTagID() != null) ori.setTagID(cur.getTagID());
        if (cur.getMark() != null) ori.setMark(cur.getMark());
        if (cur.getBareID() != null) ori.setBareID(cur.getBareID());
    }

    /**
     * 在DAO第一次被初始化的时候反序列化文件内容
     */
    private void read() {
        Map<String, Tag> objMap = this.readObject(FILE_PATH);
        if (objMap != null) {
            for (Tag obj : objMap.values()) {
                tagMap.put(obj.getTagID(), obj);
            }
        }
    }

}
