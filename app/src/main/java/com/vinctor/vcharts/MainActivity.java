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
        RadarActivity.start(this);
    }

    @OnClick({R.id.radar, R.id.bar, R.id.line})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.radar:
                RadarActivity.start(this);
                break;
            case R.id.bar:
                BarActivity.start(this);
                break;
            case R.id.line:
                LineActivity.startActivity(this);
                break;
        }
    }
}
