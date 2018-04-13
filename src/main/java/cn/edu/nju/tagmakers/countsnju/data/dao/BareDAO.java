package cn.edu.nju.tagmakers.countsnju.data.dao;

import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.BareState;
import cn.edu.nju.tagmakers.countsnju.entity.pic.MarkType;
import cn.edu.nju.tagmakers.countsnju.filter.BareFilter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description:
 * 不含标注的图片的DAO
 *
 * @author wym
 * Created on 04/06/2018
 * <p>
 * Update:重构
 * @author wym
 * Last modified on 04/06/2018
 */
@Component
public class BareDAO extends DAO<Bare, BareFilter> {

    public BareDAO() {
        map = new ConcurrentHashMap<>();
        filePath = "data" + File.separator + "Bare.txt";
        read();
    }

    /**
     * 复合查找符合条件的对象
     *
     * @param filter 查询条件
     * @return 查询结果
     */
    @Override
    public List<Bare> find(BareFilter filter) {
        List<Bare> bareList;
        if (filter == null) {
            bareList = new ArrayList<>(map.values());
            return bareList;
        }
        Stream<Bare> bareStream = map.values().stream();
        bareStream = bareStream.filter(bare -> bare.getState() == filter.getBareState());
        if (filter.getMarkType() != MarkType.DEFAULT) {
            bareStream = bareStream.filter(bare -> bare.getMarkType() == filter.getMarkType());
        }
        return bareStream.collect(Collectors.toList());
    }

    /**
     * 将两个对象的不同的地方统一
     *
     * @param ori map中原来储存的对象
     * @param cur 更新的对象
     */
    @Override
    protected void setChanges(Bare ori, Bare cur) {
        //暂时定为所有的String默认值为null，更新的非默认值为更改值，不存在增量修改
        if (cur.getExThumbnail() != null) ori.setExThumbnail(cur.getExThumbnail());
        if (cur.getThumbnail() != null) ori.setThumbnail(cur.getThumbnail());
        if (cur.getId() != null) ori.setId(cur.getId());
        if (cur.getState() != BareState.UNMARKED) ori.setState(cur.getState());
        if (cur.getMarkType() != MarkType.DEFAULT) ori.setMarkType(cur.getMarkType());
        if (cur.getName() != null) ori.setName(cur.getName());
        if (cur.getRaw() != null) ori.setRaw(cur.getRaw());
    }


}
