package com.vinctor.vchartviews.line;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import com.vinctor.vchartviews.AutoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinctor on 2017/4/10.
 */

public class LineChart extends AutoView {

    private int width;
    private int height;
    private float availableTop;
    private float availableLeft;
    private float availableRight;
    private float availableBottom;
    private float availableHeight;
    private float availableWidth;

    private Paint coordinatePaint = new Paint();
    private Paint linePaint = new Paint();
    private Paint titlePaint = new Paint();
    private Paint circlePaint = new Paint();

    private int coorinateColor = 0xff888888;
    private int titleCorlor = 0xff555555;

    private int coordinateTextSize = 30;
    private float coordinateStrokeWidth = 1;
    private int titleTextSize = 35;
    private float lineStrokeWidth = 8;
    float innerCircleRadius = lineStrokeWidth;

    private float leftMargin;

    private int min = 0;
    private int max = 100;
    private int density = 10;

    private String titles[] = new String[]{"考试1", "考试2", "考试3", "考试4", "考试5"};
    List<LineData> list = new ArrayList<>();

    public LineChart setLineSmoothness(float smoothness) {
        BesselCalculator.setSmoothness(smoothness);
        return this;
    }

    public void resetSmoothness() {
        BesselCalculator.setSmoothness(0.6f);
    }

    public LineChart setMin(int min) {
        this.min = min;
        return this;
    }

    public LineChart setMax(int max) {
        this.max = max;
        return this;
    }

    public LineChart setMinAndMax(int min, int max) {
        this.min = min;
        this.max = max;
        return this;
    }

    public LineChart setDensity(int density) {
        this.density = density;
        return this;
    }

    public LineChart setTitles(String[] titles) {
        this.titles = titles;
        return this;
    }

    public LineChart setData(LineData data) {
        list.clear();
        list.add(data);
        return this;
    }

    public LineChart addData(LineData data) {
        list.add(data);
        return this;
    }

    public LineChart setList(List<LineData> list) {
        this.list = list;
        return this;
    }

    public LineChart clearDatas() {
        this.list.clear();
        return this;
    }

    public LineChart setCoorinateColor(int coorinateColor) {
        this.coorinateColor = coorinateColor;
        return this;
    }

    public LineChart setTitleCorlor(int titleCorlor) {
        this.titleCorlor = titleCorlor;
        return this;
    }

    public LineChart setCoordinateTextSize(int coordinateTextSize) {
        this.coordinateTextSize = getAutoHeightSize(coordinateTextSize);
        return this;
    }

    public LineChart setTitleTextSize(int titleTextSize) {
        this.titleTextSize = getAutoHeightSize(titleTextSize);
        return this;
    }

    public LineChart setLineStrokeWidth(float lineStrokeWidth) {
        this.lineStrokeWidth = getAutoWidthSize(lineStrokeWidth);
        return this;
    }

    public void commit() {
        checkMinAndMax();
        setPaint();
        invalidate();
    }

    private void checkMinAndMax() {
        if (min >= max) {
            throw new IllegalArgumentException("you cannot set max less than max!");
        }
    }

    public LineChart(Context context) {
        super(context);
        init(context);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(0xffffffff);

        coordinatePaint.setAntiAlias(true);


        linePaint.setAntiAlias(true);


        titlePaint.setAntiAlias(true);

        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setPaint();
        float leftWidth = getLeftWidth();
        availableLeft = leftWidth;
        availableTop = 100;
        availableRight = width - titlePaint.measureText(titles[titles.length - 1]) / 2;
        availableBottom = height - titleTextSize * 2;
        availableHeight = availableBottom - availableTop;
        availableWidth = availableRight - availableLeft;

        drawCoordinate(canvas);//绘制刻度
        drawLineAndPoints(canvas);//绘制折线
    }

