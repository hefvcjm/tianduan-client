package com.tianduan.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianduan.MyApplication;
import com.tianduan.model.Engineer;
import com.tianduan.model.Maintain;
import com.tianduan.model.Status;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RepairDetailActivity extends Activity {

    private static final String TAG = "RepairDetailActivity";

    private TextView tv_top_bar_title;
    private TextView tv_top_bar_right;

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
        Log.d(TAG, "data" + intent.getStringExtra("data"));
//        try {
        maintain = new Gson().fromJson(intent.getStringExtra("data"), Maintain.class);
        //maintain = new Maintain(intent.getStringExtra("data"));
        Log.d(TAG, "repair:" + maintain.getRepair().toString());
//        Log.d(TAG, "engineers:" + maintain.getEngineers().toString());
//        Log.d(TAG, "statuses:" + maintain.getStatuses().toString());
        setData();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void init() {
        tv_top_bar_title = findViewById(R.id.tv_top_bar_title);
        tv_top_bar_right = findViewById(R.id.tv_top_bar_right);
        tv_top_bar_title.setText("报修详情");
        tv_top_bar_right.setVisibility(View.GONE);

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
        title.setText("这是标题");
        ticket.setText(maintain.getRepair().getTicket());
        status.setText(maintain.getRepair().getStatuses().iterator().next().getStatus());
        time.setText(maintain.getRepair().getTime().split(" ")[0].replace("-", "/"));

        tv_repair_detail_device_name.setText(maintain.getRepair().getName());
        tv_repair_detail_device_code.setText(maintain.getRepair().getCode());
        tv_repair_detail_fault_part.setText(maintain.getRepair().getFaultpart());
        tv_repair_detail_question_description.setText(maintain.getRepair().getDescription());

        Iterator<Status> statusIterator = maintain.getRepair().getStatuses().iterator();
        Map<String, String> map = new HashMap<>();
        while (statusIterator.hasNext()) {
            Status status = statusIterator.next();
            map.put(status.getStatus(), status.getTime());
        }
        Set<String> keySet = map.keySet();
        if (keySet.contains("报修完成")) {
            //status bar
            iv_expand_status_icon_1.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.mipmap.ic_repair_done));
            iv_expand_status_icon_2.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.mipmap.ic_repair_done));
            iv_expand_status_icon_3.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.mipmap.ic_repair_done));
            tv_expand_status_1.setTextColor(MyApplication.newInstance().getResources().getColor(R.color.gray));
            tv_expand_status_2.setTextColor(MyApplication.newInstance().getResources().getColor(R.color.gray));
            tv_expand_status_3.setTextColor(MyApplication.newInstance().getResources().getColor(R.color.dark));
            iv_expand_status_1_to_2.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.drawable.solid_line));
            iv_expand_status_2_to_3.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.drawable.solid_line));
            //status description
            tv_item_detail_status.setText("报修完成");
            tv_item_detail_description.setText(map.get("报修完成"));
            //engineer
            Engineer engineer = null;
            if (maintain.getEngineers() != null && maintain.getEngineers().iterator().hasNext()) {
                engineer = maintain.getEngineers().iterator().next();
            }
            if (engineer != null) {
                if (engineer.getUser().getPicture() != null) {
                    //iv_repair_expand_repair_engineer_picture = convertView.findViewById(R.id.iv_repair_expand_repair_engineer_picture);
                }
                //iv_repair_expand_repair_engineer_picture = convertView.findViewById(R.id.iv_repair_expand_repair_engineer_picture);
                tv_item_detail_engineer_name.setText(engineer.getUser().getName());
            } else {
                ll_expand_detail_engineer.setVisibility(View.GONE);
            }

        } else if (keySet.contains("派单")) {
            //status bar
            iv_expand_status_icon_1.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.mipmap.ic_repair_done));
            iv_expand_status_icon_2.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.mipmap.ic_repair_dealing));
            iv_expand_status_icon_3.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.mipmap.ic_repair_last));
            tv_expand_status_1.setTextColor(MyApplication.newInstance().getResources().getColor(R.color.gray));
            tv_expand_status_2.setTextColor(MyApplication.newInstance().getResources().getColor(R.color.dark));
            tv_expand_status_3.setTextColor(MyApplication.newInstance().getResources().getColor(R.color.gray));
            iv_expand_status_1_to_2.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.drawable.solid_line));
            iv_expand_status_2_to_3.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.drawable.dotted_line));
            //status description
            tv_item_detail_status.setText("派单");
            tv_item_detail_description.setText(map.get("派单"));
            //engineer
            Engineer engineer = null;
            if (maintain.getEngineers() != null && maintain.getEngineers().iterator().hasNext()) {
                engineer = maintain.getEngineers().iterator().next();
            }
            if (engineer != null) {
                if (engineer.getUser().getPicture() != null) {
                    //iv_repair_expand_repair_engineer_picture = convertView.findViewById(R.id.iv_repair_expand_repair_engineer_picture);
                }
                //iv_repair_expand_repair_engineer_picture = convertView.findViewById(R.id.iv_repair_expand_repair_engineer_picture);
                tv_item_detail_engineer_name.setText(engineer.getUser().getName());
            } else {
                ll_expand_detail_engineer.setVisibility(View.GONE);
            }
        } else {
            //status bar
            iv_expand_status_icon_1.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.mipmap.ic_repair_done));
            iv_expand_status_icon_2.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.mipmap.ic_repair_dealing));
            iv_expand_status_icon_3.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.mipmap.ic_repair_last));
            tv_expand_status_1.setTextColor(MyApplication.newInstance().getResources().getColor(R.color.gray));
            tv_expand_status_2.setTextColor(MyApplication.newInstance().getResources().getColor(R.color.dark));
            tv_expand_status_3.setTextColor(MyApplication.newInstance().getResources().getColor(R.color.gray));
            iv_expand_status_1_to_2.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.drawable.solid_line));
            iv_expand_status_2_to_3.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.drawable.dotted_line));
            //status description
            tv_item_detail_status.setText("派单");
            tv_item_detail_description.setText(map.get("派单"));
            //engineer
            Engineer engineer = null;
            if (maintain.getEngineers() != null && maintain.getEngineers().iterator().hasNext()) {
                engineer = maintain.getEngineers().iterator().next();
            }
            if (engineer != null) {
                if (engineer.getUser().getPicture() != null) {
                    //iv_repair_expand_repair_engineer_picture = convertView.findViewById(R.id.iv_repair_expand_repair_engineer_picture);
                }
                //iv_repair_expand_repair_engineer_picture = convertView.findViewById(R.id.iv_repair_expand_repair_engineer_picture);
                tv_item_detail_engineer_name.setText(engineer.getUser().getName());
            } else {
                ll_expand_detail_engineer.setVisibility(View.GONE);
            }
        }
    }
}
