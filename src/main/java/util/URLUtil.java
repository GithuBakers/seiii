package util;

/**
 * Description:
 * 去除url的权限信息
 *
 * @author xxz
 * Created on 04/19/2018
 */
public class URLUtil {

    public static String processURL(String url) {
        return url.replaceAll("\\?upload.*", "");
    }
}
