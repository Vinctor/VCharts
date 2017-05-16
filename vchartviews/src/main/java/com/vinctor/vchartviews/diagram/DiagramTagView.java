package com.vinctor.vchartviews.diagram;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Vinctor on 2017/5/15.
 */

public class DiagramTagView extends View {
    private Context context;
    private int height;
    private int width;

    private Paint paint = new Paint();

    private int borderWidth = 10;
    private int borderColor = Color.BLUE;

    private int innerColor = Color.BLACK;
    private Rect innerRect;
    private Rect borderRect;

    public DiagramTagView setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public DiagramTagView setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public DiagramTagView setInnerColor(int innerColor) {
        this.innerColor = innerColor;
        return this;
    }

    public void commit() {
        compute();
        invalidate();
    }

    public DiagramTagView(Context context) {
        super(context);
        init(context, null);
    }

    public DiagramTagView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public DiagramTagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        if (attrs != null) {
            //to do
        }
        paint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        compute();
    }

    private void compute() {
        if (width == 0 || height == 0) {
            return;
        }
        innerRect = new Rect(0, 0, width, height);
        borderRect = new Rect(borderWidth / 2, borderWidth / 2, width - borderWidth / 2, height - borderWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (innerRect != null) {
            //content
            paint.setColor(innerColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(innerRect, paint);

            //borderRect
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(borderWidth);
            canvas.drawRect(borderRect, paint);
        }
    }
}
