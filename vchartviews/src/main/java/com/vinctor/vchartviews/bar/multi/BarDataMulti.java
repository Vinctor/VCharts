package com.vinctor.vchartviews.bar.multi;

import java.util.List;

/**
 * Created by Vinctor on 2017/4/10.
 */

public class BarDataMulti {
    List<SingleData> list;
    String title;

    public BarDataMulti(List<SingleData> list, String title) {
        this.list = list;
        this.title = title;
    }

    public List<SingleData> getList() {
        return list;
    }

    public String getTitle() {
        return title;
    }
}
