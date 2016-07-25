package com.bluedatax.w65;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.bluedatax.w65.Service.MyService;
import com.bluedatax.w65.activity.Login;
import com.bluedatax.w65.utils.SharedPrefsUtil;
import com.bluedatax.w65.utils.android_websockets.WebSocketClient;
import com.bluedatax.w65.utils.getTime;
import com.bluedatax.w65.utils.sendHandler.AllMessageWhat;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * Created by bdx108 on 15/12/5.
 */
public class BaseActivity extends Activity {
    //handler发送的what
    public static final int SEND_PHONE=0X01;//临时密码中给服务器发送手机号
    public static final int CLOCK_SECOND = 0x02;//临时密码中发送时间递减
    public static final int CLOCK_SECOND_DELETE =0X03;//忘记密码中发送时间递减
    public static final int SEND_CODE=0X04;//临时密码中给服务器发送验证码
    public static final int SEND_LOGIN=0X05;//登录中给服务器发送用户民和密码
    public static final int FORGET_PHONE=0X06;//忘记密码中给服务器发送手机号
    public static final int FORGET_CODE=0X07;//忘记密码中给服务器发送验证码
    public static final int FORGET_PASS=0X08;//忘记密码中给服务器发送修改后的密码
    public static final int GET_HEALTH_INFO=0X09;//历史信息获取数据
    public static final int FIXPASS_COMMIT=0X10;//修改密码中提交数据
    public static final int AUTH_REGISTER=0X11;//用户授权中提交
    public static final int AUTH_INQUIRE=0X12;//用户授权中查询
    public static final String TAG = "BaseActivity";
    private SharedPreferences mSharedPreference;
    private String latitude;
    private String lnglatitude;
    private String currentTime;
    private long token;
    private MyService myService;

//    ServiceConnection conn = new ServiceConnection() {
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//        }
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            //返回一个MsgService对象
//            myService = ((MyService.MyBinder)service).getService();
//
//            //注册回调接口来接收下载进度的变化
//            myService.setOnConnectListener(new MyService.OnConnectListener() {
//
//                @Override
//                public void serConnect(String progress) {
//
//                }
//            });
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityControler.addActivity(this);

        mSharedPreference = getSharedPreferences("count", MODE_PRIVATE);
        latitude = mSharedPreference.getString("lat", "");
        lnglatitude = mSharedPreference.getString("lng", "");
        Log.d("取出存入的经纬度", latitude + "#" + lnglatitude);
        currentTime = getTime.getCurrentTime();
        token = SharedPrefsUtil.getLong(this, "loginToken", 0L);

    }
        @Override
        protected void onDestroy () {
            super.onDestroy();
            ActivityControler.removeActivity(this);
            System.out.println("销毁activity-------" + getClass().getSimpleName());
        }
}
