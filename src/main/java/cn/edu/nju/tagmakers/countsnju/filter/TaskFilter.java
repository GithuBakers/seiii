package cn.edu.nju.tagmakers.countsnju.filter;

import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;

/**
 * Description:
 * 众包任务的筛选器
 *
 * @author xxz
 * Created on 04/06/2018
 *
 * Update:
 * 增加"任务种类"字段
 * @author xxz
 * Created on 04/15/2018
 */
public class TaskFilter implements Filter {

    //是否已被发起者终止
    private Boolean isFinished;

    //创建人
    private String initiatorName;

    private MarkType markType;


    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public void setInitiatorName(String initiatorName) {
        this.initiatorName = initiatorName;
    }

    public MarkType getMarkType() {
        return markType;
    }

    public void setMarkType(MarkType markType) {
        this.markType = markType;
    }
}
