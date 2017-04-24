package com.vinctor.vchartviews.radar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.vinctor.vchartviews.AutoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinctor on 2017/4/8.
 */

public class RadarChart extends AutoView {
    private int count = 6;                //数据个数
    private Context context;

    private int centerX;//中心店X坐标
    private int centerY;//中心点Y坐标
    private int height;
    private int width;
    private float maxRadius;//半径
    private int titleTextSize = 30;//标题文字大小
    private int density = 8;
    private Paint mainPaint = new Paint();//雷达底图画笔
    private Paint tagPaint = new Paint();//标记画笔
    private Paint valuePaint = new Paint();//雷达范围画笔
    private Paint tagPaintBack = new Paint();//标记画笔背景
    private Paint titlePaint = new Paint();//标题画笔
    private float tagTextSize = 0f;//标记文字大小
    private List<RadarData> list = new ArrayList<>();
    private String[] titles = new String[]{"012345678", "fad", "d", "adf", "afd", "adf"};
    float peerAngle;
    private float min = 0;
    private float max = 100;
    private float peerDiff;
    private int alpha = 120;
    private float maxTitleWidth;
    private int lineColor = 0xff929292;
    private int titleColor = Color.GRAY;


    private float radarStrokeWidth = 3;

    private float widthAviable;
    private float heightAviable;
    private float leftAviable;
    private float topAviable;
    private float rightAviable;
    private float bottomAviable;

    private float titleHorMargin;
    private int titleVerMargin;


    public RadarChart setRadarStrokeWidth(float radarStrokeWidth) {
        this.radarStrokeWidth = getAutoWidthSize(radarStrokeWidth);
        return this;
    }

    public RadarChart setCount(int count) {
        this.count = count;
        return this;
    }

    public RadarChart setTitleTextSize(int titleTextSize) {
        this.titleTextSize = getAutoHeightSize(titleTextSize);
        return this;
    }

    public RadarChart setTagTextSize(float tagTextSize) {
        this.tagTextSize = getAutoHeightSize(tagTextSize);
        return this;
    }

    public RadarChart setAlpha(int alpha) {
        this.alpha = alpha;
        return this;
    }

    public RadarChart setLineColor(int lineColor) {
        this.lineColor = lineColor;
        return this;
    }

    public RadarChart setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public RadarChart setList(List<RadarData> list) {
        this.list = list;
        return this;
    }

    public RadarChart setData(RadarData data) {
        list.clear();
        list.add(data);
        return this;
    }

    public RadarChart addData(RadarData data) {
        list.add(data);
        return this;
    }

    public RadarChart clearData() {
        list.clear();
        return this;
    }

    public RadarChart setTitles(String[] titles) {
        this.titles = titles;
        if (titles.length != count)
            throw new IllegalArgumentException("titles's lenth must be " + count);
        return this;
    }

    public RadarChart setMin(float min) {
        this.min = min;
        return this;
    }

    public RadarChart setMax(float max) {
        this.max = max;
        return this;
    }

    public RadarChart setDensity(int density) {
        this.density = density;
        commit();
        return this;
    }

    public RadarChart setMinAndMax(float min, float max) {
        this.min = min;
        this.max = max;
        return this;
    }

    public void commit() {
        checkMinAndMax();
        setPaint();
        invalidate();
    }

    private void checkMinAndMax() {
        if (min >= max) {
            throw new IllegalArgumentException("you cannot set max less than max!");
        }
    }

    public RadarChart(Context context) {
        super(context);
        init(context);
    }

    public RadarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RadarChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setBackgroundColor(0xffffffff);


        mainPaint.setAntiAlias(true);
        mainPaint.setStyle(Paint.Style.STROKE);


        tagPaint.setAntiAlias(true);
        tagPaint.setStyle(Paint.Style.FILL);


