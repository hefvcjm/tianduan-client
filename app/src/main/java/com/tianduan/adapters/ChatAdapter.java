package com.tianduan.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianduan.model.MsgData;

import java.util.List;

import com.tianduan.activities.R;

/**
 * Created by fengshawn on 2017/8/10.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MsgViewHolder> {

    private List<MsgData> listData;
    private Context context;

    public ChatAdapter(Context context, List<MsgData> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public MsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_msg_list, parent, false);
        return new MsgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MsgViewHolder holder, int position) {
        MsgData currentMsgData = listData.get(position);
        MsgData preMsgData = null;
        if (position >= 1)
            preMsgData = listData.get(position - 1);
        switch (currentMsgData.getRole()) {
            case MsgData.TYPE_RECEIVER:
                initTimeStamp(holder, currentMsgData, preMsgData);
                holder.senderLayout.setVisibility(View.GONE);
                holder.receiverLayout.setVisibility(View.VISIBLE);
                holder.receiveMsg.setText(currentMsgData.getMsg());
                holder.receiver_profile.setImageResource(currentMsgData.getProfile_res());
                break;


            case MsgData.TYPE_SENDER:
                initTimeStamp(holder, currentMsgData, preMsgData);
                holder.senderLayout.setVisibility(View.VISIBLE);
                holder.receiverLayout.setVisibility(View.GONE);
                holder.sendMsg.setText(currentMsgData.getMsg());
                holder.send_profile.setImageResource(currentMsgData.getProfile_res());
                break;
        }
    }

    private void initTimeStamp(MsgViewHolder holder, MsgData currentMsgData, MsgData preMsgData) {
        String showTime;
        if (preMsgData == null) {
            showTime = HelpUtils.calculateShowTime(HelpUtils.getCurrentMillisTime(), currentMsgData.getTimeStamp());
        } else {
            showTime = HelpUtils.calculateShowTime(currentMsgData.getTimeStamp(), preMsgData.getTimeStamp());
        }
        if (showTime != null) {
            holder.timeStamp.setVisibility(View.VISIBLE);
            holder.timeStamp.setText(showTime);
        } else {
            holder.timeStamp.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class MsgViewHolder extends RecyclerView.ViewHolder {

        ImageView item_msg_iv_receiver_profile;
        ImageView item_msg_iv_sender_profile;
        TextView item_msg_iv_time_stamp;
        TextView item_msg_tv_receiver_msg;
        TextView item_msg_tv_sender_msg;
        RelativeLayout item_msg_layout_sender;
        LinearLayout item_msg_layout_receiver;

        public MsgViewHolder(View itemView) {
            super(itemView);
            item_msg_iv_receiver_profile = itemView.findViewById(R.id.item_msg_iv_receiver_profile);
            item_msg_iv_sender_profile = itemView.findViewById(R.id.item_msg_iv_sender_profile);
            item_msg_iv_time_stamp = itemView.findViewById(R.id.item_msg_iv_time_stamp);
            item_msg_tv_receiver_msg = itemView.findViewById(R.id.item_msg_tv_receiver_msg);
            item_msg_tv_sender_msg = itemView.findViewById(R.id.item_msg_tv_sender_msg);
            item_msg_layout_sender = itemView.findViewById(R.id.item_msg_layout_sender);
            item_msg_layout_receiver = itemView.findViewById(R.id.item_msg_layout_receiver);
        }
    }
}
