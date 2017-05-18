package com.vinctor.vchartviews.sector;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.vinctor.vchartviews.AutoView;

/**
 * Created by Vinctor on 2017/5/18.
 */

public class SectorView extends AutoView {
    private Context context;

    private float availableWidth;
    private float availableHeight;
    private float availableLeft;
    private float availableTop;
    private float availableRight;
    private float availableBottom;
    private float centerX;
    private float centerY;

    private Paint mainPaint=new Paint(Paint.ANTI_ALIAS_FLAG);


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
}
