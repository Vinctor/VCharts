package com.vinctor.vchartviews.bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinctor on 2017/4/10.
 */

public class BarCharSingle extends AbsBarChart {

    protected List<BarDataSingle> list = new ArrayList<>();

    public BarCharSingle setData(BarDataSingle barData) {
        list.clear();
        this.list.add(barData);
        return this;
    }

    public BarCharSingle addData(BarDataSingle barData) {
        this.list.add(barData);
        return this;
    }

    public BarCharSingle setList(List<BarDataSingle> list) {
        this.list = list;
        return this;
    }

    public BarCharSingle clearDatas() {
        list.clear();
        return this;
    }

    public BarCharSingle(Context context) {
        super(context);
    }

    public BarCharSingle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BarCharSingle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 绘制柱状图
     *
     * @param canvas
     */
    protected void drawBar(Canvas canvas) {
        int barCount = list.size();
        if (barCount == 0) return;
        float barMargin = (availableWidth - barWidth * barCount) / (barCount + 1);

        for (int i = 0; i < barCount; i++) {
            BarDataSingle data = list.get(i);
            int num = data.getNum();
            int barColor = data.getBarColor();
            String title = data.getTitle();
            int trueNum = num;
            if (num >= max) trueNum = max;
            if (num <= min) trueNum = min;
            float currentBarLeft = availableLeft + i * barWidth + (i + 1) * barMargin;
            float currentBarRight = currentBarLeft + barWidth;
            float currentBarBottom = availableBottom;
            float currentBarTop = getBarY(trueNum);
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
}
