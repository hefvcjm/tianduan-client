package com.tiamuan.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MessageActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MessageActivity";

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
        setContentView(R.layout.activity_message);
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

        iv_bar_message.setImageResource(R.mipmap.ic_message_active);
        tv_bar_message.setTextColor(getResources().getColor(R.color.button_bar_text_active));

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
        switch (id) {
            case R.id.ll_bar_message:
                break;
            case R.id.ll_bar_repair:
                startActivity(new Intent(MessageActivity.this, RepairActivity.class));
                break;
            case R.id.ll_bar_me:
                startActivity(new Intent(MessageActivity.this, MeActivity.class));
                break;
            default:
                break;
        }
    }
}
