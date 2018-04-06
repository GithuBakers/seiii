package cn.edu.nju.tagmakers.countsnju.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

/**
 * Description:
 * 对图片的整体描述
 *
 * @author xxz
 * Created on 03/17/2018
 * <p>
 * update: 用枚举类型来表示mark的类型
 * @author xxz
 * modified on 03/21/2018
 */
@JsonAutoDetect(fieldVisibility = ANY)

public class Description extends Mark implements Serializable {
    private static final long serialVersionUID = 3L;
    public Description() {
        type = MarkType.DESC;
    }


}
