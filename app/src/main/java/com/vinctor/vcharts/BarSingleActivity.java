package com.vinctor.vcharts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vinctor.vchartviews.bar.AbsBarChart;
import com.vinctor.vchartviews.bar.BarCharSingle;
import com.vinctor.vchartviews.bar.BarDataSingle;
import com.vinctor.vchartviews.tools.onBarShowTagListener;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BarSingleActivity extends BaseActivity {

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
        setStyle();
        singleBar.setOnShowDataListener(new AbsBarChart.OnShowDataListener() {
            @Override
            public String onShow(int num) {
                return num + "分";
            }
        });
        singleBar.setOnShowOtherViewCallback(
                new onBarShowTagListener(
                        "查看测验详情",//文字
                        32,          //字体大小,已适配autolayout
                        0xffffffff,     //字体颜色
                        0xff2cb072,     //内部填充颜色
                        0xff186d45,     //border描边颜色
                        4,              //border宽度,已适配autolayout
                        new int[]{-26, 0}, //定位偏移量x,y,,已适配autolayout
                        5               //圆角半径,已适配autolayout

                ) {
                    @Override
                    public int onShowIndex() {
                        return 0;
                    }

                    @Override
                    public void onClick() {//点击监听
                        Toast.makeText(thisActivity, "press", Toast.LENGTH_SHORT).show();
                    }

                }
        );
        singleBar.setData(new BarDataSingle("语文", 0, Color.BLUE))
                .addData(new BarDataSingle("数学", 80, Color.RED))
                .addData(new BarDataSingle("英语", 120, Color.MAGENTA))
                .addData(new BarDataSingle("物理", 60, Color.GREEN))
                .commit();
    }

    @OnClick({R.id.toggle_show, R.id.auto})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toggle_show:
                singleBar.setShowGraduation(!singleBar.isShowGraduation()).commit();
                break;
            case R.id.auto:
                singleBar.setAuto(!singleBar.isAuto());
                setStyle();
                break;
        }
    }

    private void setStyle() {
        singleBar.setShowGraduation(true)
                .setMinAndMax(50, 100)
                .setDensity(4)//数值方向的刻度密度
                .setBarWidth(30)//柱状图宽度.默认为宽度的1/10
                .setGraduationTextSize(30)//左侧刻度的文字大小
                .setTitleTextSize(30)//底部文字大小
                .setBarTextSize(30)//柱状图上方数字大小
                .commit();
    }
}
