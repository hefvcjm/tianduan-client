package com.tianduan.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tianduan.MyApplication;
import com.tianduan.adapters.MyExpandableListAdapter;
import com.tianduan.adapters.RepairHistoryListAdapter;
import com.tianduan.model.Maintain;
import com.tianduan.net.MyHttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RepairHistoryActivity extends Activity {

    private static final String TAG = "RepairHistoryActivity";

    private TextView tv_top_bar_title;
    private TextView tv_top_bar_right;

    private ListView lv_repair_history;
    private ImageView iv_nothing;

    List<Maintain> maintains;
    RepairHistoryListAdapter adapter;

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

        lv_repair_history = findViewById(R.id.lv_repair_history);
        iv_nothing = findViewById(R.id.iv_nothing);

        lv_repair_history.setVisibility(View.GONE);
        iv_nothing.setVisibility(View.VISIBLE);

        requestForData();

        lv_repair_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onChildClick: position:" + position + ", id:" + id);
                if (maintains != null) {
                    Intent intent = new Intent(RepairHistoryActivity.this, RepairDetailActivity.class);
//                    Log.d(TAG, maintains.get(groupPosition).toJson().toString());
                    intent.putExtra("data", new Gson().toJson(maintains.get(position), Maintain.class));
                    Log.d(TAG, maintains.get(position).toString());
                    startActivity(intent);
                }
            }
        });
    }

    private void requestForData() {
        MyHttpRequest stringRequest = new MyHttpRequest(Request.Method.GET
                , MyApplication.buildURL("/repair/queryall")
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray arrays = new JSONArray(json.getString("data"));
                    maintains = new ArrayList<>();
                    int len = arrays.length();
                    for (int i = 0; i < len; i++) {
                        Maintain item = new Maintain(arrays.get(i).toString());
                        if (item != null) {
                            maintains.add(item);
                        }
                    }
                    Log.d(TAG, maintains.size() + "");
                    Log.d(TAG, "new adapter");
                    adapter = new RepairHistoryListAdapter(maintains);
                    lv_repair_history.setAdapter(adapter);
                    if (maintains.size() == 0) {
                        lv_repair_history.setVisibility(View.GONE);
                        iv_nothing.setVisibility(View.VISIBLE);
                    } else {
                        lv_repair_history.setVisibility(View.VISIBLE);
                        iv_nothing.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "error");
            }
        });
        MyApplication.newInstance().getRequestQueue().add(stringRequest);
    }
}
