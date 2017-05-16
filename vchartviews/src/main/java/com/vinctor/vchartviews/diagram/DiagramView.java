package com.vinctor.vchartviews.diagram;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.SoundEffectConstants;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vinctor.vchartviews.R;
import com.zhy.autolayout.utils.AutoUtils;


/**
 * Created by Vinctor on 2017/4/18.
 */

public class DiagramView extends LinearLayout implements Checkable {
    private Context context;
    private DiagramTagView view;
    private TextView textView;
    private LayoutParams viewLp;
    private LayoutParams textViewLp;


    private int diagramSize = 20;
    private int textSize = 24;
    private int textMargin = 14;
    private String text = "";

    private int diagramBorderWidth = 3;

    private int diagramBorderColorSelected = Color.BLUE;
    private int diagramBorderColorUnSelected;

    private int diagramInnerColorUnSelected = Color.YELLOW;
    private int diagramInnerColorSelected = Color.GRAY;

    private int textColorUnselected = Color.DKGRAY;
    private int textColorselected = Color.BLUE;
    private int textColorDefault = 0xff9b9b9b;

    public DiagramView setDiagramBorderWidth(int diagramBorderWidth) {
        if (diagramBorderWidth == -1) {
            return this;
        }
        this.diagramBorderWidth = AutoUtils.getPercentWidthSize(diagramBorderWidth);
        return this;
    }

    public DiagramView setDiagramBorderColorSelected(int diagramBorderColorSelected) {
        this.diagramBorderColorSelected = diagramBorderColorSelected;
        return this;
    }

    public DiagramView setDiagramBorderColorUnSelected(int diagramBorderColorUnSelected) {
        this.diagramBorderColorUnSelected = diagramBorderColorUnSelected;
        return this;
    }

    public DiagramView setDiagramInnerColorUnSelected(int diagramInnerColorUnSelected) {
        this.diagramInnerColorUnSelected = diagramInnerColorUnSelected;
        return this;
    }

    public DiagramView setDiagramInnerColorSelected(int diagramInnerColorSelected) {
        this.diagramInnerColorSelected = diagramInnerColorSelected;
        return this;
    }

    public DiagramView setDiagramSize(int diagramSize) {
        this.diagramSize = AutoUtils.getPercentWidthSize(diagramSize);
        return this;
    }

    public DiagramView setTextColorselected(int textColorselected) {
        if (textColorselected == 0) {
            this.textColorselected = textColorDefault;
            return this;
        }
        this.textColorselected = textColorselected;
        return this;
    }

    public DiagramView setTextColorUnselected(int textColorUnselected) {
        if (textColorUnselected == 0) {
            this.textColorUnselected = textColorDefault;
            return this;
        }
        this.textColorUnselected = textColorUnselected;
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
        setClickable(true);
        this.context = context;
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        view = new DiagramTagView(context);
        viewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView = new TextView(context);
        textViewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DiagramView);

            diagramSize = ta.getDimensionPixelSize(R.styleable.DiagramView_tagViewSize, 10);
            diagramBorderWidth = ta.getDimensionPixelOffset(R.styleable.DiagramView_tagViewborderWidth, 3);

            diagramInnerColorSelected = ta.getColor(R.styleable.DiagramView_tagViewInnerColorSelected, diagramInnerColorSelected);
            diagramBorderColorSelected = ta.getColor(R.styleable.DiagramView_tagViewBorderColorSelected, Color.BLACK);
            diagramInnerColorUnSelected = ta.getColor(R.styleable.DiagramView_tagViewInnerColorUnSelected, diagramInnerColorUnSelected);
            diagramBorderColorUnSelected = ta.getColor(R.styleable.DiagramView_tagViewBorderColorUnSelected, Color.BLACK);

            textColorDefault = ta.getColor(R.styleable.DiagramView_contentColor, textColorDefault);
            textMargin = ta.getDimensionPixelSize(R.styleable.DiagramView_contentMargin, 10);
            textSize = ta.getDimensionPixelSize(R.styleable.DiagramView_contentSize, 12);
            text = ta.getString(R.styleable.DiagramView_content);
            this.setDiagramBorderWidth(diagramBorderWidth)
                    .setDiagramSize(diagramSize)
                    .setTextColorUnselected(textColorDefault)
                    .setTextColorselected(textColorDefault)
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
        view
                .setInnerColor(diagramInnerColorUnSelected)
                .setBorderColor(diagramBorderColorUnSelected)
                .setBorderWidth(diagramBorderWidth)
                .commit();
        viewLp.width = diagramSize;
        viewLp.height = diagramSize;

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setTextColor(textColorUnselected);
        textViewLp.leftMargin = textMargin;
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public String getText() {
        return textView.getText().toString();
    }

    boolean isChecked = false;
    OnCheckChangedListener onCheckChangedListener;
    private DiagramFlowLayout flowLayout;

    public DiagramView setOnCheckChangedListener(OnCheckChangedListener onCheckChangedListener) {
        this.onCheckChangedListener = onCheckChangedListener;
        return this;
    }

    @Override
    public boolean performClick() {
        toggle();

        final boolean handled = super.performClick();
        if (!handled) {
            // View only makes a sound effect if the onClickListener was
            // called, so we'll need to make one here instead.
            playSoundEffect(SoundEffectConstants.CLICK);
        }

        return handled;
    }

    @Override
    public void setChecked(boolean checked) {
        if (isChecked == checked) {
            return;
        }
        isChecked = checked;
        if (onCheckChangedListener != null) {
            onCheckChangedListener.onChanged(isChecked);
        }
        if (checked) {
            view.setInnerColor(diagramInnerColorSelected)
                    .setBorderColor(diagramBorderColorSelected)
                    .commit();
            textView.setTextColor(textColorselected);
        } else {
            view.setInnerColor(diagramInnerColorUnSelected)
                    .setBorderColor(diagramBorderColorUnSelected)
                    .commit();
            textView.setTextColor(textColorUnselected);
        }
        if (isChecked) {//通知其他view
            if (flowLayout == null) {
                ViewGroup parent = (ViewGroup) getParent();
                if (parent instanceof DiagramFlowLayout) {
                    flowLayout = (DiagramFlowLayout) parent;
                } else return;
            }
            flowLayout.notifyisCheck(this);
        } else {
            flowLayout.notifyisUnCheck(this);
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }


    public interface OnCheckChangedListener {
        void onChanged(boolean isCheked);
    }
}
