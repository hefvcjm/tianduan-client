package com.tiamuan.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MeFragment";

    private LinearLayout ll_me_user;
    private LinearLayout ll_me_emall;
    private LinearLayout ll_me_devices;
    private LinearLayout ll_me_repairs;
    private LinearLayout ll_me_setting;
    private LinearLayout ll_me_contract;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ll_me_user = view.findViewById(R.id.ll_me_user);
        ll_me_emall = view.findViewById(R.id.ll_me_emall);
        ll_me_devices = view.findViewById(R.id.ll_me_devices);
        ll_me_repairs = view.findViewById(R.id.ll_me_repairs);
        ll_me_setting = view.findViewById(R.id.ll_me_setting);
        ll_me_contract = view.findViewById(R.id.ll_me_contract);
        ll_me_user.setOnClickListener(this);
        ll_me_emall.setOnClickListener(this);
        ll_me_devices.setOnClickListener(this);
        ll_me_repairs.setOnClickListener(this);
        ll_me_setting.setOnClickListener(this);
        ll_me_contract.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_me_user:
                startActivity(new Intent(getActivity(), MeDetailActivity.class));
                break;
            case R.id.ll_me_emall:
                break;
            case R.id.ll_me_devices:
                break;
            case R.id.ll_me_repairs:
                startActivity(new Intent(getActivity(), RepairHistoryActivity.class));
                break;
            case R.id.ll_me_setting:
                break;
            case R.id.ll_me_contract:
                break;
            default:
                break;
        }
    }
}
