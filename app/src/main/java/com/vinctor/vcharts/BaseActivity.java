package com.vinctor.vcharts;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by Vinctor on 2017/4/13.
 */

public class BaseActivity extends AppCompatActivity {
    Activity thisActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity =this;
    }
}
