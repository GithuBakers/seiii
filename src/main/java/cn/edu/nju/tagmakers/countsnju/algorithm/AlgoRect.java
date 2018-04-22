package cn.edu.nju.tagmakers.countsnju.algorithm;

import cn.edu.nju.tagmakers.countsnju.entity.pic.Rect;

/**
 * Description:
 * 算法用的矩形
 *
 * @author xxz
 * Created on 04/22/2018
 */
public class AlgoRect {
    private Integer[] data = new Integer[4];
    private Rect rect;

    /**
     * 将一个标注转换为对角线的坐标
     */
    public AlgoRect(Rect rect) {
        data[0] = rect.getX();
        data[1] = rect.getY();
        data[2] = rect.getX() + rect.getWidth();
        data[3] = rect.getY() + rect.getHeight();
        //x,y 从小到大
        if (data[0] > data[2]) {
            int tmp = data[0];
            data[0] = data[2];
            data[2] = tmp;

        }
        if (data[1] > data[3]) {
            int tmp = data[1];
            data[1] = data[3];
            data[3] = tmp;
        }

        this.rect = rect;
    }

    public AlgoRect(Integer[] data) {
        this.data = data;
        rect = new Rect();
        wrapRect();
    }

    public AvxVector getAvxVector() {
        return new AvxVector(data);
    }

    public void setAvxVector(AvxVector vector) {
        data = vector.getData();
    }

    public Rect getRect() {
        wrapRect();
        return rect;
    }

    private void wrapRect() {
        rect.setX(data[0]);
        rect.setY(data[1]);
        rect.setHeight(data[3] - data[1]);
        rect.setWidth(data[2] - data[0]);
    }
}
