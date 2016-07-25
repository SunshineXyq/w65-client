package com.bluedatax.w65.fragment;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.bluedatax.w65.MainActivity;
import com.bluedatax.w65.R;
import com.bluedatax.w65.Service.MyService;
import com.bluedatax.w65.Service.OnConnectListener;
import com.bluedatax.w65.activity.HealthInfo;
import com.bluedatax.w65.activity.Login;
import com.bluedatax.w65.chart.CircleMenuLayout;
import com.bluedatax.w65.fragment.firstPagerHeart.ViewPagerDistance;
import com.bluedatax.w65.fragment.firstPagerHeart.ViewPagerHeart;
import com.bluedatax.w65.activity.AccidentReport;
import com.bluedatax.w65.litepal.HeartRateStep;
import com.bluedatax.w65.utils.SharedPrefsUtil;
import com.bluedatax.w65.utils.downloadimage.HttpUtils;
import com.bluedatax.w65.utils.getTime;
import com.bluedatax.w65.utils.sql_query_dev_dt.queryHour;
import com.bluedatax.w65.utils.timeCalculate.Calculate;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.LogRecord;

/**
 * 首页
 */
public class FirstPager extends Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {

    private RadioGroup mRadioGroup;//底部四个按钮的父组件
    private RadioButton mRadioButtonHeart;//首页中心率按钮
    private RadioButton mRadioButtonDistance;//行走距离按钮
    private ImageView mImageNotification;//右上角点击可以弹出通知的按钮
    //管理首页中心率和行走距离两个fragment
    private FragmentManager mManger;
    private FragmentTransaction mTransaction;
    private ImageView deviceOne;
    private ImageView deviceTwo;
    private ImageView deviceThree;
    private ImageView deviceFour;
    private ImageView deviceFive;
    private ImageView deviceSix;
    //心率和行走距离中的viewpager类
    private ViewPagerHeart mViewPagerHeart;
    private ViewPagerDistance mViewPagerDistance;
    private final String ACTION = "发送心率";
    private SharedPreferences mSharedPreference;
    private String latitude;
    private String lnglatitude;
    private String accidentJson;
    private String CurrentTime;
    private downloadImageTask task;
    private int cur_index = 0;
    //private LayoutInflater mInflater;
    //类似建行的一个圆形旋转布局
    private CircleMenuLayout mCircleMenuLayout;
    //通知和通知管理器
    private NotificationManager mNotificationManger;
    private Notification mNotification;
    private FirstPagerReceiver firstPagerReceiver;
    private long token;
    private boolean _isExe = false;
    public static String device;
    private ArrayList downloadImg;
    private String fub;
    private String auid;
    public int icon = 0;
    public static Bitmap mDownloadImageOne;
    public static Bitmap mDownloadImageTwo;
    public static Bitmap mDownloadImageMine;
    //圆形布局上的数据
    private String[] mItemTexts = new String[]{"安全中心 ", "特色服务", "投资理财",
            "转账汇款", "我的账户", "信用卡"};
    //圆形布局上的图片
    private int[] mItemImgs = new int[]{R.mipmap.home_mbank_1_normal,
            R.mipmap.home_mbank_2_normal, R.mipmap.home_mbank_3_normal,
            R.mipmap.home_mbank_4_normal, R.mipmap.home_mbank_5_normal,
            R.mipmap.home_mbank_6_normal};
    //    private downloadImageTwoTask taskTwo;
    private downloadImageMineTask taskMine;
    private final String ACTION_NAME = "发送广播";
    private ImageView deviceMine;
    public static ArrayList<HashMap<String, Object>> list;

    private HeartRateStep heartRateStep;
    private SimpleDateFormat df;
    private SimpleDateFormat df1;
    public static int average1;
    public static int average2;
    public static int average3;
    public static int average4;
    public static int average5;
    public static int average6;
    public static int average7;
    public static int averageStep1;
    public static int averageStep2;
    public static int averageStep3;
    public static int averageStep4;
    public static int averageStep5;
    public static int averageStep6;
    public static int averageStep7;
    private SwipeRefreshLayout mSwipeLayout;

    private Context mContext = this.getActivity();

