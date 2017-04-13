package com.vinctor.vchartviews;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
    }

    public AutoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AutoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
}