package com.tiamuan.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tiamuan.MyApplication;
import com.tiamuan.adapters.Constant;
import com.tiamuan.adapters.MyExpandableListAdapter;
import com.tiamuan.adapters.NormalExpandableListAdapter;
import com.tiamuan.adapters.OnGroupExpandedListener;
import com.tiamuan.model.Maintain;
import com.tiamuan.net.MyHttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 普通 ExpandableListView，支持只展开一个子项
 */
public class RepairFragment extends Fragment {
    private static final String TAG = "RepairFragment";
    private ExpandableListView mExpandableListView;
    private MyExpandableListAdapter adapter;
    private ImageButton ib_new_repair;

    private TextView tv_top_bar_title;
    private TextView tv_top_bar_right;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repair, container, false);
        tv_top_bar_title = view.findViewById(R.id.tv_top_bar_title);
        tv_top_bar_right = view.findViewById(R.id.tv_top_bar_right);
        tv_top_bar_title.setText("报修");
        tv_top_bar_right.setText("建议与投诉");
        mExpandableListView = view.findViewById(R.id.expandable_list);

        MyHttpRequest stringRequest = new MyHttpRequest(Request.Method.GET
                , MyApplication.buildURL("/repair/queryall")
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
                        Maintain item = new Gson().fromJson(arrays.getString(i), Maintain.class);
                        if (item != null) {
                            maintains.add(item);
                        }
                    }
                    Log.d(TAG, maintains.size() + "");
                    adapter = new MyExpandableListAdapter(maintains);
                    mExpandableListView.setAdapter(adapter);
                    adapter.setOnGroupExpandedListener(new OnGroupExpandedListener() {
                        @Override
                        public void onGroupExpanded(int groupPosition) {
                            expandOnlyOne(groupPosition);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("hefvcjm-1", "error");
            }
        });
        MyApplication.newInstance().getRequestQueue().add(stringRequest);

//        adapter.setOnGroupExpandedListener(new OnGroupExpandedListener() {
//            @Override
//            public void onGroupExpanded(int groupPosition) {
//                expandOnlyOne(groupPosition);
//            }
//        });

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
//                Toast.makeText(RepairFragment.this, Constant.FIGURES[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        ib_new_repair = view.findViewById(R.id.ib_new_repair);
        ib_new_repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewRepairActivity.class));
            }
        });
        tv_top_bar_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SuggestArgueActivity.class));
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final NormalExpandableListAdapter adapter = new NormalExpandableListAdapter(Constant.BOOKS, Constant.FIGURES);
        mExpandableListView = ((MainActivity) getActivity()).findViewById(R.id.expandable_list);
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
//                Toast.makeText(RepairFragment.this, Constant.FIGURES[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
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
