package com.vinctor.vchartviews.dount;

/**
 * Created by Vinctor on 2017/6/30.
 */

public class DountData {
    private int num;
    private int dountColor = 0xFF645E;
    private String tagText = "啊啊啊啊啊测试";


    public DountData(int num) {
        this.num = num;
    }

    public String getTagText() {
        return tagText;
    }

    public int getNum() {
        return num;
    }

    public int getDountColor() {
        return dountColor;
    }

    private float angle;

    float getAngle() {
        return angle;
    }

    void setAngle(float angle) {
        this.angle = angle;
    }
}
