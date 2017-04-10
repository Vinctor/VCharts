package com.vinctor.vcharts;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vinctor.vchartviews.line.LineChart;
import com.vinctor.vchartviews.line.LineData;

public class LineActivity extends AppCompatActivity {

    public static void startActivity(Context context) {
        Intent starter = new Intent(context, LineActivity.class);
        context.startActivity(starter);
    }

    LineChart line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        line = (LineChart) findViewById(R.id.line);
        line.setData(new LineData(new int[]{20, 50, 20, 70, 0}, 0xff61B6E7));
        line.commit();
    }
}
