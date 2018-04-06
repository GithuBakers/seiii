package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Counter {
    private static final String BARE_COUNT_PATH = "counter" + File.separator + "BareCounter.txt";

    /**
     * 获得BareCounter的值
     * @return BareCounter的值
     */
    public static String getBareCounter(){
        String counter = readCounter(BARE_COUNT_PATH);
        counter = incID(counter);
        writeCount(counter,BARE_COUNT_PATH);
        return counter;
    }

    /**
     * 读取计数值
     *
     * @return counter的值
     */
    private static String readCounter(String path){
        FileCreator.createFile(path);
        String count = null;
        File file = new File(path);
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] content = new byte[((int) file.length())];
            inputStream.read(content);
            count = new String(content).trim();
            //如果是新文件，把计数器初始化为0
            if ("".equals(count)) {
                count = "0";
            }
        } catch (IOException e) {
            Log.log("读计数器异常", LogPriority.ERROR);
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 将计数值写入文件
     *  @param count 计数值
     *
     */
    private static void writeCount(String count,String path) {
        FileCreator.createFile(path);
        try (FileOutputStream os = new FileOutputStream(new File(path))) {
            os.write(count.getBytes());
            os.flush();
        } catch (IOException e) {
            Log.log("写计数器异常", LogPriority.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * 增加id的值
     *
     * @param count 计数值
     * @return 新的计数值
     */
    private static String incID(String count) {
        return (Integer.parseInt(count) + 1) + "";
    }
}
