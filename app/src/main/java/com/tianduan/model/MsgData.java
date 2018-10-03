package com.tianduan.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MsgData implements Serializable {

    private static final String TAG = "MsgData";
    public static final int TYPE_RECEIVER = 0;
    public static final int TYPE_SENDER = 1;

    private static final String KEY_TYPE = "type";
    private static final String KEY_SENDER = "sender";
    private static final String KEY_RECEIVER_TYPE = "receiverType";
    private static final String KEY_RECEIVER_ID = "receiverId";
    private static final String KEY_CONTENT_TYPE = "contentType";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_TIME = "time";

    private int role;
    private String type;
    private String sender;
    private String receiverType;
    private String receiverId;
    private String contentType;
    private String content;
    private Date time;

    public MsgData() {

    }

    public MsgData(JSONObject json) throws JSONException {
        Iterator<String> iterator = json.keys();
        Set<String> keys = new HashSet<>();
        while (iterator.hasNext()) {
            keys.add(iterator.next());
        }
        setType(json.getString(KEY_TYPE));
        setSender(json.getString(KEY_SENDER));
        setReceiverType(json.getString(KEY_RECEIVER_TYPE));
        setReceiverId(json.getString(KEY_RECEIVER_ID));
        setContentType(json.getString(KEY_CONTENT_TYPE));
        setContent(json.getString(KEY_CONTENT));
        if (keys.contains(KEY_TIME)) {
            try {
                setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String) json.get(KEY_TIME)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            setTime(new Date());
        }
    }

    public MsgData(String json) throws JSONException {
        this(new JSONObject(json));
    }

    public JSONObject createMessage() {
        JSONObject json = null;
        try {
            json = new JSONObject().put(KEY_TYPE, type)
                    .put(KEY_SENDER, sender)
                    .put(KEY_CONTENT_TYPE, contentType)
                    .put(KEY_CONTENT, content)
                    .put(KEY_RECEIVER_TYPE, receiverType)
                    .put(KEY_RECEIVER_ID, receiverId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
