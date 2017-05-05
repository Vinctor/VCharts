package com.vinctor.vchartviews.ring;

import java.util.List;

/**
 * Created by Vinctor on 2017/4/11.
 */

public class RingData {
    List<Data> list;
    Integer colors[];

    public RingData(List<Data> list, Integer[] colors) {
        this.list = list;
        this.colors = colors;
        if (list.size() != colors.length) {
            throw new IllegalArgumentException("the nums's lenth must equals the colors' lenth !");
        }
    }

    public List<Data> getList() {
        return list;
    }

    public Integer[] getColors() {
        return colors;
    }
}
