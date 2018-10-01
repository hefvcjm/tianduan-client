package com.tianduan.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WebSocketReceiver extends BroadcastReceiver {

    private static final String TAG = "WebSocketReceiver";

    private OnReceive receive;

    public void setOnReceive(OnReceive receive) {
        this.receive = receive;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String data = intent.getStringExtra("data");
//        receive.onReceive(data);
        Log.d(TAG, data);
    }

    interface OnReceive {
        void onReceive(String data);
    }
}
