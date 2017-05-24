package com.vinctor.vchartviews.sector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.vinctor.vchartviews.AutoView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Vinctor on 2017/5/18.
 */

public class SectorView extends AutoView {
    private Context context;

    private float borderWidth = 5;
    private float availableWidth;
    private float availableHeight;
    private float availableLeft;
    private float availableTop;
    private float availableRight;
    private float availableBottom;
    private float centerX;
    private float centerY;
    private float height;
    private float width;
    private float radius;
    private RectF outRecF;
    private RectF borderRecF;

    private List<SectorData> list = new ArrayList<>();

    private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint descriptionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint descriptionTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float descriptionTextSize = 15;
    private float descriptionBorderWidth = 5;
    private OnShowDescriptionLinstener onShowDescriptionLinstener;

    public void commit() {
        compute();
        invalidate();
    }

    public SectorView setBorderWidth(float borderWidth) {
        this.borderWidth = getAutoWidthSize(borderWidth);
        return this;
    }

    public SectorView setDescriptionTextSize(float descriptionTextSize) {
        this.descriptionTextSize = getAutoHeightSize(descriptionTextSize);
        return this;
    }

    public SectorView setOnShowDescriptionLinstener(OnShowDescriptionLinstener onShowDescriptionLinstener) {
        this.onShowDescriptionLinstener = onShowDescriptionLinstener;
        return this;
    }

    public SectorView setList(List<SectorData> list) {
        this.list = list;
        return this;
    }

    public SectorView setData(SectorData data) {
        list.clear();
        list.add(data);
        return this;
    }

    public SectorView addData(SectorData data) {
        list.add(data);
        return this;
    }

    public SectorView cleartData(SectorData data) {
        list.clear();
        return this;
    }

    public SectorView addList(List<SectorData> list) {
        list.addAll(list);
        return this;
    }

    public SectorView(Context context) {
        super(context);
        init(context, null);
    }

    public SectorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        if (attrs != null) {
            //to do
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        centerX = w / 2;
        centerY = h / 2;
        compute();
    }

    private void compute() {
        if (width == 0 || height == 0) {
            return;
        }
        checkList();
        setPaint();
        computAvailable();

        float total = 0f;
        for (SectorData data : list) {
            total += data.getNum();
        }
        float peerAngle = 360 / total;
        for (SectorData data : list) {
            data.setAngle(peerAngle * data.getNum());
            data.setPercent((int) (data.getNum() * 100 / total));
        }
    }

