package com.vinctor.vchartviews.tools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import com.vinctor.vchartviews.bar.BarCharSingle;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Vinctor on 2017/5/24.
 */

public abstract class onBarShowTag2Listener implements BarCharSingle.OnShowOtherViewCallback {

    private int indicatorCircleRadius = 30;
    private int indicatorAngle = 80;
    private int color = 0xffFE5560;
    private BarCharSingle bar;
    private float offset[] = new float[2];

    private float borderWidth;

    Bitmap bitmap;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path indicatorPath = new Path();
    private float indicatorHeight;
    private int fromAngle;
    private int toAngle;
    private Bitmap avater;

    public onBarShowTag2Listener(BarCharSingle bar) {
        this.bar = bar;
        init();
    }

    public onBarShowTag2Listener(BarCharSingle bar, int indicatorCircleRadius, int indicatorAngle, int color, float[] offset) {
        this.indicatorCircleRadius = AutoUtils.getPercentWidthSize(indicatorCircleRadius);
        this.indicatorAngle = indicatorAngle;
        this.color = color;
        this.bar = bar;
        this.offset = offset;
        if (borderWidth == 0) {
            borderWidth = indicatorCircleRadius / 3;
        }
        if (offset.length < 2) {
            throw new IllegalArgumentException();
        }
        init();
    }

    public onBarShowTag2Listener(BarCharSingle bar, int indicatorCircleRadius, int indicatorAngle, int color, float[] offset, float borderWidth) {
        this.indicatorCircleRadius = AutoUtils.getPercentWidthSize(indicatorCircleRadius);
        this.indicatorAngle = indicatorAngle;
        this.color = color;
        this.bar = bar;
        this.offset = offset;
        this.borderWidth = AutoUtils.getPercentWidthSize((int) borderWidth);
        if (this.borderWidth == 0) {
            this.borderWidth = this.indicatorCircleRadius / 3;
        }
        if (offset.length < 2) {
            throw new IllegalArgumentException();
        }
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.FILL);

        indicatorHeight = (float) (indicatorCircleRadius / Math.sin(indicatorAngle / 2 * Math.PI / 180))
                + indicatorCircleRadius;
        int noneAngle = 180 - indicatorAngle;
        fromAngle = 0 - 90 - 90 - (90 - noneAngle / 2);
        toAngle = 90 - noneAngle / 2;
    }

    @Override
    public Bitmap onShowBitmap() {
        float bitmapWidth = indicatorCircleRadius * 2;
        float bitmapHeight = indicatorHeight;
        bitmap = Bitmap.createBitmap((int) bitmapWidth, (int) bitmapHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);

        indicatorPath.reset();
        indicatorPath.moveTo(indicatorCircleRadius, indicatorHeight);
        RectF outRecf = new RectF(0, 0, indicatorCircleRadius * 2, indicatorCircleRadius * 2);
        indicatorPath.arcTo(outRecf, fromAngle, toAngle - fromAngle);
        indicatorPath.close();

        paint.setColor(color);
        paint.setAlpha(0xff);
        canvas.drawPath(indicatorPath, paint);

        if (avater != null) {
            canvas.save();
            canvas.translate(borderWidth, borderWidth);
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(bar.getContext().getResources(), avater);
            drawable.setCircular(true);
            Bitmap handlerBitmap = drawableToBitamp(drawable);

            int bitmapSize = Math.min(handlerBitmap.getHeight(), avater.getWidth());
            Rect srcRect = new Rect(0, 0, bitmapSize, bitmapSize);
            RectF desRecf = new RectF(0, 0, (indicatorCircleRadius - borderWidth) * 2, (indicatorCircleRadius - borderWidth) * 2);
            canvas.drawBitmap(handlerBitmap, srcRect, desRecf, null);
            canvas.restore();
        }

        return bitmap;
    }

    private Bitmap drawableToBitamp(Drawable drawable) {
        Bitmap temp;
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        temp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(temp);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return temp;
    }

    public void setBitmap(Bitmap bitmap) {
        this.avater = bitmap;
        bar.commit();
    }

    @Override
    public float[] onShowOffsetToBar(Bitmap bitmap) {
        float offsetX = bar.getPeerBarWidth() / 2 - bitmap.getWidth() / 2;
        return new float[]{0 + offsetX - AutoUtils.getPercentWidthSize((int) offset[0]),
                0 - bitmap.getHeight() - AutoUtils.getPercentWidthSize((int) offset[1])};
    }
}
