package com.tiamuan.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RepairHistoryActivity extends Activity {


    private TextView tv_top_bar_title;
    private TextView tv_top_bar_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_repair_history);
        init();
    }

    private void init() {
        tv_top_bar_title = findViewById(R.id.tv_top_bar_title);
        tv_top_bar_right = findViewById(R.id.tv_top_bar_right);
        tv_top_bar_title.setText("我的报修");
        tv_top_bar_right.setVisibility(View.GONE);
    }
}
