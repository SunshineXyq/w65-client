package com.bluedatax.w65.litepal;

import org.litepal.crud.DataSupport;

/**
 * Created by xuyuanqiang on 6/16/16.
 */
public class UseMedicineRemind extends DataSupport {
    private int id;
    private String startRemindTime;
    private String recordDuration;
    private String remindRecordPath;
    private String gdid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartRemindTime() {
        return startRemindTime;
    }

    public void setStartRemindTime(String startRemindTime) {
        this.startRemindTime = startRemindTime;
    }

    public String getRecordDuration() {
        return recordDuration;
    }

    public void setRecordDuration(String recordDuration) {
        this.recordDuration = recordDuration;
    }

    public String getRemindRecordPath() {
        return remindRecordPath;
    }

    public void setRemindRecordPath(String remindRecordPath) {
        this.remindRecordPath = remindRecordPath;
    }

    public String getGdid() {
        return gdid;
    }

    public void setGdid(String gdid) {
        this.gdid = gdid;
    }
}
