package com.vinctor.vchartviews.line;

/**
 * Created by Vinctor on 2017/4/10.
 */

public class Point {
    /**
     * 在canvas中的X坐标
     */
    public float x;
    /**
     * 在canvas中的Y坐标
     */
    public float y;
    /**
     * 实际的X数值
     */
    public int valueX;
    /**
     * 实际的Y数值
     */
    public int valueY;

    public Point() {
    }

    public Point(int valueX, int valueY) {
        this.valueX = valueX;
        this.valueY = valueY;
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

}
