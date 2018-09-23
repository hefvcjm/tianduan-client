package com.tiamuan.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tiamuan.MyApplication;
import com.tiamuan.adapters.MyExpandableListAdapter;
import com.tiamuan.adapters.OnGroupExpandedListener;
import com.tiamuan.model.Maintain;
import com.tiamuan.model.Repair;
import com.tiamuan.net.MyHttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewRepairActivity extends Activity {

    private static final String TAG = "NewRepairActivity";

    private TextView tv_top_bar_title;
    private TextView tv_top_bar_right;

    private EditText et_new_repair_name;
    private EditText et_new_repair_code;
    private EditText et_new_repair_question_description;
    private EditText et_new_repair_fault_part;

    private Button bn_new_repair_commit;

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

        et_new_repair_name = findViewById(R.id.et_new_repair_name);
        et_new_repair_code = findViewById(R.id.et_new_repair_code);
        et_new_repair_question_description = findViewById(R.id.et_new_repair_question_description);
        et_new_repair_fault_part = findViewById(R.id.et_new_repair_fault_part);
        bn_new_repair_commit = findViewById(R.id.bn_new_repair_commit);

        bn_new_repair_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIsEmptyField()) {
                    MyHttpRequest stringRequest = new MyHttpRequest(Request.Method.PUT
                            , MyApplication.buildURL("/repair/new")
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("hefvcjm-1", response);
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONArray arrays = new JSONArray(json.getString("data"));
                                List<Maintain> maintains = new ArrayList<>();
                                int len = arrays.length();
                                for (int i = 0; i < len; i++) {
                                    Maintain item = new Maintain(arrays.get(i).toString());
                                    if (item != null) {
                                        maintains.add(item);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("hefvcjm-1", "error");
                        }
                    }) {
                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            Repair repair = new Repair();
                            repair.setName(et_new_repair_name.getText().toString());
                            repair.setCode(et_new_repair_code.getText().toString());
                            repair.setDescription(et_new_repair_question_description.getText().toString());
                            repair.setFaultpart(et_new_repair_fault_part.getText().toString());
                            try {
                                JSONObject json = new JSONObject(repair.toString());
                                Log.d(TAG, repair.toString());
                                Iterator<String> keySet = json.keys();
                                while (keySet.hasNext()) {
                                    String key = keySet.next();
                                    if (json.get(key) == JSONObject.NULL) {
                                        json.remove(key);
                                    }
                                }
                                return json.toString().getBytes();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, repair.toString());
                            return null;
                        }

                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }
                    };
                    MyApplication.newInstance().getRequestQueue().add(stringRequest);
                }
            }
        });
    }

    private boolean checkIsEmptyField() {
        if (replaceBlank(et_new_repair_name.getText().toString()) == ""
                || replaceBlank(et_new_repair_code.getText().toString()) == ""
                || replaceBlank(et_new_repair_question_description.getText().toString()) == ""
                || replaceBlank(et_new_repair_fault_part.getText().toString()) == "") {
            return true;
        }
        return false;
    }

    protected String replaceBlank(String str) {
        String dest = null;
        if (str == null) {
            return dest;
        } else {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
            return dest;
        }
    }
}
