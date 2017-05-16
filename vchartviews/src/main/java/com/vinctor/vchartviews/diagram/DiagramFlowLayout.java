package com.vinctor.vchartviews.diagram;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.vinctor.vchartviews.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vinctor on 2017/4/19.
 */

public class DiagramFlowLayout extends FlowLayout {

    public List<DiagramData> list = new ArrayList<>();
    private Context context;

    private int diagramColor = Color.GRAY;
    private int diagramSize = 20;
    private int textColorDefault = Color.DKGRAY;
    private int textSize = 24;
    private int textMargin = 14;
    private int spaceHorizontal = diagramSize;
    private int spaceVertical = diagramSize;

    private String text = "";
    private int diagramBorderWidth;

    public DiagramFlowLayout setDiagramColor(int diagramColor) {
        this.diagramColor = diagramColor;
        return this;
    }

    public DiagramFlowLayout setDiagramSize(int diagramSize) {
        this.diagramSize = AutoUtils.getPercentWidthSize(diagramSize);
        return this;
    }

    public DiagramFlowLayout setTextColorDefault(int textColorDefault) {
        this.textColorDefault = textColorDefault;
        return this;
    }

    public DiagramFlowLayout setTextSize(int textSize) {
        this.textSize = AutoUtils.getPercentWidthSize(textSize);
        return this;
    }

    public DiagramFlowLayout setTextMargin(int textMargin) {
        this.textMargin = AutoUtils.getPercentWidthSize(textMargin);
        return this;
    }

    public void commit() {
        changeData();
    }

    public DiagramFlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public DiagramFlowLayout(Context context) {
        this(context, null);
    }

    public DiagramFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DiagramView);
        diagramSize = ta.getDimensionPixelSize(R.styleable.DiagramView_tagViewSize, 10);
        diagramBorderWidth = ta.getDimensionPixelOffset(R.styleable.DiagramView_tagViewborderWidth, 3);


        textMargin = ta.getDimensionPixelSize(R.styleable.DiagramView_contentMargin, textMargin);
        textSize = ta.getDimensionPixelSize(R.styleable.DiagramView_contentSize, textSize);
        textColorDefault = ta.getColor(R.styleable.DiagramView_contentColor, Color.GRAY);
        ta.recycle();


        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.DiagramViewFlowLayout);
        spaceHorizontal = t.getDimensionPixelSize(R.styleable.DiagramViewFlowLayout_spaceHorizontal, spaceHorizontal);
        spaceVertical = t.getDimensionPixelSize(R.styleable.DiagramViewFlowLayout_spaceVertical, spaceVertical);
        t.recycle();
    }

    public void setList(List<DiagramData> list) {
        this.list = list;
        changeData();
    }

    private void changeData() {
        int count = list.size();
        if (count == 0) {
            return;
        }
        for (int i = 0; i < count; i++) {
            DiagramData data = list.get(i);
            DiagramView diagramView = new DiagramView(context);
            diagramView
                    .setDiagramInnerColorSelected(data.diagramInnerColorselected)
                    .setDiagramInnerColorUnSelected(data.diagramInnerColorUnselected)
                    .setDiagramBorderColorUnSelected(data.diagramBorderColorUnselected)
                    .setDiagramBorderColorSelected(data.diagramBorderColorSelected)
                    .setDiagramBorderWidth(data.diagramBorderWidth)
                    .setTextColorselected(data.textColorSelected)
                    .setTextColorUnselected(data.textColorUnselected)
                    .setDiagramSize(diagramSize)
                    .setTextMargin(textMargin)
                    .setTextSize(textSize)
                    .commit();
            diagramView.setText(data.text);

            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(
                    0,
                    0,
                    AutoUtils.getPercentWidthSize((int) (spaceHorizontal * 1.5)),
                    AutoUtils.getPercentWidthSize(spaceVertical));
            diagramView.setLayoutParams(lp);
            addView(diagramView);
        }
    }


    OnCheckChangedListener onCheckChangedListener;

    public DiagramFlowLayout setOnCheckChangedListener(OnCheckChangedListener onCheckChangedListener) {
        this.onCheckChangedListener = onCheckChangedListener;
        return this;
    }


    public void clearCheck() {
        setCheck(null);
    }

    public void notifyisCheck(DiagramView diagramView) {
        setCheck(diagramView);
    }

    public void setCheck(DiagramView diagramView) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view instanceof DiagramView) {
                DiagramView current = (DiagramView) view;
                if (diagramView != null && diagramView == current) {
                    if (onCheckChangedListener != null) {
                        onCheckChangedListener.onCheckChangedChange(diagramView, i, true);
                    }
                } else {
                    if (!current.isChecked) {
                        continue;
                    }
                    current.setChecked(false);
                    if (onCheckChangedListener != null) {
                        onCheckChangedListener.onCheckChangedChange(diagramView, i, false);
                    }
                }
            }
        }
    }

    public void notifyisUnCheck(DiagramView diagramView) {
        int childCount = getChildCount();
        int index = -1;
        boolean isCancel = true;

        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view instanceof DiagramView) {
                DiagramView current = (DiagramView) view;
                if (diagramView != null && diagramView == current) {
                    index = i;
                }
                if (current.isChecked) {
                    isCancel = false;
                }
            }
        }
        if (onCheckChangedListener != null && index != -1 && isCancel) {
            onCheckChangedListener.onClearCheck();
        }

    }

//    public interface OnCheckedListener {
//        /**
//         * 当某个子view被选中时调用
//         *
//         * @param diagramView
//         * @param position
//         */
//        void onCheckedChange(DiagramView diagramView, int position);
//    }

    public interface OnCheckChangedListener {
        /**
         * 子view选中状态发生改变时调用
         *
         * @param diagramView
         * @param position
         */
        void onCheckChangedChange(DiagramView diagramView, int position, boolean isCheck);

        /**
         * 点击取消,没有选择任何选项的时候调用
         */
        void onClearCheck();
    }
}
