package cn.edu.nju.tagmakers.countsnju.filter;

import cn.edu.nju.tagmakers.countsnju.entity.pic.BareState;
import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;

/**
 * Description:
 *
 * @author xxz
 * Created on 03/21/2018
 * <p>
 * update: 定义了·状态·这个筛选条件
 * @author xxz
 * modified on 03/21/2018
 * <p>
 * update: 增加了 · 标记类型  · 这个筛选条件
 * @author xxz
 * modified on 03/22/2018
 */
public class ImageFilter implements Filter {
    private BareState bareState = BareState.UNMARKED;

    private MarkType markType = MarkType.DEFAULT;

    public BareState getBareState() {
        return bareState;
    }

    public void setBareState(BareState bareState) {
        this.bareState = bareState;
    }

    public MarkType getMarkType() {
        return markType;
    }

    public void setMarkType(MarkType markType) {
        this.markType = markType;
    }
}
