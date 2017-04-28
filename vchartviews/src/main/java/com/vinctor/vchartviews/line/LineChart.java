package com.vinctor.vchartviews.line;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;

import com.vinctor.vchartviews.AutoView;
import com.vinctor.vchartviews.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinctor on 2017/4/10.
 */

public class LineChart extends AutoView {
    private int[] animatorMinAndMax = new int[]{0, 100};
    private int duration = 3000;

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

    private float min = 0;
    private float max = 100;
    private int density = 10;

    private String titles[] = new String[]{"", "", "", "", ""};
    List<LineData> list = new ArrayList<>();
    List<RegionData> regionDatas = new ArrayList<>();
    private OnTitleClickListener onTitleClickListener;
    //animator
    boolean isShowAnimation = false;
    private boolean animationEnd = false;
    List<LineAndCircle> dataLines = new ArrayList<>();
    private ValueAnimator animatior;

    public LineChart setShowAnimation(boolean showAnimation) {
        isShowAnimation = showAnimation;
        return this;
    }

    public void setOnTitleClickListener(OnTitleClickListener onTitleClickListener) {
        this.onTitleClickListener = onTitleClickListener;
    }

    public LineChart setLineSmoothness(float smoothness) {
        BesselCalculator.setSmoothness(smoothness);
        return this;
    }

    public void resetSmoothness() {
        BesselCalculator.setSmoothness(0.3f);
    }

    public LineChart setMin(float min) {
        this.min = min;
        return this;
    }

    public LineChart setMax(float max) {
        this.max = max;
        return this;
    }

    public LineChart setMinAndMax(float min, float max) {
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
        if (isShowAnimation) {
            startAnimation();
        } else {
            showDataLine();
        }
    }


    public void startAnimation() {
        isShowAnimation = true;
        initAnimation();
        if (animatior != null) {
            animatior.cancel();
            animatior.start();
        }
    }

    private void checkMinAndMax() {
        if (min >= max) {
            throw new IllegalArgumentException("you cannot set max less than max!");
        }
    }

