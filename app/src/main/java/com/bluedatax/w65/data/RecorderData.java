package com.bluedatax.w65.data;

/**
 * Created by bdx108 on 15/12/25.
 */
public class RecorderData {
    private int id;
    private String user;
    private String name;
    private String path;
    private int time;

    public RecorderData() {
    }

    public RecorderData(int id,String user, String name, String path, int time) {
        this.user = user;
        this.name = name;
        this.path = path;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
