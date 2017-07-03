package com.vinctor.vchartviews.dount;

/**
 * Created by Vinctor on 2017/6/30.
 */

public class DountData {
    private int num;
    private int dountColor = 0xFF645E;


    public DountData(int num) {
        this.num = num;
    }


    public DountData(int num, int dountColor) {
        this.num = num;
        this.dountColor = dountColor;
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

    private String tagText;

    String getTagText() {
        return tagText;
    }

    void setTagText(String tagText) {
        this.tagText = tagText;
    }
}
