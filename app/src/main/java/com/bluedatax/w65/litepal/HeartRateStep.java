package com.bluedatax.w65.litepal;

import org.litepal.crud.DataSupport;


import java.sql.Timestamp;
import java.util.Date;


/**
 * Created by xuyuanqiang on 4/9/16.
 */
public class HeartRateStep extends DataSupport {
    private int id;
    private String gdid;
    private int heartRate;
    private int steps;
    private long dev_dt;

    public long getDev_dt() {
        return dev_dt;
    }

    public void setDev_dt(long dev_dt) {
        this.dev_dt = dev_dt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGdid() {
        return gdid;
    }

    public void setGdid(String gdid) {
        this.gdid = gdid;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}
