package cn.edu.nju.tagmakers.countsnju.data.dao;

import cn.edu.nju.tagmakers.countsnju.exception.NotFoundException;
import cn.edu.nju.tagmakers.countsnju.filter.Filter;
import util.Log;
import util.LogPriority;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * DAO的抽象基类
 *
 * @author xxz
 * Created on 03/17/2018
 * <p>
 * Update:将检查str，obj是否为null值的方法抽到了父类中
 * @author wym
 * Last modified on 03/20/2018
 * <p>
 * Update:修改read的实现逻辑
 * @author wym
 * Last modified on 03/20/2018
 * <p>
 * Update:增加日志功能
 * Update:修复读计数器的bug
 * @author wym
 * Last modified on 03/20/2018
 */
public abstract class DAO<T, U extends Filter> {

    /**
     * 增加一个新的对象
     *
     * @param obj 新增的
     * @return 新增的对象
     */
    public abstract T add(T obj);

    /**
     * 删除指定ID的对象
     *
     * @param id 对象ID
     * @return 是否删除成功
     */
    public abstract boolean delete(String id);

    /**
     * 更新一个已存在的对象
     *
     * @param obj 需要更新的对象
     * @return 更新后的对象
     */
    public abstract T update(T obj);

    /**
     * 复合查找符合条件的对象
     *
     * @param filter 查询条件
     * @return 查询结果
     */
    public abstract List<T> find(U filter);

    /**
     * 按ID查找对象
     *
     * @param id 对象ID
     * @return 查找结果（如果不存在，返回null）
     */
    public abstract T findByID(String id);

    /**
     * 在调用方法前确保str有效
     *
     * @param str 传入方法的str
     * @return str是否为NULL值
     */
    protected boolean checkStringEqualsNull(String str) {
        return str == null;
    }

    /**
     * 在调用方法前确保对象有效
     *
     * @param obj 传入方法的对象
     * @return obj是否为NULL值
     */
    protected boolean checkObjEqualsNull(T obj) {
        return obj == null;
    }

    /**
     * 将对象序列化后写入文件
     */
    protected void writeObject(ConcurrentHashMap<String, T> objMap, String path) {
        createFile(path);
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File(path)))) {
            os.writeObject(objMap);
        } catch (IOException e) {
            Log.log("写对象异常", LogPriority.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * 在DAO第一次被初始化的时候反序列化文件内容
     */
    protected Map<String, T> readObject(String path) {
        createFile(path);
        Map<String, T> objList = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(path)))) {
            objList = (ConcurrentHashMap<String, T>) ois.readObject();
        } catch (EOFException e) {
            objList = new ConcurrentHashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            Log.log("读对象异常", LogPriority.ERROR);
            e.printStackTrace();
        }
        return objList;
    }

    /**
     * 读取计数值
     *
     * @param path 文件路径
     * @return
     * @throws IOException
     */
    protected String readCounter(String path) throws IOException {
        createFile(path);
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
     *
     * @param count 计数值
     * @param path  文件路径
     */
    protected void writeCount(String count, String path) {
        createFile(path);
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
    protected String incID(String count) {
        return (Integer.parseInt(count) + 1) + "";
    }

    /**
     * 创建要读写的文件
     *
     * @param path 要读写文件的路径
     */
    private void createFile(String path) {
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
                    throw new NotFoundException("创建数据文件失败！");
                }
            } catch (IOException e) {
                throw new NotFoundException("创建数据文件失败！");
            }
        }
    }
}
