package com.tiamuan.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiamuan.model.Maintain;

import org.json.JSONException;

public class RepairDetailActivity extends Activity {

    private static final String TAG = "RepairDetailActivity";

    TextView title;
    TextView ticket;
    TextView status;
    TextView time;

    //status bar
    ImageView iv_expand_status_icon_1;
    ImageView iv_expand_status_icon_2;
    ImageView iv_expand_status_icon_3;
    TextView tv_expand_status_1;
    TextView tv_expand_status_2;
    TextView tv_expand_status_3;
    ImageView iv_expand_status_1_to_2;
    ImageView iv_expand_status_2_to_3;

    //status description
    TextView tv_item_detail_status;
    TextView tv_item_detail_description;

    //engineer
    ImageView iv_repair_expand_repair_engineer_picture;
    TextView tv_item_detail_engineer_name;
    Button bn_item_detail_engineer_contract;
    LinearLayout ll_expand_detail_engineer;

    //completed
    Button bn_item_expand_repair_completed;

    //contract service
    TextView tv_item_expand_repair_contract_service;

    //detail
    TextView tv_repair_detail_device_name;
    TextView tv_repair_detail_device_code;
    TextView tv_repair_detail_fault_part;
    TextView tv_repair_detail_question_description;

    Maintain maintain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_repair_detail);
        init();
        Intent intent = getIntent();
        Log.d(TAG, intent.getStringExtra("data"));
//        try {
//            maintain = new Maintain(intent.getStringExtra("data"));
//            Log.d(TAG, maintain.getRepair().toString());
//            Log.d(TAG, maintain.getEngineers().toString());
//            Log.d(TAG, maintain.getStatuses().toString());
            setData();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void init() {
        title = findViewById(R.id.tv_item_repair_title);
        ticket = findViewById(R.id.tv_item_repair_ticket);
        status = findViewById(R.id.tv_item_repair_status);
        time = findViewById(R.id.tv_item_repair_time);

        //status bar
        iv_expand_status_icon_1 = findViewById(R.id.iv_expand_status_icon_1);
        iv_expand_status_icon_2 = findViewById(R.id.iv_expand_status_icon_2);
        iv_expand_status_icon_3 = findViewById(R.id.iv_expand_status_icon_3);
        tv_expand_status_1 = findViewById(R.id.tv_expand_status_1);
        tv_expand_status_2 = findViewById(R.id.tv_expand_status_2);
        tv_expand_status_3 = findViewById(R.id.tv_expand_status_3);
        iv_expand_status_1_to_2 = findViewById(R.id.iv_expand_status_1_to_2);
        iv_expand_status_2_to_3 = findViewById(R.id.iv_expand_status_2_to_3);
        //status description
        tv_item_detail_status = findViewById(R.id.tv_item_detail_status);
        tv_item_detail_description = findViewById(R.id.tv_item_detail_description);
        //engineer
        iv_repair_expand_repair_engineer_picture = findViewById(R.id.iv_repair_expand_repair_engineer_picture);
        tv_item_detail_engineer_name = findViewById(R.id.tv_item_detail_engineer_name);
        bn_item_detail_engineer_contract = findViewById(R.id.bn_item_detail_engineer_contract);
        ll_expand_detail_engineer = findViewById(R.id.ll_expand_detail_engineer);
        //completed
        bn_item_expand_repair_completed = findViewById(R.id.bn_item_expand_repair_completed);
        //contract service
        tv_item_expand_repair_contract_service = findViewById(R.id.tv_item_expand_repair_contract_service);

        //detail
        tv_repair_detail_device_name = findViewById(R.id.tv_repair_detail_device_name);
        tv_repair_detail_device_code = findViewById(R.id.tv_repair_detail_device_code);
        tv_repair_detail_fault_part = findViewById(R.id.tv_repair_detail_fault_part);
        tv_repair_detail_question_description = findViewById(R.id.tv_repair_detail_question_description);
    }

    private void setData() {
        if (maintain == null) {
            return;
        }

    }
}
