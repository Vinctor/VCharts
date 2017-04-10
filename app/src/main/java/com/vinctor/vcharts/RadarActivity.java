package com.vinctor.vcharts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vinctor.vchartviews.radar.RadarData;
import com.vinctor.vchartviews.radar.RadarChart;

import java.util.ArrayList;
import java.util.List;

public class RadarActivity extends AppCompatActivity {

    RadarChart radarView;

    public static void start(Context context) {
        Intent starter = new Intent(context, RadarActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);
        radarView = (RadarChart) findViewById(R.id.radarview);
        List<RadarData> list = new ArrayList<>();


        RadarData data = new RadarData();
        data.setColor(0xff0000);
        data.setDatas(new float[]{80, 70, 30, 70, 80, 70});

        RadarData data2 = new RadarData();
        data2.setColor(0x0000ff);
        data2.setDatas(new float[]{92, 88, 84, 67, 88, 78});
        list.add(data2);
        radarView.setCount(6)//多边形几条边
                .setDensity(6)//雷达图蜘蛛网密度
                .setMinAndMax(0, 100)//最小与最大值
                .setAlpha(150)//雷达图数据遮盖透明度
                .clearData()//清楚数据
                .setList(list)//设置数据
                .addData(data)//添加数据
                .setTitleTextSize(30)//雷达边角标题文字大小(px)默认30
                .setTagTextSize(30)//雷达刻度文字大小
                .setTitles(new String[]{"语文", "数学", "英语", "物理", "化学", "生物"})//边角文字
                .commit();//以上设置需要此方法才能生效
    }
}
