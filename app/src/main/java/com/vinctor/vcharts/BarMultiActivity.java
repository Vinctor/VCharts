package com.vinctor.vcharts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.vinctor.vchartviews.bar.BarCharMulti;
import com.vinctor.vchartviews.bar.multi.BarDataMulti;
import com.vinctor.vchartviews.bar.multi.SingleData;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BarMultiActivity extends BaseActivity {

    private BarCharMulti multiBar;

    public static void startActivity(Context context) {
        Intent starter = new Intent(context, BarMultiActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_multi);
        ButterKnife.bind(this);

        multiBar = (BarCharMulti) findViewById(R.id.bar_multi);
        setStyle();
        List<SingleData> singles = new ArrayList<>();
        singles.add(new SingleData(90, Color.BLUE));
        singles.add(new SingleData(80, Color.RED));
        singles.add(new SingleData(40, Color.DKGRAY));

        List<SingleData> singles2 = new ArrayList<>();
        singles2.add(new SingleData(120, Color.MAGENTA));
        singles2.add(new SingleData(60, Color.GREEN));
        singles2.add(new SingleData(30, Color.CYAN));

        multiBar.setBarGroupCount(3)
                .addData(new BarDataMulti(singles, "语文"))
                .addData(new BarDataMulti(singles2, "数学"))
                .commit();
    }


    @OnClick({R.id.toggle_show, R.id.auto})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toggle_show:
                multiBar.setShowGraduation(!multiBar.isShowGraduation()).commit();
                break;
            case R.id.auto:
                //multiBar.setAuto(!multiBar.isAuto());
                setStyle();
                break;
        }
    }

    private void setStyle() {
        multiBar.setShowGraduation(true)
                .setMinAndMax(0, 100)
                .setShowGraduation(false)
                .setDensity(4)//数值方向的刻度密度
                .setBarWidth(30)//柱状图宽度.默认为宽度的1/10
                .setGraduationTextSize(30)//左侧刻度的文字大小
                .setTitleTextSize(30)//底部文字大小
                .setBarTextSize(30)//柱状图上方数字大小
                .commit();
    }
}
