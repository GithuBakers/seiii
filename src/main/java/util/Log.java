package util;

import cn.edu.nju.tagmakers.countsnju.exception.FileIOException;
import org.aspectj.lang.annotation.Aspect;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Description:
 * 日志记录类
 *
 * @author xxz
 * Created on 03/22/2018
 */
@Aspect
public class Log {
    private static final File LOG_FILE = new File("log.csv");

    public static void log(String message, LogPriority priority) {
        try {
            if (!LOG_FILE.exists() && !LOG_FILE.createNewFile()) {
                throw new FileIOException("日志文件创建失败");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true));//以app的形式写文件
            writer.write(new Date() + " , " + priority.toString() + "," + "\"# " + message + " #\"" + System.lineSeparator());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
