package com.vinctor.vchartviews.bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.vinctor.vchartviews.bar.multi.BarDataMulti;
import com.vinctor.vchartviews.bar.multi.SingleData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinctor on 2017/4/10.
 */

public class BarCharMulti extends AbsBarChart {

    private int barGroupCount = 2;

    List<BarDataMulti> list = new ArrayList<>();

    public BarCharMulti setBarGroupCount(int barGroupCount) {
        this.barGroupCount = barGroupCount;
        if (barGroupCount < 2) {
            throw new IllegalArgumentException("you cannot set barCounts less than 2,you can use the view BarCharSingle!");
        }
        return this;
    }

    public BarCharMulti setData(BarDataMulti barData) {
        list.clear();
        this.list.add(barData);
        return this;
    }

    public BarCharMulti addData(BarDataMulti barData) {
        this.list.add(barData);
        return this;
    }

    public BarCharMulti setList(List<BarDataMulti> list) {
        this.list = list;
        return this;
    }

    public BarCharMulti clearDatas() {
        list.clear();
        return this;
    }

    public BarCharMulti(Context context) {
        super(context);
    }

    public BarCharMulti(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BarCharMulti(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void drawBar(Canvas canvas) {
        int groupCount = list.size();
        if (groupCount <= 0) return;
        float intervalBarWidth = barWidth / 4;
        float peerGroupTotalWidth = barWidth * barGroupCount + (barGroupCount - 1) * intervalBarWidth;
        float peerWidth = (availableWidth - peerGroupTotalWidth * groupCount) / (groupCount + 1);

        for (int i = 0; i < groupCount; i++) {
            BarDataMulti multi = list.get(i);
            float lastLeft = availableLeft + (i + 1) * peerWidth + peerGroupTotalWidth * i;
            List<SingleData> singleList = multi.getList();
            int count = singleList.size();
            if (count != barGroupCount) {
                throw new IllegalArgumentException("peer group's size should be " + groupCount + ", you can invoke setBarGroupCount() to set the group count!");
            }
            for (int j = 0; j < count; j++) {
                SingleData single = singleList.get(j);
                int num = single.getNum();
                int barColor = single.getBarColor();
                int trueNum = num;
                if (num <= min) trueNum = min;
                if (num >= max) trueNum = max;
                float singleLeft = lastLeft + (barWidth + intervalBarWidth) * j;
                float singleTop = getBarY(trueNum);
                float singleRight = singleLeft + barWidth;
                RectF rect = new RectF(singleLeft, singleTop, singleRight, availableBottom);
                barPaint.setColor(barColor);
                canvas.drawRoundRect(rect, barWidth / 10, barWidth / 10, barPaint);


                //数字
                String dataToShow = num + "";
                if (onShowDataListener != null) {
                    dataToShow = onShowDataListener.onShow(num);
                }
                float currentTextWidth = barPaint.measureText(dataToShow);
                float textCenterX = (singleLeft + singleRight) / 2;
                float textY = singleTop - barTextMargin;
                float textX = textCenterX - currentTextWidth / 2;
                canvas.drawText(dataToShow, textX, textY, barPaint);
            }

            //title
            String title = multi.getTitle();
            float titleWidth = titlePaint.measureText(title);
            float centerX = lastLeft + peerGroupTotalWidth / 2;
            float titleX = centerX - titleWidth / 2;
            float titleY = availableBottom + barTitleMargin + titleTextSize;
            canvas.drawText(title, titleX, titleY, titlePaint);
        }
    }
}

