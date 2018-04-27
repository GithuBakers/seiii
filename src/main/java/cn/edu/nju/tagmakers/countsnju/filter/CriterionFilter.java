package cn.edu.nju.tagmakers.countsnju.filter;

/**
 * Update:
 * 增加筛选条件 发起者
 *
 * @author xxz
 * Created on 04/22/2018
 * <p>
 * Update:
 * 增加筛选条件 发起者是否已经完成
 * //TODO:DAO
 * @author xxz
 * Created on 04/27/2018
 */
public class CriterionFilter implements Filter {

    /**
     * 发起者ID
     */
    private String initiatorID;

    /**
     * 发起者是否完成
     */
    private Boolean isFinished;

    public String getInitiatorID() {
        return initiatorID;
    }

    public void setInitiatorID(String initiatorID) {
        this.initiatorID = initiatorID;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }
}
