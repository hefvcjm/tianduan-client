package com.tiamuan.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.tiamuan.activities.R;
import com.tiamuan.model.Maintain;

import java.util.List;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "MyExpandableListAdapter";

    List<Maintain> items;
    private OnGroupExpandedListener onGroupExpandedListener;

    public MyExpandableListAdapter(List<Maintain> item) {
        this.items = item;
    }

    public void setOnGroupExpandedListener(OnGroupExpandedListener onGroupExpandedListener) {
        this.onGroupExpandedListener = onGroupExpandedListener;
    }

    @Override
    public int getGroupCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return items.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand_group_repair, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.title = convertView.findViewById(R.id.tv_item_repair_title);
            groupViewHolder.ticket = convertView.findViewById(R.id.tv_item_repair_ticket);
            groupViewHolder.status = convertView.findViewById(R.id.tv_item_repair_status);
            groupViewHolder.time = convertView.findViewById(R.id.tv_item_repair_time);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.title.setText("这是标题");
        groupViewHolder.ticket.setText(items.get(groupPosition).getRepair().getTicket());
        groupViewHolder.status.setText(items.get(groupPosition).getRepair().getStatuses().toString());
        groupViewHolder.time.setText(items.get(groupPosition).getRepair().getTime());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand_child_repair_detail, parent, false);
//            childViewHolder = new ChildViewHolder();
//            childViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_expand_child);
//            convertView.setTag(childViewHolder);
        } else {
//            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
//        childViewHolder.tvTitle.setText(childData[groupPosition][childPosition]);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        Log.d(TAG, "onGroupExpanded() called with: groupPosition = [" + groupPosition + "]");
        if (onGroupExpandedListener != null) {
            onGroupExpandedListener.onGroupExpanded(groupPosition);
        }
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        Log.d(TAG, "onGroupCollapsed() called with: groupPosition = [" + groupPosition + "]");
    }

    class GroupViewHolder {
        TextView title;
        TextView ticket;
        TextView status;
        TextView time;
    }

    class ChildViewHolder{

    }
}
