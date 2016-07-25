package com.bluedatax.w65.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bluedatax.w65.BaseApplication;

/**
 * Created by bdx108 on 15/12/29.
 */
public class NetWorkUtils {
    //定义网络连接的管理器
    private  static  ConnectivityManager mManager  = (ConnectivityManager) BaseApplication.getContext().getSystemService(BaseApplication.getContext().CONNECTIVITY_SERVICE);
    private static NetworkInfo networkInfo  = mManager.getActiveNetworkInfo();;

    /**
     * 判断网络是否连接
     * @return 连接返回true，未连接返回false.
     */
    public static boolean isConnection(){
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 获得网络连接的类型
     * @return 返回网络连接的类型，例如Wifi.
     */
    public  static String getConnectionType() {
        String type = "";
        if (networkInfo != null && networkInfo.isConnected()) {
            type = networkInfo.getTypeName();
        } else {
            type = "无网络连接";
        }
        return type;
    }
}
