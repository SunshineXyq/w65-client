package com.bluedatax.w65.data;

/**
 * Created by xuyuanqiang on 6/8/16.
 */
public class UseMedicineData {
    private String note;
    private String time;

    public UseMedicineData(String note,String time) {
        this.note = note;
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
