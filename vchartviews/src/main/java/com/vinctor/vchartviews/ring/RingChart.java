package com.vinctor.vchartviews.ring;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Vinctor on 2017/4/11.
 */

public class RingChart extends View {
    boolean isDebug = false;

    private final int LEFT = 0;
    private final int RIGHT = 1;
    private final int Top = 2;

    private float width;
    private float height;
    private float centerX;
    private float centerY;
    private float maxRadius;

    private float availableTop;
    private float availableBottom;
    private float availableRight;
    private float availableLeft;
    private float availableWidth;
    private float availableHeight;

    private RingData data;
    private float maxRingWidth = 0f;
    private float minRingWidth = 0f;
    private boolean isShowTag = true;
    private int tagTextSize = 20;
    private int tagRectColor = 0xff3A8EBD;
    private int tagTextColor = 0xff939EA0;
    private int tagStrokeWidth = 1;
    private float tagPadding = 5;
    private float tagMargin = 5;

    private Paint ringPaint = new Paint();
    private Paint tagpaint = new Paint();

    private float tagRectHeight;
    private float tagMaxRectWidth;
    private float maxUserSetRingWidth = 0f;
    private float minUserSetRingWidth = 0F;


    public RingChart setShowTag(boolean showTag) {
        isShowTag = showTag;
        return this;
    }

    public boolean isShowTag() {
        return isShowTag;
    }

    public RingChart setMaxRingWidth(float maxRingWidth) {
        this.maxUserSetRingWidth = maxRingWidth;
        return this;
    }

    public RingChart setMinRingWidth(float minRingWidth) {
        this.minUserSetRingWidth = minRingWidth;
        return this;
    }

    public RingChart setData(RingData data) {
        this.data = data;
        return this;
    }

    public void commit() {
        resetAvailable();
        postInvalidate();
    }

    public RingChart(Context context) {
        super(context);
        init(context);
    }

    public RingChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RingChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RingChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(0xfffffff);

        ringPaint.setAntiAlias(true);
        ringPaint.setStyle(Paint.Style.STROKE);

        tagpaint.setAntiAlias(true);
        tagpaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
        centerX = width / 2;
        centerY = height / 2;

