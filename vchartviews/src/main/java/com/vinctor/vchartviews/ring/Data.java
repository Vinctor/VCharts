package com.vinctor.vchartviews.ring;

/**
 * Created by Vinctor on 2017/4/11.
 */

public class Data {
    public Data(int num, String title) {
        this.num = num;
        this.tag = title;
    }

    int num;
    String tag;

    public int getNum() {
        return num;
    }

    public String getTag() {
        return tag;
    }
}
