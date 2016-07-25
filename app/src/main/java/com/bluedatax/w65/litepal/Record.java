package com.bluedatax.w65.litepal;

import org.litepal.crud.DataSupport;

/**
 * Created by xuyuanqiang on 16/3/28.
 * 电话留言
 */
public class Record extends DataSupport{
    private int id;
    private String startTime;    //开始录音时间
    private String duration;     //录音时长
    private String recordPath;   //录音路径
    private String recordRole;   //录音角色


    public String getRecordRole() {
        return recordRole;
    }

    public void setRecordRole(String recordRole) {
        this.recordRole = recordRole;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRecordPath() {
        return recordPath;
    }

    public void setRecordPath(String recordPath) {
        this.recordPath = recordPath;
    }
}
