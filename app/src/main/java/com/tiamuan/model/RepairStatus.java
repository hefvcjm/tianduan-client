package com.tiamuan.model;

import com.tiamuan.annotation.Column;

public class RepairStatus extends Model {

    @Column
    private String time;
    @Column
    private String status;

    public RepairStatus() {

    }

}
