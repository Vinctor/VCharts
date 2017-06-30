package com.vinctor.vcharts;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vinctor.vchartviews.dount.DountData;
import com.vinctor.vchartviews.dount.DountView;

public class DountActivity extends AppCompatActivity {

    public static void startActivity(Context context) {
        Intent starter = new Intent(context, DountActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dount);


        DountView dountView = (DountView) findViewById(R.id.dount);

        dountView.clearList()
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(100))
                .addData(new DountData(240))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(2))
                .addData(new DountData(240))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(240))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(240))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(240))
                .addData(new DountData(170))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .addData(new DountData(1))
                .commit();
    }
}
