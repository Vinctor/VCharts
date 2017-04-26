package com.vinctor.vchartviews.line;

/**
 * Created by Vinctor on 2017/4/10.
 */

public class LineData {
    float nums[];
    int lineColor;

    public LineData(float[] nums, int lineColor) {
        this.nums = nums;
        this.lineColor = lineColor;
    }

    public float[] getNums() {
        return nums;
    }

    public void setNums(float nums[]) {
        this.nums = nums;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }
}
