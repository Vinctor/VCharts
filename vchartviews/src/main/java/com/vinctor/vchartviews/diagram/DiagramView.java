package com.vinctor.vchartviews.diagram;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vinctor.vchartviews.R;
import com.zhy.autolayout.utils.AutoUtils;


/**
 * Created by Vinctor on 2017/4/18.
 */

public class DiagramView extends LinearLayout {
    private Context context;
    private View view;
    private TextView textView;
    private LayoutParams viewLp;
    private LayoutParams textViewLp;

    private int diagramColor = Color.GRAY;
    private int diagramSize = 20;
    private int textColor = Color.DKGRAY;
    private int textSize = 24;
    private int textMargin = 14;
    private String text = "";

    public DiagramView setDiagramColor(int diagramColor) {
        this.diagramColor = diagramColor;
        return this;
    }

    public DiagramView setDiagramSize(int diagramSize) {
        this.diagramSize = AutoUtils.getPercentWidthSize(diagramSize);
        return this;
    }

    public DiagramView setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public DiagramView setTextSize(int textSize) {
        this.textSize = AutoUtils.getPercentWidthSize(textSize);
        return this;
    }

    public DiagramView setTextMargin(int textMargin) {
        this.textMargin = AutoUtils.getPercentWidthSize(textMargin);
        return this;
    }

    public void commit() {
        setViews();
        requestLayout();
    }

    public DiagramView(Context context) {
        super(context);
        init(context, null);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        view = new View(context);
        viewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView = new TextView(context);
        textViewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DiagramView);
            diagramColor = ta.getColor(R.styleable.DiagramView_viewColor, diagramColor);
            diagramSize = ta.getDimensionPixelSize(R.styleable.DiagramView_viewSize, 10);
            textColor = ta.getColor(R.styleable.DiagramView_contentColor, textColor);
            textMargin = ta.getDimensionPixelSize(R.styleable.DiagramView_contentMargin, 10);
            textSize = ta.getDimensionPixelSize(R.styleable.DiagramView_contentSize, 12);
            text = ta.getString(R.styleable.DiagramView_content);
            this.setDiagramColor(diagramColor)
                    .setDiagramSize(diagramSize)
                    .setTextColor(textColor)
                    .setTextMargin(textMargin)
                    .setTextSize(textSize);
            setText(text);
            ta.recycle();
        }
        setViews();
        addView(view, viewLp);
        addView(textView, textViewLp);
    }

    private void setViews() {
        view.setBackgroundColor(diagramColor);
        viewLp.width = diagramSize;
        viewLp.height = diagramSize;

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        textView.setTextColor(textColor);
        textViewLp.leftMargin = textMargin;
    }

    public void setText(String text) {
        textView.setText(text);
    }
}