    public static final int UPDATE_VIEW = 1;
    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_VIEW:
                    mViewPagerHeart = new ViewPagerHeart();
                    mViewPagerDistance = new ViewPagerDistance();
                    mManger = getFragmentManager();
                    mTransaction = mManger.beginTransaction();
                    mTransaction.replace(R.id.framelayout_chart, mViewPagerHeart);
                    mRadioGroup.check(R.id.button_heart_chart);
                    mTransaction.commitAllowingStateLoss();
                    break;
                default:
                    break;
            }
        }
    };
    private ArrayList<Object> listImg;
    private String dt_s;
    private String dt_e;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_firstpager, null);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.radiogroup_chart);
        mRadioButtonHeart = (RadioButton) view.findViewById(R.id.button_heart_chart);
        mRadioButtonHeart.setOnClickListener(this);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.id_swipe_ly);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mRadioButtonDistance = (RadioButton) view.findViewById(R.id.button_distance_chart);
        mRadioButtonDistance.setOnClickListener(this);
        mImageNotification = (ImageView) view.findViewById(R.id.image_notification);
        mImageNotification.setOnClickListener(this);
        deviceOne = (ImageView) view.findViewById(R.id.device_number).findViewById(R.id.device1);
        deviceTwo = (ImageView) view.findViewById(R.id.device_number).findViewById(R.id.device2);
        deviceThree = (ImageView) view.findViewById(R.id.device_number).findViewById(R.id.device3);
        deviceFour = (ImageView) view.findViewById(R.id.device_number).findViewById(R.id.device4);
        deviceFive = (ImageView) view.findViewById(R.id.device_number).findViewById(R.id.device5);
        deviceSix = (ImageView) view.findViewById(R.id.device_number).findViewById(R.id.device6);
        deviceMine = (ImageView) view.findViewById(R.id.device_number).findViewById(R.id.device_mine);
        deviceThree.setOnClickListener(this);
        deviceFour.setOnClickListener(this);
        token = SharedPrefsUtil.getLong(getActivity(), "loginToken", 0L);
        fub = SharedPrefsUtil.getValue(getActivity(), "fub", "");
        auid = SharedPrefsUtil.getValue(getActivity(), "auid", "");
        Log.d("取出登录返回的token", token + "");
        CurrentTime = getTime.getCurrentTime();
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df1 = new SimpleDateFormat("yyyyMMdd");
        dt_e = Calculate.calculateWeek(0);
        dt_s = Calculate.calculateWeek(7);
        System.out.println("一周的时间" + dt_s+"-------"+dt_e);
        mSharedPreference = getActivity().getSharedPreferences("count", Context.MODE_PRIVATE);
        latitude = mSharedPreference.getString("lat", "");
        lnglatitude = mSharedPreference.getString("lng", "");
        Log.d("取出存入的经纬度", latitude + "#" + lnglatitude);
        mNotificationManger = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        return view;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
                homeQuery("device");
            }
        },4000);
    }

    /**
     * 广播接收者
     */

    public class FirstPagerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            accidentJson = intent.getStringExtra("accidentJson");
