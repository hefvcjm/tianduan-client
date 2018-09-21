package com.tiamuan.model;

import com.tiamuan.annotation.Column;

public class Role extends Model {

    @Column
    private String name;
    @Column
    private String description;

    public Role() {

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
