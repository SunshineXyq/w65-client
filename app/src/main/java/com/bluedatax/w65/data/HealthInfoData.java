package com.bluedatax.w65.data;

/**
 * Created by bdx108 on 15/11/24.
 */
public class HealthInfoData {
    private String date;
    private int imageIcon;
    private int imageSecurity;
    private String textIsSecurity;
    private String textSecurityResult;
    private int imageHeart;
    private int textHeartNum;

    public HealthInfoData(String date, int imageIcon, int imageSecurity, String textIsSecurity, String textSecurityResult, int imageHeart, int textHeartNum) {
        this.date = date;
        this.imageIcon = imageIcon;
        this.imageSecurity = imageSecurity;
        this.textIsSecurity = textIsSecurity;
        this.textSecurityResult = textSecurityResult;
        this.imageHeart = imageHeart;
        this.textHeartNum = textHeartNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(int imageIcon) {
        this.imageIcon = imageIcon;
    }

    public int getImageSecurity() {
        return imageSecurity;
    }

    public void setImageSecurity(int imageSecurity) {
        this.imageSecurity = imageSecurity;
    }

    public String getTextIsSecurity() {
        return textIsSecurity;
    }

    public void setTextIsSecurity(String textIsSecurity) {
        this.textIsSecurity = textIsSecurity;
    }

    public String getTextSecurityResult() {
        return textSecurityResult;
    }

    public void setTextSecurityResult(String textSecurityResult) {
        this.textSecurityResult = textSecurityResult;
    }

    public int getImageHeart() {
        return imageHeart;
    }

    public void setImageHeart(int imageHeart) {
        this.imageHeart = imageHeart;
    }

    public int getTextHeartNum() {
        return textHeartNum;
    }

    public void setTextHeartNum(int textHeartNum) {
        this.textHeartNum = textHeartNum;
    }
}
