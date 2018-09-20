package com.tiamuan.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tiamuan.util.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {

    private String phone;
    private String password;

    private EditText et_phone;
    private EditText et_password;
    private Button bn_login;
    private TextView tv_log;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        et_phone = findViewById(R.id.et_login_phone);
        et_password = findViewById(R.id.et_login_password);
        bn_login = findViewById(R.id.bn_login);
        tv_log = findViewById(R.id.tv_log);
        queue = HttpUtil.getInstance(this).getRequestQueue();

        bn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = et_phone.getText().toString();
                password = et_password.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST
                        , "http://192.168.2.224:8080/tianduan/user/login"
                        , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        tv_log.setText(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tv_log.setText("error");
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
                queue.add(stringRequest);
            }
        });
    }
}
