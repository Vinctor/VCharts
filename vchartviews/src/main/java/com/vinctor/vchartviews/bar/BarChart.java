package com.vinctor.vchartviews.bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinctor on 2017/4/9.
 */
public class BarChart extends View {

    float availableWidth;
    float availableBottom;
    float availableTop;
    float availableLeft;
    float availableRight;

    private int density = 4;
    private int min = 0;
    private int max = 100;

    private int graduationColor = 0xff929292;
    private int graduationTextSize = 50;
    private int titleColor = 0xff222222;
    private float barTitleMargin = 20;
    private int titleTextSize = 50;
    private float barTextMargin = 10;
    private int barTextSize = 50;


    private Paint graduationPaint = new Paint();
    private Paint titlePaint = new Paint();
    private Paint barPaint = new Paint();

    private int height;
    private int width;
    private float graduationStrokeWidth = 3;
    private float barWidth = 0;

    private List<BarData> list = new ArrayList<>();

    public BarChart setBarWidth(float barWidth) {
        this.barWidth = barWidth;
        return this;
    }

    public BarChart setGraduationTextSize(int graduationTextSize) {
        this.graduationTextSize = graduationTextSize;
        return this;
    }

    public BarChart setGraduationColor(int graduationColor) {
        this.graduationColor = graduationColor;
        return this;
    }

    public BarChart setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public BarChart setTitleTextSize(int titleTextSize) {
        this.titleTextSize = titleTextSize;
        return this;
    }

    public BarChart setBarTextSize(int barTextSize) {
        this.barTextSize = barTextSize;
        return this;
    }

    public BarChart setLineWidth(float graduationStrokeWidth) {
        this.graduationStrokeWidth = graduationStrokeWidth;
        return this;
    }

    public BarChart setData(BarData barData) {
        list.clear();
        this.list.add(barData);
        return this;
    }

    public BarChart addData(BarData barData) {
        this.list.add(barData);
        return this;
    }

    public BarChart setList(List<BarData> list) {
        this.list = list;
        return this;
    }

    public BarChart clearDatas() {
        list.clear();
        return this;
    }

    public BarChart setDensity(int density) {
        this.density = density;
        return this;
    }

    public BarChart setMin(int min) {
        this.min = min;
        return this;
    }

    public BarChart setMax(int max) {
        this.max = max;
        return this;
    }

    public BarChart setMinAndMax(int min, int max) {
        this.min = min;
        this.max = max;
        return this;
    }

    public void commit() {
        setPaint();
        postInvalidate();
    }

    public BarChart(Context context) {
        super(context);
        init(context);
    }

    public BarChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BarChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BarChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
        drawGraduation(canvas, graduationWidth);//背景刻度
        drawBar(canvas);
    }

    /**
     * 绘制柱状图
     *
     * @param canvas
     */
    private void drawBar(Canvas canvas) {
        int barCount = list.size();
        if (barCount == 0) return;
        float barMargin = (availableWidth - barWidth * barCount) / (barCount + 1);

        for (int i = 0; i < barCount; i++) {
            BarData data = list.get(i);
            int num = data.getNum();
            int barColor = data.getBarColor();
            String title = data.getTitle();
            int trueNum = num;
            if (num >= max) trueNum = max;
            if (num <= min) trueNum = min;
            float currentBarLeft = availableLeft + i * barWidth + (i + 1) * barMargin;
            float currentBarRight = currentBarLeft + barWidth;
            float currentBarBottom = availableBottom;
            float currentBarTop = availableBottom - (trueNum - min) * (availableBottom - availableTop) / (max - min);
            RectF rectF = new RectF(currentBarLeft, currentBarTop, currentBarRight, currentBarBottom);

            //柱子
            barPaint.setColor(barColor);
            canvas.drawRoundRect(rectF, barWidth / 10, barWidth / 10, barPaint);

            //数字
            float currentTextWidth = barPaint.measureText(num + "");
            float textCenterX = (currentBarLeft + currentBarRight) / 2;
            float textY = currentBarTop - barTextMargin;
            float textX = textCenterX - currentTextWidth / 2;
            canvas.drawText(num + "", textX, textY, barPaint);

            //标题文字
            float currentTitleWidth = titlePaint.measureText(title);
            float titleCenterX = textCenterX;
            float titleY = availableBottom + barTitleMargin + titleTextSize;
            float titleX = titleCenterX - currentTitleWidth / 2;
            canvas.drawText(title, titleX, titleY, titlePaint);
        }
    }

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
        float graduaionMargin = graduationWidth * 0.5f;//刻度margin
        availableTop = barTextSize + barTextMargin * 2;
        availableLeft = graduationWidth + graduaionMargin;
        availableBottom = height - titleTextSize - barTitleMargin - graduationStrokeWidth - barTitleMargin;
        availableRight = width;
        availableWidth = availableRight - availableLeft;
        if (barWidth == 0) {
            barWidth = availableWidth / 10;
        }

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
}
