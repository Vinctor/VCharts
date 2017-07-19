package com.vinctor.vcharts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vinctor.vchartviews.dount.DountData;
import com.vinctor.vchartviews.dount.DountView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DountActivity extends BaseActivity {

    @BindView(R.id.dount)
    DountView dount;
    @BindView(R.id.toggle)
    Button toggle;

    public static void startActivity(Context context) {
        Intent starter = new Intent(context, DountActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dount);
        ButterKnife.bind(this);

        setData().commit();
    }

    private DountView setData() {
        return dount.clearList()///清楚数据
                .addData(new DountData(5, "5个人"))
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
                    public String onShowTag(int num, int position, String ss, @DountView.DountNumType int tag) {
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
                        return ss;
                    }
                });
    }

    @OnClick({R.id.toggle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.dount:
//                break;
            case R.id.toggle:
                setData().setEmptyStyle(!dount.isEmptyStyle())
                        .setEmptyTextSize(34)
                        .setEmptyText("本月没有学习知识点")
                        .setEmptyRadius(400)
                        .setEmptyDountColor(0xe0e0e0)
                        .setEmptyTextColor(0x666666)
                        .commit();
                break;
        }
    }
}
