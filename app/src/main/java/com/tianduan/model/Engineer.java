package com.tianduan.model;

import com.tianduan.annotation.Column;
import com.tianduan.annotation.ToStringIgnore;

import org.json.JSONException;

import java.util.Set;

public class Engineer extends Model {

    @Column
    private User user;
    @Column
    private String code;
    @Column
    private Set<Maintain> maintains;

    public Engineer() {
    }

    public Engineer(String json) throws JSONException {
        super(json);
    }

    public Engineer(User user, String code) {
        this.user = user;
        this.code = code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<Maintain> getMaintains() {
        return maintains;
    }

    public void setMaintains(Set<Maintain> maintains) {
        this.maintains = maintains;
    }
}
