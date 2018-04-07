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
 */
public class TagFilter implements Filter {
    private String bareID;

    public String getBareID() {
        return bareID;
    }

    public void setBareID(String bareID) {
        this.bareID = bareID;
    }

}
