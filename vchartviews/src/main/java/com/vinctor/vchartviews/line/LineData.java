package com.vinctor.vchartviews.line;

/**
 * Created by Vinctor on 2017/4/10.
 */

public class LineData {
    float nums[];
    int lineColor;
    int tagBorderColor;

    public LineData(float[] nums, int lineColor) {
        this.nums = nums;
        this.lineColor = lineColor;
        this.tagBorderColor = lineColor;
        setTagString();
    }

    private void setTagString() {
        tagString = new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == (int) nums[i]) {
                int num = (int) nums[i];
                tagString[i] = num + "";
            } else {
                tagString[i] = nums[i] + "";
            }
        }
    }

    public LineData(float[] nums, int lineColor, int tagBorderColor) {
        this.nums = nums;
        this.lineColor = lineColor;
        this.tagBorderColor = tagBorderColor;
        setTagString();
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

    public int getTagBorderColor() {
        return tagBorderColor;
    }

    private String[] tagString;

    public String[] getTagString() {
        return tagString;
    }

    public LineData setTagString(String[] tagString) {
        this.tagString = tagString;
        return this;
    }
}
