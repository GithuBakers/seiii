package util;

import cn.edu.nju.tagmakers.countsnju.exception.FileIOException;
import com.aliyun.oss.OSSClient;

import java.io.*;
import java.util.Arrays;

/**
 * Description:
 * 向OSS写文件的Util
 *
 * @author xxz
 * Created on 04/07/2018
 */
public class OSSWriter {
    public static String upload(File filePath) {
        String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
        String accessKeyID = "LTAIg4CGHlXTTAqF";
        String accessKeySecrete = "e1JQWrRzf8iZb88xIJbNpbRzWoW8Ea";
        if (!filePath.exists()) {
            throw new FileIOException("[OSS-File-ERROR] file do not exist: " + filePath);
        }

        OSSClient ossClient = new OSSClient(endpoint, accessKeyID, accessKeySecrete);
        //打开要上传的文件
        try (InputStream inputStream = new FileInputStream(filePath)) {
            ossClient.putObject("makers", filePath.getName(), inputStream);

        } catch (FileNotFoundException e) {
            throw new FileIOException(Arrays.toString(e.getStackTrace()));
        } catch (IOException e) {
            throw new FileIOException("上传到服务器时发生IO错误");
        }


        ossClient.shutdown();
        Log.log("已上传文件:  " + filePath, LogPriority.VERBOSE);

        String PREFIX = "https://makers.oss-cn-shanghai.aliyuncs.com/";

        return PREFIX + filePath.getName();

    }
}
