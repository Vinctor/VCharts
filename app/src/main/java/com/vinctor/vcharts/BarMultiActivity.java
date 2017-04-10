package com.vinctor.vcharts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vinctor.vchartviews.bar.BarCharMulti;
import com.vinctor.vchartviews.bar.multi.BarDataMulti;
import com.vinctor.vchartviews.bar.multi.SingleData;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BarMultiActivity extends AppCompatActivity {

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
        multiBar.setShowGraduation(true)
                .setMinAndMax(50, 100)
                .setShowGraduation(false)
                .setDensity(4)//数值方向的刻度密度
//                .setBarWidth(30)//柱状图宽度.默认为宽度的1/10
                .setGraduationTextSize(30)//左侧刻度的文字大小
                .setTitleTextSize(30)//底部文字大小
                .setBarTextSize(30)//柱状图上方数字大小
                .commit();
        List<SingleData> singles = new ArrayList<>();
        singles.add(new SingleData(90, Color.BLUE));
        singles.add(new SingleData(80, Color.RED));

        List<SingleData> singles2 = new ArrayList<>();
        singles2.add(new SingleData(120, Color.MAGENTA));
        singles2.add(new SingleData(60, Color.GREEN));

        multiBar.addData(new BarDataMulti(singles, "语文"))
                .addData(new BarDataMulti(singles2, "数学"))
                .commit();
    }

    @OnClick(R.id.toggle_show)
    public void onViewClicked() {
        multiBar.setShowGraduation(!multiBar.isShowGraduation()).commit();
    }
}
