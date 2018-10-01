package com.tianduan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianduan.activities.R;
import com.tianduan.model.Maintain;
import com.tianduan.model.MessageItem;

import java.util.List;

public class MessageListAdapter extends BaseAdapter {

    private static final String TAG = "MessageListAdapter";

    List<MessageItem> items;

    public MessageListAdapter(List<MessageItem> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.iv_message_item_head = convertView.findViewById(R.id.iv_message_item_head);
            viewHolder.tv_message_item_name = convertView.findViewById(R.id.tv_message_item_name);
            viewHolder.tv_message_item_time = convertView.findViewById(R.id.tv_message_item_time);
            viewHolder.tv_message_item_content = convertView.findViewById(R.id.tv_message_item_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.iv_message_item_head.setImageDrawable();
//        viewHolder.tv_message_item_name.setText(items.get(position).getName());
//        viewHolder.tv_message_item_time.setText(items.get(position).getTime());
//        viewHolder.tv_message_item_content.setText(items.get(position).getContent());
        return convertView;
    }

    class ViewHolder {
        ImageView iv_message_item_head;
        TextView tv_message_item_name;
        TextView tv_message_item_time;
        TextView tv_message_item_content;
    }
}
