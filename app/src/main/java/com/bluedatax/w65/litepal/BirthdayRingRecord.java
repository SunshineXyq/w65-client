package com.bluedatax.w65.litepal;

import org.litepal.crud.DataSupport;

/**
 * Created by xuyuanqiang on 16/3/30.
 */
public class BirthdayRingRecord extends DataSupport{
    private int id;
    private String startRingTime;
    private String RingDuration;
    private String RingRecordPath;
    private String ringJudge;
    private String gdid;

    public String getGdid() {
        return gdid;
    }

    public void setGdid(String gdid) {
        this.gdid = gdid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartRingTime() {
        return startRingTime;
    }

    public void setStartRingTime(String startRingTime) {
        this.startRingTime = startRingTime;
    }

    public String getRingDuration() {
        return RingDuration;
    }

    public void setRingDuration(String ringDuration) {
        RingDuration = ringDuration;
    }

    public String getRingRecordPath() {
        return RingRecordPath;
    }

    public void setRingRecordPath(String ringRecordPath) {
        RingRecordPath = ringRecordPath;
    }
    public String getRingJudge() {
        return ringJudge;
    }

    public void setRingJudge(String ringJudge) {
        this.ringJudge = ringJudge;
    }
}
