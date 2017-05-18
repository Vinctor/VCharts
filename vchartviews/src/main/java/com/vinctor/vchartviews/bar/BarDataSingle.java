package com.vinctor.vchartviews.bar;

/**
 * Created by Vinctor on 2017/4/9.
 */

public class BarDataSingle {
    String title;
    int num;
    int barColor;
    int barStrokeColor;

    public BarDataSingle() {
    }

    public BarDataSingle(String title, int num, int color) {
        this.title = title;
        this.num = num;
        this.barColor = color;
        this.barStrokeColor = color;
    }

    public BarDataSingle(String title, int num, int barColor, int barStrokeColor) {
        this.title = title;
        this.num = num;
        this.barColor = barColor;
        this.barStrokeColor = barStrokeColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getBarColor() {
        return barColor;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }

    public int getBarStrokeColor() {
        return barStrokeColor;
    }
}
