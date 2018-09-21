package com.tiamuan.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class NewRepairActivity extends Activity {

    private TextView tv_top_bar_title;
    private TextView tv_top_bar_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_repair);
        init();
    }

    private void init() {
        tv_top_bar_title = findViewById(R.id.tv_top_bar_title);
        tv_top_bar_right = findViewById(R.id.tv_top_bar_right);
        tv_top_bar_title.setText("新建报修");
        tv_top_bar_right.setText("保存");
    }
}
