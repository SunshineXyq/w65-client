package com.bluedatax.w65.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.MainActivity;
import com.bluedatax.w65.R;
import com.bluedatax.w65.Service.MyService;
import com.bluedatax.w65.Service.OnConnectListener;
import com.bluedatax.w65.utils.SharedPrefsUtil;
import com.bluedatax.w65.utils.map.ApplicationMap;
//import com.bluedatax.w65.utils.map.ApplicationMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 事故报告
 * Created by bdx108 on 15/12/7.
 */
public class AccidentReport extends BaseActivity implements View.OnClickListener {


    private Button mButtonNo;//无需帮助按钮
    private Button mButtonYes;//需要帮助按钮
    private CircleImageView mCircleImage;//圆形头像
    private TextView accidentStartTime; //事故事件开始时间
    private TextView accidentHappenTime;  //事故事件发生时间
    private TextView accidentDesc;    //事故描述
    private String desc;
    private String accidentTime;
    private String tm_s;
    private String time;
    private String CurrentTime;
    private ArrayList listName;
    private ArrayList listTime;
    private SharedPreferences mSharedPreference;
    private String latitude;
    private String lnglatitude;
    private long token;
    private final String ACTION_NAME = "发送广播";
    private String pushEventMes;
    private String queryEventJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accident_report);
        initWidget();
        token = SharedPrefsUtil.getLong(this, "loginToken", 0L);
        mSharedPreference = getSharedPreferences("count", Context.MODE_PRIVATE);
        latitude = mSharedPreference.getString("lat", "");
        lnglatitude = mSharedPreference.getString("lng", "");
        CurrentTime = getCurrentTime();
        pushEventMes = getIntent().getStringExtra("accidentJson");
        queryEventJson = getIntent().getStringExtra("queryEventJson");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        DeviceEventReceiver deviceEventReceiver = new DeviceEventReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(ACTION_NAME);
