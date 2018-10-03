package com.tianduan.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tianduan.MyApplication;
import com.tianduan.model.User;
import com.tianduan.net.MyHttpRequest;
import com.tianduan.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private LinearLayout ll_bar_message;
    private LinearLayout ll_bar_repair;
    private LinearLayout ll_bar_me;

    private ImageView iv_bar_message;
    private ImageView iv_bar_repair;
    private ImageView iv_bar_me;

    private TextView tv_bar_message;
    private TextView tv_bar_repair;
    private TextView tv_bar_me;

    MessageFragment messageFragment;
    RepairFragment repairFragment;
    MeFragment meFragment;
    FragmentManager manager;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        ll_bar_message = findViewById(R.id.ll_bar_message);
        ll_bar_repair = findViewById(R.id.ll_bar_repair);
        ll_bar_me = findViewById(R.id.ll_bar_me);

        iv_bar_message = findViewById(R.id.iv_bar_message);
        iv_bar_repair = findViewById(R.id.iv_bar_repair);
        iv_bar_me = findViewById(R.id.iv_bar_me);

        tv_bar_message = findViewById(R.id.tv_bar_message);
        tv_bar_repair = findViewById(R.id.tv_bar_repair);
        tv_bar_me = findViewById(R.id.tv_bar_me);

        iv_bar_repair.setImageResource(R.mipmap.ic_repair_active);
        tv_bar_repair.setTextColor(getResources().getColor(R.color.button_bar_text_active));

        messageFragment = new MessageFragment();
        repairFragment = new RepairFragment();
        meFragment = new MeFragment();
        manager = getFragmentManager();
//        transaction = manager.beginTransaction();
//        transaction.add(R.id.page_fragment, messageFragment)
//                .add(R.id.page_fragment, repairFragment)
//                .add(R.id.page_fragment, meFragment)
//                .hide(messageFragment)
//                .hide(meFragment)
//                .show(repairFragment)
//                .commit();

        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            Class<?> fieldType = field.getType();
            if (fieldType.isAssignableFrom(LinearLayout.class)) {
                field.setAccessible(true);
                try {
                    Method listener = fieldType.getMethod("setOnClickListener", View.OnClickListener.class);
                    if (listener != null) {
                        listener.invoke(field.get(this), this);
                        Log.d(TAG, field.getName() + "设置了setOnClickListener");
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

//        MyHttpRequest stringRequest = new MyHttpRequest(Request.Method.GET
//                , "http://192.168.2.224:8080/tianduan/repair/queryall"
//                , new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("hefvcjm-1", response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("hefvcjm-1", "error");
//            }
//        });
//        MyApplication.newInstance().getRequestQueue().add(stringRequest);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        transaction = manager.beginTransaction();
        switch (id) {
            case R.id.ll_bar_message:
//                transaction.show(messageFragment).hide(repairFragment).hide(meFragment);
                transaction.replace(R.id.page_fragment, messageFragment);
                transaction.commit();
                iv_bar_message.setImageResource(R.mipmap.ic_message_active);
                tv_bar_message.setTextColor(getResources().getColor(R.color.button_bar_text_active));
                iv_bar_me.setImageResource(R.mipmap.ic_me_default);
                tv_bar_me.setTextColor(getResources().getColor(R.color.button_bar_text_default));
                iv_bar_repair.setImageResource(R.mipmap.ic_repair_default);
                tv_bar_repair.setTextColor(getResources().getColor(R.color.button_bar_text_default));
                break;
            case R.id.ll_bar_repair:
//                transaction.hide(messageFragment).show(repairFragment).hide(meFragment);
                transaction.replace(R.id.page_fragment, repairFragment);
                transaction.commit();
                iv_bar_repair.setImageResource(R.mipmap.ic_repair_active);
                tv_bar_repair.setTextColor(getResources().getColor(R.color.button_bar_text_active));
                iv_bar_me.setImageResource(R.mipmap.ic_me_default);
                tv_bar_me.setTextColor(getResources().getColor(R.color.button_bar_text_default));
                iv_bar_message.setImageResource(R.mipmap.ic_message_default);
                tv_bar_message.setTextColor(getResources().getColor(R.color.button_bar_text_default));
                break;
            case R.id.ll_bar_me:
//                transaction.hide(messageFragment).hide(repairFragment).show(meFragment);
                transaction.replace(R.id.page_fragment, meFragment);
                transaction.commit();
                iv_bar_me.setImageResource(R.mipmap.ic_me_active);
                tv_bar_me.setTextColor(getResources().getColor(R.color.button_bar_text_active));
                iv_bar_message.setImageResource(R.mipmap.ic_message_default);
                tv_bar_message.setTextColor(getResources().getColor(R.color.button_bar_text_default));
                iv_bar_repair.setImageResource(R.mipmap.ic_repair_default);
                tv_bar_repair.setTextColor(getResources().getColor(R.color.button_bar_text_default));
                break;
            default:
                break;
        }
    }

    /**
     * 返回监听
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        PackageManager pm = getPackageManager();
        ResolveInfo homeInfo = pm.resolveActivity(
                new Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_HOME), 0);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityInfo ai = homeInfo.activityInfo;
            Intent startIntent = new Intent(Intent.ACTION_MAIN);
            startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            startIntent
                    .setComponent(new ComponentName(ai.packageName, ai.name));
            startActivitySafely(startIntent);
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    private void startActivitySafely(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


}
