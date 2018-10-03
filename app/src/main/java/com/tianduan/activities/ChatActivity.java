package com.tianduan.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianduan.MyApplication;
import com.tianduan.adapters.ChatAdapter;
import com.tianduan.model.MsgData;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ChatActivity extends Activity {

    private static final String TAG = "ChatActivity";

    private TextView tv_top_bar_title;
    private ImageView iv_back;
    private ImageView iv_voice;
    private EditText et_msg;
    private ImageView iv_emoji;
    private ImageView iv_add;
    private Button btn_send;
    private RecyclerView rv;
    private int profileId = R.mipmap.ic_head_portrait;

    private List<MsgData> data;
    private ChatAdapter adapter;

    private IntentFilter intentFilter;
    private LocalChatReceiver localReceiver;

    private String name;
    private String senderObjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);

        initComponents();
    }

    private void initComponents() {
        tv_top_bar_title = findViewById(R.id.tv_top_bar_title);
        iv_back = findViewById(R.id.iv_back);
        iv_voice = findViewById(R.id.activity_chat_iv_voice);
        et_msg = findViewById(R.id.activity_chat_et_msg);
        iv_emoji = findViewById(R.id.activity_chat_iv_emoji);
        iv_add = findViewById(R.id.activity_chat_iv_add);
        btn_send = findViewById(R.id.activity_chat_btn_send);
        rv = findViewById(R.id.rv_chat_view);
        rv.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        senderObjectId = intent.getStringExtra("sender");
        profileId = getIntent().getIntExtra("profileId", R.mipmap.ic_head_portrait);
        if (name != null) {
            tv_top_bar_title.setText(name);
        }

        intentFilter = new IntentFilter();
        intentFilter.addAction("com.tianduan.broadcast.WEBSOCKET");
        localReceiver = new LocalChatReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(localReceiver, intentFilter);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_send.startAnimation(getVisibleAnim(false, btn_send));
        btn_send.setVisibility(View.GONE);


        et_msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("tag", "onTextChanged --- start -> " + start + " , count ->" + count + "，before ->" + before);
                if (start == 0 && count > 0) {
                    btn_send.startAnimation(getVisibleAnim(true, btn_send));
                    btn_send.setVisibility(View.VISIBLE);
                    iv_add.setVisibility(View.GONE);
                }

                if (start == 0 && count == 0) {
                    //btn_send.startAnimation(getVisibleAnim(false, btn_send));
                    btn_send.setVisibility(View.GONE);
                    iv_add.startAnimation(getVisibleAnim(true, iv_add));
                    iv_add.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        data = MyApplication.newInstance().getChatMap().get(senderObjectId);
        if (data == null) {
            data = new ArrayList<>();
        }
        adapter = new ChatAdapter(this, data);
        rv.setAdapter(adapter);
        rv.scrollToPosition(data.size() - 1);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendMsg = et_msg.getText().toString();
                MsgData msgData = new MsgData();
                msgData.setRole(MsgData.TYPE_SENDER);
                msgData.setContent(sendMsg);
                msgData.setTime(new Date());
                msgData.setContentType("text");
                msgData.setType("message");
                msgData.setReceiverType("person");
                msgData.setReceiverId(senderObjectId);
                msgData.setSender(MyApplication.newInstance().getUser().getObjectId());
                MyApplication.newInstance().webSocketClientSend(msgData);
                Log.d(TAG, "send msg:" + msgData.createMessage().toString());
                data = MyApplication.newInstance().getChatMap().get(senderObjectId);
                adapter.notifyDataSetChanged();
                rv.scrollToPosition(data.size() - 1);
                et_msg.setText("");
            }
        });
    }

    private Animation getVisibleAnim(boolean show, View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int y = view.getMeasuredHeight() / 4;
        int x = view.getMeasuredWidth() / 4;
        if (show) {
            ScaleAnimation showAnim = new ScaleAnimation(0.01f, 1f, 0.01f, 1f, x, y);
            showAnim.setDuration(200);
            return showAnim;

        } else {

            ScaleAnimation hiddenAnim = new ScaleAnimation(1f, 0.01f, 1f, 0.01f, x, y);
            hiddenAnim.setDuration(200);
            return hiddenAnim;
        }
    }

    class LocalChatReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            data = MyApplication.newInstance().getChatMap().get(senderObjectId);
            adapter.notifyDataSetChanged();
            rv.scrollToPosition(data.size() - 1);
//            String str = intent.getStringExtra("data");
//            Log.d(TAG, "receive :" + str);
//            try {
//                MsgData msg = new MsgData(str);
//                msg.setRole(MsgData.TYPE_RECEIVER);
//                if (senderObjectId.equals(msg.getSender())) {
//                    data.add(data.size(), msg);
//                    adapter.notifyDataSetChanged();
//                    rv.scrollToPosition(data.size() - 1);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
    }

}