//        registerReceiver(deviceEventReceiver, intentFilter);
        if (pushEventMes != null) {
            parsePushEventMes(pushEventMes);
        }
        try {
            JSONObject acceentJsonObject = new JSONObject(queryEventJson);
            JSONObject accentJsonBody = acceentJsonObject.getJSONObject("body");
            if (accentJsonBody.getJSONArray("event") != null) {
                Log.d("判断event是否为空", "*********");
                parseAccentJsonObject(queryEventJson);
                getTimeSubtraction();
            } else {
                Toast.makeText(AccidentReport.this, "您好，老人平安无事，没有发生事件!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析出推送消息中的eid
     *
     * @param eventMes
     */

    private void parsePushEventMes(String eventMes) {
        try {
            JSONObject jsonObject = new JSONObject(eventMes);
            JSONObject body = jsonObject.getJSONObject("body");
            JSONArray event = body.getJSONArray("event");
            JSONObject jsonObject1 = event.getJSONObject(0);
            int eid = jsonObject1.getInt("eid");
            String gdid = jsonObject1.getString("gdid");
            System.out.println(eid + gdid);
            deviceEventQuery(eid, gdid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    private class DeviceEventReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String accidentJson = intent.getStringExtra("accidentJson");
//            try {
//                JSONObject acceentJsonObject = new JSONObject(accidentJson);
//                JSONObject accentJsonBody = acceentJsonObject.getJSONObject("body");
//                if (accentJsonBody.getJSONArray("event") != null) {
//                    Log.d("判断event是否为空", "*********");
//                    parseAccentJsonObject(accidentJson);
//                    getTimeSubtraction();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * 查询消息推送中的eid和gdid
     *
     * @param eid
     * @param gdid
     */

    private void deviceEventQuery(int eid, String gdid) {

        try {
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lat", latitude);
            jsonGeo.put("lng", lnglatitude);
            Log.d("json经纬度", jsonGeo.toString());
            JSONObject jsonObject = new JSONObject(jsonGeo.toString());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("eid", eid);
            jsonBody.put("gdid", gdid);
            jsonBody.put("req", "event");
            jsonBody.put("ts", CurrentTime);
            jsonBody.put("geo", jsonObject);
            JSONObject jsonRang = new JSONObject();
            jsonRang.put("dt_s", "");
            jsonRang.put("dt_e", "");
            jsonBody.put("rang", jsonRang);

            JSONObject jsonReq = new JSONObject();
            jsonReq.put("msg", 112);
            jsonReq.put("body", jsonBody);
            jsonReq.put("token", token);

            MyService.client.send(jsonReq.toString());
        } catch (Exception e) {

        }
    }

    private void getTimeSubtraction() {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            java.util.Date now = df.parse(CurrentTime);
            java.util.Date date = df.parse(tm_s);
            long l = now.getTime() - date.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            time = "" + day + "天" + hour + "小时" + min + "分" + s + "秒";
            Log.d("时间", time);
            System.out.println("" + day + "天" + hour + "小时" + min + "分" + s + "秒");
            accidentHappenTime.setText("" + day + "天" + hour + "小时" + min + "分" + s + "秒");
            accidentStartTime.setText(tm_s);
            accidentDesc.setText(desc);
        } catch (Exception e) {
        }
    }

    private String getCurrentTime() {
        Date date = new Date();

        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制

        String CurrentTime = sdformat.format(date);

        Log.d("AccidentReport获取的当前的时间", CurrentTime);

        return CurrentTime;
    }

    private void parseAccentJsonObject(String accidentJson) {
        listName = new ArrayList();
        listTime = new ArrayList();

        try {
            JSONObject acceentJsonObject = new JSONObject(accidentJson);
            long acceentJsonObjectToken = acceentJsonObject.getLong("token");
            Log.d("返回的token数据", acceentJsonObjectToken + "");
            JSONObject accentJsonBody = acceentJsonObject.getJSONObject("body");
            Log.d("解析后的body数据", accentJsonBody + "");
            JSONObject auth_name = accentJsonBody.getJSONObject("auth_name");
            String event1 = auth_name.getString("event");
            Log.d("auth_name event", event1 + "");
            String auth_id = accentJsonBody.getString("auth_id");
            Log.d("auth_id", auth_id);
            JSONArray event = accentJsonBody.getJSONArray("event");
            for (int i = 0; i < event.length(); i++) {
                JSONObject eventJson = event.getJSONObject(i);
                int eid = eventJson.getInt("eid");
                Log.d("设备事件通知eid", eid + "");
                String gdid = eventJson.getString("gdid");
                Log.d("gdid", gdid);
                String type = eventJson.getString("type");
                Log.d("type", type);
                String ename = eventJson.getString("ename");
                Log.d("ename", ename);
                String title = eventJson.getString("title");
                Log.d("title", title);
                desc = eventJson.getString("desc");
                Log.d("desc", desc);
                String tm_c = eventJson.getString("tm_c");
                Log.d("tm_c", tm_c);
                tm_s = eventJson.getString("tm_s");
                Log.d("tm_s", tm_s);
                String tm_e = eventJson.getString("tm_e");
                Log.d("tm_e", tm_e);
                String video = eventJson.getString("video");
                Log.d("video", video);
                String addr = eventJson.getString("addr");
                Log.d("addr", addr);
                JSONObject list = eventJson.getJSONObject("list");
                String accendTitle = list.getString("title");
                Log.d("title", accendTitle);
                JSONArray item = list.getJSONArray("item");
                for (int j = 0; j < item.length(); j++) {
                    JSONObject itemJson = item.getJSONObject(j);
                    int id = itemJson.getInt("id");
                    Log.d("id", id + "");
                    int idx = itemJson.getInt("idx");
                    Log.d("idx", idx + "");
                    String name = itemJson.getString("name");
                    listName.add(name);
                    Log.d("name", name);
                    String tm = itemJson.getString("tm");
                    listTime.add(tm);
                    Log.d("tm", tm);
                    String op_sign_name = itemJson.getString("op_sign_name");
                    Log.d("op_sign_name", op_sign_name);
                    int order_id = itemJson.getInt("order_id");
                    Log.d("order_id", order_id + "");
                    String order_status = itemJson.getString("order_status");
                    Log.d("order_status", order_status);

                    Iterator its = listTime.iterator();
                    while (its.hasNext()) {
                        Object obj = its.next();
                        Log.d("time******", obj + "");
                    }

                    Iterator it = listName.iterator();
                    while (it.hasNext()) {
                        Object obj = it.next();
                        Log.d("name******", obj + "");

                    }
                }
            }
        } catch (Exception e) {

        }
    }

    private void initWidget() {
        mButtonNo = (Button) findViewById(R.id.button_accident_no);
        mButtonNo.setOnClickListener(this);
        mButtonYes = (Button) findViewById(R.id.button_accident_yes);
        mButtonYes.setOnClickListener(this);
        mCircleImage = (CircleImageView) findViewById(R.id.circleimage_accident);
        mCircleImage.setOnClickListener(this);
        accidentStartTime = (TextView) findViewById(R.id.accident_start_time);
        accidentHappenTime = (TextView) findViewById(R.id.accident_happen_time);
        accidentDesc = (TextView) findViewById(R.id.accident_desc);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button_accident_no:
                // 回到主界面
                intent = new Intent(AccidentReport.this, MainActivity.class);
                break;
            case R.id.button_accident_yes:
                //切换到事故状态界面
                intent = new Intent(AccidentReport.this, AccidentState.class);
                intent.putExtra("accident_start_time", tm_s);
                intent.putExtra("accident_happen_time", time);
                intent.putStringArrayListExtra("name", listName);
                intent.putStringArrayListExtra("time", listTime);
                break;
            case R.id.circleimage_accident:
                //调用地图
                intent = new Intent(AccidentReport.this, ApplicationMap.class);
                break;
        }
        startActivity(intent);
    }
}
