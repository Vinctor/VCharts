package com.vinctor.vcharts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vinctor.vchartviews.bar.BarCharSingle;
import com.vinctor.vchartviews.bar.BarDataSingle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BarSingleActivity extends AppCompatActivity {

    BarCharSingle singleBar;

    public static void start(Context context) {
        Intent starter = new Intent(context, BarSingleActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);
        ButterKnife.bind(this);
        singleBar = (BarCharSingle) findViewById(R.id.bar_single);
        singleBar.setShowGraduation(true)
                .setMinAndMax(50, 100)
                .setDensity(4)//数值方向的刻度密度
//                .setBarWidth(30)//柱状图宽度.默认为宽度的1/10
                .setGraduationTextSize(30)//左侧刻度的文字大小
                .setTitleTextSize(30)//底部文字大小
                .setBarTextSize(30)//柱状图上方数字大小
                .commit();
        singleBar.setData(new BarDataSingle("语文", 0, Color.BLUE))
                .addData(new BarDataSingle("数学", 80, Color.RED))
                .addData(new BarDataSingle("英语", 120, Color.MAGENTA))
                .addData(new BarDataSingle("物理", 60, Color.GREEN))
                .commit();
    }

    @OnClick(R.id.toggle_show)
    public void onViewClicked() {
        singleBar.setShowGraduation(!singleBar.isShowGraduation()).commit();
    }
}
