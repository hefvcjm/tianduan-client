package com.tianduan.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianduan.MyApplication;
import com.tianduan.activities.ChatActivity;
import com.tianduan.activities.R;
import com.tianduan.model.Engineer;
import com.tianduan.model.Maintain;
import com.tianduan.model.Status;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "MyExpandableListAdapter";

    private Context context;
    private List<Maintain> items;
    private ButtonInterface buttonInterface;
    private OnGroupExpandedListener onGroupExpandedListener;

    public MyExpandableListAdapter(Context context, List<Maintain> item, ButtonInterface buttonInterface) {
        this.context = context;
        this.items = item;
        this.buttonInterface = buttonInterface;
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
        groupViewHolder.status.setText(items.get(groupPosition).getRepair().getStatuses().iterator().next().getStatus());
        groupViewHolder.time.setText(items.get(groupPosition).getRepair().getTime().split(" ")[0].replace("-", "/"));
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand_child_repair_detail, parent, false);
            childViewHolder = new ChildViewHolder();
            //status bar
            childViewHolder.iv_expand_status_icon_1 = convertView.findViewById(R.id.iv_expand_status_icon_1);
            childViewHolder.iv_expand_status_icon_2 = convertView.findViewById(R.id.iv_expand_status_icon_2);
            childViewHolder.iv_expand_status_icon_3 = convertView.findViewById(R.id.iv_expand_status_icon_3);
            childViewHolder.tv_expand_status_1 = convertView.findViewById(R.id.tv_expand_status_1);
            childViewHolder.tv_expand_status_2 = convertView.findViewById(R.id.tv_expand_status_2);
            childViewHolder.tv_expand_status_3 = convertView.findViewById(R.id.tv_expand_status_3);
            childViewHolder.iv_expand_status_1_to_2 = convertView.findViewById(R.id.iv_expand_status_1_to_2);
            childViewHolder.iv_expand_status_2_to_3 = convertView.findViewById(R.id.iv_expand_status_2_to_3);
            //status description
            childViewHolder.tv_item_detail_status = convertView.findViewById(R.id.tv_item_detail_status);
            childViewHolder.tv_item_detail_description = convertView.findViewById(R.id.tv_item_detail_description);
            //engineer
            childViewHolder.iv_repair_expand_repair_engineer_picture = convertView.findViewById(R.id.iv_repair_expand_repair_engineer_picture);
            childViewHolder.tv_item_detail_engineer_name = convertView.findViewById(R.id.tv_item_detail_engineer_name);
            childViewHolder.bn_item_detail_engineer_contract = convertView.findViewById(R.id.bn_item_detail_engineer_contract);
            childViewHolder.ll_expand_detail_engineer = convertView.findViewById(R.id.ll_expand_detail_engineer);
            //completed
            childViewHolder.bn_item_expand_repair_completed = convertView.findViewById(R.id.bn_item_expand_repair_completed);
            //contract service
            childViewHolder.tv_item_expand_repair_contract_service = convertView.findViewById(R.id.tv_item_expand_repair_contract_service);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.bn_item_detail_engineer_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonInterface.onClick(v, groupPosition);
            }
        });
        Iterator<Status> statusIterator = items.get(groupPosition).getRepair().getStatuses().iterator();
        Map<String, String> map = new HashMap<>();
        while (statusIterator.hasNext()) {
            Status status = statusIterator.next();
            map.put(status.getStatus(), status.getTime());
        }
        Set<String> keySet = map.keySet();
        if (keySet.contains("报修完成")) {
            //status bar
            childViewHolder.iv_expand_status_icon_1.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.mipmap.ic_repair_done));
            childViewHolder.iv_expand_status_icon_2.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.mipmap.ic_repair_done));
            childViewHolder.iv_expand_status_icon_3.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.mipmap.ic_repair_done));
            childViewHolder.tv_expand_status_1.setTextColor(MyApplication.newInstance().getResources().getColor(R.color.gray));
            childViewHolder.tv_expand_status_2.setTextColor(MyApplication.newInstance().getResources().getColor(R.color.gray));
            childViewHolder.tv_expand_status_3.setTextColor(MyApplication.newInstance().getResources().getColor(R.color.dark));
            childViewHolder.iv_expand_status_1_to_2.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.drawable.solid_line));
            childViewHolder.iv_expand_status_2_to_3.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.drawable.solid_line));
            //status description
            childViewHolder.tv_item_detail_status.setText("报修完成");
            childViewHolder.tv_item_detail_description.setText(map.get("报修完成"));
        } else if (keySet.contains("派单")) {
            //status bar
            childViewHolder.iv_expand_status_icon_1.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.mipmap.ic_repair_done));
            childViewHolder.iv_expand_status_icon_2.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.mipmap.ic_repair_dealing));
            childViewHolder.iv_expand_status_icon_3.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.mipmap.ic_repair_last));
            childViewHolder.tv_expand_status_1.setTextColor(MyApplication.newInstance().getResources().getColor(R.color.gray));
            childViewHolder.tv_expand_status_2.setTextColor(MyApplication.newInstance().getResources().getColor(R.color.dark));
            childViewHolder.tv_expand_status_3.setTextColor(MyApplication.newInstance().getResources().getColor(R.color.gray));
            childViewHolder.iv_expand_status_1_to_2.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.drawable.solid_line));
            childViewHolder.iv_expand_status_2_to_3.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.drawable.dotted_line));
            //status description
            childViewHolder.tv_item_detail_status.setText("派单");
            childViewHolder.tv_item_detail_description.setText(map.get("派单"));
        } else {
            //status bar
            childViewHolder.iv_expand_status_icon_1.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.mipmap.ic_repair_done));
            childViewHolder.iv_expand_status_icon_2.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.mipmap.ic_repair_dealing));
            childViewHolder.iv_expand_status_icon_3.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.mipmap.ic_repair_last));
            childViewHolder.tv_expand_status_1.setTextColor(MyApplication.newInstance().getResources().getColor(R.color.gray));
            childViewHolder.tv_expand_status_2.setTextColor(MyApplication.newInstance().getResources().getColor(R.color.dark));
            childViewHolder.tv_expand_status_3.setTextColor(MyApplication.newInstance().getResources().getColor(R.color.gray));
            childViewHolder.iv_expand_status_1_to_2.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.drawable.solid_line));
            childViewHolder.iv_expand_status_2_to_3.setImageDrawable(MyApplication.newInstance().getResources().getDrawable(R.drawable.dotted_line));
            //status description
            childViewHolder.tv_item_detail_status.setText("派单");
            childViewHolder.tv_item_detail_description.setText(map.get("派单"));
        }
        //engineer
        Engineer engineer = null;
        if (items.get(groupPosition).getEngineers() != null && items.get(groupPosition).getEngineers().iterator().hasNext()) {
            engineer = items.get(groupPosition).getEngineers().iterator().next();
        }
        if (engineer != null) {
            if (engineer.getUser().getPicture() != null) {
                //childViewHolder.iv_repair_expand_repair_engineer_picture = convertView.findViewById(R.id.iv_repair_expand_repair_engineer_picture);
            }
            childViewHolder.ll_expand_detail_engineer.setVisibility(View.VISIBLE);
            //childViewHolder.iv_repair_expand_repair_engineer_picture = convertView.findViewById(R.id.iv_repair_expand_repair_engineer_picture);
            childViewHolder.tv_item_detail_engineer_name.setText(engineer.getUser().getName());
        } else {
            childViewHolder.ll_expand_detail_engineer.setVisibility(View.GONE);
        }
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

    static class GroupViewHolder {
        TextView title;
        TextView ticket;
        TextView status;
        TextView time;
    }

    class ChildViewHolder {
        //status bar
        ImageView iv_expand_status_icon_1;
        ImageView iv_expand_status_icon_2;
        ImageView iv_expand_status_icon_3;
        TextView tv_expand_status_1;
        TextView tv_expand_status_2;
        TextView tv_expand_status_3;
        ImageView iv_expand_status_1_to_2;
        ImageView iv_expand_status_2_to_3;

        //status description
        TextView tv_item_detail_status;
        TextView tv_item_detail_description;

        //engineer
        ImageView iv_repair_expand_repair_engineer_picture;
        TextView tv_item_detail_engineer_name;
        Button bn_item_detail_engineer_contract;
        LinearLayout ll_expand_detail_engineer;

        //completed
        Button bn_item_expand_repair_completed;

        //contract service
        TextView tv_item_expand_repair_contract_service;
    }
}