        resetAvailable();

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()) {
            return;
        }
        drawRing(canvas);//圆环
    }

    /**
     * 绘制环形
     *
     * @param canvas
     */
    private void drawRing(Canvas canvas) {

        List<Data> list = data.getList();
        int colors[] = data.getColors();
        int ringCount = list.size();
        if (ringCount <= 0) return;

        int intervalCount = ringCount;
        if (ringCount == 1) ringCount = 0;

        int totalNum = 0;
        for (int i = 0; i < ringCount; i++) {
            totalNum += list.get(i).getNum();
        }

        float intervalAngle = 2.0f;
        float peerAngle = (360 - intervalAngle * intervalCount) / totalNum;
        float startAngle = -90 + intervalAngle / 2;

        List<Data> sortNums = getSortNums(list);
        float peerStrokeWidth = (maxRingWidth - minRingWidth) / ringCount;


        for (int i = 0; i < ringCount; i++) {

            float currentAngle = peerAngle * list.get(i).getNum();

            int sortIndex = sortNums.indexOf(list.get(i));
            float strikeWidth = maxRingWidth - sortIndex * peerStrokeWidth;
            float currentRaduis = maxRadius - strikeWidth / 2;
            RectF outRectF = new RectF(centerX - currentRaduis, centerY - currentRaduis, centerX + currentRaduis, centerY + currentRaduis);
            ringPaint.setColor(colors[i]);
            ringPaint.setStrokeWidth(strikeWidth);
            canvas.drawArc(outRectF, startAngle, currentAngle, false, ringPaint);

            if (isShowTag) {
                prerawTag(canvas, startAngle, currentAngle, list.get(i).getTag());
            }

            startAngle += currentAngle + intervalAngle;
        }
    }

    /**
     * 标签
     *
     * @param canvas
     * @param startAngle
     * @param currentAngle
     * @param tag
     */
    private void prerawTag(Canvas canvas, float startAngle, float currentAngle, String tag) {
        float centerAngle = startAngle + currentAngle / 2 + 90;
        float centerangleX = getPointX(centerAngle, maxRadius);
        float centerangleY = getPointY(centerAngle, maxRadius);
        float textWidth = tagpaint.measureText(tag);
        float currentWidth = getTagWidth(textWidth);

        if (isDebug)
            canvas.drawCircle(centerangleX, centerangleY, 5, tagpaint);

        int directionH = -1;
        int directionV = -1;
        float tagLeft = 0;
        float margin = 20;
        int qr = getQr2(centerAngle);
        switch (qr) {
            case 11:
                directionV = Top;
                tagLeft = centerangleX + margin;
            case 1:
            case 2:
            case 22:
                directionH = RIGHT;
                tagLeft = centerangleX + margin;
                break;

            case 44:
                directionV = Top;
                tagLeft = centerangleX - currentWidth - margin;
            case 3:
            case 4:
            case 33:
                directionH = LEFT;
                tagLeft = centerangleX - currentWidth - margin;
                break;
        }

        float textX = 0;
        //绘制
        Path path = new Path();
        if (directionH == LEFT && directionV != Top) {
            textX = 2 * tagPadding;
            path.moveTo(0, 0);
            path.lineTo(currentWidth, 0);//-
            path.lineTo(getTagRectWidth(textWidth), tagRectHeight / 4);///
            path.lineTo(getTagRectWidth(textWidth), tagRectHeight);//|
            path.lineTo(0, tagRectHeight);//-
            path.close();//-
        }
        if (directionH == RIGHT && directionV != Top) {
            float sanJiaoKuan = currentWidth - getTagRectWidth(textWidth);
            textX = 2 * tagPadding + sanJiaoKuan;
            path.moveTo(0, 0);
            path.lineTo(currentWidth, 0);//-
            path.lineTo(currentWidth, tagRectHeight);///
            path.lineTo(sanJiaoKuan, tagRectHeight);
            path.lineTo(sanJiaoKuan, tagRectHeight / 4);
            path.close();
        }

        if (directionH == LEFT && directionV == Top) {
            textX = 2 * tagPadding;
            path.moveTo(0, -tagRectHeight);
            path.lineTo(getTagRectWidth(textWidth), -tagRectHeight);//-
            path.lineTo(getTagRectWidth(textWidth), -tagRectHeight / 4);//|
            path.lineTo(currentWidth, 0);//\
            path.lineTo(0, 0);//-
            path.close();//-
        }
        if (directionH == RIGHT && directionV == Top) {
            float sanJiaoKuan = currentWidth - getTagRectWidth(textWidth);
            textX = 2 * tagPadding + sanJiaoKuan;
            path.moveTo(sanJiaoKuan, -tagRectHeight);
            path.lineTo(currentWidth, -tagRectHeight);//-
            path.lineTo(currentWidth, 0);//|
            path.lineTo(0, 0);
            path.lineTo(sanJiaoKuan, -tagRectHeight / 4);
            path.close();
        }

        canvas.translate(tagLeft, centerangleY);

        tagpaint.setStyle(Paint.Style.STROKE);
        tagpaint.setColor(tagRectColor);
        canvas.drawPath(path, tagpaint);

        tagpaint.setStyle(Paint.Style.FILL);
        tagpaint.setColor(tagTextColor);
        if (directionV != Top) {
            canvas.drawText(tag, textX, tagRectHeight / 2 + tagTextSize / 2, tagpaint);
        } else
            canvas.drawText(tag, textX, -tagRectHeight / 2 + tagTextSize / 2, tagpaint);


        canvas.translate(-tagLeft, -centerangleY);
    }

    /**
     * 若以圆心为原点，返回该角度顶点的所在象限
     *
     * @param angle
     * @return
     */
    private int getQr2(float angle) {

        if (angle <= 30) return 11; //原点右侧
        if ((360 - angle) < 30) return 44;//原点右侧
        float another = 180 - angle;
        if (another < 30 && another > 0) return 22;//180度右侧
        if (another > -30 && another < 0) return 33;//180度右侧

        if (angle >= 0 && angle <= 90) return 1;
        if (angle > 90 && angle <= 180) return 2;
        if (angle > 180 && angle <= 270) return 3;
        if (angle > 270 && angle <= 360) return 4;
        return -1;
    }

    /**
     * 若以圆心为原点，返回该角度顶点的所在象限
     *
     * @param angle
     * @return
     */
    private int getQr(float angle) {
        if (angle >= 0 && angle <= 90) return 1;
        if (angle > 90 && angle <= 180) return 2;
        if (angle > 180 && angle <= 270) return 3;
        if (angle > 270 && angle <= 360) return 4;
        return -1;
    }

    /**
     * 返回多边形顶点X坐标
     *
     * @param angle
     * @return
     */
    private float getPointX(float angle, float radius) {
        float newAngle = getNewAngle(angle);
        float res = 0;
        float width = (float) (radius * Math.cos(newAngle / 180 * Math.PI));
        int qr = getQr(angle);
        float offset = this.width / 2 - maxRadius;
        switch (qr) {
            case 1:
            case 2:
                res = maxRadius + width;
                break;
            case 3:
            case 4:
                res = maxRadius - width;
                break;
            default:
                break;
        }
        res += offset;
        return res;
    }

    /**
     * 返回多边形顶点Y坐标
     */
    private float getPointY(float angle, float radius) {
        float newAngle = getNewAngle(angle);
        float height = (float) (radius * Math.sin(newAngle / 180 * Math.PI));
        float res = 0;
        int qr = getQr(angle);
        float offset = this.height / 2 - maxRadius;
        switch (qr) {
            case 1:
            case 4:
                res = maxRadius - height + offset;
                break;
            case 2:
            case 3:
                res = maxRadius + height + offset;
                break;
            default:
                break;
        }
        return res;
    }

    /**
     * 得到需要计算的角度
     *
     * @param angle 角度，例：30.60.90
     * @return res
     */
    private float getNewAngle(float angle) {
        float res = angle;
        if (angle >= 0 && angle <= 90) {
            res = 90 - angle;
        } else if (angle > 90 && angle <= 180) {
            res = angle - 90;
        } else if (angle > 180 && angle <= 270) {
            res = 270 - angle;
        } else if (angle > 270 && angle <= 360) {
            res = angle - 270;
        }
        return res;
    }

    //排序
    private List<Data> getSortNums(List<Data> list) {
        List<Data> sortList = new ArrayList<>();
        sortList.addAll(list);
        Collections.copy(sortList, list);
        Collections.sort(sortList, new RingDataSort());
        return sortList;
    }

    private void resetAvailable() {
        maxRingWidth = maxUserSetRingWidth;
        minRingWidth = minUserSetRingWidth;
        availableLeft = 0;
        availableTop = 0;
        availableRight = width;
        availableBottom = height;
        //tag
        if (isShowTag) {
            tagpaint.setTextSize(tagTextSize);
            tagpaint.setColor(tagTextColor);
            tagpaint.setStrokeWidth(tagStrokeWidth);

            tagRectHeight = tagTextSize + 2 * tagPadding + 2 * tagMargin;

            List<Data> list = data.getList();
            int count = list.size();
            float tagMaxWidth = 0;
            for (int i = 0; i < count; i++) {
                float current = tagpaint.measureText(list.get(i).getTag());
                if (current >= tagMaxWidth) tagMaxWidth = current;
            }
            tagMaxRectWidth = getTagWidth(tagMaxWidth) + tagMargin * 2;

            //reset
            availableLeft = tagMaxRectWidth;
            availableRight = width - tagMaxRectWidth;
            availableTop = tagRectHeight;
            availableBottom = height - tagRectHeight;
        }

        availableHeight = availableBottom - availableTop;
        availableWidth = availableRight - availableLeft;

        maxRadius = Math.min(availableHeight, availableWidth) / 2;
        if (maxRingWidth == 0f) {
            maxRingWidth = maxRadius / 2;
        }
        if (minRingWidth == 0f) {
            minRingWidth = maxRingWidth / 3;
        }
    }

    private float getTagWidth(float tagWidth) {
        return (float) (getTagRectWidth(tagWidth) + tagRectHeight / 4 * 1.732);
    }

    private float getTagRectWidth(float tagWidth) {
        return tagWidth + tagPadding * 4;
    }
}
