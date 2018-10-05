package com.tianduan.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tianduan.MyApplication;
import com.tianduan.model.User;
import com.tianduan.net.MyHttpRequest;
import com.tianduan.services.InitService;
import com.tianduan.services.MyService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";

    public static final String LOGIN_URL = "http://192.168.2.224:8080/tianduan/user/login";

    private String phone;
    private String password;
    private User user;

    private EditText et_phone;
    private EditText et_password;
    private Button bn_login;
    private TextView tv_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MyApplication.newInstance().getUser() != null) {
            if (!MyApplication.newInstance().isServiceRunning(MyService.class.getName())) {
                startService(new Intent(LoginActivity.this, MyService.class));
            }
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        et_phone = findViewById(R.id.et_login_phone);
        et_password = findViewById(R.id.et_login_password);
        bn_login = findViewById(R.id.bn_login);
        tv_log = findViewById(R.id.tv_log);
        tv_log.setMovementMethod(ScrollingMovementMethod.getInstance());

        bn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                finish();
                phone = et_phone.getText().toString();
                password = et_password.getText().toString();

                MyHttpRequest stringRequest = new MyHttpRequest(Request.Method.POST
                        , MyApplication.buildURL("/user/login")
                        , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Log.d(TAG, response);
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getInt("code") == 0) {
                                user = gson.fromJson(json.getString("data"), User.class);
                                MyApplication.newInstance().setUser(user);
                                tv_log.setText(user.toString());
                                Log.d(TAG, user.toString());
                                startService(new Intent(LoginActivity.this, MyService.class));
                                startService(new Intent(LoginActivity.this, InitService.class));
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else if (json.getInt("code") == 1004) {
                                Toast.makeText(LoginActivity.this, json.getJSONObject("data").getString("description"), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(LoginActivity.this, json.getString("msg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tv_log.setText("error");
                        Toast.makeText(LoginActivity.this, "发生错误", Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("phone", et_phone.getText().toString());
                        params.put("password", et_password.getText().toString());
                        return params;
                    }
                };
                MyApplication.newInstance().getRequestQueue().add(stringRequest);
            }
        });
    }
}
