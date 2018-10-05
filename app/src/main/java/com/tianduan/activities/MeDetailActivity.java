package com.tianduan.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tianduan.MyApplication;
import com.tianduan.model.User;
import com.tianduan.net.MyJsonRequest;
import com.tianduan.util.CompressUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cz.msebera.android.httpclient.Header;

public class MeDetailActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MeDetailActivity";

    private final int REQUEST_CODE_GALLERY = 1001;

    private TextView tv_top_bar_title;
    private TextView tv_top_bar_right;
    private ImageView iv_back;

    private ImageView iv_me_head_portrait;
    private EditText et_me_name;
    private EditText et_me_sex;
    private EditText et_me_phone;
    private EditText et_me_address;
    private LinearLayout me_detail_picture;

    private String headPath;

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
        iv_back = findViewById(R.id.iv_activity_back);
        tv_top_bar_title.setText("我的资料");
        tv_top_bar_right.setText("编辑");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_me_head_portrait = findViewById(R.id.iv_me_head_portrait);
        et_me_name = findViewById(R.id.et_me_name);
        et_me_sex = findViewById(R.id.et_me_sex);
        et_me_phone = findViewById(R.id.et_me_phone);
        et_me_address = findViewById(R.id.et_me_address);
        me_detail_picture = findViewById(R.id.me_detail_picture);

        User user = MyApplication.newInstance().getUser();
        if (user != null) {
            et_me_name.setText(user.getName());
            et_me_sex.setText(user.getSex());
            et_me_phone.setText(user.getPhone());
            et_me_address.setText(user.getAddress());
            Bitmap bitmap = user.getHeadBitmap();
            if (bitmap != null) {
                iv_me_head_portrait.setImageBitmap(bitmap);
            }
        } else {
            et_me_name.setText("");
            et_me_sex.setText("");
            et_me_phone.setText("");
            et_me_address.setText("");
        }

        tv_top_bar_right.setOnClickListener(this);
        me_detail_picture.setOnClickListener(this);
    }

    private boolean editable = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_bar_right:
                editable = !editable;
                et_me_name.setEnabled(editable);
                et_me_sex.setEnabled(editable);
                et_me_phone.setEnabled(editable);
                et_me_address.setEnabled(editable);
                if (editable) {
                    tv_top_bar_right.setText("确认修改");
                } else {
                    tv_top_bar_right.setText("编辑");
                    User user = new User();
                    user.setName(et_me_name.getText().toString());
                    user.setSex(et_me_sex.getText().toString());
                    user.setPhone(et_me_phone.getText().toString());
                    user.setAddress(et_me_address.getText().toString());
                    Log.d(TAG, user.toStringNotNullField());
                    MyJsonRequest jsonRequest = null;
                    try {
                        jsonRequest = new MyJsonRequest(Request.Method.POST
                                , MyApplication.buildURL("/user/update")
                                , new JSONObject(user.toStringNotNullField())
                                , new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                                try {
                                    User user = new Gson().fromJson(response.getString("data"), User.class);
                                    if (headPath != null) {
                                        try {
                                            Bitmap bitmap = CompressUtil.getSmallBitmap(headPath);
                                            final String path = MyApplication.newInstance().getContext().getCacheDir().getAbsolutePath() + "/" + user.getObjectId() + "/user/head_pic";
                                            File file = new File(path);
                                            if (!file.exists()) {
                                                file.mkdirs();
                                            }
                                            file = new File(path, "picture.png");
                                            if (!file.exists()) {
                                                try {
                                                    file.createNewFile();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            FileOutputStream fos = new FileOutputStream(file);
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                            fos.flush();
                                            fos.close();
                                            RequestParams params = new RequestParams();
                                            params.put("picture", file);
                                            MyApplication.newInstance().getAsyncHttpClient().post(MyApplication.buildURL("/user/update/head")
                                                    , params
                                                    , new AsyncHttpResponseHandler() {
                                                        @Override
                                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                            Log.d(TAG, new String(responseBody));
                                                            File file = new File(path, "picture.png");
                                                            if (file.exists()) {
                                                                file.delete();
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                                            Log.d(TAG, new String(responseBody));
                                                        }
                                                    });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    MyApplication.newInstance().setUser(user);
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
                break;
            case R.id.me_detail_picture:
                if (editable) {
                    GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 选取图片后的回调
     */
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                Log.e(TAG, resultList.get(0).getPhotoPath());
                headPath = resultList.get(0).getPhotoPath();
                iv_me_head_portrait.setImageURI(Uri.fromFile(new File(headPath)));
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Log.e(TAG, errorMsg);
        }
    };

    private void postFile(String key, String path) throws
            Exception {
        RequestParams params = new RequestParams();
        File file = new File(path);
        params.put(key, file);
        MyApplication.newInstance().getAsyncHttpClient().post(MyApplication.buildURL("/user/update/head")
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

    private boolean checkFieldIsLegal() {
        return false;
    }
}