    private void checkList() {
        Iterator<SectorData> it = list.iterator();
        while (it.hasNext()) {
            SectorData data = it.next();
            if (data.getNum() <= 0) {
                it.remove();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (width == 0 || height == 0 || outRecF == null) {
            return;
        }
        //绘制扇形
        if (!drawSector(canvas)) {
            return;
        }
        //绘制描述
        drawDescription(canvas);
    }

    private void drawDescription(Canvas canvas) {
        int count = list.size();
        if (count == 0) {
            return;
        }

        SectorData leftData = list.get(1);

        //left
        float leftLeft = descriptionOuterMargin;
        float leftTop = centerY - radius + descriptionTextSize;
        float leftRight = leftLeft + peerDescriptionWidth;
        float leftBottom = leftTop + descriptionHeight;
        //rect
        RectF leftRecf = new RectF(leftLeft, leftTop, leftRight, leftBottom);
        descriptionPaint.setStyle(Paint.Style.FILL);
        descriptionPaint.setColor(leftData.getSoildColor());
        canvas.drawRoundRect(leftRecf, 5f, 5f, descriptionPaint);
        descriptionPaint.setStyle(Paint.Style.STROKE);
        descriptionPaint.setColor(leftData.getBorderColor());
        canvas.drawRoundRect(leftRecf, 5f, 5f, descriptionPaint);
        //text
        String leftPercentString = leftData.getPercent() + "%";
        String leftDesString = leftData.getNum() + "";
        if (onShowDescriptionLinstener != null) {
            leftDesString = onShowDescriptionLinstener.onShowDes(leftData.getNum());
        }
        float leftPercnetWidth = descriptionTextPaint.measureText(leftPercentString);
        float leftDesWidth = descriptionTextPaint.measureText(leftDesString);
        descriptionTextPaint.setColor(leftData.getDescriptionTextColor());
        canvas.drawText(leftPercentString, (leftLeft + leftRight) / 2 - leftPercnetWidth / 2, leftTop + descriptionPaddingV + descriptionTextSize, descriptionTextPaint);
        canvas.drawText(leftDesString, (leftLeft + leftRight) / 2 - leftDesWidth / 2, leftTop + descriptionPaddingV + descriptionTextSize * 2 + descriptionTextMargin, descriptionTextPaint);


        //right
        SectorData rightData = list.get(0);
        float rightLeft = centerX + radius + descriptionOuterMargin;
        float rightTop = centerY - descriptionHeight;
        float rightRight = rightLeft + peerDescriptionWidth;
        float rightBottom = rightTop + descriptionHeight;
        RectF rightRecf = new RectF(rightLeft, rightTop, rightRight, rightBottom);
        descriptionPaint.setStyle(Paint.Style.FILL);
        descriptionPaint.setColor(rightData.getSoildColor());
        canvas.drawRoundRect(rightRecf, 5f, 5f, descriptionPaint);
        descriptionPaint.setStyle(Paint.Style.STROKE);
        descriptionPaint.setColor(rightData.getBorderColor());
        canvas.drawRoundRect(rightRecf, 5f, 5f, descriptionPaint);
        //text
        String percentString = rightData.getPercent() + "%";
        String desString = rightData.getNum() + "";
        if (onShowDescriptionLinstener != null) {
            desString = onShowDescriptionLinstener.onShowDes(rightData.getNum());
        }
        float percnetWidth = descriptionTextPaint.measureText(percentString);
        float desWidth = descriptionTextPaint.measureText(desString);
        descriptionTextPaint.setColor(rightData.getDescriptionTextColor());
        canvas.drawText(percentString, (rightLeft + rightRight) / 2 - percnetWidth / 2, rightTop + descriptionPaddingV + descriptionTextSize, descriptionTextPaint);
        canvas.drawText(desString, (rightLeft + rightRight) / 2 - desWidth / 2, rightTop + descriptionPaddingV + descriptionTextSize * 2 + descriptionTextMargin, descriptionTextPaint);

//            centerAngle += data.getAngle() / 2;
//
//            float currentX = getPointX(centerAngle, radius);
//            float currentY = getPointY(centerAngle, radius);
//
//            int qr = getQr(centerAngle);
//            switch (qr) {
//                case 1:
//                case 2:
//                    Path path = new Path();
//                    path.moveTo(currentX + descriptionOuterMargin + descriptionJiao, top);
//                    path.lineTo(currentX + peerDescriptionWidth, top);
//                    path.lineTo(currentX + peerDescriptionWidth, top + descriptionHeight - descriptionJiao);
//                    path.lineTo(currentX + descriptionOuterMargin + descriptionJiao * 2, top + descriptionHeight - descriptionJiao);
//                    path.lineTo(currentX + descriptionOuterMargin, top + descriptionHeight);
//                    path.lineTo(currentX + descriptionOuterMargin + descriptionJiao, top + descriptionJiao);
//                    path.close();
//                    descriptionPaint.setStyle(Paint.Style.FILL);
//                    descriptionPaint.setColor(data.getSoildColor());
//                    canvas.drawPath(path, descriptionPaint);
//                    descriptionPaint.setStyle(Paint.Style.STROKE);
//                    descriptionPaint.setColor(data.getBorderColor());
//                    canvas.drawPath(path, descriptionPaint);
//                    break;
//                case 3:
//                case 4:
//                    break;
//            }

//            canvas.drawText("------", currentX, currentY, descriptionPaint);

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

    private boolean drawSector(Canvas canvas) {
        int count = list.size();
        if (count == 0) {
            return false;
        }
        float startAngle = 270f;
        for (int i = count - 1; i >= 0; i--) {
            SectorData data = list.get(i);
            startAngle = startAngle - data.getAngle();
            float sweepAngle = data.getAngle();

            mainPaint.setStyle(Paint.Style.FILL);
            mainPaint.setColor(data.getSoildColor());
            canvas.drawArc(outRecF, startAngle, sweepAngle, true, mainPaint);

            mainPaint.setStyle(Paint.Style.STROKE);
            mainPaint.setColor(data.getBorderColor());
            mainPaint.setStrokeWidth(borderWidth);
            canvas.drawArc(borderRecF, startAngle, sweepAngle, true, mainPaint);
        }
        return true;
    }

    private void setPaint() {
        descriptionTextPaint.setTextSize(descriptionTextSize);

        descriptionPaint.setStrokeCap(Paint.Cap.ROUND);
        descriptionPaint.setStrokeJoin(Paint.Join.ROUND);
        descriptionPaint.setStrokeWidth(borderWidth);
    }

    private float descriptionTextMargin;
    private float descriptionPaddingV;
    private float descriptionPaddingH;
    private float descriptionOuterMargin;
    private float descriptionJiao;
    private float descriptionHeight;
    private float peerDescriptionWidth;

    private void computAvailable() {
        descriptionTextMargin = descriptionTextSize / 2;
        descriptionPaddingV = descriptionTextSize / 2;
        descriptionPaddingH = 15;
        descriptionOuterMargin = 20;
        descriptionJiao = 0;//---------------------------------
        descriptionHeight = descriptionBorderWidth * 2 + descriptionPaddingV * 2 + descriptionTextSize * 2 + descriptionTextMargin + descriptionJiao;

        //V
        availableTop = 0;
        availableBottom = height;
        availableHeight = availableBottom - availableTop;
        //H
        int max = 0;
        for (SectorData data : list) {
            int num = data.getNum();
            int length = (num + "").length();
            if (length > (max + "").length()) {
                max = num;
            }
        }
        String description = max + "";
        if (onShowDescriptionLinstener != null) {
            description = onShowDescriptionLinstener.onShowDes(max);
        }
        peerDescriptionWidth = descriptionJiao + descriptionBorderWidth * 2 + descriptionPaddingH * 2 + descriptionTextPaint.measureText(description);
        availableLeft = peerDescriptionWidth + descriptionOuterMargin * 2;
        availableRight = width - peerDescriptionWidth - descriptionOuterMargin * 2;
        availableWidth = availableRight - availableLeft;

        //radius
        radius = Math.min(availableHeight, availableWidth) / 2;


        //recf
        outRecF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        borderRecF = new RectF(outRecF.left + borderWidth / 2, outRecF.top + borderWidth / 2, outRecF.right - borderWidth / 2, outRecF.bottom - borderWidth / 2);
    }

    public interface OnShowDescriptionLinstener {
        String onShowDes(int num);
    }
}

