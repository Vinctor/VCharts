package com.vinctor.vcharts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vinctor.vchartviews.bar.BarChart;
import com.vinctor.vchartviews.bar.BarData;

public class BarActivity extends AppCompatActivity {

    BarChart bar;

    public static void start(Context context) {
        Intent starter = new Intent(context, BarActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);
        bar = (BarChart) findViewById(R.id.bar);
        bar.setMinAndMax(50, 100)
                .setDensity(4)
                .setBarWidth(30)
                .setGraduationTextSize(30)
                .setTitleTextSize(30)
                .setBarTextSize(30)
                .setData(new BarData("语文", 0, Color.BLUE))
                .addData(new BarData("数学", 80, Color.RED))
                .addData(new BarData("英语", 120, Color.MAGENTA))
                .addData(new BarData("物理", 60, Color.GREEN))
                .commit();
    }
}
