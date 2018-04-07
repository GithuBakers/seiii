package cn.edu.nju.tagmakers.countsnju.data.dao;

import cn.edu.nju.tagmakers.countsnju.entity.Entity;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import cn.edu.nju.tagmakers.countsnju.exception.NotFoundException;
import cn.edu.nju.tagmakers.countsnju.filter.Filter;
import util.FileCreator;
import util.Log;
import util.LogPriority;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * DAO的抽象基类
 * @author wym
 * Created on 04/06/2018
 * Update:重构
 * @author wym
 * Last modified on 04/06/2018
 * Update:深拷贝
 * @author wym
 * Last modified on 04/06/2018
 */
public abstract class DAO<T extends Entity, U extends Filter> {
    ConcurrentHashMap<String,T> map;
    String filePath;

    /**
     * 增加一个新的对象
     *
     * @param obj 新增的
     * @return 新增的对象
     */
    public synchronized T add(T obj){
        //检查输入
        if (checkObjEqualsNull(obj)) {
            throw new InvalidInputException("操作：添加 非法输入：空对象");
        }
        if (checkStringEqualsNull(obj.getPrimeKey())) {
            throw new InvalidInputException("操作：添加 非法输入：空ID");
        }
        T toCopy = (T) obj.copy();
        T ret = (T) obj.copy();
        map.put(obj.getPrimeKey(), toCopy);
        writeObject(map, filePath);
        return ret;
    }

    /**
     * 删除指定ID的对象
     *
     * @param id 对象ID
     * @return 是否删除成功
     */
    public synchronized boolean delete(String id){
        //输入检测
        if (checkStringEqualsNull(id)) {
            throw new InvalidInputException("操作：删除 非法输入：空ID");
        }
        if (checkObjExist(id)) {
            throw new NotFoundException("操作：删除 未能找到要删除的对象");
        }

        //实现
        map.remove(id);
        writeObject(map, filePath);
        return true;
    }

    /**
     * 更新一个已存在的对象
     *
     * @param obj 需要更新的对象
     * @return 更新后的对象, 不存在对象的时候返回null
     */
    public synchronized T update(T obj) {
        //输入检测
        if (checkObjEqualsNull(obj)) {
            throw new InvalidInputException("操作：更新 非法输入：空对象");
        }
        if (checkStringEqualsNull(obj.getPrimeKey())) {
            throw new InvalidInputException("操作：更新 非法输入：空ID");
        }
        if (checkObjExist(obj.getPrimeKey())) {
            throw new NotFoundException("操作：更新 未能找到要更新的对象");
        }

        //实现
        T toCopy = (T) map.get(obj.getPrimeKey()).copy();
        setChanges(toCopy, obj);
        map.put(toCopy.getPrimeKey(), toCopy);
        writeObject(map, filePath);
        T ret = (T) map.get(obj.getPrimeKey()).copy();
        return ret;
    }

    /**
     * 复合查找符合条件的对象
     *
     * @param filter 查询条件
     * @return 查询结果
     */
    public abstract List<T> find(U filter);

    /**
     * 按ID查找对象
     *
     * @param id 对象ID
     * @return 查找结果（如果不存在，返回null）
     */
    public T findByID(String id){
        //传入空ID的时候返回一个null，不抛出异常
        if (checkStringEqualsNull(id)) {
            return null;
        }
        if (map.containsKey(id))
            return (T) map.get(id).copy();
        return null;
    }

    /**
     * 将两个对象的不同的地方统一
     *
     * @param ori map中原来储存的对象
     * @param cur 更新的对象
     */
    protected abstract void setChanges(T ori,T cur);

    /**********************************

     检查输入

    **********************************/

    private boolean checkStringEqualsNull(String str) {
        return str == null;
    }

    private boolean checkObjEqualsNull(T obj) {
        return obj == null;
    }

    private boolean checkObjExist(String id) {
        return !map.containsKey(id);
    }

    /**********************************

     读写对象

    **********************************/

    private void writeObject(ConcurrentHashMap<String, T> objMap, String path) {
        FileCreator.createFile(path);
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File(path)))) {
            os.writeObject(objMap);
        } catch (IOException e) {
            Log.log("写对象异常", LogPriority.ERROR);
            e.printStackTrace();
        }
    }

    private Map<String, T> readObject(String path) {
        FileCreator.createFile(path);
        Map<String, T> objList = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(path)))) {
            objList = (ConcurrentHashMap<String, T>) ois.readObject();
        } catch (EOFException e) {
            objList = new ConcurrentHashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            Log.log("读对象异常", LogPriority.ERROR);
            e.printStackTrace();
        }
        return objList;
    }

    void read() {
        Map<String, T> objMap = this.readObject(filePath);
        if (objMap != null) {
            for (T obj : objMap.values()) {
                map.put(obj.getPrimeKey(), obj);
            }
        }
    }
}
