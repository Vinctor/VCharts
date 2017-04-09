package com.vinctor.vcharts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vinctor.vchartviews.RadarData;
import com.vinctor.vchartviews.RadarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RadarView radarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radarView = (RadarView) findViewById(R.id.radarview);
        List<RadarData> list = new ArrayList<>();


        RadarData data = new RadarData();
        data.setColor(0xff0000);
        data.setDatas(new float[]{80, 70, 30, 70, 80, 70});
//        list.add(data);

        RadarData data2 = new RadarData();
        data2.setColor(0x0000ff);
        data2.setDatas(new float[]{92, 88, 84, 67, 88, 78});
        list.add(data2);
        radarView.setList(list);
        radarView.setTitles(new String[]{"语文", "数学", "英语", "物理", "化学", "生物"});
        radarView.setMinAndMax(60, 100);
        radarView.setDensity(6);
    }
}
