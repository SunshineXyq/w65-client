package com.bluedatax.w65.data;

/**
 * Created by bdx108 on 15/11/27.
 */
public class LeaveMessageData {
    private String name;
    private String time;
    private String path;
    private String startTime;


    public LeaveMessageData(String name, String time, String startTime) {
        this.name = name;
        this.time = time;
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;

    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
