package com.vinctor.vchartviews.diagram;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
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
    private int textColor = Color.DKGRAY;
    private int textSize = 24;
    private int textMargin = 14;
    private int spaceHorizontal = diagramSize;
    private int spaceVertical = diagramSize;

    private String text = "";

    public DiagramFlowLayout setDiagramColor(int diagramColor) {
        this.diagramColor = diagramColor;
        return this;
    }

    public DiagramFlowLayout setDiagramSize(int diagramSize) {
        this.diagramSize = AutoUtils.getPercentWidthSize(diagramSize);
        return this;
    }

    public DiagramFlowLayout setTextColor(int textColor) {
        this.textColor = textColor;
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
        diagramColor = ta.getColor(R.styleable.DiagramView_viewColor, diagramColor);
        diagramSize = ta.getDimensionPixelSize(R.styleable.DiagramView_viewSize, diagramSize);
        textMargin = ta.getDimensionPixelSize(R.styleable.DiagramView_contentMargin, textMargin);
        textSize = ta.getDimensionPixelSize(R.styleable.DiagramView_contentSize, textSize);
        textColor = ta.getColor(R.styleable.DiagramView_contentColor, Color.GRAY);
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
            diagramView.setDiagramColor(data.diagramColor)
                    .setDiagramSize(diagramSize)
                    .setTextColor(textColor)
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
}
