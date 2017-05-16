package com.vinctor.vcharts;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhy.autolayout.AutoLayoutActivity;

/**
 * Created by Vinctor on 2017/4/13.
 */

public class BaseActivity extends AutoLayoutActivity {
    Activity thisActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity =this;
    }
}
