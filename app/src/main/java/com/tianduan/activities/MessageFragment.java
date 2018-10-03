package com.tianduan.activities;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tianduan.MyApplication;
import com.tianduan.adapters.MessageListAdapter;
import com.tianduan.model.MessageItem;
import com.tianduan.model.MsgData;
import com.tianduan.util.ChatUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {

    private static final String TAG = "MessageFragment";

    private TextView tv_top_bar_title;
    private TextView tv_top_bar_right;
    private ListView lv_message_list;
    private ImageView iv_nothing;

    private MessageListAdapter adapter;

    private IntentFilter intentFilter;
    private ChatReceiver localReceiver;

    List<MessageItem> messageItems;
    List<String> objectIds;

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

        messageItems = MyApplication.newInstance().getMessageItems();
        objectIds = MyApplication.newInstance().getMessageObjectIds();
        if (messageItems == null) {
            messageItems = new ArrayList<>();
            objectIds = new ArrayList<>();
        }
        if (adapter == null) {
            adapter = new MessageListAdapter(messageItems);
        }
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
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("name", messageItems.get(position).getName());
                intent.putExtra("sender", messageItems.get(position).getObjectId());
                startActivity(intent);
            }
        });

        intentFilter = new IntentFilter();
        intentFilter.addAction("com.tianduan.broadcast.WEBSOCKET");
        localReceiver = new ChatReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(localReceiver, intentFilter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        messageItems = MyApplication.newInstance().getMessageItems();
        objectIds = MyApplication.newInstance().getMessageObjectIds();
        if (messageItems == null) {
            messageItems = new ArrayList<>();
            objectIds = new ArrayList<>();
        }
        if (adapter == null) {
            adapter = new MessageListAdapter(messageItems);
        }
        lv_message_list.setAdapter(adapter);
        if (messageItems.size() == 0) {
            lv_message_list.setVisibility(View.GONE);
            iv_nothing.setVisibility(View.VISIBLE);
        } else {
            lv_message_list.setVisibility(View.VISIBLE);
            iv_nothing.setVisibility(View.GONE);
        }
    }

    private List<MessageItem> getMessageList() {
        List<MessageItem> list = new ArrayList<>();
        list.add(new MessageItem());
        list.add(new MessageItem());
        list.add(new MessageItem());
        return list;
    }

    public class ChatReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getStringExtra("data");
            Log.d(TAG, "receive :" + str);
            try {
                MsgData msg = new MsgData(str);
                msg.setRole(MsgData.TYPE_RECEIVER);
                String senderObjectId = msg.getSender();
                if (objectIds.contains(senderObjectId)) {
                    int index = objectIds.indexOf(senderObjectId);
                    MessageItem item = messageItems.get(index);
                    item.setContent(msg.getContent());
                    item.setTimeStamp(msg.getTime().getTime());
                    item.setTime(ChatUtil.calculateShowTime(item.getTimeStamp(), item.getTimeStamp() - 60 * 1000 - 10));
                    item.setName(msg.getSenderName());
                    messageItems.remove(item);
                    messageItems.add(0, item);
                    objectIds.remove(senderObjectId);
                    objectIds.add(0, senderObjectId);
                    adapter.notifyDataSetChanged();
                } else {
                    MessageItem item = new MessageItem();
                    item.setTimeStamp(msg.getTime().getTime());
                    item.setTime(ChatUtil.calculateShowTime(item.getTimeStamp(), item.getTimeStamp() - 60 * 1000 - 10));
                    item.setContent(msg.getContent());
                    item.setObjectId(msg.getSender());
                    item.setName(msg.getSenderName());
                    messageItems.add(0, item);
                    objectIds.add(0, senderObjectId);
                    adapter.notifyDataSetChanged();
                }
                if (messageItems.size() != 0) {
                    lv_message_list.setVisibility(View.VISIBLE);
                    iv_nothing.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
