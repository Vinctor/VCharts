package com.vinctor.vchartviews.sector;

/**
 * Created by Vinctor on 2017/5/22.
 */

public class SectorData {

    private int num;
    private int soildColor;
    private int borderColor;
    private int descriptionTextColor;

    public SectorData(int num, int soildColor, int borserColor, int descriptionTextColor) {
        this.num = num;
        this.soildColor = soildColor;
        this.borderColor = borserColor;
        this.descriptionTextColor = descriptionTextColor;
    }

    public int getNum() {
        return num;
    }

    public int getSoildColor() {
        return soildColor;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public int getDescriptionTextColor() {
        return descriptionTextColor;
    }

    float angle;
    int percent;

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