    public LineChart(Context context) {
        super(context);
        init(context, null);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LineChart);
            setCoordinateTextSize(ta.getDimensionPixelSize(R.styleable.LineChart_lineGradutionTextSize, coordinateTextSize));
            setCoorinateColor(ta.getColor(R.styleable.LineChart_lineGradutaionColor, coorinateColor));
            setTitleCorlor(ta.getColor(R.styleable.LineChart_lineTitleColor, titleCorlor));
            setTitleTextSize(ta.getDimensionPixelSize(R.styleable.LineChart_lineTitleTextSize, titleTextSize));
            setLineStrokeWidth(ta.getDimension(R.styleable.LineChart_lineGradutaionLineWidth, lineStrokeWidth));
            setDensity(ta.getInt(R.styleable.LineChart_linedensity, density));
            setMin(ta.getFloat(R.styleable.LineChart_lineMin, min));
            setMax(ta.getFloat(R.styleable.LineChart_lineMax, max));
        }
        setClickable(true);
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
        setAvaiable();
        computeLines();
        super.onSizeChanged(w, h, oldw, oldh);
    }


    private void computeLines() {
        if (list.size() == 0) {
            return;
        }
        int dataCount = list.size();
        if (dataCount <= 0) return;
        int titleCount = titles.length;
        float peerWidth = availableWidth / (titleCount - 1);

        List<Point> points = new ArrayList<>();
        for (int i = 0; i < dataCount; i++) {
            Path path = new Path();

            LineData data = list.get(i);
            final int lineColor = data.getLineColor();
            float nums[] = data.getNums();
            int numsCount = nums.length;
            if (numsCount != titles.length)
                throw new IllegalArgumentException("the data nums's lengh must be " + titles.length + "!");
            points.clear();
            List<CirclePoint> circlePoints = new ArrayList<>();
            circlePoints.clear();
            for (int j = 0; j < numsCount; j++) {
                float currentX = availableLeft + j * (peerWidth );
                float trueNum = nums[j];
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
            dataLines.add(new LineAndCircle(lineColor, path, circlePoints));
        }
    }

    boolean isFirst = true;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFirst) {
            isFirst = false;
            if (isShowAnimation) {
                initAnimation();
                animatior.start();
            } else {
                animatorLineAndCircleList.clear();
                animatorLineAndCircleList.addAll(dataLines);
            }
        }
        drawCoordinate(canvas);//绘制刻度
        if (isError) {
            drawError(canvas);
        } else {
            drawLineAndPoints(canvas);//绘制折线
        }
    }

    List<LineAndCircle> animatorLineAndCircleList = new ArrayList<>();

    private void initAnimation() {
        animationEnd = false;
        animatorLineAndCircleList.clear();
        if (animatior == null) {
            animatior = ValueAnimator.ofFloat(animatorMinAndMax[0], animatorMinAndMax[1]);
            animatior.setDuration(duration);
            animatior.setInterpolator(new LinearInterpolator());
            animatior.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    animationEnd = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    animationEnd = false;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    animationEnd = true;
                }
            });
        }
        animatior.removeAllUpdateListeners();
        for (LineAndCircle peer : dataLines) {
            final LineAndCircle lineAndCircle = new LineAndCircle();
            lineAndCircle.getPath().moveTo(peer.getCirclePoints().get(0).getX(), peer.getCirclePoints().get(0).getY());
            final float[] start = {0.0f};
            final PathMeasure pathMeasure = new PathMeasure(peer.getPath(), false);
            animatior.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatorValue = (float) animation.getAnimatedValue() / (animatorMinAndMax[1] - animatorMinAndMax[0]) * pathMeasure.getLength();
                    //硬件加速 你妈逼
                    pathMeasure.getSegment(start[0], animatorValue, lineAndCircle.getPath(), false);
                    start[0] = animatorValue;
                    invalidate();
                }
            });
            lineAndCircle.setLineColor(peer.getLineColor());
            lineAndCircle.setCirclePoints(peer.circlePoints);
            animatorLineAndCircleList.add(lineAndCircle);
        }
    }

    private void showDataLine() {
        animatorLineAndCircleList.clear();
        animatorLineAndCircleList.addAll(dataLines);
        invalidate();
    }

    //绘制错误
    private void drawError(Canvas canvas) {
        drawErrorText(canvas, width, height);
    }

    /**
     * 绘制折线
     *
     * @param canvas
     */
    private void drawLineAndPoints(Canvas canvas) {

        for (LineAndCircle lineAndCircle : animatorLineAndCircleList) {
            linePaint.setColor(lineAndCircle.getLineColor());
            if (!isShowAnimation) {
                canvas.drawPath(lineAndCircle.getPath(), linePaint);
                drawCircleRing(lineAndCircle.circlePoints, lineAndCircle.getLineColor(), canvas);
            } else {
                canvas.drawPath(lineAndCircle.getPath(), linePaint);
                if (animationEnd) {
                    drawCircleRing(lineAndCircle.circlePoints, lineAndCircle.getLineColor(), canvas);
                }
            }
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
        float offset = titleTextSize / 2;
        regionDatas.clear();
        int titleCount = titles.length;
        float peerWidth = availableWidth / (titleCount - 1);
        for (int i = 0; i < titleCount; i++) {
            float currentTitleWidth = titlePaint.measureText(titles[i]);
            float titleCenterX = availableLeft + i * peerWidth;
            float currentX = titleCenterX - currentTitleWidth / 2;
            float currentY = availableBottom + titleTextSize + coordinateTextSize / 2;
            canvas.drawText(titles[i], currentX, currentY, titlePaint);
            //add region

            Region region = new Region(
                    (int) (currentX - offset),
                    (int) (currentY - titleTextSize - offset),
                    (int) (currentX + currentTitleWidth + offset),
                    (int) (currentY + offset)
            );
            regionDatas.add(new RegionData(region, i, titles[i]));
        }
    }

    int downIndex = -1;
    int upIndex = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (onTitleClickListener == null) {
            return false;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downIndex = getPressIndex(x, y);
                if (downIndex < 0) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                upIndex = getPressIndex(x, y);
                if (upIndex < 0) {
                    return false;
                }
                if (upIndex == downIndex && onTitleClickListener != null) {
                    onTitleClickListener.onClick(this, regionDatas.get(downIndex).getTitle(), downIndex);
                    downIndex = -1;
                    upIndex = -1;
                    return true;
                }
                downIndex = -1;
                upIndex = -1;
                return false;
            case MotionEvent.ACTION_CANCEL:
                downIndex = -1;
                upIndex = -1;
                break;
        }
        return super.onTouchEvent(event);
    }

    private int getPressIndex(int x, int y) {
        int size = regionDatas.size();
        if (size < 1) {
            return -1;
        }
        for (int i = 0; i < size; i++) {
            Region region = regionDatas.get(i).getRegion();
            if (region.contains(x, y)) {
                return i;
            }
        }
        return -1;
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

    private void setAvaiable() {
        float leftWidth = getLeftWidth();
        availableLeft = leftWidth;
        availableTop = coordinateTextSize;
        availableRight = width - Math.max(titlePaint.measureText(titles[titles.length - 1]) / 2, getCircleRadius(innerCircleRadius) / 2);
        availableBottom = height - titleTextSize * 2;
        availableHeight = availableBottom - availableTop;
        availableWidth = availableRight - availableLeft;
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

    static class RegionData {
        Region region;
        int index;
        String title;

        public RegionData(Region region, int index, String title) {
            this.region = region;
            this.index = index;
            this.title = title;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public Region getRegion() {
            return region;
        }

        public String getTitle() {
            return title;
        }
    }

    static class LineAndCircle {

        public LineAndCircle() {

        }

        int lineColor;
        Path path = new Path();
        List<CirclePoint> circlePoints;

        public LineAndCircle(int lineColor, List<CirclePoint> circlePoints) {
            this.lineColor = lineColor;
            this.circlePoints = circlePoints;
        }

        public LineAndCircle(int lineColor, android.graphics.Path path, List<CirclePoint> circlePoints) {
            this.lineColor = lineColor;
            this.path = path;
            this.circlePoints = circlePoints;
        }

        public Path getPath() {
            return path;
        }

        public List<CirclePoint> getCirclePoints() {
            return circlePoints;
        }

        public int getLineColor() {
            return lineColor;
        }

        public LineAndCircle setLineColor(int lineColor) {
            this.lineColor = lineColor;
            return this;
        }

        public LineAndCircle setCirclePoints(List<CirclePoint> circlePoints) {
            this.circlePoints = circlePoints;
            return this;
        }

        public LineAndCircle setPath(android.graphics.Path path) {
            this.path = path;
            return this;
        }
    }

    public interface OnTitleClickListener {
        void onClick(LineChart linechart, String title, int index);
    }
}
