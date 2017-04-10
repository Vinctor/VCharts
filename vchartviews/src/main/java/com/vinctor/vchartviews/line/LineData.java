package com.vinctor.vchartviews.line;

/**
 * Created by Vinctor on 2017/4/10.
 */

public class LineData {
    int nums[];
    int lineColor;

    public LineData(int[] nums, int lineColor) {
        this.nums = nums;
        this.lineColor = lineColor;
    }

    public int[] getNums() {
        return nums;
    }

    public void setNums(int nums[]) {
        this.nums = nums;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }
}
