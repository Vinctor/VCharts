package com.vinctor.vcharts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.vinctor.vchartviews.line.LineChart;
import com.vinctor.vchartviews.line.LineData;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LineActivity extends BaseActivity {

    public static void startActivity(Context context) {
        Intent starter = new Intent(context, LineActivity.class);
        context.startActivity(starter);
    }

    LineChart line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        ButterKnife.bind(this);
        line = (LineChart) findViewById(R.id.line);
        line.setTitles(new String[]{"语文", "数学", "英语", "物理", "化学", "ss", "ss"})//底部标题,需与折线数据长度一致
                .clearDatas()
                .addData(new LineData(new float[]{20.5f, 50, 0, 70, 90, 70, 76}, 0xff61B6E7))//需与title长度一致
                .addData(new LineData(new float[]{30, 80, 50, 80, 70.8f, 60, 100}, 0xffF8AC58))
//                .addData(new LineData(new float[]{-10, 30, 80, 50, 60, 80, 1500}, 0xffF593A0))
//                .addData(new LineData(new float[]{10, 60, 80, 65, 0, 80, 100}, 0xff61B6E7))//需与title长度一致
//
//                .addData(new LineData(new float[]{90, 90, 87, 78, 60, 50, 0}, 0xfffFeeA0))
//                .addData(new LineData(new float[]{0, 90, 98, 40, 80, 80, 0}, 0xffF85558))
                .setShowAnimation(true)
                .commit();
        line.setOnTitleClickListener(new LineChart.OnTitleClickListener() {
            @Override
            public void onClick(LineChart linechart, String title, int index) {
                Toast.makeText(LineActivity.this, title, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.auto)
    public void onViewClicked() {
        line.setTitles(new String[]{"语文"})
                .clearDatas()
                .addData(new LineData(new float[]{20}, 0xff61B6E7))
                .commit();
    }
}
