package com.vinctor.vcharts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.vinctor.vchartviews.ring.Data;
import com.vinctor.vchartviews.ring.RingChart;
import com.vinctor.vchartviews.ring.RingData;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RingActivity extends BaseActivity {

    public static void startActivity(Context context) {
        Intent starter = new Intent(context, RingActivity.class);
        context.startActivity(starter);
    }

    RingChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie);
        ButterKnife.bind(this);
        chart = (RingChart) findViewById(R.id.pie);

        List<Data> datas = new ArrayList<>();
        datas.add(new Data(1, "1人掌握"));
        datas.add(new Data(17, "17人掌握"));
        datas.add(new Data(17, "17人掌握"));
        datas.add(new Data(2, "2人掌握"));
        datas.add(new Data(3, "3人掌握"));
        datas.add(new Data(16, "16人掌握"));
        datas.add(new Data(17, "17人掌握"));
        datas.add(new Data(1, "1人掌握"));

        chart
                .setData(new RingData(datas,
                        new Integer[]{0xff5EB9EE, 0xffC9E9FE, 0xff3B8DBD, 0xff31769F, Color.GREEN, Color.CYAN, 0xff3176eF, 0xff3f769F,}));
    }

    @OnClick({R.id.btn, R.id.auto})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn:
                chart.setShowTag(!chart.isShowTag()).commit();
                break;
            case R.id.auto:
                chart.setAuto(!chart.isAuto());
                set();
                break;
        }
    }

    private void set() {
        chart
                .setMaxRingWidth(100)
                .setMinRingWidth(70)
                .commit();
    }
}
