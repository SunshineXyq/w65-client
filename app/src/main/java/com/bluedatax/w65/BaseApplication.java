package com.bluedatax.w65;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * Created by bdx108 on 15/12/25.
 */
public class BaseApplication extends LitePalApplication {

    private static String mLock = "LOCK";
    private static BaseApplication mApplication;
    public BaseApplication() {
    }
    public static BaseApplication newInstance(){
        if(mApplication!=null){
            synchronized(mLock ){
                if(mApplication!=null){
                    mApplication = new BaseApplication();
                }
            }
        }
        return mApplication;
    }
    /**
     * 获取Context
     * @return 返回Context的对象
     */
    public static Context getContext(){
        return mApplication.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mApplication = this;
        /*
        *做一些初始化的处理：初始化数据库，化图片缓存，初始化地图等
        */
    }
}
