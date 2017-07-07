package com.vinctor.vchartviews.compare;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.vinctor.vchartviews.AutoView;
import com.vinctor.vchartviews.R;

/**
 * Created by Vinctor on 2017/5/25.
 */

public class CompareView extends AutoView {
    private Context context;
    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path linePath = new Path();

    private float width;
    private float peerProgressWidth;
    private float height;
    private float centerX;
    private float centerY;
    private float progressOffset;
    private float circleOffset;


    private float raduis = 65;
    private float progressHeight = 30;
    private float progressStrokeWidth = 3;
    private float circleStorkeWidth = 6;
    private float lineStrokeWidth = 3;
    private int borderColor = 0xff186D45;
    private int lessBorderColor = Integer.MAX_VALUE;
    private int moreBorderColor = Integer.MAX_VALUE;
    private int moreDataColor = 0xffFF7E76;
    private int lessDataColor = Color.WHITE;
    private int lineColor = Color.WHITE;
    private PathEffect pathEffect;
    private int min = 0;
    private int max = 100;
    private float origin = 95;
    private float other = 50;
    private int imgResID = -1;


    public CompareView(Context context) {
        super(context);
        init(context, null);
    }

    public CompareView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CompareView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.compareChart);
            setRaduis(ta.getDimension(R.styleable.compareChart_radius, raduis));
            setProgressHeight(ta.getDimension(R.styleable.compareChart_progressHeight, progressHeight));
            setProgressStrokeWidth(ta.getDimension(R.styleable.compareChart_progressStrokeWidth, progressStrokeWidth));
            setCircleStorkeWidth(ta.getDimension(R.styleable.compareChart_circleStrokeWidth, circleStorkeWidth));
            setLineStrokeWidth(ta.getDimension(R.styleable.compareChart_lineStrokeWidth, lineStrokeWidth));
            borderColor = ta.getColor(R.styleable.compareChart_borderColor, borderColor);
            moreDataColor = ta.getColor(R.styleable.compareChart_moreDataColor, moreDataColor);
            lessDataColor = ta.getColor(R.styleable.compareChart_lessDataColor, lessDataColor);
            lineColor = ta.getColor(R.styleable.compareChart_lineColor, lineColor);
            min = ta.getInt(R.styleable.compareChart_minNum, min);
            max = ta.getInt(R.styleable.compareChart_maxNum, max);
            imgResID = ta.getResourceId(R.styleable.compareChart_imgRes, -1);
        }

        setPaint();
        compute();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        centerX = w / 2;
        centerY = h / 2;
        circleOffset = (float) Math.sqrt(Math.pow(raduis, 2) - Math.pow(progressHeight / 2, 2));
        peerProgressWidth = width / 2 - circleOffset;
        setPaint();
        compute();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);
        drawProgress(canvas);
        drawCircle(canvas);
        drawPic(canvas);
    }

    //中心图
    private void drawPic(Canvas canvas) {
        if (imgResID <= 0) {
            return;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgResID, options);
        float destSize = (float) (raduis * Math.sqrt(2));//边长
        destSize = (float) (raduis * 1.3);
        float srcSize = Math.min(options.outHeight, options.outWidth);
        int sample = 1;
        if (srcSize < destSize) {
            sample = (int) (destSize / srcSize + 1);
        } else {
            sample = (int) (srcSize / destSize);
        }
        options.inSampleSize = sample;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), imgResID, options);

        canvas.drawBitmap(bitmap,
                new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                new RectF(centerX - destSize / 2, centerY - destSize / 2, centerX + destSize / 2, centerY + destSize / 2),
                null
        );
    }

    //中心圆
    private void drawCircle(Canvas canvas) {
        float trueRadius = raduis - circleStorkeWidth / 2;

        mainPaint.setColor(Color.WHITE);
        mainPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX, centerY, trueRadius, mainPaint);

        mainPaint.setColor(borderColor);
        mainPaint.setStyle(Paint.Style.STROKE);
        mainPaint.setStrokeWidth(circleStorkeWidth);
        canvas.drawCircle(centerX, centerY, trueRadius, mainPaint);
    }

    //进度条
    private void drawProgress(Canvas canvas) {
        drawMoreProgress(canvas);
        drawLessProgress(canvas);
    }

    //其他进度
    private void drawMoreProgress(Canvas canvas) {
        float linewidth = Math.max(origin, other) * peerProgressWidth / max;
        if (linewidth == 0) {
            return;
        }

        //left
        if (origin > other) {
            float leftOffset = peerProgressWidth - linewidth + progressHeight / 2;

            canvas.save();
            canvas.translate(leftOffset, 0);

            mainPaint.setColor(moreBorderColor);
            mainPaint.setStyle(Paint.Style.STROKE);
            mainPaint.setStrokeWidth(progressHeight);
            canvas.drawLine(0, centerY, linewidth - progressHeight / 2, centerY, mainPaint);
            mainPaint.setStrokeWidth(progressHeight - progressStrokeWidth * 2);
            mainPaint.setColor(moreDataColor);
            canvas.drawLine(0, centerY, linewidth - progressHeight / 2, centerY, mainPaint);

            mainPaint.setStyle(Paint.Style.FILL);
            mainPaint.setColor(moreBorderColor);
            canvas.drawArc(new RectF(
                    -progressHeight / 2,
                    centerY - progressHeight / 2,
                    progressHeight / 2,
                    centerY + progressHeight / 2), 90, 180, true, mainPaint);
            mainPaint.setColor(moreDataColor);
            canvas.translate(progressStrokeWidth / 2, 0);
            canvas.drawArc(new RectF(
                    -progressHeight / 2 + progressStrokeWidth,
                    centerY - progressHeight / 2 + progressStrokeWidth,
                    progressHeight / 2 - progressStrokeWidth,
                    centerY + progressHeight / 2 - progressStrokeWidth), 90, 180, true, mainPaint);

            canvas.restore();
        }


        //right
        if (origin < other) {

            canvas.save();
            canvas.translate(centerX + circleOffset, 0);
            if (linewidth >= progressHeight) {
                mainPaint.setColor(moreBorderColor);
                mainPaint.setStyle(Paint.Style.STROKE);
                mainPaint.setStrokeWidth(progressHeight);
                canvas.drawLine(0, centerY, linewidth - progressHeight / 2, centerY, mainPaint);
                mainPaint.setStrokeWidth(progressHeight - progressStrokeWidth * 2);
                mainPaint.setColor(moreDataColor);
                canvas.drawLine(0, centerY, linewidth - progressHeight / 2, centerY, mainPaint);

                canvas.save();
                canvas.translate(linewidth - progressHeight / 2, 0);
                mainPaint.setStyle(Paint.Style.FILL);
                mainPaint.setColor(moreBorderColor);
                canvas.drawArc(new RectF(-progressHeight / 2, centerY - progressHeight / 2, progressHeight / 2, centerY + progressHeight / 2), -90, 180, false, mainPaint);
                mainPaint.setColor(moreDataColor);
                canvas.translate(-progressStrokeWidth / 2, 0);
                canvas.drawArc(new RectF(-progressHeight / 2 + progressStrokeWidth, centerY - progressHeight / 2 + progressStrokeWidth, progressHeight / 2 - progressStrokeWidth, centerY + progressHeight / 2 - progressStrokeWidth), -90, 180, false, mainPaint);
                canvas.restore();
            } else {
                mainPaint.setStyle(Paint.Style.FILL);
                mainPaint.setColor(moreBorderColor);
                canvas.drawArc(new RectF(-progressHeight / 2, centerY - progressHeight / 2, progressHeight / 2, centerY + progressHeight / 2), -90, 180, false, mainPaint);
                mainPaint.setColor(moreDataColor);
                canvas.drawArc(new RectF(-progressHeight / 2 + progressStrokeWidth, centerY - progressHeight / 2 + progressStrokeWidth, progressHeight / 2 - progressStrokeWidth, centerY + progressHeight / 2 - progressStrokeWidth), -90, 180, false, mainPaint);
            }


            canvas.restore();
        }
    }

    //源进度
    private void drawLessProgress(Canvas canvas) {
        float linewidth = Math.min(origin, other) * peerProgressWidth / max;
        if (linewidth == 0) {
            return;
        }

        //left
        float leftOffset = peerProgressWidth - linewidth + progressHeight / 2;

        canvas.save();
        canvas.translate(leftOffset, 0);

        mainPaint.setColor(lessBorderColor);
        mainPaint.setStyle(Paint.Style.FILL);
        mainPaint.setStrokeWidth(progressHeight);
        canvas.drawLine(0, centerY, linewidth - progressHeight / 2, centerY, mainPaint);
        mainPaint.setStrokeWidth(progressHeight - progressStrokeWidth * 2);
        mainPaint.setColor(lessDataColor);
        canvas.drawLine(0, centerY, linewidth - progressHeight / 2, centerY, mainPaint);

        mainPaint.setStyle(Paint.Style.FILL);
        mainPaint.setColor(lessBorderColor);
        canvas.drawArc(new RectF(
                -progressHeight / 2,
                centerY - progressHeight / 2,
                progressHeight / 2,
                centerY + progressHeight / 2), 90, 180, false, mainPaint);
        mainPaint.setColor(lessDataColor);
        canvas.translate(progressStrokeWidth / 2, 0);
        canvas.drawArc(new RectF(
                -progressHeight / 2 + progressStrokeWidth,
                centerY - progressHeight / 2 + progressStrokeWidth,
                progressHeight / 2 - progressStrokeWidth,
                centerY + progressHeight / 2 - progressStrokeWidth), 90, 180, true, mainPaint);

        canvas.restore();


        //right
        canvas.save();
        canvas.translate(centerX + circleOffset, 0);
        if (linewidth >= progressHeight / 2) {
            mainPaint.setColor(lessBorderColor);
            mainPaint.setStyle(Paint.Style.STROKE);
            mainPaint.setStrokeWidth(progressHeight);
            canvas.drawLine(0, centerY, linewidth - progressHeight / 2, centerY, mainPaint);
            mainPaint.setStrokeWidth(progressHeight - progressStrokeWidth * 2);
            mainPaint.setColor(lessDataColor);
            canvas.drawLine(0, centerY, linewidth - progressHeight / 2, centerY, mainPaint);

            canvas.save();
            canvas.translate(linewidth - progressHeight / 2, 0);
            mainPaint.setStyle(Paint.Style.FILL);
            mainPaint.setColor(lessBorderColor);
            canvas.drawArc(new RectF(-progressHeight / 2, centerY - progressHeight / 2, progressHeight / 2, centerY + progressHeight / 2), -90, 180, false, mainPaint);
            mainPaint.setColor(lessDataColor);
            canvas.translate(-progressStrokeWidth / 2, 0);
            canvas.drawArc(new RectF(-progressHeight / 2 + progressStrokeWidth, centerY - progressHeight / 2 + progressStrokeWidth, progressHeight / 2 - progressStrokeWidth, centerY + progressHeight / 2 - progressStrokeWidth), -90, 180, false, mainPaint);
            canvas.restore();
        } else {
            canvas.translate(-(progressHeight / 2 - linewidth), 0);
            mainPaint.setStyle(Paint.Style.FILL);
            mainPaint.setColor(lessBorderColor);
            canvas.drawArc(new RectF(-progressHeight / 2, centerY - progressHeight / 2, progressHeight / 2, centerY + progressHeight / 2), -90, 180, false, mainPaint);
            mainPaint.setColor(lessDataColor);
            canvas.drawArc(new RectF(-progressHeight / 2 + progressStrokeWidth, centerY - progressHeight / 2 + progressStrokeWidth, progressHeight / 2 - progressStrokeWidth, centerY + progressHeight / 2 - progressStrokeWidth), -90, 180, false, mainPaint);
        }


        canvas.restore();

    }

    //线
    private void drawLine(Canvas canvas) {
        canvas.drawPath(linePath, linePaint);
    }

    private void compute() {
        if (width == 0 || height == 0) {
            return;
        }
        if (raduis > height / 2) {
            raduis = height / 2;
        }
        checkBorderColor();
        progressOffset = progressStrokeWidth / 2;

        linePath.reset();
        linePath.moveTo(0, centerY);
        linePath.lineTo(width, centerY);
    }

    private void checkBorderColor() {
        if (lessBorderColor == Integer.MAX_VALUE) {
            lessBorderColor = borderColor;
        }
        if (moreBorderColor == Integer.MAX_VALUE) {
            moreBorderColor = borderColor;
        }
    }

    private void setPaint() {
        if (width == 0 || height == 0) {
            return;
        }
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(lineStrokeWidth);
        DashPathEffect defaultPathEffect = new DashPathEffect(new float[]{20, 10}, 0);
        linePaint.setPathEffect(pathEffect == null ? defaultPathEffect : pathEffect);

    }

