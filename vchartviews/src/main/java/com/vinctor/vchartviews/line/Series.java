
package com.vinctor.vchartviews.line;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class Series {
    /**
     * 序列点集合
     */
    private List<Point> points;
    /**
     * 贝塞尔曲线点
     */
    private List<Point> besselPoints;

    /**
     * @param points 点集合
     */
    public Series(List<Point> points) {
        this.points = points;
        this.besselPoints = new ArrayList<Point>();
    }

    public List<Point> getPoints() {
        return points;
    }

    public List<Point> getBesselPoints() {
        return besselPoints;
    }

}
