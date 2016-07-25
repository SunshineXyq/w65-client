package com.bluedatax.w65.data;

/**
 * Created by bdx108 on 15/11/28.
 */
public class AuthInquireData {
    private String date;
    private String equipment;
    private String phoneNum;
    private String userName;
    private String lastDate;

    public AuthInquireData(String date, String equipment, String phoneNum, String userName, String lastDate) {
        this.date = date;
        this.equipment = equipment;
        this.phoneNum = phoneNum;
        this.userName = userName;
        this.lastDate = lastDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }
}
