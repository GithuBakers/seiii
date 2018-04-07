package cn.edu.nju.tagmakers.countsnju.entity;
/**
 * Description:
 * 抽象的实体对象
 * @author wym
 * Created on 04/06/2018
 * <p>
 * Update:增加拷贝方法
 * @author wym
 * Last modified on 04/07/2018
 */
public abstract class Entity <T>{
    /**
     * 获取实体对象的主键
     * @return id
     */
    public abstract String getPrimeKey();

    /**
     * 为了不与clone冲突产生这个方法，调用构造器实现
     * @return 新的对象
     */
    public abstract T copy();
}
