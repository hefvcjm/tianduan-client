package com.tiamuan.model;

import com.tiamuan.annotation.Column;

import org.json.JSONException;

import java.util.Set;

public class Client extends Model {

    @Column
    private User user;
    @Column
    private String code;
    @Column
    private Set<Repair> repairs;

    public Client() {
    }

    public Client(String json) throws JSONException {
        super(json);
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

    public Set<Repair> getRepairs() {
        return repairs;
    }

    public void setRepairs(Set<Repair> repairs) {
        this.repairs = repairs;
    }
}
