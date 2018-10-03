package com.tianduan.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.tianduan.MyApplication;
import com.tianduan.model.MsgData;
import com.tianduan.model.User;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    private static final String TAG = "MyService";
    private static final int COUNT_OUT = 20;

    private User user;
    private int count = 0;
    private boolean isConnected = false;
    private final Timer timer = new Timer();
    TimerTask task;
    private WebSocketClient client;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        user = MyApplication.newInstance().getUser();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //connect();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "isConnected:" + isConnected);
                if (isConnected) {
                    client.send("{\"type\":\"ping\"}");
                    MyApplication.newInstance().setWebSocketClient(client);
                } else {
                    MyApplication.newInstance().setWebSocketClient(null);
                    if (count > COUNT_OUT) {
                        timer.cancel();
                        return;
                    }
                    count++;
                    connect();
                }
            }
        };
        timer.schedule(task, 0, 1000 * 2);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client = new WebSocketClient(new URI(MyApplication.BASE_CHAT_URL + "/" + user.getObjectId()), new Draft_17()) {
                        @Override
                        public void onOpen(ServerHandshake handshakedata) {
                            Log.d(TAG, "打开通道" + handshakedata.getHttpStatus());
                            count = 0;
                            isConnected = true;
                        }

                        @Override
                        public void onMessage(String message) {
                            Log.d(TAG, "收到消息：" + message);
                            try {
                                JSONObject json = new JSONObject(message);
                                String type = json.getString("type");
                                switch (type) {
                                    case "message":
                                        MsgData msg = new MsgData(message);
                                        msg.setRole(MsgData.TYPE_RECEIVER);
                                        List<MsgData> data = MyApplication.newInstance().getChatMap().get(msg.getSender());
                                        if (data == null) {
                                            data = new ArrayList<>();
                                        }
                                        data.add(msg);
                                        MyApplication.newInstance().setChatMap(msg.getSender(), data);
                                        Intent intent = new Intent("com.tianduan.broadcast.WEBSOCKET");
                                        intent.putExtra("data", message);
//                                        sendBroadcast(intent);
                                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                                        break;
                                    case "pong":
                                        Log.d(TAG, "心跳...");
                                        break;
                                    default:
                                        break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onClose(int code, String reason, boolean remote) {
                            Log.d(TAG, "关闭通道");
                            isConnected = false;
                        }

                        @Override
                        public void onError(Exception ex) {
                            Log.d(TAG, "连接出错");
                            isConnected = false;
                        }
                    };
                    Log.d(TAG, "websocket uri:" + client.getURI().getPath());
                    client.connectBlocking();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
//        try {
//            Thread.sleep(3000);
//            client.send(String.format("{" +
//                    "\"type\":\"message\"," +
//                    "\"sender\":\"%s\"," +
//                    "\"receiverType\":\"person\"," +
//                    "\"receiverId\":\"%s\"," +
//                    "\"contentType\":\"text\"," +
//                    "\"content\":\"test for chatting.\"" +
//                    "}", user.getObjectId(), user.getObjectId()));
//            Thread.sleep(3000);
//            client.send("{\"type\":\"ping\"}");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

}
