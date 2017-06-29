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
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

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
    List<TitleClickRegionData> titleRegionDatas = new ArrayList<>();
    private OnTitleClickListener onTitleClickListener;
    //animator
    boolean isShowAnimation = false;
    private boolean animationEnd = false;
    List<LineAndCircle> dataLines = new ArrayList<>();
    private ValueAnimator animatior;

    //圆圈点击
    boolean isAllowClickShowTag = true;
    int circleClickIndex[] = new int[]{-1, -1};
    private float circlrClickOffset = 0;
    float tagCornerRadius = 5;
    float tagBorderWidth = 5;

    private GestureDetectorCompat mDetector;
    private int tagpadding;
    private float tagMargin;
    //滑动
    private float peerWidth;
    private boolean isAllowScroll = false;
    private int maxColumn = 6;
    private float factRectWidth;
    private float factRight;
    private float factRectRight;

    //阴影
    int shadowColor = 0x88888888;
    int shadowIndexStart = 3;
    int shadowIndexEnd = 5;
    Paint shadowPaint = new Paint();

    public LineChart setTagBorderWidth(float tagBorderWidth) {
        this.tagBorderWidth = getAutoWidthSize(tagBorderWidth);
        return this;
    }

    public LineChart setTagCornerRadius(float tagCornerRadius) {
        this.tagCornerRadius = tagCornerRadius;
        return this;
    }

    public void setShowTagLineIndex(int index) {
        if (index >= list.size()) {
            return;
        }
        circleClickIndex = new int[]{index, -1};
        invalidate();
    }

    public LineChart setCirclrClickOffset(float circlrClickOffset) {
        this.circlrClickOffset = getAutoWidthSize(circlrClickOffset);
        return this;
    }

    public LineChart setAllowScroll(boolean allowScroll) {
        isAllowScroll = allowScroll;
        return this;
    }

    public boolean isAllowScroll() {
        return isAllowScroll;
    }

    public LineChart setMaxColumn(int maxColumn) {
        this.maxColumn = maxColumn;
        return this;
    }

    public boolean isAllowClickShowTag() {
        return isAllowClickShowTag;
    }

    public LineChart setAllowClickShowTag(boolean allowClickShowTag) {
        isAllowClickShowTag = allowClickShowTag;
        return this;
    }

    public LineChart setDuration(int duration) {
        this.duration = duration;
        if (animatior != null) {
            animatior.setDuration(duration);
        }
        return this;
    }

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
        BesselCalculator.setSmoothness(0.4f);
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
        titles = new String[]{"", "", "", "", ""};
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

    /**
     * 网格线宽度
     *
     * @param coordinateStrokeWidth
     * @return
     */
    public LineChart setCoordinateStrokeWidth(float coordinateStrokeWidth) {
        this.coordinateStrokeWidth = coordinateStrokeWidth;
        return this;
    }

    /**
     * 折线宽度
     *
     * @param lineStrokeWidth
     * @return
     */
    public LineChart setLineStrokeWidth(float lineStrokeWidth) {
        this.lineStrokeWidth = getAutoWidthSize(lineStrokeWidth);
        return this;
    }

    public void commit() {
        checkMinAndMax();
        setPaint();
        circleClickIndex = new int[]{-1, -1};
        setAvaiable();
        computeLines();
        if (titles.length == 1) {
            showDataLine();
            return;
        }
        if (isShowAnimation) {
            startAnimation();
        } else {
            showDataLine();
        }
    }

    public void startAnimation() {
        isShowAnimation = true;
        initAnimation();
        if (dataLines.size() == 0) {
            invalidate();
        }
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
            setLineStrokeWidth(ta.getDimension(R.styleable.LineChart_lineWidth, lineStrokeWidth));
            setCoordinateStrokeWidth(ta.getDimension(R.styleable.LineChart_lineGradutaionWidth, coordinateStrokeWidth));
            setDensity(ta.getInt(R.styleable.LineChart_linedensity, density));
            setMin(ta.getFloat(R.styleable.LineChart_lineMin, min));
            setMax(ta.getFloat(R.styleable.LineChart_lineMax, max));
            setShowAnimation(ta.getBoolean(R.styleable.LineChart_showAnimation, false));
            setDuration(ta.getInteger(R.styleable.LineChart_animationduration, 3000));
            setCirclrClickOffset(ta.getDimensionPixelOffset(R.styleable.LineChart_circleClickOffset, 0));
            setAllowScroll(ta.getBoolean(R.styleable.LineChart_allowScroll, isAllowScroll));
            setMaxColumn(ta.getInteger(R.styleable.LineChart_maxColnum, maxColumn));


            checkMinAndMax();
            setPaint();
            ta.recycle();
        }
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        setClickable(true);
        setBackgroundColor(0xffffffff);

        coordinatePaint.setAntiAlias(true);

        linePaint.setAntiAlias(true);

        titlePaint.setAntiAlias(true);

        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setTextSize(coordinateTextSize);


        mDetector = new GestureDetectorCompat(context, new MyGestureListener());
        //滑动
        scroller = new Scroller(context, new DecelerateInterpolator(2f));
        touchSlop = ViewConfiguration.getTouchSlop();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
        setAvaiable();
        computeLines();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void setAvaiable() {
        if (width <= 0 || height <= 0 || titles.length == 0) {
            return;
        }
        scrollTo(0, 0);
        tagpadding = coordinateTextSize;
        tagMargin = getCircleRadius(innerCircleRadius) + coordinateTextSize / 3;

        float leftWidth = getLeftWidth();
        availableLeft = leftWidth;
        availableTop = tagMargin + getCircleRadius(innerCircleRadius) + coordinateTextSize + tagpadding + 10;//10为上方空隙,可为0,getCircleRadius为drawTag中三角形高度
        float rightPadding = Math.max(titlePaint.measureText(titles[titles.length - 1]) / 2,
                Math.max(circlePaint.measureText(max + ".0") / 2 + tagpadding, circlePaint.measureText(min + ".0")) / 2 + tagpadding);
        availableRight = width - rightPadding;
        availableBottom = height - titleTextSize * 2;
        availableHeight = availableBottom - availableTop;
        availableWidth = availableRight - availableLeft;

        int titleCount = titles.length;
        if (titleCount == 1) {
            peerWidth = availableWidth / 2;
            factRectWidth = availableWidth;
            factRectRight = availableLeft + factRectWidth;
            factRight = factRectRight + rightPadding;
        } else {
            if (!isAllowScroll) {
                peerWidth = availableWidth / (titleCount - 1);
                factRectWidth = availableWidth;
                factRight = availableRight + rightPadding;
                factRectRight = availableLeft + factRectWidth;
            } else {
                int counmeCunt = maxColumn < titleCount - 1 ? maxColumn : titleCount - 1;
                peerWidth = availableWidth / counmeCunt;
                //避免title文字相互遮挡
                float maxTwoTitleLength = getMaxTwoTitleLenth();
                while (maxTwoTitleLength > peerWidth && peerWidth > 0) {
                    counmeCunt -= 1;
                    peerWidth = availableWidth / counmeCunt;
                }

                factRectWidth = peerWidth * (titleCount - 1);
                factRectRight = availableLeft + factRectWidth;
                factRight = factRectRight + rightPadding;
            }
        }
    }

    private float getMaxTwoTitleLenth() {
        int count = titles.length;
        if (count == 0) {
            return 0f;
        }
        float result = 0f;
        for (int i = 0; i < count - 1; i++) {
            float temp = titlePaint.measureText(titles[i]) / 2 + titlePaint.measureText(titles[i + 1]) / 2;
            if (temp > result) {
                result = temp;
            }
        }
        return result;
    }

    private void computeLines() {
        if (width <= 0 || height <= 0) {
            return;
        }
        dataLines.clear();
        //circle click


        int dataCount = list.size();
        if (dataCount <= 0) return;
        int titleCount = titles.length;
        List<Point> points = new ArrayList<>();
        //1列的情况
        if (titleCount == 1) {
            for (int i = 0; i < dataCount; i++) {
                LineData data = list.get(i);
                final int lineColor = data.getLineColor();
                int tagBorderColor = data.getTagBorderColor();
                float nums[] = data.getNums();
                int numsCount = nums.length;
                if (numsCount != titles.length)
                    throw new IllegalArgumentException("the data num's lengh must be " + titles.length + "!");
                List<CirclePoint> circlePoints = new ArrayList<>();
                circlePoints.clear();
                float currentX = availableLeft + peerWidth;
                float trueNum = nums[0];
                if (trueNum >= max) trueNum = max;
                if (trueNum <= min) trueNum = min;
                float currentY = availableBottom - (trueNum - min) * (availableBottom - availableTop) / (max - min);
                points.add(new Point(currentX, currentY));
                //外圆
                circlePoints.add(new CirclePoint(nums[0], currentX, currentY));
                dataLines.add(new LineAndCircle(lineColor, tagBorderColor, null, circlePoints));
            }

            return;
        }

        //>=2列的情况
        for (int i = 0; i < dataCount; i++) {
            Path path = new Path();
            LineData data = list.get(i);
            final int lineColor = data.getLineColor();
            int tagBorderColor = data.getTagBorderColor();
            float nums[] = data.getNums();
            int numsCount = nums.length;
            if (numsCount != titles.length)
                throw new IllegalArgumentException("the data num's lengh must be " + titles.length + "!");
            points.clear();
            List<CirclePoint> circlePoints = new ArrayList<>();
            circlePoints.clear();
            for (int j = 0; j < numsCount; j++) {
                float currentX = availableLeft + j * (peerWidth);
                float trueNum = nums[j];
                if (trueNum >= max) trueNum = max;
                if (trueNum <= min) trueNum = min;
                float currentY = availableBottom - (trueNum - min) * (availableBottom - availableTop) / (max - min);
                points.add(new Point(currentX, currentY));
                //外圆
                circlePoints.add(new CirclePoint(nums[j], currentX, currentY));
            }
            //贝塞尔曲线
            List<Point> besselPoints = BesselCalculator.computeBesselPoints(points);
            for (int j = 0; j < besselPoints.size(); j = j + 3) {
                if (j == 0) {
                    path.moveTo(besselPoints.get(j).x, besselPoints.get(j).y);
                } else
                    path.cubicTo(besselPoints.get(j - 2).x, besselPoints.get(j - 2).y, besselPoints.get(j - 1).x, besselPoints.get(j - 1).y, besselPoints.get(j).x, besselPoints.get(j).y);
            }
            dataLines.add(new LineAndCircle(lineColor, tagBorderColor, path, circlePoints));
        }
    }

    boolean isFirst = true;

    @Override
    protected void onDraw(Canvas canvas) {
        if (width <= 0 || height <= 0 || titles.length == 0) {
            return;
        }
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
        tagCircles.clear();
        drawCoordinate(canvas);//绘制刻度
        drawLineAndPoints(canvas);//绘制折线
        drawTag(canvas);//标签
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

    /**
     * 绘制折线
     *
     * @param canvas
     */
    private void drawLineAndPoints(Canvas canvas) {
        int lineSize = animatorLineAndCircleList.size();
        for (int i = 0; i < lineSize; i++) {
            LineAndCircle lineAndCircle = animatorLineAndCircleList.get(i);
            linePaint.setColor(lineAndCircle.getLineColor());
            if (titles.length == 1) {
                drawCircleRing(i, lineAndCircle.circlePoints, lineAndCircle.getLineColor(), lineAndCircle.getTagBorderColor(), canvas);
            } else {
                if (!isShowAnimation) {
                    canvas.drawPath(lineAndCircle.getPath(), linePaint);
                    //drawShadow(canvas, lineAndCircle);
                    drawCircleRing(i, lineAndCircle.circlePoints, lineAndCircle.getLineColor(), lineAndCircle.getTagBorderColor(), canvas);
                } else {
                    canvas.drawPath(lineAndCircle.getPath(), linePaint);
                    if (animationEnd) {
                        //drawShadow(canvas, lineAndCircle);
                        drawCircleRing(i, lineAndCircle.circlePoints, lineAndCircle.getLineColor(), lineAndCircle.getTagBorderColor(), canvas);
                    }
                }
            }
        }
    }

    //shadow阴影
    private void drawShadow(Canvas canvas, LineAndCircle lineAndCircle) {

        List<CirclePoint> circlePointts = lineAndCircle.getCirclePoints();
        int pointCount = circlePointts.size();
        if (shadowIndexEnd > pointCount - 1) {
            shadowIndexEnd = pointCount - 1;
        }
        if (shadowIndexStart > shadowIndexEnd) {
            throw new IllegalArgumentException("shadow start index must less than end index");
        }

        Path shadowPath = new Path();
        for (int i = shadowIndexStart; i <= shadowIndexEnd; i++) {
            if (i == shadowIndexStart) {
                shadowPath.moveTo(circlePointts.get(i).getX(), circlePointts.get(i).getY());
            } else shadowPath.lineTo(circlePointts.get(i).getX(), circlePointts.get(i).getY());
        }
        shadowPath.lineTo(availableLeft + peerWidth * shadowIndexEnd, availableBottom);
        shadowPath.lineTo(availableLeft + peerWidth * shadowIndexStart, availableBottom);
        shadowPath.close();
        canvas.drawPath(shadowPath, shadowPaint);
    }

    List<CirclePoint> tagCircles = new ArrayList<>();

    /**
     * 绘制点
     *
     * @param list
     * @param lineColor
     * @param canvas
     */
    private void drawCircleRing(int lineIndex, List<CirclePoint> list, int lineColor, int tagBorderColor, Canvas canvas) {

        boolean isDrawTag = lineIndex == circleClickIndex[0];

        int circlrCount = list.size();
        for (int i = 0; i < circlrCount; i++) {
            CirclePoint point = list.get(i);
            circlePaint.setColor(lineColor);
            float outRadius = getCircleRadius(innerCircleRadius);
            float innerRadius = innerCircleRadius;

            circlePaint.setAlpha(255);
            canvas.drawCircle(point.getX(), point.getY(), outRadius, circlePaint);
            if (isDrawTag) {
                tagCircles.add(new CirclePoint(lineColor, tagBorderColor, point));
            }
            if (!(circleClickIndex[1] == i && isDrawTag)) {
                circlePaint.setAlpha(255);
                circlePaint.setColor(0xffffffff);
                canvas.drawCircle(point.getX(), point.getY(), innerRadius, circlePaint);
            }
        }
    }

    //点击标签
    private void drawTag(Canvas canvas) {
        int tagCircleCount = tagCircles.size();
        if (tagCircleCount == 0) {
            return;
        }
        float sanjiaoHeight = getCircleRadius(innerCircleRadius);
        for (CirclePoint point : tagCircles) {
            float currentX = point.getX();
            float currentY = point.getY();
            String num = point.getNum() + "";
            if (num.substring(num.length() - 1, num.length()).equals("0")) {
                num = (int) point.getNum() + "";
            }
            float numTrueWidth = circlePaint.measureText(num);
            String measureContentString = num;
            if (num.length() < 3) {
                measureContentString = "000";
            }
            float numMeasureWidth = circlePaint.measureText(measureContentString);
            float tagWidth = numMeasureWidth + 2 * tagpadding;
            float tagRectHeight = coordinateTextSize + 2 * (tagpadding / 2);
            circlePaint.setAlpha(255);
            //tag矩形
            circlePaint.setColor(point.color);
            float tagLeft = currentX - tagWidth / 2;
            float tagRight = tagLeft + tagWidth;
            float tagRectBottom = currentY - tagMargin - sanjiaoHeight;
            float tagRectTop = tagRectBottom - tagRectHeight;

            Path path = new Path();
            path.moveTo((tagLeft + tagRight) / 2, tagRectTop);//1
            path.lineTo(tagRight - tagCornerRadius, tagRectTop);//2
            path.quadTo(tagRight, tagRectTop, tagRight, tagRectTop + tagCornerRadius);//3
            path.lineTo(tagRight, tagRectBottom - tagCornerRadius);//4
            path.quadTo(tagRight, tagRectBottom, tagRight - tagCornerRadius, tagRectBottom);//5
            path.lineTo(tagRight - tagWidth / 3 + tagCornerRadius, tagRectBottom);//6
            path.quadTo(tagRight - tagWidth / 3, tagRectBottom, tagRight - tagWidth / 3 - tagCornerRadius / 2, tagRectBottom + tagCornerRadius / 2);//7
            path.lineTo(currentX, currentY - tagMargin);//8
            path.lineTo(tagLeft + tagWidth / 3 + tagCornerRadius / 2, tagRectBottom + tagCornerRadius / 2);//9
            path.quadTo(tagLeft + tagWidth / 3, tagRectBottom, tagLeft + tagWidth / 3 - tagCornerRadius / 2, tagRectBottom);//10
            path.lineTo(tagLeft + tagCornerRadius, tagRectBottom);//11
            path.quadTo(tagLeft, tagRectBottom, tagLeft, tagRectBottom - tagCornerRadius);//12
            path.lineTo(tagLeft, tagRectTop + tagCornerRadius);//13
            path.quadTo(tagLeft, tagRectTop, tagLeft + tagCornerRadius, tagRectTop);
            path.close();
            canvas.drawPath(path, circlePaint);

            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeWidth(tagBorderWidth);
            circlePaint.setColor(point.tagBorderColor);
            canvas.drawPath(path, circlePaint);

            //num
            circlePaint.setStyle(Paint.Style.FILL);
            circlePaint.setAlpha(255);
            circlePaint.setColor(0xffffffff);
            canvas.drawText(num, currentX - numTrueWidth / 2, tagRectBottom - tagpadding / 2 - (int) (circlePaint.descent() - circlePaint.ascent() - circlePaint.getTextSize()), circlePaint);
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
        canvas.drawRect(availableLeft, availableTop, factRectRight, availableBottom, coordinatePaint);

        float peerCoordinateHeight = availableHeight / density;
        //横向line
        for (int i = 0; i < density; i++) {
            float currentY = availableTop + i * peerCoordinateHeight;
            canvas.drawLine(availableLeft, currentY, factRectRight, currentY, coordinatePaint);
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
        if (titles.length == 1) {
            verLineNum = 1;
        }
        if (verLineNum > 0) {
            for (int i = 1; i <= verLineNum; i++) {
                float currentX = availableLeft + i * peerWidth;
                canvas.drawLine(currentX, availableTop, currentX, availableBottom, coordinatePaint);
            }
        }

        //title
        float offset = titleTextSize / 2;
        titleRegionDatas.clear();
        int titleCount = titles.length;
        if (titleCount == 1) {
            float currentTitleWidth = titlePaint.measureText(titles[0]);
            float titleCenterX = availableLeft + peerWidth;
            float currentX = titleCenterX - currentTitleWidth / 2;
            float currentY = availableBottom + titleTextSize + getCircleRadius(innerCircleRadius);
            canvas.drawText(titles[0], currentX, currentY, titlePaint);

            Region region = new Region(
                    (int) (currentX - offset),
                    (int) (currentY - titleTextSize),
                    (int) (currentX + currentTitleWidth + offset),
                    (int) (currentY + 2 * offset)
            );
            titleRegionDatas.add(new TitleClickRegionData(region, 0, titles[0]));

            return;
        }
        for (int i = 0; i < titleCount; i++) {
            float currentTitleWidth = titlePaint.measureText(titles[i]);
            float titleCenterX = availableLeft + i * peerWidth;
            float currentX = titleCenterX - currentTitleWidth / 2;
            float currentY = availableBottom + titleTextSize + getCircleRadius(innerCircleRadius);
            canvas.drawText(titles[i], currentX, currentY, titlePaint);
            //add region

            Region region = new Region(
                    (int) (currentX - offset),
                    (int) (currentY - titleTextSize),
                    (int) (currentX + currentTitleWidth + offset),
                    (int) (currentY + 2 * offset)
            );
            titleRegionDatas.add(new TitleClickRegionData(region, i, titles[i]));
        }
    }

    public int getPressTitleIndex(int x, int y) {
        int size = titleRegionDatas.size();
        if (size < 1) {
            return -1;
        }
        x += getScrollX();
        for (int i = 0; i < size; i++) {
            Region region = titleRegionDatas.get(i).getRegion();
            if (region.contains(x, y)) {
                return i;
            }
        }
        return -1;
    }

    public int[] getPressCircleIndex(int x, int y) {
        if (!isAllowClickShowTag) {
            return new int[]{-1, -1};
        }
        x += getScrollX();
        int[] index = new int[]{-1, -1};
        int lineCount = animatorLineAndCircleList.size();
        for (int i = 0; i < lineCount; i++) {
            LineAndCircle dataline = animatorLineAndCircleList.get(i);
            int circleCount = dataline.getCirclePoints().size();
            for (int j = 0; j < circleCount; j++) {
                CirclePoint circlePoint = dataline.getCirclePoints().get(j);
                if (circlePoint.getCircleRegion().contains(x, y)) {
                    index = new int[]{i, j};
                    return index;
                }
            }
        }
        return index;
    }

    private void setPaint() {
        coordinatePaint.setColor(coorinateColor);
        coordinatePaint.setTextSize(coordinateTextSize);
        coordinatePaint.setStrokeWidth(coordinateStrokeWidth);

        titlePaint.setColor(titleCorlor);
        titlePaint.setTextSize(titleTextSize);

        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(lineStrokeWidth);

        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(shadowColor);
        shadowPaint.setStyle(Paint.Style.FILL);

        innerCircleRadius = lineStrokeWidth;
    }

    private float getLeftWidth() {
        float graduationTextWidth = measureGraduationTextWidth();
        leftMargin = Math.max(graduationTextWidth / 4, getCircleRadius(innerCircleRadius));
        float availableLeftmargin = graduationTextWidth + leftMargin;
        if (titles[0] == null) {
            titles[0] = "";
        }
        return Math.max(availableLeftmargin, titlePaint.measureText(titles[0]) / 2);
    }

    private float measureGraduationTextWidth() {
        return Math.max(coordinatePaint.measureText(max + ""), coordinatePaint.measureText(min + ""));
    }

    class CirclePoint {
        float num;
        float x;
        float y;
        int color;
        int tagBorderColor;

        public CirclePoint(int color, int tagBorderColor, CirclePoint point) {
            this.num = point.num;
            this.x = point.x;
            this.y = point.y;
            this.color = color;
            this.tagBorderColor = tagBorderColor;
        }

        public CirclePoint(float nums, float x, float y) {
            this.num = nums;
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getNum() {
            return num;
        }

        public Region getCircleRegion() {
            float offset = circlrClickOffset;
            //click
            Region currentRegion = new Region();
            currentRegion.set(
                    (int) (x - getCircleRadius(innerCircleRadius) - offset),
                    (int) (y - getCircleRadius(innerCircleRadius) - offset),
                    (int) (x + getCircleRadius(innerCircleRadius) + offset),
                    (int) (y + getCircleRadius(innerCircleRadius) + offset));
            return currentRegion;
        }
    }

    static class TitleClickRegionData {
        Region region;
        int index;
        String title;

        public TitleClickRegionData(Region region, int index, String title) {
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
        int tagBorderColor;
        Path path = new Path();
        List<CirclePoint> circlePoints;

        public LineAndCircle(int lineColor, int tagBorderColor, android.graphics.Path path, List<CirclePoint> circlePoints) {
            this.lineColor = lineColor;
            this.tagBorderColor = tagBorderColor;
            this.path = path;
            this.circlePoints = circlePoints;
        }

        public int getTagBorderColor() {
            return tagBorderColor;
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

    boolean isScrolling = false;

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            L("onDown");
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            int x = (int) e.getX();
            int y = (int) e.getY();

            int pressTitleIndex = getPressTitleIndex(x, y);
            if (pressTitleIndex != -1 && onTitleClickListener != null) {
                onTitleClickListener.onClick(LineChart.this, titleRegionDatas.get(pressTitleIndex).getTitle(), pressTitleIndex);
                return true;
            }
            if (isAllowClickShowTag) {
                int[] pressCircleIndex = getPressCircleIndex(x, y);
                if (pressCircleIndex[0] != -1 && pressCircleIndex[1] != -1) {
                    circleClickIndex = pressCircleIndex;
                    invalidate();
                    return true;
                } else {
                    circleClickIndex = new int[]{-1, -1};
                    invalidate();
                }
            }

            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!isAllowScroll) {
                return false;
            }
            L("distanceX" + distanceX + "--distanceY" + distanceY);
            isScrolling = true;
            getParent().requestDisallowInterceptTouchEvent(true);


            if (distanceX < 0) {
                if (getScrollX() + distanceX <= 0) {
                    scrollTo(0, 0);
                } else scrollBy((int) distanceX, 0);
            }
            int offset = width + getScrollX() - (int) factRight;
            if (distanceX > 0) {
                if (offset + distanceX >= 0) {
                    scrollTo((int) factRight - width, 0);
                } else scrollBy((int) distanceX, 0);
            }

            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (!isAllowScroll) {
                return false;
            }
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                if (Math.abs(velocityX) < 1000) {
                    return true;
                }
                int distanceX = (int) (Math.abs(velocityX) / 1000 * 150);
                if (velocityX < 0) {//左滑-->滑动至尾部
                    int leftWidth = (int) factRight - Math.abs(getScrollX()) - width;
                    if (Math.abs(distanceX) > leftWidth) {
                        distanceX = leftWidth;
                    }
                } else {//右滑-->滑动至头部
                    if (Math.abs(distanceX) > Math.abs(getScrollX())) {
                        distanceX = Math.abs(getScrollX());
                    }
                    distanceX = -distanceX;
                }
                if (getScrollX() < 0) {//头部超出边界
                    distanceX = -getScrollX();
                }
                scroller.startScroll(getScrollX(), 0, distanceX, 0, scrollDuration);
                invalidate();
                return true;
            }
            return false;
        }
    }

    float lastX;
    float lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewGroup viewGroup = (ViewGroup) getParent();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();
            case MotionEvent.ACTION_MOVE:
                float currentX = event.getX();
                float currentY = event.getY();
                float distanceX = currentX - lastX;
                float distanceY = currentY - lastY;

                L("move--" + isScrolling);
                L("distanceX" + distanceX + "--distanceY" + distanceY);
                if (!isScrolling) {
                    if (Math.abs(distanceX) < Math.abs(distanceY)) {
                        viewGroup.requestDisallowInterceptTouchEvent(false);
                        return false;//竖向滑动
                    }

                    int offset = width + getScrollX() - (int) factRight;
                    if ((getScrollX() == 0 && distanceX > 0) || (offset == 0 && distanceX < 0)) {
                        viewGroup.requestDisallowInterceptTouchEvent(false);
                        return false;//横向滑动
                    }

                }
                viewGroup.requestDisallowInterceptTouchEvent(true);
                lastX = currentX;
                lastY = currentY;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isScrolling = false;
                lastX = 0f;
                lastY = 0f;
                viewGroup.requestDisallowInterceptTouchEvent(false);
                break;
        }


        if (mDetector.onTouchEvent(event))
            return true;
        else {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                isScrolling = false;
                if (getScrollX() < 0) {//头部滑动超出边界,回退
                    scroller.startScroll(getScrollX(), 0, 0 - getScrollX(), 0, scrollDuration);
                    invalidate();
                    return true;
                } else {
                    //尾部滑动超出边界,回退
                    int offset = width + getScrollX() - (int) factRight;
                    if (offset > 0) {
                        scroller.startScroll(getScrollX(), 0, -offset, 0, scrollDuration);
                        invalidate();
                        return true;
                    }
                }
            }
            return false;
        }
    }

    int scrollDuration = 500;

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), 0);
            postInvalidate();
        }
    }

    //滑动
    Scroller scroller;
    int touchSlop;

    private void L(String msg) {
        if (false) {
            Log.e("vinctor", msg);
        }
    }
}