        tagPaintBack.setAntiAlias(true);
        tagPaintBack.setColor(0xffffffff);
        tagPaintBack.setStyle(Paint.Style.FILL);

        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);


        titlePaint.setAntiAlias(true);

        setPaint();
    }

    private void setPaint() {
        peerDiff = (max - min) / density;

        mainPaint.setColor(lineColor);
        mainPaint.setStrokeWidth(radarStrokeWidth);

        tagPaint.setColor(lineColor);
        tagPaint.setStrokeWidth(3);
        titlePaint.setColor(titleColor);
        titlePaint.setTextSize(titleTextSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        titlePaint.setTextSize(titleTextSize);
        maxTitleWidth = getMaxTitleWidth();

        titleHorMargin = maxTitleWidth / 4;
        titleVerMargin = titleTextSize;

        widthAviable = (w - 2 * maxTitleWidth - 2 * titleHorMargin);
        heightAviable = (h - 2 * titleTextSize - 2 * titleHorMargin - titleVerMargin);
        leftAviable = maxTitleWidth + titleHorMargin;
        topAviable = titleTextSize + titleHorMargin;
        rightAviable = w - maxTitleWidth;
        bottomAviable = h - titleTextSize;
        maxRadius = Math.min(widthAviable, heightAviable) / 2;
        height = h;
        width = w;
        //中心坐标
        centerX = w / 2;
        centerY = h / 2;

        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private float getMaxTitleWidth() {
        float result = 0f;
        for (String s : titles) {
            float temp = titlePaint.measureText(s);
            if (temp > result) result = temp;
        }
        return result * 1.2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        peerAngle = 360 / count;
        if (tagTextSize == 0f) {
            tagTextSize = (int) (maxRadius / (density + 2));
        }
        tagPaint.setTextSize(tagTextSize);
        drawPolygon(canvas);//雷达环形
        drawLines(canvas);//雷达连接线
        drawIconText(canvas);//雷达刻度
        drawRegion(canvas);//绘制范围区
        drawTitle(canvas);
    }

    /**
     * 绘制标题文字
     *
     * @param canvas
     */
    private void drawTitle(Canvas canvas) {
        for (int i = 0; i < count; i++) {

            float titleCurrentWidth = titlePaint.measureText(titles[i]);
            float x = getPointX(i * peerAngle, maxRadius + titleHorMargin);
            float y = getPointY(i * peerAngle, maxRadius + titleHorMargin);

            int quadrant = getQuadrant(i);
            switch (quadrant) {
                case 1:
                    x -= titleHorMargin;
//                    y += titleTextSize / 2;
                    break;
                case 2:
//                    x -= titleHorMargin;
                    y += titleTextSize / 2;
                    break;
                case 3:
                    x -= titleCurrentWidth;
                    y += titleTextSize / 2;
                    break;
                case 4:
                    x -= titleCurrentWidth;
//                    y += titleTextSize / 2;
                    break;
                case 0:
                    x -= titleCurrentWidth / 2;
                    y -= titleTextSize / 2;
                    break;
                case 5:
                    x -= titleCurrentWidth / 2;
                    y += titleTextSize / 2;
                    break;
            }
            canvas.drawText(titles[i], x, y, titlePaint);
        }
    }

    //获取象限
    private int getQuadrant(int i) {
        float angle = 360 / count * i;
        int res = 0;
        if (angle == 0) return 0;
        if (Math.abs(angle - 180) < 30)
            return 5;
        if (angle >= 0 && angle < 90) {
            res = 1;
        } else if (angle >= 90 && angle <= 180) {
            res = 2;
        } else if (angle > 180 && angle <= 270) {
            res = 3;
        } else if (angle > 270 && angle <= 360) {
            res = 4;
        }
        return res;
    }

    /**
     * 雷达环形
     *
     * @param canvas
     */
    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        float r = maxRadius / density;//r是蜘蛛丝之间的间距
        for (int i = 1; i <= density; i++) {//中心点不用绘制
            float curR = r * i;//当前半径
            path.reset();
            for (int j = 0; j < count; j++) {
                float x = getPointX(j * peerAngle, curR);
                float y = getPointY(j * peerAngle, curR);
                if (j == 0) {
                    path.moveTo(x, y);
                } else {
                    //根据半径，计算出蜘蛛丝上每个点的坐标
                    path.lineTo(x, y);
                }
            }
            path.close();//闭合路径
            canvas.drawPath(path, mainPaint);
        }
    }

    /**
     * 绘制直线
     */
    private void drawLines(Canvas canvas) {
        Path path = new Path();
        float r = maxRadius / density;//r是蜘蛛丝之间的间距
        for (int i = 0; i < count; i++) {
            path.reset();
            if (i == 0) {
                path.moveTo(centerX, centerY);
                float x = getPointX(i * peerAngle, r);
                float y = getPointY(i * peerAngle, r);
                path.lineTo(x, y);
                canvas.drawPath(path, mainPaint);
                continue;
            }
            path.moveTo(centerX, centerY);
            float x = getPointX(i * peerAngle, maxRadius);
            float y = getPointY(i * peerAngle, maxRadius);
            path.lineTo(x, y);
            canvas.drawPath(path, mainPaint);
        }
    }

    /**
     * 绘制刻度
     *
     * @param canvas
     */
    private void drawIconText(Canvas canvas) {
        float factor = 1.2f;
        float r = maxRadius / density;//r是蜘蛛丝之间的间距
        float textWidth = 0;
        for (int i = 0; i <= density; i++) {
            String tag = (int) (min + i * peerDiff) + "";
            float temp = tagPaint.measureText(tag);
            if (temp > textWidth) textWidth = temp;
        }
        for (int i = 1; i <= density; i++) {//中心点不用绘制
            float curR = r * i;//当前半径
            float x = getPointX(0, curR);
            float y = getPointY(0, curR);
            String tag = (int) (min + i * peerDiff) + "";
            float left = x - textWidth / 2 * factor;
            float top = y - tagTextSize;
            float right = x + textWidth / 2 * factor;
            float bottom = y + tagTextSize;
            RectF rect = new RectF(left, top, right, bottom);

            canvas.drawRoundRect(rect, (right - left) / 2, (bottom - top) / 2, tagPaintBack);
            float currentWidth = tagPaint.measureText(tag);
            canvas.drawText(tag, x - currentWidth / 2, y + tagTextSize / 2, tagPaint);
        }
    }

    /**
     * 绘制区域
     *
     * @param canvas
     */
    private void drawRegion(Canvas canvas) {
        Path path = new Path();
        for (RadarData item : list) {
            float[] data = item.getDatas();
            if (data.length != count) {
                throw new IllegalArgumentException("the RadarData's lengh muse be" + count + "!");
            }
            path.reset();

            for (int i = 0; i < count; i++) {
                float x;
                float y;
                if (data[i] <= min) {
                    x = centerX;
                    y = centerY;
                } else {
                    float currentRadius = maxRadius * (data[i] - min) / (max - min);
                    if (data[i] >= max)
                        currentRadius = maxRadius;
                    x = getPointX(peerAngle * i, currentRadius);
                    y = getPointY(peerAngle * i, currentRadius);
                }
                if (i == 0) path.moveTo(x, y);
                else path.lineTo(x, y);
            }
            valuePaint.setColor(item.getColor());
            valuePaint.setAlpha(alpha);
            canvas.drawPath(path, valuePaint);
        }
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

    /**
     * 若以圆心为原点，返回该角度顶点的所在象限
     *
     * @param angle
     * @return
     */
    private int getQr(float angle) {
        int res = 0;
        if (angle >= 0 && angle <= 90) {
            res = 1;
        } else if (angle > 90 && angle <= 180) {
            res = 2;
        } else if (angle > 180 && angle <= 270) {
            res = 3;
        } else if (angle > 270 && angle <= 360) {
            res = 4;
        }
        return res;
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
        switch (qr) {
            case 1:
            case 2:
                res = centerX + width;
                break;
            case 3:
            case 4:
                res = centerX - width;
                break;
            default:
                break;
        }
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
                res = centerY - height;
                break;
            case 2:
            case 3:
                res = centerY + height;
                break;
            default:
                break;
        }
        return res;
    }
}