//            noticeJson = intent.getStringExtra("noticeJson");
//            if (noticeJson != null) {
//                Log.d("广播接收推送的消息", noticeJson);
//                StartNotification();
//            }
            Log.d("接收广播事故查询通知", accidentJson);
            try {
                JSONObject acceentJsonObject = new JSONObject(accidentJson);
                JSONObject accentJsonBody = acceentJsonObject.getJSONObject("body");
                if (accentJsonBody.getJSONArray("device").length() != 0) {
                    Log.d("判断device是否为空", "*********");
                    parseDeviceJSONObject(accidentJson);
                }  else if (accentJsonBody.getJSONArray("event").length() != 0) {
                    Log.d("判断event是否为空", "*********");
//                    parseDeviceEventJSON(accidentJson);
                    StartNotification();
                } else if (accentJsonBody.getJSONArray("stat").length() != 0) {
                    Log.d("判断stat是否为空", "*********");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            parseStatJSONObject(accidentJson);
                            if (DataSupport.findAll(HeartRateStep.class).size() != 0) {
                                average1 = queryHour.period(24, 20);
                                average2 = queryHour.period(20, 16);
                                average3 = queryHour.period(16, 12);
                                average4 = queryHour.period(12, 8);
                                average5 = queryHour.period(8, 4);
                                average6 = queryHour.period(4, 0);
                                average7 = queryHour.period(1, 0);
                                averageStep1 = queryHour.step(24, 20);
                                averageStep2 = queryHour.step(20, 16);
                                averageStep3 = queryHour.step(16, 12);
                                averageStep4 = queryHour.step(12, 8);
                                averageStep5 = queryHour.step(8, 4);
                                averageStep6 = queryHour.step(4, 0);
                                averageStep7 = queryHour.step(1, 0);
                                System.out.println("计算心率平均数" + average1 + "*" + average2 + "*" + average3 + "*" + average4
                                        + "*" + average5 + "*" + average6 + "*" + average7);
                                System.out.println("计算步数平均数" + averageStep1 + "*" + averageStep2 + "*" + averageStep3
                                        + "*" + averageStep4 + "*" + averageStep5 + "*" + averageStep6 +"*"+averageStep7);
                                Message message = handler.obtainMessage();
                                message.what = UPDATE_VIEW;
                                handler.sendMessage(message);
                            }
                        }
                    }).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析设备心率、步数并保存
     *
     * @param accidentJson
     */

    private void parseStatJSONObject(String accidentJson) {
        try {
            JSONObject statJsonObject = new JSONObject(accidentJson);
            long statToken = statJsonObject.getLong("token");
            Log.d("返回的token数据", statToken + "");
            JSONObject statJsonBody = statJsonObject.getJSONObject("body");
            Log.d("解析后的body数据", statJsonBody + "");
            JSONObject auth_name = statJsonBody.getJSONObject("auth_name");
            Log.d("auth_name", auth_name + "");
            JSONArray stat = statJsonBody.getJSONArray("stat");
            Log.d("stat", stat + "");
            for (int i = 0; i < stat.length(); i++) {
                System.out.println(i);
                JSONObject devicesJson = stat.getJSONObject(i);
                String gdid = devicesJson.getString("gdid");
                Log.d("gdid", gdid);
                String dev_dt = devicesJson.getString("dev_dt");
                Log.d("dev_dt", dev_dt);
                Date dt = df.parse(dev_dt);
                System.out.println(dt);
                System.out.println(dt.getTime());
                int heart_rate = devicesJson.getInt("heart_rate");
                Log.d("heart_rate", heart_rate + "");
                int walk_steps = devicesJson.getInt("walk_steps");
                Log.d("walk_steps", walk_steps + "");
                heartRateStep = new HeartRateStep();
                heartRateStep.setSteps(walk_steps);
                heartRateStep.setGdid(gdid);
                heartRateStep.setDev_dt(dt.getTime());
                heartRateStep.setHeartRate(heart_rate);
                if (heartRateStep.save()) {
                    System.out.println("存储成功");
                } else {
                    System.out.println("存储失败");
                }
                System.out.println("--------------------------");
            }
        } catch (Exception e) {
        }
    }

    private void parseDeviceEventJSON(String accidentJson) {
        try {
            JSONObject noticeJsondata = new JSONObject(accidentJson);

            Log.d("事件通知返回的数据", noticeJsondata + "");

            long noticeJsondataLongToken = noticeJsondata.getLong("token");
            Log.d("返回的token数据", noticeJsondataLongToken + "");

            JSONObject noticeJsonBody = noticeJsondata.getJSONObject("body");
            Log.d("解析后的body数据", noticeJsonBody + "");

            JSONArray event = noticeJsonBody.getJSONArray("event");
            Log.d("解析后的event数据", event + "");

            for (int i = 0; i < event.length(); i++) {
                JSONObject eventJson = event.getJSONObject(i);
                int eid = eventJson.getInt("eid");
                Log.d("设备事件通知eid", eid + "");
                String ename = eventJson.getString("ename");
                Log.d("摔倒事故状态eid", ename);
                String gdid = eventJson.getString("gdid");
                Log.d("设备gdid", gdid);
                String tm_c = eventJson.getString("tm_c");
                Log.d("当前时间", tm_c);
                String tm_s = eventJson.getString("tm_s");
                Log.d("开始时间", tm_s);
                String tm_e = eventJson.getString("tm_e");
                Log.d("结束时间", tm_e);
                String video = eventJson.getString("video");
                Log.d("设备录音", video);
                JSONObject list = eventJson.getJSONObject("list");
                String title = eventJson.getString("title");
                Log.d("摔倒事故状态", title);
                JSONArray item = list.getJSONArray("item");
                for (int j = 0; j < item.length(); j++) {
                    JSONObject itemJson = event.getJSONObject(j);
                    int id = itemJson.getInt("id");
                    Log.d("通知信息一", id + "");
                    String name = itemJson.getString("name");
                    Log.d("工作人员报告", name);
                    String tm = itemJson.getString("tm");
                    Log.d("事件当前时间", tm);
                }
            }
            StartNotification();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析设备头像等数据
     *
     * @param accidentJson
     */
    private void parseDeviceJSONObject(String accidentJson) {

        listImg = new ArrayList<>();
        try {
            JSONObject acceentJsonObject = new JSONObject(accidentJson);
            long acceentJsonObjectToken = acceentJsonObject.getLong("token");
            Log.d("返回的token数据", acceentJsonObjectToken + "");
            JSONObject accentJsonBody = acceentJsonObject.getJSONObject("body");
            Log.d("解析后的body数据", accentJsonBody + "");
            JSONObject auth_name = accentJsonBody.getJSONObject("auth_name");
            device = auth_name.getString("device");
            Log.d("auth_name device", device + "");
            String auth_id = accentJsonBody.getString("auth_id");
            Log.d("auth_id", auth_id);
            JSONArray devices = accentJsonBody.getJSONArray("device");
            Log.d("device", devices + "");
            list = new ArrayList<>();
            for (int i = 0; i < devices.length(); i++) {
                HashMap<String,Object> map = new HashMap<String,Object>();
                JSONObject devicesJson = devices.getJSONObject(i);
                int conn_status = devicesJson.getInt("conn_status");
                Log.d("conn_status", conn_status + "");
                String gdid = devicesJson.getString("gdid");

                map.put("gdid", gdid);
                Log.d("gdid", gdid);

                String name = devicesJson.getString("name");
                map.put("name", name);

                Log.d("name", name);
                int pid = devicesJson.getInt("pid");
                Log.d("pid", pid + "");
                String profile_img = devicesJson.getString("profile_img");
                listImg.add(profile_img);
                Log.d("profile_img", profile_img);

                String srv_status = devicesJson.getString("srv_status");
                map.put("srv_status", srv_status);
                Log.d("srv_status", srv_status + "");

                String upn = devicesJson.getString("upn");
                map.put("upn", upn);
                Log.d("upn", upn);
                list.add(map);
            }
        } catch (Exception e) {

        }

        dowmloadImage();
    }

    /**
     * 下载设备头像
     */
    private void dowmloadImage() {
        if (!_isExe) {
            for (int i = 0; i < listImg.size(); i++) {
                task = new downloadImageTask();
                task.execute(String.format("%s?prod=w65&id=1&fttype=dl_files&auth_user=w65_wdas&" +
                        "auth_name=%s&fname=%s&auth_id=%s&type=png&bdx=prod", fub, device, listImg.get(i), auid));
            }

            taskMine.execute(String.format("%s?prod=w65&id=1&fttype=dl_files&auth_user=w65_wdas&" +
                    "auth_name=usr_u&fname=profile_img&auth_id=%s&type=json&bdx=prod", fub, auid));

            _isExe = true;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onResume() {
//        homeQuery("stat");
        homeQuery("device");
//        homeQueryWeekStat(dt_s,dt_e);
        downloadImg = new ArrayList();
        taskMine = new downloadImageMineTask();
        firstPagerReceiver = new FirstPagerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NAME);
        getActivity().registerReceiver(firstPagerReceiver, intentFilter);
//        mViewPagerHeart=new ViewPagerHeart();
//        mViewPagerDistance=new ViewPagerDistance();
//        mManger=getChildFragmentManager();
//        mTransaction=mManger.beginTransaction();
//        mTransaction.replace(R.id.framelayout_chart,mViewPagerHeart);
//        mRadioGroup.check(R.id.button_heart_chart);
//        mTransaction.commit();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (DataSupport.findAll(HeartRateStep.class).size() != 0) {
//                    average1 = queryHour.period(24, 20);
//                    average2 = queryHour.period(20, 16);
//                    average3 = queryHour.period(16, 12);
//                    average4 = queryHour.period(12, 8);
//                    average5 = queryHour.period(8, 4);
//                    average6 = queryHour.period(4, 0);
//                    average7 = queryHour.period(1, 0);
//                    averageStep1 = queryHour.step(24, 20);
//                    averageStep2 = queryHour.step(20, 16);
//                    averageStep3 = queryHour.step(16, 12);
//                    averageStep4 = queryHour.step(12, 8);
//                    averageStep5 = queryHour.step(8, 4);
//                    averageStep6 = queryHour.step(4, 0);
//                    averageStep7 = queryHour.step(1, 0);
//                    System.out.println("计算心率平均数" + average1 + "*" + average2 + "*" + average3 + "*" + average4
//                            + "*" + average5 + "*" + average6 + "*" + average7);
//                    System.out.println("计算步数平均数" + averageStep1 + "*" + averageStep2 + "*" + averageStep3
//                            + "*" + averageStep4 + "*" + averageStep5 + "*" + averageStep6 +"*"+averageStep7);
//                    Message message = handler.obtainMessage();
//                    message.what = UPDATE_VIEW;
//                    handler.sendMessage(message);
//                }
//            }
//        }).start();
        showDeviceCount();
        super.onResume();
    }

    /**
     * 展示设备数量
     */
    private void showDeviceCount() {
        String pathOne = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage1.jpg";
        String pathTwo = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage2.jpg";
        String pathThree = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage3.jpg";
        String pathFour = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage4.jpg";
        String pathFive = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage5.jpg";
        String pathSix = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage6.jpg";
        if (pathOne.length() != 0) {
            deviceThree.setVisibility(View.VISIBLE);
            deviceThree.setImageBitmap(getLocalImage(pathOne));
        }
        if (pathTwo.length() != 0) {
            deviceFour.setVisibility(View.VISIBLE);
            deviceFour.setImageBitmap(getLocalImage(pathTwo));
        }
    }

    private void homeQueryWeekStat(String dt_s,String dt_e) {
        try {
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lat", latitude);
            jsonGeo.put("lng", lnglatitude);
            Log.d("json经纬度", jsonGeo.toString());
            JSONObject jsonObject = new JSONObject(jsonGeo.toString());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("gdid", "");
            jsonBody.put("eid", "");
            jsonBody.put("req", "stat");
            jsonBody.put("ts", CurrentTime);
            jsonBody.put("geo", jsonObject);
            JSONObject jsonRang = new JSONObject();
            jsonRang.put("dt_s", dt_s);
            jsonRang.put("dt_e", dt_e);
            jsonBody.put("rang", jsonRang);

            JSONObject jsonReq = new JSONObject();
            jsonReq.put("msg", 112);
            jsonReq.put("body", jsonBody);
            jsonReq.put("token", Login.token);

            MyService.client.send(jsonReq.toString());
        } catch (Exception e) {
        }
    }

    private Bitmap getLocalImage(String path) {
        Bitmap deviceBitmap = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                deviceBitmap = BitmapFactory.decodeFile(path);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return deviceBitmap;

    }

    public void onPause() {
        getActivity().unregisterReceiver(firstPagerReceiver);
        super.onPause();
    }

    /**
     * 保存下载完成的设备头像
     *
     * @param bm
     * @return
     * @throws IOException
     */

    public File saveLocalImage(Bitmap bm) throws IOException {
        String path = Environment.getExternalStorageDirectory().toString() + "/w65/icon_download/";

        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File myDownloadFile = new File(path + String.format("downloadImage%d.jpg", ++icon));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myDownloadFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        return myDownloadFile;
    }

    /**
     * 下载设备图片
     */

    class downloadImageTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            System.out.println("[downloadImageTask->]doInBackground "
                    + params[0]);
            mDownloadImageOne = HttpUtils.getNetWorkBitmap(params[0]);  //将地址传入利用http下载图片
            try {
                saveLocalImage(mDownloadImageOne);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            if (icon == 1) {
                deviceThree.setImageBitmap(mDownloadImageOne);
            } else if (icon == 2) {
                deviceFour.setImageBitmap(mDownloadImageOne);
            }
            System.out.println("result = " + result);
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }
    }

    /**
     * 下载自己设置的头像
     */
    class downloadImageMineTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            System.out.println("[downloadImageTask->]doInBackground "
                    + params[0]);
            try {
                mDownloadImageMine = HttpUtils.getNetWorkBitmap(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("捕获的异常为" + e.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),"下载图片失败，请稍后重试!",Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            deviceMine.setImageBitmap(mDownloadImageMine);
            System.out.println("result = " + result);
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        //接下来两个监听事件是用来切换心率和行走距离的图表的
        mTransaction = mManger.beginTransaction();
        switch (v.getId()) {
            case R.id.button_heart_chart:
                mTransaction.replace(R.id.framelayout_chart, mViewPagerHeart);
                mTransaction.commit();
                break;
            case R.id.button_distance_chart:
                mTransaction.replace(R.id.framelayout_chart, mViewPagerDistance);
                mTransaction.commit();
                break;
            case R.id.image_notification:
                //发送通知
                deviceEventQuery();
//                StartNotification();
                break;
            case R.id.device3:
                Intent intent = new Intent(getActivity(),HealthInfo.class);
                startActivity(intent);
                break;
            case R.id.device4:
                Intent i = new Intent(getActivity(),HealthInfo.class);
                startActivity(i);
            default:
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void StartNotification() {
        //仅启动notification，点击返回键时直接返回到主界面而不是当前应用，所以使用makeIntent来加入Activity，使的点击返回键时启动该应用
        // Intent intent = new Intent(getActivity().getApplicationContext(), AccidentReport.class);
        PendingIntent pend = PendingIntent.getActivities(getActivity().getApplicationContext(), 1, makeIntentStack(getActivity())
                , PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification = new Notification.Builder(getActivity().getApplicationContext()).
                 setSmallIcon(R.mipmap.ic_launcher).setTicker("消息来了")
                .setContentTitle("注意啦！").setContentText("这是一个用来显示提醒的notification").setContentInfo("可以点击")
                .setContentIntent(pend).build();
        //将notification设置为自动取消，即点击完之后自动消失
        mNotification.defaults = Notification.DEFAULT_VIBRATE;
        //设置notification的提示音为默认和震动
        mNotification.defaults = Notification.DEFAULT_SOUND;
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotificationManger.notify(2, mNotification);
    }

    /**
     * 发送设备事件查询decice以及心率、行走步数
     */

    private void homeQuery(String homeReq) {
        try {
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lat", latitude);
            jsonGeo.put("lng", lnglatitude);
            Log.d("json经纬度", jsonGeo.toString());
            JSONObject jsonObject = new JSONObject(jsonGeo.toString());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("gdid", "");
            jsonBody.put("eid", "");
            jsonBody.put("req", homeReq);
            jsonBody.put("ts", CurrentTime);
            jsonBody.put("geo", jsonObject);
            JSONObject jsonRang = new JSONObject();
            jsonRang.put("dt_s", "2016-02-29 00:00:00");
            jsonRang.put("dt_e", "2016-03-01 00:00:00");
            jsonBody.put("rang", jsonRang);

            JSONObject jsonReq = new JSONObject();
            jsonReq.put("msg", 112);
            jsonReq.put("body", jsonBody);
            jsonReq.put("token", Login.token);

            MyService.client.send(jsonReq.toString());
        } catch (Exception e) {
        }
    }

    /**
     * 主动查询设备event
     */
    private void deviceEventQuery() {

        try {
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lat", latitude);
            jsonGeo.put("lng", lnglatitude);
            Log.d("json经纬度", jsonGeo.toString());
            JSONObject jsonObject = new JSONObject(jsonGeo.toString());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("gdid", "21-1");
            jsonBody.put("eid", 5);
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

    /**
     * @param context
     * @return intent 数组
     */
    Intent[] makeIntentStack(Context context) {
        Intent[] intents = new Intent[2];
        intents[0] = Intent.makeRestartActivityTask(new ComponentName(context, MainActivity.class));
        intents[1] = new Intent(context, AccidentReport.class);
        intents[1].setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intents[1].putExtra("queryEventJson",accidentJson);
        return intents;
    }
}
