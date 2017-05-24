package com.vinctor.vchartviews.bar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

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

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (otherRegion.contains((int) (e.getX()), (int) (e.getY()))) {
                    if (onShowOtherViewCallback != null) {
                        onShowOtherViewCallback.onClick();
                    }
                }
                return super.onSingleTapUp(e);
            }
        };
        gestureDetector = new GestureDetector(context, simpleOnGestureListener);
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


            //柱子
            //outer
            RectF outRectF = new RectF(currentBarLeft, currentBarTop, currentBarRight, currentBarBottom);
            int barStrokeColor = data.getBarStrokeColor();
            barPaint.setColor(barStrokeColor);
            canvas.drawRoundRect(outRectF, barWidth / 8, barWidth / 8, barPaint);
            //inner
            if (barStrokeWidth > 0) {
                RectF innerRectF = new RectF(
                        currentBarLeft + barStrokeWidth,
                        currentBarTop + barStrokeWidth,
                        currentBarRight - barStrokeWidth,
                        currentBarBottom - barStrokeWidth
                );
                barPaint.setColor(barColor);
                canvas.drawRoundRect(innerRectF, (barWidth - barStrokeWidth) / 8, (barWidth - barStrokeWidth) / 8, barPaint);
            }


            //数字
            String dataToShow = num + "";
            if (onShowDataListener != null) {
                dataToShow = onShowDataListener.onShow(num);
            }
            float currentTextWidth = barPaint.measureText(dataToShow);
            float textCenterX = (currentBarLeft + currentBarRight) / 2;
            float textY = currentBarTop - barTextMargin;
            float textX = textCenterX - currentTextWidth / 2;
            canvas.drawText(dataToShow, textX, textY, barPaint);

            //标题文字
            float currentTitleWidth = titlePaint.measureText(title);
            float titleCenterX = textCenterX;
            float titleY = availableBottom + barTitleMargin + titleTextSize;
            float titleX = titleCenterX - currentTitleWidth / 2;
            canvas.drawText(title, titleX, titleY, titlePaint);

            //otherView
            if (onShowOtherViewCallback != null && onShowOtherViewCallback.onShowIndex() == i) {
                Bitmap bitmap = onShowOtherViewCallback.onShowBitmap();
                if (onShowOtherViewCallback.onShowOffsetToBar(bitmap).length < 2) {
                    throw new IllegalArgumentException();
                }
                float viewX = currentBarLeft + onShowOtherViewCallback.onShowOffsetToBar(bitmap)[0];
                float viewY = currentBarTop + onShowOtherViewCallback.onShowOffsetToBar(bitmap)[1];

                otherRegion = new Region((int) viewX, (int) viewY, (int) currentBarLeft, (int) currentBarTop);

                canvas.drawBitmap(bitmap, viewX, viewY, null);
            }
        }
    }

    Region otherRegion;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (onShowOtherViewCallback == null) {
            return super.onTouchEvent(event);
        }
        return gestureDetector.onTouchEvent(event);
    }


    OnShowOtherViewCallback onShowOtherViewCallback;
    GestureDetector gestureDetector;

    public BarCharSingle setOnShowOtherViewCallback(OnShowOtherViewCallback onShowOtherViewCallback) {
        this.onShowOtherViewCallback = onShowOtherViewCallback;
        return this;
    }

    public interface OnShowOtherViewCallback {
        Bitmap onShowBitmap();

        float[] onShowOffsetToBar(Bitmap bitmap);

        int onShowIndex();

        void onClick();
    }
}
