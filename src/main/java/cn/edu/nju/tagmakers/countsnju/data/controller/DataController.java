package cn.edu.nju.tagmakers.countsnju.data.controller;

import java.util.List;

/**
 * Description:
 * 数据层Controller的接口
 *
 * @author xxz
 * Created on 03/21/2018
 * <p>
 * Update:
 * @author xxz
 * Last modified on
 */
public interface DataController<T, U> {

    /**
     * 增加一个对象
     *
     * @param obj 需要增加的对象
     * @return 增加的对象
     */
    T add(T obj);

    /**
     * 更新对象的信息
     *
     * @param obj 需要更新的对象
     * @return 更新后的对象
     */
    T update(T obj);

    /**
     * 按条件查找
     *
     * @param filter 查找条件
     * @return 查找结果
     */
    List<T> find(U filter);

    /**
     * 删除某一对象（按ID）
     *
     * @param id 要删除的对象ID
     * @return 是否删除成功
     */
    boolean delete(String id);

    /**
     * 按ID查找对象
     */
    T findByID(String id);
}
