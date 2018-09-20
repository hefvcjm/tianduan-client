package com.tiamuan.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (id) {
            case R.id.ll_bar_message:
                MessageFragment messageFragment = new MessageFragment();
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
                RepairFragment repairFragment = new RepairFragment();
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
                MeFragment meFragment = new MeFragment();
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
}