    /**
     * 绘制折线
     *
     * @param canvas
     */
    private void drawLineAndPoints(Canvas canvas) {
        int dataCount = list.size();
        if (dataCount <= 0) return;
        int titleCount = titles.length;
        float peerWidth = availableWidth / (titleCount - 1);
        Path path = new Path();
        List<Point> points = new ArrayList<>();
        List<CirclePoint> circlePoints = new ArrayList<>();
        for (int i = 0; i < dataCount; i++) {
            LineData data = list.get(i);
            int lineColor = data.getLineColor();
            linePaint.setColor(lineColor);
            int nums[] = data.getNums();
            int numsCount = nums.length;
            if (numsCount != titles.length)
                throw new IllegalArgumentException("the data nums's lengh must be " + titles.length + "!");
            path.reset();
            points.clear();
            circlePoints.clear();
            for (int j = 0; j < numsCount; j++) {
                float currentX = availableLeft + j * (peerWidth + coordinateStrokeWidth);
                int trueNum = nums[j];
                if (trueNum >= max) trueNum = max;
                if (trueNum <= min) trueNum = min;
                float currentY = availableBottom - (trueNum - min) * (availableBottom - availableTop) / (max - min);
                points.add(new Point(currentX, currentY));
                //外圆
                circlePoints.add(new CirclePoint(currentX, currentY));
            }
            //贝塞尔曲线
            List<Point> besselPoints = BesselCalculator.computeBesselPoints(points);
            for (int j = 0; j < besselPoints.size(); j = j + 3) {
                if (j == 0) {
                    path.moveTo(besselPoints.get(j).x, besselPoints.get(j).y);
                } else
                    path.cubicTo(besselPoints.get(j - 2).x, besselPoints.get(j - 2).y, besselPoints.get(j - 1).x, besselPoints.get(j - 1).y, besselPoints.get(j).x, besselPoints.get(j).y);
            }
            canvas.drawPath(path, linePaint);
            drawCircleRing(circlePoints, lineColor, canvas);
        }
    }

    /**
     * 绘制点
     *
     * @param list
     * @param lineColor
     * @param canvas
     */
    private void drawCircleRing(List<CirclePoint> list, int lineColor, Canvas canvas) {
        for (CirclePoint point : list) {
            circlePaint.setColor(lineColor);
            canvas.drawCircle(point.getX(), point.getY(), getCircleRadius(innerCircleRadius), circlePaint);

            circlePaint.setColor(0xffffffff);
            canvas.drawCircle(point.getX(), point.getY(), innerCircleRadius, circlePaint);
        }
    }

    private float getCircleRadius(float innerCircleRadius) {
        return lineStrokeWidth + innerCircleRadius;
    }

    /**
     * 绘制刻度,包括:网格线,数字标尺,底部title
     *
     * @param canvas
     */
    private void drawCoordinate(Canvas canvas) {
        coordinatePaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(availableLeft, availableTop, availableRight, availableBottom, coordinatePaint);

        float peerCoordinateHeight = availableHeight / density;
        //横向line
        for (int i = 0; i < density; i++) {
            float currentY = availableTop + i * peerCoordinateHeight;
            canvas.drawLine(availableLeft, currentY, availableRight, currentY, coordinatePaint);
        }

        //数字
        float totalDiff = max - min;
        float peerDiff = totalDiff / density;
        coordinatePaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i <= density; i++) {
            float graduationTextY = availableTop + peerCoordinateHeight * i;
            String currentGraduation = (int) (max - i * peerDiff) + "";
            float currentGraduationTextWidth = coordinatePaint.measureText(currentGraduation);
            canvas.drawText(currentGraduation,
                    availableLeft - currentGraduationTextWidth - leftMargin, graduationTextY + coordinateTextSize / 2,
                    coordinatePaint);
        }

        //竖向line
        int verLineNum = titles.length - 2;
        if (verLineNum > 0) {
            float peerWidth = availableWidth / (verLineNum + 1);
            for (int i = 1; i <= verLineNum; i++) {
                float currentX = availableLeft + i * peerWidth;
                canvas.drawLine(currentX, availableTop, currentX, availableBottom, coordinatePaint);
            }
        }

        //title
        int titleCount = titles.length;
        float peerWidth = availableWidth / (titleCount - 1);
        for (int i = 0; i < titleCount; i++) {
            float currentTitleWidth = titlePaint.measureText(titles[i]);
            float titleCenterX = availableLeft + i * peerWidth;
            float currentX = titleCenterX - currentTitleWidth / 2;
            float currentY = availableBottom + titleTextSize + coordinateTextSize / 2;
            canvas.drawText(titles[i], currentX, currentY, titlePaint);
        }
    }

    private void setPaint() {
        coordinatePaint.setColor(coorinateColor);
        coordinatePaint.setTextSize(coordinateTextSize);
        coordinatePaint.setStrokeWidth(coordinateStrokeWidth);

        titlePaint.setColor(titleCorlor);
        titlePaint.setTextSize(titleTextSize);

        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(lineStrokeWidth);
    }

    private float getLeftWidth() {
        float graduationTextWidth = measureGraduationTextWidth();
        leftMargin = Math.max(graduationTextWidth / 4, getCircleRadius(innerCircleRadius));
        float availableLeftmargin = graduationTextWidth + leftMargin;
        return Math.max(availableLeftmargin, titlePaint.measureText(titles[0]) / 2);
    }

    private float measureGraduationTextWidth() {
        return Math.max(coordinatePaint.measureText(max + ""), coordinatePaint.measureText(min + ""));
    }

    static class CirclePoint {
        float x;
        float y;

        public CirclePoint(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }
}
