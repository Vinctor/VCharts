package com.vinctor.vchartviews.tools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.vinctor.vchartviews.bar.BarCharSingle;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Vinctor on 2017/5/24.
 */

public abstract class onBarShowTagListener implements BarCharSingle.OnShowOtherViewCallback {

    private int radius = 5;
    float textMarginV = 0;
    float textMarginH = 0;
    float jiaoSize = 0;


    Bitmap bitmap;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    int textSize = 20;
    int textColor = Color.WHITE;
    int innerColor = 0xff00A959;
    int borderColor = 0xff005E32;
    float borderWidth = 3;
    String text = "test";
    int[] offset = new int[]{0, 0};

    public onBarShowTagListener(String text, int textSize, int textColor, int innerColor, int borderColor, float borderWidth, int[] offset, int raduis) {
        this.textSize = AutoUtils.getPercentHeightSize(textSize);
        this.textColor = textColor;
        this.innerColor = innerColor;
        this.borderColor = borderColor;
        this.borderWidth = AutoUtils.getPercentHeightSize((int) borderWidth);
        this.text = text;
        this.offset = offset;
        this.radius = AutoUtils.getPercentWidthSize(raduis);
        if (offset.length < 2) {
            throw new IllegalArgumentException();
        }
        init();
    }


    public onBarShowTagListener() {
        init();
    }

    private void init() {
        paint.setTextSize(textSize);
        paint.setStyle(Paint.Style.FILL);
        textMarginV = textSize / 2;
        textMarginH = textSize;
        jiaoSize = (float) (textSize / 2.5);
    }

    @Override
    public Bitmap onShowBitmap() {
        if (bitmap != null) {
            return bitmap;
        }
        float textWidth = paint.measureText(text);
        float bitmapWidth = textWidth + 2 * textMarginH + borderWidth * 2 + jiaoSize;
        float bitmapHeight = textMarginV * 2 + textSize + borderWidth * 2;
        bitmap = Bitmap.createBitmap((int) bitmapWidth, (int) bitmapHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        float rectLeft = 0;
        float rectRight = bitmapWidth - jiaoSize;
        float rextTop = 0;
        float rectBottom = bitmapHeight;


        float offset = borderWidth / 2;
        Path path = new Path();
        path.moveTo(bitmapWidth - offset, bitmapHeight - offset);
        //左下
        path.lineTo(0 + offset + getRadius(), bitmapHeight - offset);
        path.quadTo(0 + offset, bitmapHeight - offset, 0 + offset, bitmapHeight - getRadius() - offset);
        //左上
        path.lineTo(0 + offset, 0 + offset + getRadius());
        path.quadTo(0 + offset, 0 + offset, 0 + offset + getRadius(), 0 + offset);
        //右上
        path.lineTo(rectRight - getRadius() - offset, 0 + offset);
        path.quadTo(rectRight - offset, 0 + offset, rectRight - offset, 0 + offset + getRadius());
        //右中
        path.lineTo(rectRight - offset, rectBottom - offset - jiaoSize);

        path.close();
        paint.setColor(innerColor);
        canvas.drawPath(path, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setColor(borderColor);
        canvas.drawPath(path, paint);

        paint.setColor(textColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, textMarginH, textMarginV + textSize, paint);

        return bitmap;
    }

    @Override
    public float[] onShowOffsetToBar(Bitmap bitmap) {
        return new float[]{0 - bitmap.getWidth() + AutoUtils.getPercentWidthSize(offset[0]), 0 - bitmap.getHeight() + AutoUtils.getPercentHeightSize(offset[1])};
    }



    float getRadius() {
        return radius;
    }

}
