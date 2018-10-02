package com.tianduan.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
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

import com.tianduan.adapters.ChatAdapter;
import com.tianduan.model.MsgData;
import com.tianduan.util.ChatUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class ChatActivity extends Activity {

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
    ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);

        initComponents();
        String name = getIntent().getStringExtra("name");
        profileId = getIntent().getIntExtra("profileId", R.mipmap.ic_head_portrait);
        if (name != null) {
            tv_top_bar_title.setText(name);
        }

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


        String[] msgs = {"在吗", "不在", "不在怎么回消息的", "我是天才机器人", "机器人这么智能啊", "是啊，时代在发展", "那你唱歌看看"
                , "你叫唱就唱啊，多没面子", "那你能干嘛", "啥都不能", "那你有啥用？", "没啥用你和我聊啥", "我想看下有到底有啥用",
                "你这智商看不出来的", "你智商能看出来啥？", "你智商不足", "不带你这么聊天的...", "那你说应该怎么聊天才对？",
                "没啥对不对就是瞎聊", "看到一条新闻\"43岁男友不回家带饭 27岁女友放火点房子涉刑罪\" ， 好逗！！！", "哈哈哈哈~~~"};
        data = new ArrayList<>();

        for (int i = 0; i < msgs.length; i++) {
            MsgData msgData = new MsgData();
            msgData.setRole(i % 2 == 0 ? MsgData.TYPE_RECEIVER : MsgData.TYPE_SENDER);
            msgData.setContent(msgs[i]);
            msgData.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            data.add(i, msgData);
        }

        adapter = new ChatAdapter(this, data);
        rv.setAdapter(adapter);
        rv.scrollToPosition(data.size() - 1);
        final Handler smartReplyMsgHandler = new Handler();


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendMsg = et_msg.getText().toString();
                MsgData msgData = new MsgData();
                msgData.setRole(MsgData.TYPE_SENDER);
                msgData.setContent(sendMsg);
                msgData.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                data.add(data.size(), msgData);
                adapter.notifyDataSetChanged();
                rv.scrollToPosition(data.size() - 1);
                et_msg.setText("");

                smartReplyMsgHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        {
                            MsgData msgData1 = getRandomMessage();
                            data.add(data.size(), msgData1);
                            adapter.notifyDataSetChanged();
                            rv.scrollToPosition(data.size() - 1);
                            smartReplyMsgHandler.removeCallbacksAndMessages(null);
                        }
                    }
                }, 500);
            }
        });
    }


    private MsgData getRandomMessage() {
        String[] randomMsgs = {"我是机器人", "你发再多我主人也看不到", "不要回了", "你好无聊啊", "其实你发的没点用，我会全部把它过滤掉"
                , "因为我是机器人", "不管你信不信，反正我是不信", "再发我就神经错乱了", "我无法正常跟你沟通", "你说的我懂，我就是不做", "哈哈哈~~~"
                , "嘻嘻", "呵呵", "你可以走了", "我没逻辑的不要奢求多了", "我就呵呵了", "什么鬼", "我不懂", "啥意思？", "好了，你可以退下了", "本机器宝宝累了"};
        Random random = new Random();
        int pos = random.nextInt(randomMsgs.length);
        MsgData msgData = new MsgData();
        msgData.setRole(MsgData.TYPE_RECEIVER);
        msgData.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        if (pos >= 0 && pos < randomMsgs.length - 1)
            msgData.setContent(randomMsgs[pos]);
        else
            msgData.setContent(randomMsgs[0]);
        return msgData;
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

}
