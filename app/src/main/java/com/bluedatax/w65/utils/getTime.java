package com.bluedatax.w65.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bdx109 on 16/2/25.
 */
public class getTime {

    private static String CurrentTime;

    public static String getCurrentTime() {
        Date date = new Date();

        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制

        CurrentTime = sdformat.format(date);


        Log.d("静态方法当前时间为", CurrentTime);

        return CurrentTime;

    }
}
