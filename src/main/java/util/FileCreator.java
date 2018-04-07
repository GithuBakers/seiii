package util;

import cn.edu.nju.tagmakers.countsnju.exception.FileIOException;

import java.io.File;
import java.io.IOException;

public class FileCreator {
    /**
     * 创建要读写的文件
     *
     * @param path 要读写文件的路径
     */
    public static void createFile(String path) {
        String[] str;
        if (File.separator.equals("\\")) {
            str = path.split("\\\\");
        } else {
            str = path.split(File.separator);
        }
        //str[0]开始为目标文件的父目录,str最后一个为目标文件
        String dir = str[0];

        //先判断最高一层的目录是否存在
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdir();
            Log.log("创建目录" + dir, LogPriority.VERBOSE);
        }
        //依次判断其余目录
        for (int i = 0; i < str.length - 2; i++) {
            dir += File.separator + str[i + 1];
            dirFile = new File(dir);
            if (!dirFile.exists()) {
                dirFile.mkdir();
                Log.log("创建目录" + dir, LogPriority.VERBOSE);
            }
        }
        //创建文件
        dirFile = new File(path);
        if (!dirFile.exists()) {
            Log.log("创建文件" + dir, LogPriority.VERBOSE);
            try {
                if (!dirFile.createNewFile()) {
                    throw new FileIOException("创建数据文件失败！");
                }
            } catch (IOException e) {
                throw new FileIOException("创建数据文件失败！");
            }
        }
    }
}
