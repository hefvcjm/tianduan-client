package com.tiamuan.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tiamuan.adapters.Constant;
import com.tiamuan.adapters.NormalExpandableListAdapter;
import com.tiamuan.adapters.OnGroupExpandedListener;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * 普通 ExpandableListView，支持只展开一个子项
 */
public class RepairActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "RepairActivity";
    private ExpandableListView mExpandableListView;

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
        setContentView(R.layout.activity_repair);
        mExpandableListView = findViewById(R.id.expandable_list);
        final NormalExpandableListAdapter adapter = new NormalExpandableListAdapter(Constant.BOOKS, Constant.FIGURES);
        mExpandableListView.setAdapter(adapter);
        adapter.setOnGroupExpandedListener(new OnGroupExpandedListener() {
            @Override
            public void onGroupExpanded(int groupPosition) {
                expandOnlyOne(groupPosition);
            }
        });

        //  设置分组项的点击监听事件
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.d(TAG, "onGroupClick: groupPosition:" + groupPosition + ", id:" + id);
                // 请务必返回 false，否则分组不会展开
                return false;
            }
        });

        //  设置子选项点击监听事件
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(RepairActivity.this, Constant.FIGURES[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
                return true;
            }
        });
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
        switch (id) {
            case R.id.ll_bar_message:
                startActivity(new Intent(RepairActivity.this, MessageActivity.class));
                break;
            case R.id.ll_bar_repair:
                break;
            case R.id.ll_bar_me:
                startActivity(new Intent(RepairActivity.this, MeActivity.class));
                break;
            default:
                break;
        }
    }

    // 每次展开一个分组后，关闭其他的分组
    private boolean expandOnlyOne(int expandedPosition) {
        boolean result = true;
        int groupLength = mExpandableListView.getExpandableListAdapter().getGroupCount();
        for (int i = 0; i < groupLength; i++) {
            if (i != expandedPosition && mExpandableListView.isGroupExpanded(i)) {
                result &= mExpandableListView.collapseGroup(i);
            }
        }
        return result;
    }

}
