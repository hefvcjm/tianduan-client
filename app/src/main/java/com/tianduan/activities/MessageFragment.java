package com.tianduan.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tianduan.adapters.MessageListAdapter;
import com.tianduan.model.MessageItem;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {

    private static final String TAG = "MessageFragment";

    private TextView tv_top_bar_title;
    private TextView tv_top_bar_right;
    private ListView lv_message_list;
    private ImageView iv_nothing;

    MessageListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        tv_top_bar_title = view.findViewById(R.id.tv_top_bar_title);
        tv_top_bar_right = view.findViewById(R.id.tv_top_bar_right);
        tv_top_bar_title.setText("聊天");
        tv_top_bar_right.setVisibility(View.GONE);

        lv_message_list = view.findViewById(R.id.lv_message_list);
        iv_nothing = view.findViewById(R.id.iv_nothing);

        lv_message_list.setVisibility(View.GONE);
        iv_nothing.setVisibility(View.VISIBLE);

        List<MessageItem> messageItems = getMessageList();
        adapter = new MessageListAdapter(messageItems);
        lv_message_list.setAdapter(adapter);
        if (messageItems.size() == 0) {
            lv_message_list.setVisibility(View.GONE);
            iv_nothing.setVisibility(View.VISIBLE);
        } else {
            lv_message_list.setVisibility(View.VISIBLE);
            iv_nothing.setVisibility(View.GONE);
        }

        lv_message_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), ChatActivity.class));
            }
        });

        return view;
    }

    private List<MessageItem> getMessageList() {
        List<MessageItem> list = new ArrayList<>();
        list.add(new MessageItem());
        list.add(new MessageItem());
        list.add(new MessageItem());
        return list;
    }
}
