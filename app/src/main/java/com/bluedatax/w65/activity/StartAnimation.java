package com.bluedatax.w65.activity;



import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bluedatax.w65.ActivityControler;
import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.R;
import com.bluedatax.w65.Service.MyService;
import com.bluedatax.w65.Service.OnConnectListener;
import com.bluedatax.w65.adapter.StartViewAdapter;
import com.bluedatax.w65.utils.InitJsonMessage;
import com.bluedatax.w65.utils.MD5Utils;
import com.bluedatax.w65.utils.android_websockets.WebSocketClient;
import com.bluedatax.w65.utils.GetPhoneMes.GetAppVersion;
import com.bluedatax.w65.utils.getTime;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 进入app时候显示的三张图片
 */

public class StartAnimation extends BaseActivity implements LocationListener {

    private ViewPager mViewPager;
    private List<View>mViews;
    private StartViewAdapter mAdapter;
    private Button mButtonExperience;//第三页上立即体验
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private LocationManager mLocationManager;
    public String LocationData;
    private Activity mContext = this;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSharedPreferences;
    private TelephonyManager tm;
    private String CurrentTime;
    private StringBuffer sb;
    private String duid;
    private String name;
    private String sver;
    private String model;
    private String aver;
    private String geo;
    private String lat;
    private String lng;
    public String DEVICE_ID;
    private static final String TAG = "StartAnimation";
    private String provider;


//    ServiceConnection conn = new ServiceConnection() {
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//        }
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            Log.d("1234456","*************");
//            //返回一个MsgService对象
//            myService = ((MyService.MyBinder)service).getService();
//
//            //注册回调接口来接收下载进度的变化
//            myService.setOnConnectListener(new OnConnectListener() {
//
//                @Override
//                public void onJSonObject(JSONObject json) {
//                    Log.d("传递过来的json数据",json+"");
////                    try {
////
////                        parseInitJSONObject(json);
////
////
////                    } catch (Exception e){
////
////                    }
//                }
//                public void onConnected(String notice) {
//                    Log.d("传递过来的notice",notice);
//                    if (notice == "Connected") {
//                        initServer();
//                    }else {
//                        Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_LONG).show();
//                    }
//
//                }
//            });
//        }
//    };



    @Override
	protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_start);
        getPostion();
        CurrentTime = getTime.getCurrentTime();

//      获取设备信息

        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        name = android.os.Build.MANUFACTURER;          //name
        Log.d(TAG, name);
        Log.d(TAG, "开始界面");
        sver = android.os.Build.VERSION.RELEASE;    //sver
        Log.d(TAG, sver);
        model = Build.MODEL;                //device model 型号
        Log.d(TAG, model);
        GetAppVersion mGetAppVersion = new GetAppVersion(mContext);
        aver = mGetAppVersion.getVersion();      //aver
        Log.d(TAG, aver);
        DEVICE_ID = tm.getDeviceId();
        Log.d(TAG, DEVICE_ID);
        String MD5DeviceID = MD5Utils.encode(DEVICE_ID);
        Log.d("MD5设备号码", MD5DeviceID);
        sb = new StringBuffer(MD5DeviceID);
        sb.insert(6, "-");
        sb.insert(11, "-");
        sb.insert(16, "-");
        sb.insert(21, "-");
        Log.d(TAG, sb.toString());
        duid = sb.toString();
        Log.d("正确格式的duid", duid);


        mViewPager = (ViewPager) findViewById(R.id.viewpager_start);
        preferences = getSharedPreferences("count", MODE_PRIVATE);
        int count = preferences.getInt("count", 0);
        Log.d("123gangkashi", count + "");
        //判断程序与第几次运行，如果是第一次运行则跳转到引导页面
        if (count == 0) {
            initView();
            mAdapter = new StartViewAdapter(mViews);
            mViewPager.setAdapter(mAdapter);
            mButtonExperience.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(StartAnimation.this, Login.class);
                    startActivity(intent);
                }
            });
            editor = preferences.edit();
            //存入数据
            editor.putInt("count", ++count);
            //提交修改
            editor.commit();
        } else {
            Intent intent = new Intent(StartAnimation.this, Login.class);
            startActivity(intent);
        }
    }

    /**
     *
     * 获取手机经纬度
     *
     */

    private void getPostion() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);      //获取系统定位管理器
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000, 5, this);

        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            LocationData = "Enabled:"+LocationManager.GPS_PROVIDER;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        Log.i(TAG, "location:" + location.toString());
        geo = location.getLatitude() + "#" + location.getLongitude();
        lat = location.getLatitude()+"";
        lng = location.getLongitude()+"";

        SharedPreferences mShare1 = getSharedPreferences("count",MODE_PRIVATE);
        mEditor = mShare1.edit();
        mEditor.putString("lat", lat);
        mEditor.putString("lng", lng);
        Log.d("存入本地的经纬度", lat+"#"+lng);
        mEditor.commit();

        Log.d("获取手机经纬度",geo);
        Log.d("lat",lat);
        Log.d("lng",lng);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
        String strText = String.format("provider is %s, status = %d", provider, status);
        Log.i(TAG, strText);

        LocationData = strText;
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
        Log.i(TAG, provider);

        LocationData = "Enabled:"+provider;
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
        Log.i(TAG, provider);

        LocationData = "Disabled:"+provider;
    }

    /**
     * 初始化三张图片
     */
    private void initView() {
        mViews=new ArrayList<View>();
        LayoutInflater inflater=getLayoutInflater();

        View view1=inflater.inflate(R.layout.viewpager_start_one, null);
        View view2=inflater.inflate(R.layout.viewpager_start_two,null);
        View view3=inflater.inflate(R.layout.viewpager_start_three,null);

       mButtonExperience= (Button) view3.findViewById(R.id.button_start_experience);
        mViews.add(view1);
        mViews.add(view2);
        mViews.add(view3);
    }

}
