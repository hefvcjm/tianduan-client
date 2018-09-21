package com.tiamuan.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class SuggestArgueActivity extends Activity {

    private TextView tv_top_bar_title;
    private TextView tv_top_bar_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_suggest_argue);
        init();

    }

    private void init() {
        tv_top_bar_title = findViewById(R.id.tv_top_bar_title);
        tv_top_bar_right = findViewById(R.id.tv_top_bar_right);
        tv_top_bar_title.setText("建议与投诉");
        tv_top_bar_right.setText("保存");
    }
}
