package cn.edu.nju.tagmakers.countsnju.algorithm;

import cn.edu.nju.tagmakers.countsnju.exception.AlgorithmException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * 有利于JVM 编译出Avx指令的Vector
 *
 * @author xxz
 * Created on 04/22/2018
 */
public class AvxVector {
    private int size;
    private Integer[] data;

    public AvxVector(Collection<Integer> data) {
        this.size = data.size();
        this.data = new Integer[size];
        data.toArray(this.data);
    }

    public AvxVector(Integer[] data) {
        init(data);
    }

    /**
     * 浅拷贝构造器
     */
    private AvxVector(Integer[] data, boolean avoidCopy) {
        if (avoidCopy) {
            this.size = data.length;
            this.data = data;
        } else {
            init(data);
        }
    }

    private void init(Integer[] data) {
        this.size = data.length;
        this.data = new Integer[size];
        System.arraycopy(data, 0, this.data, 0, size);
    }

    public Integer[] getData() {
        return data;
    }

    public AvxVector add(AvxVector v) {
        checkSize(v);
        Integer[] ret = new Integer[size];
        for (int i = 0; i < size; i++) {
            ret[i] = data[i] + v.data[i];
        }
        return new AvxVector(ret, true);
    }

    public AvxVector sub(AvxVector v) {
        checkSize(v);
        Integer[] ret = new Integer[size];
        for (int i = 0; i < size; i++) {
            ret[i] = data[i] - v.data[i];
        }
        return new AvxVector(ret, true);
    }

    public AvxVector mul(AvxVector v) {
        checkSize(v);
        Integer[] ret = new Integer[size];
        for (int i = 0; i < size; i++) {
            ret[i] = data[i] * v.data[i];
        }
        return new AvxVector(ret, true);
    }

    public AvxVector div(AvxVector v) {
        checkSize(v);
        Integer[] ret = new Integer[size];
        for (int i = 0; i < size; i++) {
            ret[i] = data[i] / v.data[i];
        }
        return new AvxVector(ret, true);
    }

    public AvxVector scale(double index) {
        Double[] ret = new Double[size];
        for (int i = 0; i < size; i++) {
            ret[i] = data[i] * index;
        }
        List<Integer> list = Arrays.stream(ret).parallel()
                .map(Double::intValue)
                .collect(Collectors.toList());
        return new AvxVector(list);
    }

    /**
     * 计算欧氏距离
     * sqrt(sum( (a[i] - b[i])^2 ) )
     */
    public double distance(AvxVector v) {
        Integer[] diff = new Integer[size];
        for (int i = 0; i < size; i++) {
            diff[i] = data[i] - v.data[i];
        }
        Integer[] power = new Integer[size];

        for (int i = 0; i < size; i++) {
            power[i] = diff[i] * diff[i];
        }
        int sum = 0;

        for (int i = 0; i < size; i++) {
            sum += power[i];
        }


        return Math.sqrt(sum);
    }

    public int sum() {
        return Arrays.stream(data).parallel()
                .reduce((integer, integer2) -> integer + integer2)
                .orElse(0);
    }


    private void checkSize(AvxVector v) {
        if (v.size != this.size) {
            throw new AlgorithmException();
        }
    }

}
