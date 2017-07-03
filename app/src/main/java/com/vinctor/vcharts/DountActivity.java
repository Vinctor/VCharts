package com.vinctor.vcharts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

        dountView.clearList()///清楚数据
                .addData(new DountData(5))
                .addData(new DountData(5))
                .addData(new DountData(5))
                .addData(new DountData(5))
                .addData(new DountData(5))
                .addData(new DountData(100))
                .addData(new DountData(240))
                .addData(new DountData(5))
                .addData(new DountData(5))
                .addData(new DountData(1))
                .addData(new DountData(5))
                .addData(new DountData(5))
                .addData(new DountData(5))
                .addData(new DountData(5))
                .addData(new DountData(5))
                .addData(new DountData(2))
                .addData(new DountData(200, 0x00A4FF))
                .addData(new DountData(5))
                .addData(new DountData(5))
                .addData(new DountData(5))
                .addData(new DountData(1))
                .addData(new DountData(5))
                .addData(new DountData(240))
                .addData(new DountData(5))
                .addData(new DountData(5))
                .addData(new DountData(5))
                .addData(new DountData(5))
                .addData(new DountData(5))
                .addData(new DountData(200, 0x6D8F54))
                .addData(new DountData(5))
                .addData(new DountData(5))
                .addData(new DountData(5))
                .addData(new DountData(5))
                .addData(new DountData(5))
                .addData(new DountData(200, 0x3468A4))//自定义圆环块颜色
                .addData(new DountData(570))
                .addData(new DountData(4))
                .addData(new DountData(5))
                .addData(new DountData(6))
                .addData(new DountData(5))
                .setDountWidth(80)//圆环的宽度
                .setSpaceAngle(5)//白色间隔的角度
                .setTagLineWidth(3)//标签线的线宽
                .setTagLineColor(0x4E9AC1)//标签线的颜色
                .setTagTextColor(0xE47C6D)//标签文字颜色
                .setTextSize(30)//标签文字大小
                .setOnShowTagCallBack(new DountView.onShowTagCallBack() {//显示文字回调
                    @Override
                    public String onShowTag(int num, int position, @DountView.DountNumType int tag) {
                        //数字类型
                        // NUM_TYPE_DEFAULT 正常
                        // NUM_TYPE_MAX  最大
                        // NUM_TYPE_MIN  最小
                        if (num == 200) {
                            return num + ",自定义颜色";
                        }
                        if (tag == DountView.NUM_TYPE_MAX) {
                            return num + ",这是最大值!";
                        }
                        if (tag == DountView.NUM_TYPE_MIN) {
                            return num + ",这是最小值!";
                        }
                        return num + "个人";
                    }
                })
                .commit();//提交进行绘制
    }
}
