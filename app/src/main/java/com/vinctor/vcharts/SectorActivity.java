package com.vinctor.vcharts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.vinctor.vchartviews.sector.SectorData;
import com.vinctor.vchartviews.sector.SectorView;

public class SectorActivity extends BaseActivity {

    public static void startActivity(Context context) {
        Intent starter = new Intent(context, SectorActivity.class);
        context.startActivity(starter);
    }

    SectorView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sector);

        view = (SectorView) findViewById(R.id.view);
        view.setOnShowDescriptionLinstener(new SectorView.OnShowDescriptionLinstener() {
            @Override
            public String onShowDes(int num) {
                return num + "个知识点";
            }
        });
        view.setDescriptionTextSize(30)
                .setBorderWidth(8)
                .setData(new SectorData(120, 0xff2CB072, 0xff186D45, Color.WHITE))
                .addData(new SectorData(60, 0xffDDF4E9, 0xff2CB072, 0xff186D45))
                .commit();
    }
}