//=--------------------------------------------------------------------------------------------------------------------------------------------

    public CompareView setRaduis(float raduis) {
        this.raduis = getAutoHeightSize(raduis);
        return this;
    }

    public CompareView setProgressHeight(float progressHeight) {
        this.progressHeight = getAutoHeightSize(progressHeight);
        return this;
    }

    public CompareView setProgressStrokeWidth(float progressStrokeWidth) {
        this.progressStrokeWidth = getAutoHeightSize(progressStrokeWidth);
        return this;
    }

    public CompareView setCircleStorkeWidth(float circleStorkeWidth) {
        this.circleStorkeWidth = getAutoHeightSize(circleStorkeWidth);
        return this;
    }

    public CompareView setLineStrokeWidth(float lineStrokeWidth) {
        this.lineStrokeWidth = getAutoHeightSize(lineStrokeWidth);
        return this;
    }

    public CompareView setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public CompareView setMoreDataColor(int moreDataColor) {
        this.moreDataColor = moreDataColor;
        return this;
    }

    public CompareView setLineColor(int lineColor) {
        this.lineColor = lineColor;
        return this;
    }

    public CompareView setPathEffect(PathEffect pathEffect) {
        this.pathEffect = pathEffect;
        return this;
    }

    public CompareView setMin(int min) {
        this.min = min;
        return this;
    }

    public CompareView setMax(int max) {
        this.max = max;
        return this;
    }

    public CompareView setOrigin(float origin) {
        this.origin = origin;
        return this;
    }

    public CompareView setOther(float other) {
        this.other = other;
        return this;
    }

    public float getOrigin() {
        return origin;
    }

    public float getOther() {
        return other;
    }

    public CompareView setImgResID(@DrawableRes int imgResID) {
        this.imgResID = imgResID;
        return this;
    }

    public CompareView setLessDataColor(int lessDataColor) {
        this.lessDataColor = lessDataColor;
        return this;
    }

    public CompareView setLessBorderColor(int lessBorderColor) {
        this.lessBorderColor = lessBorderColor;
        return this;
    }

    public CompareView setMoreBorderColor(int moreBorderColor) {
        this.moreBorderColor = moreBorderColor;
        return this;
    }

    public void commit() {
        checkBorderColor();
        checkMinAndMax();
        invalidate();
    }

    private void checkMinAndMax() {
        if (min > max) {
            throw new RuntimeException();
        }
        if (origin > max) {
            origin = max;
        }
        if (origin < min) {
            origin = min;
        }
        if (other > max) {
            other = max;
        }
        if (other < min) {
            other = min;
        }
    }
}
