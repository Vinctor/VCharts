package com.vinctor.vchartviews.bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.vinctor.vchartviews.AutoView;

/**
 * Created by Vinctor on 2017/4/9.
 */
public abstract class AbsBarChart extends AutoView {

    float availableWidth;
    float availableBottom;
    float availableTop;
    float availableLeft;
    float availableRight;

    private int density = 4;
    protected int min = 0;
    protected int max = 100;

    private int graduationColor = 0xff929292;
    private int graduationTextSize = 50;
    private int titleColor = 0xff222222;
    protected float barTitleMargin = 20;
    protected int titleTextSize = 50;
    protected float barTextMargin = 10;
    private int barTextSize = 50;


    private Paint graduationPaint = new Paint();
    protected Paint titlePaint = new Paint();
    protected Paint barPaint = new Paint();

    private int height;
    private int width;
    private float graduationStrokeWidth = 3;
    protected float barWidth = 0;
    protected boolean isEnableGraduation = true;

    private float graduaionMargin;

    public AbsBarChart setShowGraduation(boolean enableGraduation) {
        isEnableGraduation = enableGraduation;
        return this;
    }

    public boolean isShowGraduation() {
        return isEnableGraduation;
    }

    public AbsBarChart setBarWidth(float barWidth) {
        this.barWidth = getAutoWidthSize(barWidth);
        return this;
    }

    public AbsBarChart setGraduationTextSize(int graduationTextSize) {
        this.graduationTextSize = getAutoHeightSize(graduationTextSize);
        return this;
    }

    public AbsBarChart setGraduationColor(int graduationColor) {
        this.graduationColor = graduationColor;
        return this;
    }

    public AbsBarChart setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public AbsBarChart setTitleTextSize(int titleTextSize) {
        this.titleTextSize = getAutoHeightSize(titleTextSize);
        return this;
    }

    public AbsBarChart setBarTextSize(int barTextSize) {
        this.barTextSize = getAutoHeightSize(barTextSize);
        return this;
    }

    public AbsBarChart setLineWidth(float graduationStrokeWidth) {
        this.graduationStrokeWidth = getAutoWidthSize(graduationStrokeWidth);
        return this;
    }

    public AbsBarChart setDensity(int density) {
        this.density = density;
        return this;
    }

    public AbsBarChart setMin(int min) {
        this.min = min;
        return this;
    }

    public AbsBarChart setMax(int max) {
        this.max = max;
        return this;
    }

    public AbsBarChart setMinAndMax(int min, int max) {
        this.min = min;
        this.max = max;
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

    public AbsBarChart(Context context) {
        super(context);
        init(context);
    }

    public AbsBarChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AbsBarChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        graduationPaint.setAntiAlias(true);

        titlePaint.setAntiAlias(true);
        titlePaint.setStyle(Paint.Style.FILL);


        barPaint.setAntiAlias(true);
        barPaint.setStyle(Paint.Style.FILL);

        setPaint();
    }

    private void setPaint() {
        graduationPaint.setColor(graduationColor);
        graduationPaint.setTextSize(graduationTextSize);
        graduationPaint.setStrokeWidth(graduationStrokeWidth);

        titlePaint.setStrokeWidth(graduationStrokeWidth);
        titlePaint.setColor(titleColor);
        titlePaint.setTextSize(titleTextSize);

        barPaint.setTextSize(barTextSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.height = h;
        this.width = w;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float graduationWidth = measureGraduationTextWidth();

        graduaionMargin = graduationWidth * 0.5f;//刻度margin
        availableTop = barTextSize + barTextMargin * 2;
        if (isEnableGraduation) {
            availableLeft = graduationWidth + graduaionMargin;
        } else availableLeft = 0;
        availableBottom = height - titleTextSize - barTitleMargin - graduationStrokeWidth - barTitleMargin;
        availableRight = width;
        availableWidth = availableRight - availableLeft;
        if (barWidth == 0) {
            barWidth = availableWidth / 10;
        }
        if (isEnableGraduation) {
            drawGraduation(canvas, graduationWidth);//背景刻度
        }
        drawBar(canvas);
    }

    protected abstract void drawBar(Canvas canvas);

    private float measureGraduationTextWidth() {
        return graduationPaint.measureText(max + "");
    }

    /**
     * 绘制网格线
     *
     * @param canvas
     * @param graduationWidth
     */
    private void drawGraduation(Canvas canvas, float graduationWidth) {

        //刻度外矩形
        graduationPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(availableLeft, availableTop, availableRight, availableBottom, graduationPaint);


        float peerLineHeight = (availableBottom - availableTop) / density;
        for (int i = 1; i <= density; i++) {
            float currentLine = availableTop + peerLineHeight * i;
            //横向网格
            canvas.drawLine(availableLeft, currentLine, availableRight, currentLine, graduationPaint);
        }

        //刻度
        float totalDiff = max - min;
        float peerDiff = totalDiff / density;
        graduationPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i <= density; i++) {
            float graduationTextY = availableTop + peerLineHeight * i;
            String currentGraduation = (int) (max - i * peerDiff) + "";
            float currentGraduationTextWidth = graduationPaint.measureText(currentGraduation);
            canvas.drawText(currentGraduation,
                    availableLeft - currentGraduationTextWidth - graduaionMargin, graduationTextY + graduationTextSize / 2, graduationPaint);
        }
    }

    protected float getBarY(int num) {
        return availableBottom - (num - min) * (availableBottom - availableTop) / (max - min);
    }
}
