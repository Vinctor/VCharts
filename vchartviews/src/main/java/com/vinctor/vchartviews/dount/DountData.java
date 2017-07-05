package com.vinctor.vchartviews.dount;

/**
 * Created by Vinctor on 2017/6/30.
 */

public class DountData {
    private int num;
    private int dountColor = 0xFF645E;
    private String tagText;


    public DountData(int num) {
        this.num = num;
    }

    public DountData(int num, String tagText) {
        this.num = num;
        this.tagText = tagText;
    }

    public DountData(int num, String tagText, int dountColor) {
        this.num = num;
        this.dountColor = dountColor;
        this.tagText = tagText;
    }

    public DountData(int num, int dountColor) {
        this.num = num;
        this.dountColor = dountColor;
    }

    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
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
