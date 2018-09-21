package com.tiamuan.model;

import com.tiamuan.annotation.Column;

import java.io.Serializable;
import java.util.Set;

public class Repair extends Model {

    @Column
    private String name;
    @Column
    private String code;
    @Column
    private String description;
    @Column
    private String faultpart;
    @Column
    private String ticket;
    @Column
    private String time;
    @Column
    private String pictures;
    @Column
    private String audios;
    @Column
    private String videos;
    @Column
    private Set<RepairStatus> statuses;

    public Repair() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFaultpart() {
        return faultpart;
    }

    public void setFaultpart(String faultpart) {
        this.faultpart = faultpart;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getAudios() {
        return audios;
    }

    public void setAudios(String audios) {
        this.audios = audios;
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

    public Set<RepairStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(Set<RepairStatus> statuses) {
        this.statuses = statuses;
    }
}
