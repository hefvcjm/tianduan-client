package com.tiamuan.model;

import com.tiamuan.annotation.Column;

import org.json.JSONException;

public class Status extends Model {

    @Column
    private String time;
    @Column
    private String status;

    public Status() {

    }

    public Status(String json) throws JSONException {
        super(json);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
