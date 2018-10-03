package com.tianduan;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.tianduan.model.MessageItem;
import com.tianduan.model.MsgData;
import com.tianduan.model.User;
import com.tianduan.util.ChatUtil;

import org.java_websocket.client.WebSocketClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.impl.client.BasicCookieStore;

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "JSESSIONID";

    //教研室：192.168.2.224
    //手机:192.168.43.253
    public static final String BASE_IP = "192.168.43.253";
    public static final String BASE_URL = "http://" + BASE_IP + ":8080/tianduan";
    public static final String BASE_CHAT_URL = "ws://" + BASE_IP + ":8080/tianduan/chat";

    private static MyApplication instance;
    private RequestQueue requestQueue;
    private SharedPreferences preferences;
    private WebSocketClient webSocketClient;
    private Map<String, List<MsgData>> chatMap;
    private List<MessageItem> messageItems;
    private List<String> messageObjectIds;
    private AsyncHttpClient asyncHttpClient;

    private User user;

    public static MyApplication newInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        requestQueue = Volley.newRequestQueue(this);
        chatMap = new HashMap<>();
        messageItems = new ArrayList<>();
        messageObjectIds = new ArrayList<>();
        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setConnectTimeout(60);//5s超时
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }


    /**
     * 检查返回的Response header中有没有session
     *
     * @param responseHeaders Response Headers.
     */
    public final void checkSessionCookie(Map<String, String> responseHeaders) {
        if (responseHeaders.containsKey(SET_COOKIE_KEY)
                && responseHeaders.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
            String cookie = responseHeaders.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                SharedPreferences.Editor prefEditor = preferences.edit();
                prefEditor.putString(SESSION_COOKIE, cookie);
                prefEditor.commit();
            }
        }
    }

    /**
     * 添加session到Request header中
     */
    public final void addSessionCookie(Map<String, String> requestHeaders) {
        String sessionId = preferences.getString(SESSION_COOKIE, "");
        if (sessionId.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId);
            if (requestHeaders.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(requestHeaders.get(COOKIE_KEY));
            }
            requestHeaders.put(COOKIE_KEY, builder.toString());
            asyncHttpClient.addHeader(COOKIE_KEY, builder.toString());
        }
    }


    public static String buildURL(String uri) {
        return BASE_URL + uri;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public AsyncHttpClient getAsyncHttpClient() {
        return asyncHttpClient;
    }

    private WebSocketClient getWebSocketClient() {
        return webSocketClient;
    }

    public void webSocketClientSend(MsgData msg) {
        if (webSocketClient != null) {
            List<MsgData> data = getChatMap().get(msg.getReceiverId());
            if (data == null) {
                data = new ArrayList<>();
            }
            data.add(msg);
            setChatMap(msg.getReceiverId(), data);
            getWebSocketClient().send(msg.createMessage().toString());
        }
    }

    public void setWebSocketClient(WebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    public Map<String, List<MsgData>> getChatMap() {
        return chatMap;
    }

//    public void setChatMap(Map<String, List<MsgData>> chatMap) {
//        this.chatMap = chatMap;
//    }

    public void setChatMap(String objectId, List<MsgData> data) {
        Log.d(TAG, objectId);
        chatMap.put(objectId, data);
        MsgData msg = data.get(data.size() - 1);
        if (messageObjectIds.contains(objectId)) {
            int index = messageObjectIds.indexOf(objectId);
            MessageItem item = messageItems.get(index);
            item.setContent(msg.getContent());
            item.setTimeStamp(msg.getTime().getTime());
            item.setTime(ChatUtil.calculateShowTime(item.getTimeStamp(), item.getTimeStamp() - 60 * 1000 - 10));
            if (msg.getRole() == MsgData.TYPE_RECEIVER) {
                item.setName(msg.getSenderName());
            } else {
                item.setName(msg.getReceiverName());
            }
            messageItems.remove(item);
            messageItems.add(0, item);
            messageObjectIds.remove(objectId);
            messageObjectIds.add(0, objectId);
        } else {
            MessageItem item = new MessageItem();
            item.setTimeStamp(msg.getTime().getTime());
            item.setTime(ChatUtil.calculateShowTime(item.getTimeStamp(), item.getTimeStamp() - 60 * 1000 - 10));
            item.setContent(msg.getContent());
            if (msg.getRole() == MsgData.TYPE_RECEIVER) {
                item.setName(msg.getSenderName());
                item.setObjectId(msg.getSender());
            } else {
                item.setName(msg.getReceiverName());
                item.setObjectId(msg.getReceiverId());
            }
            messageItems.add(0, item);
            messageObjectIds.add(0, objectId);
        }
    }

    /**
     * 判断服务是否开启	 * 	 * @return
     */
    public boolean isServiceRunning(String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null) {
            return false;
        }
        ActivityManager myManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }


    public List<MessageItem> getMessageItems() {
        return messageItems;
    }

    public List<String> getMessageObjectIds() {
        return messageObjectIds;
    }
}

