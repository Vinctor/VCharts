package com.vinctor.vcharts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.vinctor.vchartviews.diagram.DiagramData;
import com.vinctor.vchartviews.diagram.DiagramFlowLayout;

import java.util.ArrayList;
import java.util.List;

public class DiagragActivity extends BaseActivity {

    public static void startActivity(Context context) {
        Intent starter = new Intent(context, DiagragActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagrag);

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
    }

}
