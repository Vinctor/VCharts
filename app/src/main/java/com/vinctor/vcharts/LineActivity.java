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
                .setAllowClickShowTag(true)
                .setTitles(new String[]{"语文", "数学", "英语", "物理", "化学", "ss", "ss"})//底部标题,需与折线数据长度一致
                .addData(new LineData(new float[]{20.5f, 50, 0, 70, 90, 70, -100}, 0xff2cb072, 0xff186d45))//需与title长度一致
                .addData(new LineData(new float[]{30, 80, 50, 80, 70.8f, 60, 100}, 0xffF8AC58))
                .commit();
        line.setOnTitleClickListener(new LineChart.OnTitleClickListener() {
            @Override
            public void onClick(LineChart linechart, String title, int index) {
                Toast.makeText(LineActivity.this, title, Toast.LENGTH_SHORT).show();
            }
        });


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

    @OnClick({R.id.can, R.id.cannot, R.id.showtag, R.id.startAni, R.id.single, R.id.multi, R.id.shadow})
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
            case R.id.shadow:
                line.clearDatas()
                        .setTitles(new String[]{"语文", "数学", "英语", "物理", "化学", "ss", "ss"})//底部标题,需与折线数据长度一致
                        .addData(new LineData(new float[]{20.5f, 50, 0, 70, 90, 70, -100}, 0xff61B6E7))//需与title长度一致
                        .setShowAnimation(true)
                        .commit();
                break;
        }
    }
}
