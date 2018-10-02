package com.tianduan.model;

import java.io.Serializable;

public class MsgData implements Serializable {

    private static final String TAG = "MsgData";
    public static final int TYPE_RECEIVER = 0;
    public static final int TYPE_SENDER = 1;

    private int role;
    private String type;
    private String sender;
    private String receiverType;
    private String receiverId;
    private String content;
    private String time;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
