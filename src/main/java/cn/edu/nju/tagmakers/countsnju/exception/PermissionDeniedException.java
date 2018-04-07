package cn.edu.nju.tagmakers.countsnju.exception;

/**
 * Description:
 * 权限不足等 异常
 *
 * @author xxz
 * Created on 04/07/2018
 */
public class PermissionDeniedException extends RuntimeException {

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public PermissionDeniedException() {
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public PermissionDeniedException(String message) {
        super(message);
    }
}
