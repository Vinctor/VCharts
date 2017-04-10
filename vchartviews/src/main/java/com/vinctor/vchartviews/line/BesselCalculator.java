package com.vinctor.vchartviews.line;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinctor on 2017/4/10.
 */

public class BesselCalculator {

    private static float smoothness = 0.4f;

    /**
     * 计算贝塞尔结点
     */
    public static List<Point> computeBesselPoints(List<Point> points) {
        List<Point> besselPoints = new ArrayList<>();
        int count = points.size();
        besselPoints.clear();
        for (int i = 0; i < count; i++) {
            if (i == 0 || i == count - 1) {
                computeUnMonotonePoints(i, points, besselPoints);
            } else {
                Point p0 = points.get(i - 1);
                Point p1 = points.get(i);
                Point p2 = points.get(i + 1);
                if ((p1.y - p0.y) * (p1.y - p2.y) >= 0) {// 极值点
                    computeUnMonotonePoints(i, points, besselPoints);
                } else {
                    computeMonotonePoints(i, points, besselPoints);
                }
            }
        }
        return besselPoints;
    }

    /**
     * 计算非单调情况的贝塞尔结点
     */
    private static void computeUnMonotonePoints(int i, List<Point> points, List<Point> besselPoints) {
        if (i == 0) {
            Point p1 = points.get(0);
            Point p2 = points.get(1);
            besselPoints.add(p1);
            besselPoints.add(new Point(p1.x + (p2.x - p1.x) * smoothness, p1.y));
        } else if (i == points.size() - 1) {
            Point p0 = points.get(i - 1);
            Point p1 = points.get(i);
            besselPoints.add(new Point(p1.x - (p1.x - p0.x) * smoothness, p1.y));
            besselPoints.add(p1);
        } else {
            Point p0 = points.get(i - 1);
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            besselPoints.add(new Point(p1.x - (p1.x - p0.x) * smoothness, p1.y));
            besselPoints.add(p1);
            besselPoints.add(new Point(p1.x + (p2.x - p1.x) * smoothness, p1.y));
        }
    }

    /**
     * 计算单调情况的贝塞尔结点
     *
     * @param i
     * @param points
     * @param besselPoints
     */
    private static void computeMonotonePoints(int i, List<Point> points, List<Point> besselPoints) {
        Point p0 = points.get(i - 1);
        Point p1 = points.get(i);
        Point p2 = points.get(i + 1);
        float k = (p2.y - p0.y) / (p2.x - p0.x);
        float b = p1.y - k * p1.x;
        Point p01 = new Point();
        p01.x = p1.x - (p1.x - (p0.y - b) / k) * smoothness;
        p01.y = k * p01.x + b;
        besselPoints.add(p01);
        besselPoints.add(p1);
        Point p11 = new Point();
        p11.x = p1.x + (p2.x - p1.x) * smoothness;
        p11.y = k * p11.x + b;
        besselPoints.add(p11);
    }

    public static void setSmoothness(float smoothness) {
        BesselCalculator.smoothness = smoothness;
    }
}
