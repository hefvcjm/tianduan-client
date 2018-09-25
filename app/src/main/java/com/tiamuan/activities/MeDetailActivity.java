package com.tiamuan.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiamuan.MyApplication;
import com.tiamuan.model.User;

public class MeDetailActivity extends Activity {

    private TextView tv_top_bar_title;
    private TextView tv_top_bar_right;

    private ImageView iv_me_head_portrait;
    private EditText et_me_name;
    private EditText et_me_sex;
    private EditText et_me_phone;
    private EditText et_me_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_me_detail);
        init();
    }

    private void init() {
        tv_top_bar_title = findViewById(R.id.tv_top_bar_title);
        tv_top_bar_right = findViewById(R.id.tv_top_bar_right);
        tv_top_bar_title.setText("我的资料");
        tv_top_bar_right.setText("编辑");

        iv_me_head_portrait = findViewById(R.id.iv_me_head_portrait);
        et_me_name = findViewById(R.id.et_me_name);
        et_me_sex = findViewById(R.id.et_me_sex);
        et_me_phone = findViewById(R.id.et_me_phone);
        et_me_address = findViewById(R.id.et_me_address);

        User user = MyApplication.newInstance().getUser();
        if (user != null) {
            et_me_name.setText(user.getName());
            et_me_sex.setText(user.getSex());
            et_me_phone.setText(user.getPhone());
            et_me_address.setText(user.getAddress());
        } else {
            et_me_name.setText("");
            et_me_sex.setText("");
            et_me_phone.setText("");
            et_me_address.setText("");
        }
    }

}
