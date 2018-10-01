package com.tianduan.model;

import com.tianduan.annotation.Column;

import org.json.JSONException;

public class Role extends Model {

    @Column
    private String name;
    @Column
    private String description;

    public Role() {

    }

    public Role(String json) throws JSONException {
        super(json);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
