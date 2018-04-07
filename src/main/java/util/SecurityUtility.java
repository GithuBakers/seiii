package util;

import cn.edu.nju.tagmakers.countsnju.security.SecurityUser;
import org.springframework.security.core.context.SecurityContext;

/**
 * Description:
 * 用于查询用户身份
 *
 * @author xxz
 * Created on 04/07/2018
 */
public class SecurityUtility {
    /**
     * 获取用户名
     *
     * @param context 安全上下文
     */
    public static String getUserName(SecurityContext context) {
        return ((SecurityUser) context.getAuthentication().getDetails()).getUsername();
    }
}
