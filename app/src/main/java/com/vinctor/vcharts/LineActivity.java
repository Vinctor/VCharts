package com.vinctor.vcharts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vinctor.vchartviews.diagram.DiagramData;
import com.vinctor.vchartviews.diagram.DiagramFlowLayout;
import com.vinctor.vchartviews.diagram.DiagramView;
import com.vinctor.vchartviews.line.LineChart;
import com.vinctor.vchartviews.line.LineData;
import com.vinctor.vchartviews.line.OnShowTagCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LineActivity extends BaseActivity {

    public static void startActivity(Context context) {
        Intent starter = new Intent(context, LineActivity.class);
        context.startActivity(starter);
    }

    LineChart line;
    @BindView(R.id.column_input)
    EditText input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        ButterKnife.bind(this);
        line = (LineChart) findViewById(R.id.line);
        line.clearDatas()
                .setHorizontalOpen(false)//---是否左右开放,无坐标轴
                .setShowHorGraduation(false)//---在setHorizontalOpen(false)的前提下,设置是否按照setDensity(int)显示刻度线
                .setSpecialLineNum(60.3f)//---在setHorizontalOpen(false)的前提下,设置特殊刻度(比如合格线)
                .setShowTagRectBack(false)//---设置是否显示数字标签的背景,默认true
                .setShowAllTag(true)//----设置是否显示全部的数字标签,默认为false
                .setCoordinateRectLineWidth(10)//---设置刻度矩形的线宽
                .setSpecialLineWidth(10)//--设置setSpecialLineNum(float)中特殊线的线宽
                .setShowTitleRect(true)//--是否显示底部标题的矩形,默认为false
                .setOnShowTagCallBack(new OnShowTagCallBack() {
                    @Override
                    public String onShow(float num) {
                        if (num < 30) {
                            return "不及格";
                        }
                        return num + "分";
                    }

                    @Override
                    public String onShow(int num) {
                        if (num < 30) {
                            return "不及格";
                        }
                        return num + "";
                    }
                })

                .setShowAnimation(false)//设置绘制时是否显示动画
                .setDensity(5)//设置刻度密度
                .setAllowClickShowTag(true)//设置是否允许点击节点显示当前线的tag
                .setLineSmoothness(0f)//设置曲线的平滑系数(0.0f~0.5f),默认0.4
                .setCoordinateTextSize(30)//设置刻度文字的大小
                .setTagTextSize(40)//设置数字标签的字体大小(px)
                .setTitles(new String[]{"语文111", "数学", "英语", "物理", "化学", "ss", "ss"})//底部标题,需与折线数据长度一致
                .addData(new LineData(new float[]{20.5f, 50, 0, 70.9f, 90, 70, -100}, 0xff2cb072, 0xff186d45))//需与title长度一致
                .addData(new LineData(new float[]{30, 80, 50, 80.5f, 70.8f, 60, 100}, 0xffF8AC58))
                .setOnTitleClickListener(new LineChart.OnTitleClickListener() {
                    @Override
                    public void onClick(LineChart linechart, String title, int index) {
                        Toast.makeText(LineActivity.this, title, Toast.LENGTH_SHORT).show();
                    }
                })
                .commit();


        DiagramFlowLayout flowLayout = (DiagramFlowLayout) findViewById(R.id.flowlayout);
        List<DiagramData> list = new ArrayList<>();
        list.add(new DiagramData(
                Color.WHITE, 0xff7ED1FC, 0xff7ED1FC, 0xff3b8dbd, 0xff7ED1FC, 0xff3b8dbd, 5,
                "0~25个知识点"));
        list.add(new DiagramData(
                Color.WHITE, 0xff7ED1FC, 0xff7ED1FC, 0xff3b8dbd, 0xff7ED1FC, 0xff3b8dbd, 5,
                "25~50个知识点"));
//        list.add(new DiagramData(0xff30769F, "25~50个知识点"));
        flowLayout.setList(list);
        flowLayout.setOnCheckChangedListener(new DiagramFlowLayout.OnCheckChangedListener() {
            @Override
            public void onCheckChangedChange(DiagramView diagramView, int position, boolean isCheck) {
                if (isCheck) {
                    line.setShowTagLineIndex(position);
                }
            }

            @Override
            public void onClearCheck() {
                line.setShowTagLineIndex(-1);
            }
        });
    }

    @OnClick({R.id.showtagback, R.id.can, R.id.cannot, R.id.showtag, R.id.startAni, R.id.single, R.id.multi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.can:
                String num = input.getText().toString();
                if (TextUtils.isEmpty(num)) {
                    num = "3";
                }
                line.setAllowScroll(true)
                        .setMaxColumn(Integer.parseInt(num))
                        .setShowAnimation(true)
                        .commit();
                break;
            case R.id.cannot:
                line.setAllowScroll(false)
                        .setShowAnimation(true)
                        .commit();
                break;
            case R.id.showtag:
                line.setAllowClickShowTag(!line.isAllowClickShowTag()).commit();
                break;
            case R.id.startAni:
                line.clearDatas()
                        .setTitles(new String[]{"语文", "数学", "英语", "物理", "化学", "ss", "ss"})//底部标题,需与折线数据长度一致
                        .addData(new LineData(new float[]{20.5f, 50, 0, 70, 90, 70, -100}, 0xff61B6E7))//需与title长度一致
                        .addData(new LineData(new float[]{30, 80, 50, 80, 70.8f, 60, 100}, 0xffF8AC58))
                        .setShowAnimation(true)
                        .commit();
                break;
            case R.id.single:
                line.setTitles(new String[]{"语文"})
                        .clearDatas()
                        .addData(new LineData(new float[]{20}, 0xff61B6E7))
                        .commit();
                break;
            case R.id.multi:
                line.clearDatas()
                        .setTitles(new String[]{"语文", "数学", "英语", "物理", "化学", "ss", "ss"})//底部标题,需与折线数据长度一致
                        .addData(new LineData(new float[]{20.5f, 50, 0, 70, 90, 70, -100}, 0xff61B6E7))//需与title长度一致
                        .addData(new LineData(new float[]{30, 80, 50, 80, 70.8f, 60, 100}, 0xffF8AC58))
                        .setShowAnimation(true)
                        .commit();
                break;

            case R.id.showtagback:
                line.setShowTagRectBack(!line.isShowTagRectBack())
                        .commit();
                break;
        }
    }
}
