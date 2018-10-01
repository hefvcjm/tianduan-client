package com.tianduan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianduan.activities.R;
import com.tianduan.model.Maintain;

import java.util.List;

public class RepairHistoryListAdapter extends BaseAdapter {

    private static final String TAG = "MyExpandableListAdapter";

    List<Maintain> items;

    public RepairHistoryListAdapter(List<Maintain> items) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand_group_repair, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = convertView.findViewById(R.id.tv_item_repair_title);
            viewHolder.ticket = convertView.findViewById(R.id.tv_item_repair_ticket);
            viewHolder.status = convertView.findViewById(R.id.tv_item_repair_status);
            viewHolder.time = convertView.findViewById(R.id.tv_item_repair_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText("这是标题");
        viewHolder.ticket.setText(items.get(position).getRepair().getTicket());
        viewHolder.status.setText(items.get(position).getRepair().getStatuses().iterator().next().getStatus());
        viewHolder.time.setText(items.get(position).getRepair().getTime().split(" ")[0].replace("-", "/"));
        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView ticket;
        TextView status;
        TextView time;
    }
}
