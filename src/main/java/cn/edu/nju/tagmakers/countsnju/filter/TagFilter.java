package cn.edu.nju.tagmakers.countsnju.filter;

/**
 * Description:
 * n/a
 *
 * @author xxz
 * Created on 03/17/2018
 * <p>
 * Update:  增加了原图ID这个筛选条件，即根据tag对应的原图来查找tag
 * @author xxz
 * Last modified on 03/21/2018
 * <p>
 * Update:
 * 增加字段 工人ID
 * //TODO:DAO
 * @author xxz
 * Created on 04/27/2018
 */
public class TagFilter implements Filter {
    private String bareID;

    /**
     * 产生这张Tag的工人的ID(如果是发起者的答案 则为null
     */
    private String workerID;

    public String getWorkerID() {
        return workerID;
    }

    public void setWorkerID(String workerID) {
        this.workerID = workerID;
    }

    public String getBareID() {
        return bareID;
    }

    public void setBareID(String bareID) {
        this.bareID = bareID;
    }


}
