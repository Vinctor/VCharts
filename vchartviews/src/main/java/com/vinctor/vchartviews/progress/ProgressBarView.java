package com.vinctor.vchartviews.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;

import com.vinctor.vchartviews.AutoView;
import com.vinctor.vchartviews.R;

/**
 * Created by Vinctor on 2017/6/28.
 */

public class ProgressBarView extends AutoView {
    private Context context;
    private float width;
    private float height;
    private float availableLeft = 0;
    private float availableRight = 0;
    private float availableWidth = 0;
    private int indicatorBorderScale = 8;
    private float bitmapBorder;

    private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path indicatorOutPath = new Path();
    private Path indicatorInnerPath = new Path();

    private float progressWidth = 0;
    private float indicatorHeight = 0;


    //set
    private int max = 100;
    private int min = 0;
    private int progress = 0;
    private int progressBackColor = 0xFFCAC8;
    private int progressColor = 0xFE5560;
    private float progressBarHeight = 0;
    private float indicatorCircleRadius = 0;
    private int indicatorAngle = 80;
    private Bitmap bitmap = null;


    public ProgressBarView(Context context) {
        super(context);
        init(context, null);
    }

    public ProgressBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        mainPaint.setStyle(Paint.Style.FILL);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressBarView);
            progressColor = ta.getColor(R.styleable.ProgressBarView_progressColor, progressColor);
            progressBackColor = ta.getColor(R.styleable.ProgressBarView_progressBackColor, progressBackColor);
            setProgressBarHeight(ta.getDimension(R.styleable.ProgressBarView_progressBarHeight, progressBarHeight));
            setIndicatorCircleRadius(ta.getDimension(R.styleable.ProgressBarView_indicatorCircleRadius, indicatorCircleRadius));
            indicatorAngle = ta.getInteger(R.styleable.ProgressBarView_indicatorAngle, 80);

            ta.recycle();
        }
    }

    public void commit() {
        compute();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        compute();
    }

    private void compute() {
        if (width == 0 || height == 0) {
            return;
        }
        if (max < min) {
            return;
        }
        if (progress < min) {
            progress = min;
        }
        if (progress > max) {
            progress = max;
        }

        if (progressBarHeight == 0) {
            progressBarHeight = 50;
        }
        if (indicatorCircleRadius == 0) {
            indicatorCircleRadius = 70;
        }


        //available
        availableLeft = indicatorCircleRadius;
        availableRight = width - indicatorCircleRadius;
        availableWidth = availableRight - availableLeft;

        //progress
        progressWidth = Math.abs((progress - min) * availableWidth / (max - min));

        //indicator
        int noneAngle = 180 - indicatorAngle;
        int fromAngle = 0 - 90 - 90 - (90 - noneAngle / 2);
        int toAngle = 90 - noneAngle / 2;

        indicatorHeight = (float) (indicatorCircleRadius / Math.sin(indicatorAngle / 2 * Math.PI / 180))
                + indicatorCircleRadius;
        //outer
        indicatorOutPath.reset();
        indicatorOutPath.moveTo(indicatorCircleRadius, indicatorHeight);
        RectF outRecf = new RectF(0, 0, indicatorCircleRadius * 2, indicatorCircleRadius * 2);
        indicatorOutPath.arcTo(outRecf, fromAngle, toAngle - fromAngle);
        indicatorOutPath.close();

        indicatorInnerPath.reset();
        float indicatorBorder = indicatorCircleRadius / indicatorBorderScale;
        indicatorInnerPath.moveTo(indicatorCircleRadius, indicatorHeight - indicatorBorder);
        RectF innerRecf = new RectF(indicatorBorder, indicatorBorder,
                indicatorCircleRadius * 2 - indicatorBorder, indicatorCircleRadius * 2 - indicatorBorder);
        indicatorInnerPath.arcTo(innerRecf, fromAngle, toAngle - fromAngle);
        indicatorInnerPath.close();

        //bitmap
        bitmapBorder = indicatorCircleRadius / indicatorBorderScale * 3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (width == 0 || height == 0) {
            return;
        }
        if (max < min) {
            return;
        }

        //progress
        drawProgress(canvas);
        //indicator
        drawIndicator(canvas);
        //Pic
        drawPic(canvas);
    }

    private Bitmap handlerBitmap;

    private void drawPic(Canvas canvas) {
        if (bitmap == null) {
            return;
        }
        canvas.translate(bitmapBorder, bitmapBorder);
        if (isNewBitmap) {
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
            drawable.setCircular(true);
            handlerBitmap = drawableToBitamp(drawable);
            isNewBitmap = false;
        }

        int bitmapSize = Math.min(handlerBitmap.getHeight(), bitmap.getWidth());
        Rect srcRect = new Rect(0, 0, bitmapSize, bitmapSize);
        RectF desRecf = new RectF(0, 0, (indicatorCircleRadius - bitmapBorder) * 2, (indicatorCircleRadius - bitmapBorder) * 2);
        canvas.drawBitmap(handlerBitmap, srcRect, desRecf, null);
        canvas.restore();
    }

    private void drawIndicator(Canvas canvas) {
        canvas.save();
        canvas.translate(availableLeft + progressWidth / 2 - indicatorCircleRadius, height - progressBarHeight / 2 - indicatorHeight);
        //outer
        mainPaint.setColor(0xffffffff);
        canvas.drawPath(indicatorOutPath, mainPaint);
        //inner
        mainPaint.setColor(progressColor);
        mainPaint.setAlpha(0xff);
        canvas.drawPath(indicatorInnerPath, mainPaint);

    }

    private void drawProgress(Canvas canvas) {
        canvas.save();
        canvas.translate(availableLeft, height - progressBarHeight);
        //whole
        mainPaint.setColor(progressBackColor);
        mainPaint.setAlpha(255);
        RectF wholeRecf = new RectF(0, 0, availableWidth, progressBarHeight);
        canvas.drawRoundRect(wholeRecf, progressBarHeight / 2, progressBarHeight / 2, mainPaint);
        //progress
        mainPaint.setColor(progressColor);
        mainPaint.setAlpha(255);

        if (progressWidth <= progressBarHeight) {
            float padding = (progressBarHeight - progressWidth) / 2;
            RectF progressRecf = new RectF(0, padding, progressWidth, progressBarHeight - padding);
            canvas.drawRoundRect(progressRecf, progressWidth / 2, progressWidth / 2, mainPaint);
        } else {
            RectF progressRecf = new RectF(0, 0, progressWidth, progressBarHeight);
            canvas.drawRoundRect(progressRecf, progressBarHeight / 2, progressBarHeight / 2, mainPaint);
        }
        canvas.restore();
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

    boolean isNewBitmap;

    public ProgressBarView setBitmap(Bitmap bitmap) {
        isNewBitmap = true;
        this.bitmap = bitmap;
        return this;
    }

    public ProgressBarView setMax(int max) {
        this.max = max;
        return this;
    }

    public ProgressBarView setMin(int min) {
        this.min = min;
        return this;
    }

    public ProgressBarView setProgress(int progress) {
        this.progress = progress;
        return this;
    }

    public ProgressBarView setProgressBackColor(int progressBackColor) {
        this.progressBackColor = progressBackColor;
        return this;
    }

    public ProgressBarView setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        return this;
    }

    public ProgressBarView setProgressBarHeight(float progressBarHeight) {
        this.progressBarHeight = progressBarHeight;
        return this;
    }

    public ProgressBarView setIndicatorCircleRadius(float indicatorCircleRadius) {
        this.indicatorCircleRadius = indicatorCircleRadius;
        return this;
    }

    public ProgressBarView setIndicatorAngle(int indicatorAngle) {
        this.indicatorAngle = indicatorAngle;
        return this;
    }


    public int getProgress() {
        return progress;
    }
}
