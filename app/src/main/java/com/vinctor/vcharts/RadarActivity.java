package com.vinctor.vcharts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.vinctor.vchartviews.radar.RadarChart;
import com.vinctor.vchartviews.radar.RadarData;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RadarActivity extends BaseActivity {

    RadarChart radarView;

    public static void start(Context context) {
        Intent starter = new Intent(context, RadarActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);
        ButterKnife.bind(this);
        radarView = (RadarChart) findViewById(R.id.radarview);
        List<RadarData> list = new ArrayList<>();


        RadarData data = new RadarData();
        data.setColor(0x44F594A0);
        data.setDatas(new float[]{80, 70, 30, 70, 80, 100});
        list.add(data);

        RadarData data2 = new RadarData();
        data2.setColor(0x44317AA4);
        data2.setDatas(new float[]{92, 880, 84, 67, 88, 78});
        list.add(data2);
        setSetting();
        radarView
                .setList(list)//设置数据
                .setTitles(new String[]{"语文", "数学", "英语", "物理", "化学", "生物"})//边角文字
                .commit();//以上设置需要此方法才能生效
    }

    private void setSetting() {
        radarView.setCount(6)//多边形几条边
                .setDensity(6)//雷达图蜘蛛网密度
                .setMinAndMax(0, 100)//最小与最大值
                .setTitleTextSize(30)//雷达边角标题文字大小(px)默认30
                .setTagTextSize(30)//雷达刻度文字大小
                .commit();
    }

    @OnClick(R.id.auto)
    public void onViewClicked() {
        radarView.setAuto(!radarView.isAuto());
        setSetting();
    }
}
