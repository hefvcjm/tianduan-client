package com.tiamuan.model;

import com.tiamuan.annotation.Column;

public class Status extends Model {

    @Column
    private String time;
    @Column
    private String status;

    public Status() {

    }

}
