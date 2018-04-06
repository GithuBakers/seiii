package cn.edu.nju.tagmakers.countsnju.data.dao;

import cn.edu.nju.tagmakers.countsnju.entity.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.BareState;
import cn.edu.nju.tagmakers.countsnju.entity.MarkType;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import cn.edu.nju.tagmakers.countsnju.exception.NotFoundException;
import cn.edu.nju.tagmakers.countsnju.filter.BareFilter;
import org.springframework.stereotype.Component;
import util.Log;
import util.LogPriority;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Description:
 * 不含标注的图片的DAO
 *
 * @author xxz
 * Created on 03/17/2018
 * <p>
 * Update:
 * @author xxz
 * Last modified on
 * <p>
 * Update:完成了部分的DAO
 * @author wym
 * Last modified on 03/19/2018
 * <p>
 * Update:修改了可能的隐患，加入null值检查
 * @author wym
 * Last modified on 03/19/2018
 * <p>
 * Update:完成BareDAO，修改了可能的隐患
 * Update:delete，update，add方法添加同步修饰符
 * @author wym
 * Last modified on 03/20/2018
 * <p>
 * Update:去掉了多线程
 * Update:读写序列化文件直接读写map，可以一次取出
 * @author wym
 * Last modified on 03/20/2018
 * Update:delete，修复添加和更新操作之前判断id的空指针bug
 * @author wym
 * Last modified on 03/21/2018
 * Update:测试通过BareDAO
 * @author wym
 * Last modified on 03/21/2018
 * Update:添加id自增功能
 * @author wym
 * Last modified on 03/21/2018
 * Update:添加异常处理
 * @author wym
 * Last modified on 03/21/2018
 * Update:完善了筛选功能
 * @author wym
 * Last modified on 03/22/2018
 */
@Component
public class BareDAO extends DAO<Bare, BareFilter> {
    private static final String COUNT_PATH = "counter" + File.separator + "BareCounter.txt";
    private static final String FILE_PATH = "data" + File.separator + "Bare.txt";
    private ConcurrentHashMap<String, Bare> bareMap = new ConcurrentHashMap<>();

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
    public synchronized Bare add(Bare obj) {
        //检查输入
        if (checkObjEqualsNull(obj)) {
            throw new InvalidInputException("操作：添加原图 非法输入：空对象");
        }

        //增加id操作
        String bareID = null;
        try {
            bareID = readCounter(COUNT_PATH);
        } catch (IOException e) {
            Log.log("增加原图时读取id异常", LogPriority.ERROR);
            e.printStackTrace();
        }
        obj.setId(bareID);
        writeCount(incID(bareID), COUNT_PATH);


        if (!checkExist(obj.getId())) {
            bareMap.put(obj.getId(), obj);
            writeObject(bareMap, FILE_PATH);
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
            throw new InvalidInputException("操作：删除原图 非法输入：空ID");
        }
        if (!checkExist(id)) {
            throw new NotFoundException("操作：删除原图 未能找到要删除的对象");
        }
        bareMap.remove(id);
        writeObject(bareMap, FILE_PATH);
        return true;
    }

    /**
     * 更新一个已存在的对象
     *
     * @param obj 需要更新的对象
     * @return 更新后的对象, 不存在对象的时候返回null
     */
    @Override
    public synchronized Bare update(Bare obj) {
        if (checkObjEqualsNull(obj)) {
            throw new InvalidInputException("操作：更新原图 非法输入：空对象");
        }
        if (checkStringEqualsNull(obj.getId())) {
            throw new InvalidInputException("操作：更新原图 非法输入：空ID");
        }
        if (!checkExist(obj.getId())) {
            throw new NotFoundException("操作：更新原图 未能找到要更新的对象");
        }
        Bare ori = new Bare(bareMap.get(obj.getId()));
        setChanges(ori, obj);
        bareMap.put(ori.getId(), ori);
        writeObject(bareMap, FILE_PATH);
        return ori;
    }

    /**
     * 复合查找符合条件的对象
     *
     * @param filter 查询条件
     * @return 查询结果
     */
    @Override
    public List<Bare> find(BareFilter filter) {
        List<Bare> bareList = new ArrayList<>();
        if (filter == null) {
            bareList.addAll(bareMap.values());
            return bareList;
        }
        bareList = bareMap.values().stream()
                .filter(bare -> bare.getState() == filter.getBareState())
                .filter(bare -> bare.getMarkType() == filter.getMarkType())
                .collect(Collectors.toList());
        return bareList;
    }

    /**
     * 按ID查找对象
     *
     * @param id 对象ID
     * @return 查找结果（如果不存在，返回null）
     */
    @Override
    public Bare findByID(String id) {
        //传入空ID的时候返回一个null，不抛出异常
        if (checkStringEqualsNull(id)) {
            return null;
        }
        if (bareMap.containsKey(id))
            return new Bare(bareMap.get(id));
        return null;
    }

    /**
     * 在添加对象之前检查是否存在重复添加
     *
     * @param id 对象ID
     * @return 是否存在重复添加
     */
    private boolean checkExist(String id) {
        return bareMap.containsKey(id);
    }

    /**
     * 将两个对象的不同的地方统一
     *
     * @param ori map中原来储存的对象
     * @param cur 更新的对象
     */
    private void setChanges(Bare ori, Bare cur) {
        //暂时定为所有的String默认值为null，更新的非默认值为更改值，不存在增量修改
        if (cur.getExThumbnail() != null) ori.setExThumbnail(cur.getExThumbnail());
        if (cur.getThumbnail() != null) ori.setThumbnail(cur.getThumbnail());
        if (cur.getId() != null) ori.setId(cur.getId());
        if (cur.getState() != BareState.UNMARKED) ori.setState(cur.getState());
        if (cur.getMarkType() != MarkType.DEFAULT) ori.setMarkType(cur.getMarkType());
        if (cur.getName() != null) ori.setName(cur.getName());
        if (cur.getRaw() != null) ori.setRaw(cur.getRaw());
    }

    /**
     * 在DAO第一次被初始化的时候反序列化文件内容
     */
    private void read() {
        Map<String, Bare> objMap = this.readObject(FILE_PATH);
        if (objMap != null) {
            for (Bare bare : objMap.values()) {
                bareMap.put(bare.getId(), bare);
            }
        }
    }
}
