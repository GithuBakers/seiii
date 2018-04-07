package cn.edu.nju.tagmakers.countsnju.logic.service;

import cn.edu.nju.tagmakers.countsnju.entity.pic.Bare;
import cn.edu.nju.tagmakers.countsnju.entity.pic.BareState;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Image;
import cn.edu.nju.tagmakers.countsnju.entity.pic.Tag;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import cn.edu.nju.tagmakers.countsnju.exception.NotFoundException;
import cn.edu.nju.tagmakers.countsnju.filter.BareFilter;
import cn.edu.nju.tagmakers.countsnju.filter.ImageFilter;
import cn.edu.nju.tagmakers.countsnju.filter.TagFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * Description:
 * 带标注图片的逻辑对象
 *
 * @author xxz
 * Created on 03/17/2018
 * <p>
 * update: 完成了增加和查找逻辑（其实包括了创建Image对象的逻辑
 * @author xxz
 * modified on 03/21/2018
 */


@Component
@Scope(
        value = SCOPE_PROTOTYPE,
        proxyMode = ScopedProxyMode.TARGET_CLASS
)
public class ImageService {


    private final TagService tagService;


    private final BareService bareService;

    @Autowired
    public ImageService(TagService tagService, BareService bareService) {
        this.tagService = tagService;
        this.bareService = bareService;
    }

    /**
     * 添加一张图片（其实这应该是添加标注，因为暂时不支持添加图片）
     */
    public Image addImage(Image image) {
        //
        if (image == null) {
            throw new InvalidInputException("操作：添加Image");
        }
        //合法性检查（包括id是否合法，是否包括tag
        Bare bare = bareService.findBareByID(image.getBare().getId());
        if (bare == null) {
            throw new NotFoundException("没有此id的图片，请检查图片ID再试一次！！");
        }
        //注意，这是检查这个字段有没有被忽略，如果是这个list为空的话是合法的，因为一张图片可能没有标注
        if (image.getTags() == null) {
            throw new InvalidInputException("没有为此图片添加标注！请检查");
        }
        //删除原有Tag
        if (bare.getState() == BareState.MARKED) {
            TagFilter filter = new TagFilter();
            filter.setBareID(bare.getId());
            List<Tag> toDelete = tagService.findTag(filter);
            for (Tag tag : toDelete) {
                tagService.deleteTag(tag.getTagID());
            }

        }
        //改变该图片状态
        bare.setState(BareState.MARKED);
        bareService.updateBare(bare);

        //添加新的Tag
        List<Tag> tag = image.getTags();
        int i = 0;
        for (Tag t : tag) {
            t.setBareID(bare.getId());
            t.setNumberID(i++ + "");
            t.setTagID(bare.getId() + "_" + t.getNumberID());
            tagService.addTag(t);
        }

        return image;
    }


    /**
     * 按ID查找图片，这个ID其实是BareID
     *
     * @param id bareID
     */
    public Image findImageByID(String id) {
        Bare bare = bareService.findBareByID(id);
        TagFilter tagFilter = new TagFilter();
        tagFilter.setBareID(id);
        List<Tag> tags = tagService.findTag(tagFilter);
        Image image = new Image();
        image.setBare(bare);
        image.setTags(tags);
        return image;
    }


    /**
     * 按条件查找图片（含标注
     *
     * @param filter 筛选条件（暂时只支持是否标注过
     * @return 查找结果
     */
    public List<Image> findImage(ImageFilter filter) {
        BareFilter bareFilter = new BareFilter();
        bareFilter.setBareState(filter.getBareState());
        bareFilter.setMarkType(filter.getMarkType());
        //查找到所有原图
        List<Bare> bareList = bareService.findBare(bareFilter);
        List<Image> imageList = new LinkedList<>();
        for (Bare bare : bareList) {
            //对于每一个原图，为它们找到自己的标记
            TagFilter tagFilter = new TagFilter();
            tagFilter.setBareID(bare.getId());
            List<Tag> tags = tagService.findTag(tagFilter);

            Image image = new Image();
            image.setBare(bare);
            image.setTags(tags);
            imageList.add(image);
        }

        return imageList;

    }


}
