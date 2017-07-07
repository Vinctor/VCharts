package com.vinctor.vcharts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.vinctor.vchartviews.compare.CompareView;

public class CompareActivity extends BaseActivity {
    int count = 1;

    public static void startActivity(Context context) {
        Intent starter = new Intent(context, CompareActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        final CompareView compareView = (CompareView) findViewById(R.id.compare);
//        compareView.setImgResID(R.drawable.img1).commit();
        compareView.setLessDataColor(0xff00ff00)
                .setMoreBorderColor(0XFF00FF00)
        .setLessBorderColor(0xffff0000);

        Button change = (Button) findViewById(R.id.change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res = -1;
                switch (count) {
                    case 1:
                        res = R.drawable.img1;
                        break;
                    case 2:
                        res = R.drawable.img2;
                        break;
                    case 3:
                        res = R.drawable.img3;
                        break;
                    case 4:
                        res = R.drawable.img4;
                        break;
                }
                count = (count == 4 ? 1 : ++count);
                compareView.setImgResID(res).commit();
            }
        });


        AppCompatSeekBar origin_seekbar = (AppCompatSeekBar) findViewById(R.id.origin_seekbar);
        origin_seekbar.setProgress((int) compareView.getOrigin());
        origin_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                compareView.setOrigin(progress).commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        AppCompatSeekBar other_seekbar = (AppCompatSeekBar) findViewById(R.id.other_seekbar);
        other_seekbar.setProgress((int) compareView.getOther());
        other_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                compareView.setOther(progress).commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
