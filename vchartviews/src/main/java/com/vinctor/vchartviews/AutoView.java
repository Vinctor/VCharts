package com.vinctor.vchartviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * Created by Vinctor on 2017/4/12.
 */

public class AutoView extends View {

    protected int screenWidth = AutoLayoutConifg.getInstance().getScreenWidth();
    protected int designWidth = AutoLayoutConifg.getInstance().getDesignWidth();

    protected int screenHeight = AutoLayoutConifg.getInstance().getScreenHeight();
    protected int designHeight = AutoLayoutConifg.getInstance().getDesignHeight();

    private boolean isAuto = true;

    public AutoView(Context context) {
        super(context);
        init();
    }


    public AutoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void setAuto(boolean auto) {
        isAuto = auto;
    }

    public boolean isAuto() {
        return isAuto;
    }

    /**
     * 获取aotu之后的数值,默认以width为基准
     *
     * @param val
     * @return
     */
    protected float getAutoWidthSize(float val) {
        if (isAuto)
            return val / designWidth * screenWidth;
        return val;

    }

    /**
     * 获取auto之后的数据,以height为基准
     *
     * @param val
     * @return
     */
    protected float getAutoHeightSize(float val) {
        if (isAuto) {
            return val / designHeight * screenHeight;
        }
        return val;
    }

    /**
     * 获取aotu之后的数值,默认以width为基准
     *
     * @param val
     * @return
     */
    protected int getAutoWidthSize(int val) {
        if (isAuto)
            return (int) (val * 1.0f / designWidth * screenWidth);
        return val;

    }

    /**
     * 获取auto之后的数据,以height为基准
     *
     * @param val
     * @return
     */
    protected int getAutoHeightSize(int val) {
        if (isAuto) {
            return (int) (val * 1.0f / designHeight * screenHeight);
        }
        return val;
    }

    public float getPercentWidth1px() {
        return 1.0f * screenWidth / designWidth;
    }

    public float getPercentHeight1px() {
        return 1.0f * screenHeight / designHeight;
    }


    protected Paint errorPaint = new Paint();
    protected int errorColor = 0xff444444;
    protected int errorTextSize = 40;
    protected String errorText = "数据错误,点击重试";
    protected boolean isError = false;

    Paint shadowErrorPaint = new Paint();

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        errorPaint.setColor(errorColor);
        errorPaint.setAntiAlias(true);
        errorPaint.setTextSize(errorTextSize);
        errorPaint.setStyle(Paint.Style.FILL);
        errorPaint.setStrokeWidth(8);

        shadowErrorPaint.setStyle(Paint.Style.FILL);
        shadowErrorPaint.setAntiAlias(true);
    }

//    public void showError(String errorText) {
//        this.isError = true;
//        this.errorText = errorText;
//        postInvalidate();
//    }
//
//    public void showError(boolean isError) {
//        this.isError = isError;
//        postInvalidate();
//    }


    protected void drawErrorText(Canvas canvas, float width, float height) {

        shadowErrorPaint.setColor(0x22000000);

        canvas.drawRect(0, 0, width, height, shadowErrorPaint);

        float textWidth = 0f;
        while (true) {
            textWidth = errorPaint.measureText(errorText);
            if (textWidth <= width * 0.8) {
                break;
            }
            errorText = errorText.substring(0, errorText.length() - 1);
        }

        float leftMargin = (width - textWidth) / 2;
        float topMargin = (height - errorTextSize) / 2 + errorTextSize;

        shadowErrorPaint.setColor(0xffffffff);
        float reactOffset = errorTextSize / 2;
        RectF rectF = new RectF(leftMargin - reactOffset,
                topMargin - errorTextSize - reactOffset,
                leftMargin + textWidth + reactOffset,
                topMargin + reactOffset);
        canvas.drawRoundRect(rectF, errorTextSize/3, errorTextSize/3, shadowErrorPaint);

        canvas.drawText(errorText, leftMargin, topMargin, errorPaint);
    }
}