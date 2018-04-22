package cn.edu.nju.tagmakers.countsnju.filter;

/**
 * Update:
 * 增加筛选条件 发起者
 *
 * @author xxz
 * Created on 04/22/2018
 */
public class CriterionFilter implements Filter {

    /**
     * 发起者ID
     */
    private String initiatorID;

    public String getInitiatorID() {
        return initiatorID;
    }

    public void setInitiatorID(String initiatorID) {
        this.initiatorID = initiatorID;
    }
}
