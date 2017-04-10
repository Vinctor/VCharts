package com.vinctor.vchartviews.line;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinctor on 2017/4/10.
 */

public class LineChart extends View {

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

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMinAndMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public void setDensity(int density) {
        this.density = density;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    public void setData(LineData data) {
        list.clear();
        list.add(data);
    }

    public void setList(List<LineData> list) {
        this.list = list;
    }

    public void setCoorinateColor(int coorinateColor) {
        this.coorinateColor = coorinateColor;
    }

    public void setTitleCorlor(int titleCorlor) {
        this.titleCorlor = titleCorlor;
    }

    public void setCoordinateTextSize(int coordinateTextSize) {
        this.coordinateTextSize = coordinateTextSize;
    }

    public void setTitleTextSize(int titleTextSize) {
        this.titleTextSize = titleTextSize;
    }

    public void setLineStrokeWidth(float lineStrokeWidth) {
        this.lineStrokeWidth = lineStrokeWidth;
    }

    public void commit() {
        postInvalidate();
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
        List<CirclePoint> points = new ArrayList<>();
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
            for (int j = 0; j < numsCount; j++) {
                float currentX = availableLeft + j * (peerWidth + coordinateStrokeWidth);
                int trueNum = nums[j];
                if (trueNum >= max) trueNum = max;
                if (trueNum <= min) trueNum = min;
                float currentY = availableBottom - (trueNum - min) * (availableBottom - availableTop) / (max - min);
                if (j == 0) path.moveTo(currentX, currentY);
                else path.lineTo(currentX, currentY);
                //小圆圈
                //外圆
                points.add(new CirclePoint(currentX, currentY));
            }
            canvas.drawPath(path, linePaint);
            drawCircleRing(points, lineColor, canvas);
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
        return coordinatePaint.measureText(max + "");
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
