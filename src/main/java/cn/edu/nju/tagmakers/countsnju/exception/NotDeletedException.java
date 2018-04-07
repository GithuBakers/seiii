package cn.edu.nju.tagmakers.countsnju.exception;

/**
 * Description:
 * 删除失败异常
 *
 * @author wym
 * Created on 03/21/2018
 * <p>
 * Update:
 * @author xxz
 * Last modified on
 */
public class NotDeletedException extends RuntimeException {
    /*
     * 情况1：（未来可能的情况）产生交互的对象不允许删除*/
    public NotDeletedException(String message) {
        super(message);
    }
}
