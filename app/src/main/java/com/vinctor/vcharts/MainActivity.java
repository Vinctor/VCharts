package com.vinctor.vcharts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.radar, R.id.bar_single, R.id.bar_multi, R.id.line})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.radar:
                RadarActivity.start(this);
                break;
            case R.id.bar_single:
                BarSingleActivity.start(this);
                break;
            case R.id.bar_multi:
                BarMultiActivity.startActivity(this );
                break;
            case R.id.line:
                LineActivity.startActivity(this);
                break;
        }
    }
}
