package com.bluedatax.w65.Service;

import org.json.JSONObject;

/**
 * Created by bdx109 on 16/2/29.
 */
public interface OnConnectListener {
    void onJSonObject(JSONObject json);
    void onError(Exception msg);
    void onConnected(String notice);
    void onRingJSONObject(JSONObject ringJSON);
    void onFamilyNumber(JSONObject familyJSON);
    void onDisconnect(String message);
    void onHeartbeat(JSONObject heartbeatJSON);
}
