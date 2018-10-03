package com.tianduan.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tianduan.MyApplication;
import com.tianduan.adapters.MyExpandableListAdapter;
import com.tianduan.adapters.OnGroupExpandedListener;
import com.tianduan.model.Maintain;
import com.tianduan.model.Repair;
import com.tianduan.net.MyHttpRequest;
import com.tianduan.net.MyJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class NewRepairActivity extends Activity {

    private static final String TAG = "NewRepairActivity";

    private TextView tv_top_bar_title;
    private TextView tv_top_bar_right;

    private EditText et_new_repair_name;
    private EditText et_new_repair_code;
    private EditText et_new_repair_question_description;
    private EditText et_new_repair_fault_part;

    private Button bn_new_repair_commit;
    private Button bn_new_repair_add_picture;

    private Repair repair;
    private ArrayList<String> images;

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
        bn_new_repair_add_picture = findViewById(R.id.bn_new_repair_add_picture);

        bn_new_repair_add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiImageSelector.create(NewRepairActivity.this)
                        .showCamera(true) // show camera or not. true by default
                        .count(9) // max select image size, 9 by default. used width #.multi()
                        .multi() // multi mode, default mode;
                        .origin(images) // original select data set, used width #.multi()
                        .start(NewRepairActivity.this, 1);
            }
        });

        bn_new_repair_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIsEmptyField()) {
                    repair = new Repair();
                    repair.setName(et_new_repair_name.getText().toString());
                    repair.setCode(et_new_repair_code.getText().toString());
                    repair.setDescription(et_new_repair_question_description.getText().toString());
                    repair.setFaultpart(et_new_repair_fault_part.getText().toString());
                    Log.d(TAG, repair.toStringNotNullField());
                    MyJsonRequest jsonRequest = null;
                    try {
                        jsonRequest = new MyJsonRequest(Request.Method.PUT
                                , MyApplication.buildURL("/repair/new")
                                , new JSONObject(repair.toStringNotNullField())
                                , new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("hefvcjm-1", response.toString());
                                try {
                                    repair = new Gson().fromJson(response.getString("data"), Repair.class);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        MyApplication.newInstance().getRequestQueue().add(jsonRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void postFiles(String repairObjectId, Map<String, String[]> pathMap) throws Exception {
        RequestParams params = new RequestParams();
        Set<String> keys = pathMap.keySet();
        for (String key : keys) {
            String[] paths = pathMap.get(key);
            List<File> files = new ArrayList<>();
            for (String path : paths) {
                File file = new File(path);
                if (file.exists() && file.length() > 0) {
                    files.add(file);
                }
            }
            if (files.size() > 0) {
                File[] postFiles = new File[files.size()];
                files.toArray(postFiles);
                params.put(key, postFiles);
            }
        }
        MyApplication.newInstance().getAsyncHttpClient().post(MyApplication.buildURL("/repair/file/" + repairObjectId)
                , params
                , new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d(TAG, new String(responseBody));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d(TAG, new String(responseBody));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.d(TAG, "选择图片");
            switch (requestCode) {
                case 0:

                    break;
                case 1:
                    List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    Log.d(TAG, path.toString());
//                    String path = FileUtils.getPath(this, uri);//获取图片真实保存位置
//                    images.add(path);//记录文件路径
                    //Bitmap bitmap2 = BitMapUtils.getSmallBitmap(path2);//获取压缩图像
                    //image2.setImageBitmap(bitmap2);
                    break;
                default:
                    break;
            }
        }
    }
}
