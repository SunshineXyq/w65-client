package com.bluedatax.w65.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.R;
import com.bluedatax.w65.Service.MyService;
import com.bluedatax.w65.Service.OnConnectListener;
import com.bluedatax.w65.adapter.LeaveMessageAdapter;
import com.bluedatax.w65.data.LeaveMessageData;
import com.bluedatax.w65.fragment.FirstPager;
import com.bluedatax.w65.litepal.BirthdayRingRecord;
import com.bluedatax.w65.litepal.LongSitRemind;
import com.bluedatax.w65.litepal.Record;
import com.bluedatax.w65.litepal.UseMedicineRemind;
import com.bluedatax.w65.utils.SharedPrefsUtil;
import com.bluedatax.w65.utils.downloadimage.HttpUtils;
import com.bluedatax.w65.utils.getTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class BirthdaySound extends BaseActivity implements View.OnTouchListener {

    private TextView ringPersonalMes;
    private TextView titleLayoutMiddle;
    private ListView lvBirthdayRing;
    private List<LeaveMessageData> mDatas;
    private LayoutInflater mInflater;
    private LeaveMessageAdapter birthdayRingAdapter;
    private Button birthdayRingMic;
    private Boolean isRecoding;
    private String startRecordTime;
    private int time;
    private MediaRecorder birthdayRingRecorder;
    private String path;
    private File saveFilePath;
    private Dialog dialog;
    private TextView mTextViewRecordTime;
    private ImageView mImageViewShow;
    private String[] ringListFiles;
    private AnimationDrawable animationDrawable;
    private MediaPlayer ringPlayer;
    private String ringSign;
    private String remindSign;
    private ImageView ringDeviceImage;
    private String latitude;
    private String lnglatitude;
    private String currentTime;
    private long token;
    private SharedPreferences mSharedPreference;
    public static BirthdayRingRecord ringLast;
    private MyService myService;
    private boolean _isExe = false;
    private String auidId;
    private String fileId;
    public static String appfileId;
    private String authName;
    private int width;
    private int height;
    private View contentView;
    private PopupWindow mPopupWindow;
    private int mScreenHeight;
    private Button deleteRecord;
    private static int times = 0;

    ServiceConnection conn = new ServiceConnection() {
       @Override
       public void onServiceDisconnected(ComponentName name) {
       }
       @Override
       public void onServiceConnected(ComponentName name, IBinder service) {
           myService = ((MyService.MyBinder)service).getService();
           myService.setOnConnectListener(new OnConnectListener() {
               @Override
               public void onJSonObject(JSONObject json) {
               }
               @Override
               public void onError(Exception msg) {
               }
               @Override
               public void onConnected(String notice) {
               }
               public void onFamilyNumber(JSONObject familyJSON) {
               }
               @Override
               public void onDisconnect(String message) {
               }
               @Override
               public void onHeartbeat(JSONObject heartbeatJSON) {
               }
               @Override
               public void onRingJSONObject(JSONObject ringJSON) {
                   try {
                       JSONArray jsonArray = ringJSON.getJSONObject("body").getJSONArray("files");
                       fileId = jsonArray.getJSONObject(0).getString("file_id");
                       appfileId = jsonArray.getJSONObject(0).getString("app_file_id");
                       auidId = ringJSON.getJSONObject("body").getString("auth_id");
                       authName = ringJSON.getJSONObject("body").getString("auth_name");
                       System.out.println(fileId + "\n" + auidId + "\n" + authName);

                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
//                   birthdayRingPushRecord();
               }
           });
       }
   };
    private String fub;
    private String auid;
    private uploadRecordTask task;
    private int devSign;
    private List<BirthdayRingRecord> ringAll;
    private List<BirthdayRingRecord> ringItem;
    private int pos;
    private Timer mTimer;
    private int deviceRing;
    private String gdid_1 = "1-1001";
    private String gdid_2 = "2-1001";
    private String gdid_1_name = "李思奶奶";
    private String gdid_2_name = "张三大爷";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday_sound);
        initPopupWindow();
        fub = SharedPrefsUtil.getValue(this, "fub", "");
        auid = SharedPrefsUtil.getValue(this,"auid","");
        task = new uploadRecordTask();
        currentTime = getTime.getCurrentTime();
        token = SharedPrefsUtil.getLong(this, "loginToken", 0L);
        mSharedPreference = getSharedPreferences("count", Context.MODE_PRIVATE);
        latitude = mSharedPreference.getString("lat", "");
        lnglatitude = mSharedPreference.getString("lng", "");
        Log.d("取出存入的经纬度", latitude + "#" + lnglatitude);

        Intent intent = new Intent(this, MyService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        ringPersonalMes = (TextView) findViewById(R.id.ring_personal_message);
        birthdayRingMic = (Button) findViewById(R.id.imageview_mic);
        birthdayRingMic.setOnTouchListener(this);
        lvBirthdayRing = (ListView) findViewById(R.id.lv_birthday_ring);
        ringDeviceImage = (ImageView) findViewById(R.id.ring_device_image);
        deleteRecord = (Button) contentView.findViewById(R.id.delete_record);
        MyOnClickListener myOnClickListener = new MyOnClickListener();
        deleteRecord.setOnClickListener(myOnClickListener);
        ringSign = getIntent().getStringExtra("ringSign");           //判断点击是铃声或提醒声
        remindSign = getIntent().getStringExtra("remindSign");       //判断点击久坐提醒或生日提醒或用药提醒
        deviceRing = getIntent().getIntExtra("deviceRing", 0);       //判断点击铃声时查询第x个设备
        devSign = getIntent().getIntExtra("deviceSign",0);           //判断点击提醒声时查询第x个设备
        System.out.println("您点击的声音类别是---" + ringSign);
        System.out.println("您进入的提醒是---" + remindSign);
        System.out.println("您查询的提醒声是第几个设备---"+devSign );
        mInflater = getLayoutInflater();
        mDatas = new ArrayList<LeaveMessageData>();
        birthdayRingAdapter = new LeaveMessageAdapter(mDatas, mInflater);
        lvBirthdayRing.setAdapter(birthdayRingAdapter);
        deleteRecord();
        ringPlayer = new MediaPlayer();
//        Map map_1 = (Map)FirstPager.list.get(0);
//        Map map_2 = (Map)FirstPager.list.get(1);
//        gdid_1 = (String)map_1.get("gdid");
//        gdid_2 = (String)map_2.get("gdid");
//        gdid_1_name = (String) map_1.get("name");
//        gdid_2_name = (String) map_2.get("name");
        System.out.println(gdid_1+"-------"+gdid_2);

        String pathOne = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage1.jpg";
        String pathTwo = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage2.jpg";
        if (pathOne.length() != 0 && deviceRing == 1) {
            ringDeviceImage.setImageBitmap(getLocalImage(pathOne));
            ringPersonalMes.setText(gdid_1_name + "(设备ID:" + gdid_1 + ")");
        }else if (pathTwo.length() != 0 && deviceRing == 2) {
            ringDeviceImage.setImageBitmap(getLocalImage(pathTwo));
            ringPersonalMes.setText(gdid_2_name + "(设备ID:" + gdid_2 + ")");
        }
        if (pathOne.length() != 0 && devSign == 1) {
            ringDeviceImage.setImageBitmap(getLocalImage(pathOne));
            ringPersonalMes.setText(gdid_1_name + "(设备ID:" + gdid_1 + ")");
        }else if (pathTwo.length() != 0 && devSign == 2) {
            ringDeviceImage.setImageBitmap(getLocalImage(pathTwo));
            ringPersonalMes.setText(gdid_2_name + "(设备ID:" + gdid_2 + ")");
        }
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/w65/BirthdayRingRecord/";
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }

        // 设备铃声，提醒声数据展示
        if (ringSign.equals("ring")) {
            titleLayoutMiddle = (TextView) findViewById(R.id.textview_title_middle);
            titleLayoutMiddle.setText("铃声");
            ringAll = DataSupport.where("ringJudge == ?", "ring").find(BirthdayRingRecord.class);
            if (ringAll.size() >= 0) {
                for (int i = 0; i < ringAll.size(); i++) {
                    mDatas.add(new LeaveMessageData("recorder" + ringAll.get(i).getRingDuration(),
                            ringAll.get(i).getRingDuration(), ringAll.get(i).getStartRingTime()));//向listview中添加数据
                    birthdayRingAdapter.notifyDataSetChanged();
                }
            }
        } else if(ringSign.equals("remind") &&  devSign == 1 && remindSign.equals("生日提醒")) {
            titleLayoutMiddle = (TextView) findViewById(R.id.textview_title_middle);
            titleLayoutMiddle.setText("提醒声");
            queryLocalRemind(gdid_1);
//            List<BirthdayRingRecord> remindRingAll = DataSupport.where("gdid == ?",String.format("%s", gdid_1)).find(BirthdayRingRecord.class);
//            if (remindRingAll.size() > 0) {
//                for (int i = 0; i < remindRingAll.size(); i++) {
//                    mDatas.add(new LeaveMessageData("recorder" + remindRingAll.get(i).getRingDuration(),
//                            remindRingAll.get(i).getRingDuration(), remindRingAll.get(i).getStartRingTime()));//向listview中添加数据
//                    birthdayRingAdapter.notifyDataSetChanged();
//                }
//            }
        } else if(ringSign.equals("remind") &&  devSign == 2 && remindSign.equals("生日提醒")) {
            titleLayoutMiddle = (TextView) findViewById(R.id.textview_title_middle);
            titleLayoutMiddle.setText("提醒声");
            queryLocalRemind(gdid_2);
        } else if(ringSign.equals("remind") && devSign == 1 && remindSign.equals("久坐提醒")) {
            titleLayoutMiddle = (TextView) findViewById(R.id.textview_title_middle);
            titleLayoutMiddle.setText("提醒声");
            List<LongSitRemind> longSitRemindAll = DataSupport.where("gdid == ?", "1").find(LongSitRemind.class);
            if (longSitRemindAll.size() >0) {
                for (int i = 0;i<longSitRemindAll.size();i++) {
                    mDatas.add(new LeaveMessageData("recorder" + longSitRemindAll.get(i).getRecordDuration(),
                            longSitRemindAll.get(i).getRecordDuration(), longSitRemindAll.get(i).getStartRemindTime()));//向listview中添加数据
                    birthdayRingAdapter.notifyDataSetChanged();
                }
            }
        } else if(ringSign.equals("remind") && devSign == 2 && remindSign.equals("久坐提醒")) {
            titleLayoutMiddle = (TextView) findViewById(R.id.textview_title_middle);
            titleLayoutMiddle.setText("提醒声");
            List<LongSitRemind> longSitRemindAll = DataSupport.where("gdid == ?", "2").find(LongSitRemind.class);
            if (longSitRemindAll.size() >0) {
                for (int i = 0;i<longSitRemindAll.size();i++) {
                    mDatas.add(new LeaveMessageData("recorder" + longSitRemindAll.get(i).getRecordDuration(),
                            longSitRemindAll.get(i).getRecordDuration(), longSitRemindAll.get(i).getStartRemindTime()));//向listview中添加数据
                    birthdayRingAdapter.notifyDataSetChanged();
                }
            }
        } else if (ringSign.equals("remind") && devSign == 1 && remindSign.equals("用药提醒")) {
            titleLayoutMiddle = (TextView) findViewById(R.id.textview_title_middle);
            titleLayoutMiddle.setText("提醒声");
            queryUseMedicineRemind("1");
        } else if (ringSign.equals("remind") && devSign == 2 && remindSign.equals("用药提醒")) {
            titleLayoutMiddle = (TextView) findViewById(R.id.textview_title_middle);
            titleLayoutMiddle.setText("提醒声");
            queryUseMedicineRemind("2");
        }
        //播放铃声或提醒声
        lvBirthdayRing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ringSign.equals("ring")) {
                    try {
                        ringPlayer.reset();
                        ringItem = DataSupport.where("ringJudge == ?", "ring").find(BirthdayRingRecord.class);
                        System.out.println("position-------"+position);
                        System.out.println(id);
                        ringPlayer.setDataSource(ringItem.get(position).getRingRecordPath());
                        System.out.println(ringItem.get(position).getRingRecordPath());
                        if (!ringPlayer.isPlaying()) {
                            ringPlayer.prepare();
                            ringPlayer.start();
                        } else {
                            ringPlayer.pause();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
               }else if(ringSign.equals("remind") && devSign == 1 && remindSign.equals("生日提醒")) {
               try {
                    ringPlayer.reset();
                    List<BirthdayRingRecord> ringRemindItem = DataSupport.where("gdid == ?",String.format("%s", gdid_1)).find(BirthdayRingRecord.class);
                    System.out.println(position);
                    System.out.println(id);
                    ringPlayer.setDataSource(ringRemindItem.get(position).getRingRecordPath());
                    System.out.println(ringRemindItem.get(position).getRingRecordPath());
                    if (!ringPlayer.isPlaying()) {
                        ringPlayer.prepare();
                        ringPlayer.start();
                    } else {
                        ringPlayer.pause();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
              } else if(ringSign.equals("remind") && devSign == 2 && remindSign.equals("生日提醒")) {
                    try {
                        ringPlayer.reset();
                        List<BirthdayRingRecord> ringRemindItem = DataSupport.where("gdid == ?",String.format("%s", gdid_2)).find(BirthdayRingRecord.class);
                        System.out.println(position);
                        System.out.println(id);
                        ringPlayer.setDataSource(ringRemindItem.get(position).getRingRecordPath());
                        System.out.println(ringRemindItem.get(position).getRingRecordPath());
                        if (!ringPlayer.isPlaying()) {
                            ringPlayer.prepare();
                            ringPlayer.start();
                        } else {
                            ringPlayer.pause();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if(ringSign.equals("remind") && devSign == 1 && remindSign.equals("久坐提醒")) {
                    try {
                        ringPlayer.reset();
                        List<LongSitRemind> ringRemindItem = DataSupport.where("gdid == ?","1").find(LongSitRemind.class);
                        ringPlayer.setDataSource(ringRemindItem.get(position).getRemindRecordPath());
                        System.out.println(ringRemindItem.get(position).getRemindRecordPath());
                        if (!ringPlayer.isPlaying()) {
                            ringPlayer.prepare();
                            ringPlayer.start();
                        } else {
                            ringPlayer.pause();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if(ringSign.equals("remind") && devSign == 2 && remindSign.equals("久坐提醒")) {
                    ringPlayer.reset();
                    List<LongSitRemind> longSitRemindItem = DataSupport.where("gdid == ?", "2").find(LongSitRemind.class);
                    try {
                        ringPlayer.setDataSource(longSitRemindItem.get(position).getRemindRecordPath());
                        if (!ringPlayer.isPlaying()) {
                            ringPlayer.prepare();
                            ringPlayer.start();
                        } else {
                            ringPlayer.pause();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 查询设备gdid_1录音
     * @param gdid
     */
    private void queryLocalRemind(String gdid) {
        List<BirthdayRingRecord> remindRingAll = DataSupport.where("gdid == ?",String.format("%s", gdid)).find(BirthdayRingRecord.class);
        if (remindRingAll.size() > 0) {
            for (int i = 0; i < remindRingAll.size(); i++) {
                mDatas.add(new LeaveMessageData("recorder" + remindRingAll.get(i).getRingDuration(),
                        remindRingAll.get(i).getRingDuration(), remindRingAll.get(i).getStartRingTime()));//向listview中添加数据
                birthdayRingAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 查询用药提醒录音
     * @param gdid
     */

    private void queryUseMedicineRemind(String gdid) {
        List<UseMedicineRemind> useMedicineRemindAll = DataSupport.where("gdid == ?", gdid).find(UseMedicineRemind.class);
        if (useMedicineRemindAll.size() > 0) {
            for (int i=0;i<useMedicineRemindAll.size();i++) {
                mDatas.add(new LeaveMessageData("recorder" + useMedicineRemindAll.get(i).getRecordDuration(),
                        useMedicineRemindAll.get(i).getRecordDuration(),useMedicineRemindAll.get(i).getStartRemindTime()));
                birthdayRingAdapter.notifyDataSetChanged();
            }
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.delete_record:
                    if (ringSign.equals("ring")) {
                        List<BirthdayRingRecord> deleteRingItem = DataSupport.where("ringJudge == ?", "ring").find(BirthdayRingRecord.class);
                        DataSupport.deleteAll(BirthdayRingRecord.class, "RingRecordPath == ?", deleteRingItem.get(pos).getRingRecordPath());
                        System.out.println("您好，您删除了" + deleteRingItem.get(pos).getRingRecordPath());
                        System.out.println("您删除了第" + pos + "项");
                        mPopupWindow.dismiss();
                        mDatas.clear();
                        ringAll = DataSupport.where("ringJudge == ?", "ring").find(BirthdayRingRecord.class);
                        if (ringAll.size() > 0) {
                            for (int i = 0; i < ringAll.size(); i++) {
                                mDatas.add(new LeaveMessageData("recorder" + ringAll.get(i).getRingDuration(),
                                        ringAll.get(i).getRingDuration(), ringAll.get(i).getStartRingTime()));//向listview中添加数据
                                birthdayRingAdapter.notifyDataSetChanged();
                            }
                        } else {
                            birthdayRingAdapter.notifyDataSetChanged();
                        }
                    } else if (ringSign.equals("remind") && remindSign.equals("生日提醒")) {
                        if (devSign == 1) {
                            deleteRemindRecord(gdid_1);
                        } else if (devSign == 2) {
                            deleteRemindRecord(gdid_2);
                        }
                    } else if (ringSign.equals("remind") && remindSign.equals("久坐提醒")) {
                        if (devSign == 1) {
                            deleteLongSitRemindRecord("1");
                        }else if (devSign == 2) {
                            deleteLongSitRemindRecord("2");
                        }
                    } else if (ringSign.equals("remind") && remindSign.equals("用药提醒")) {
                        if (devSign == 1) {
                            deleteUseMedicineRemind("1");
                        }else if (devSign == 2) {
                            deleteUseMedicineRemind("2");
                        }
                    }
            }
            Toast.makeText(BirthdaySound.this, "您点击了"+pos, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 删除用药提醒提醒声
     * @param gdid  删除第x个设备的提醒声
     */

    private void deleteUseMedicineRemind(String gdid) {
        List<UseMedicineRemind> deleteRemind = DataSupport.where("gdid == ?",gdid).find(UseMedicineRemind.class);
        DataSupport.deleteAll(UseMedicineRemind.class, "remindRecordPath == ?", deleteRemind.get(pos).getRemindRecordPath());
        mPopupWindow.dismiss();
        mDatas.clear();
        List<UseMedicineRemind> remind = DataSupport.where("gdid == ?",gdid).find(UseMedicineRemind.class);
        if (remind.size() > 0) {
            for (int i = 0; i < remind.size(); i++) {
                mDatas.add(new LeaveMessageData("recorder" + remind.get(i).getRecordDuration(),
                        remind.get(i).getRecordDuration(), remind.get(i).getStartRemindTime()));//向listview中添加数据
                birthdayRingAdapter.notifyDataSetChanged();
            }
        } else {
            birthdayRingAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 删除生日提醒提醒声
     * @param gdid  删除第x个设备的提醒声
     */

    private void deleteRemindRecord(String gdid) {
        List<BirthdayRingRecord> deleteRemindRing = DataSupport.where("gdid == ?",gdid).find(BirthdayRingRecord.class);
        DataSupport.deleteAll(BirthdayRingRecord.class, "RingRecordPath == ?", deleteRemindRing.get(pos).getRingRecordPath());
        mPopupWindow.dismiss();
        mDatas.clear();
        List<BirthdayRingRecord> remindRing = DataSupport.where("gdid == ?",String.format("%s",gdid)).find(BirthdayRingRecord.class);
        if (remindRing.size() > 0) {
            for (int i = 0; i < remindRing.size(); i++) {
                mDatas.add(new LeaveMessageData("recorder" + remindRing.get(i).getRingDuration(),
                        remindRing.get(i).getRingDuration(), remindRing.get(i).getStartRingTime()));//向listview中添加数据
                birthdayRingAdapter.notifyDataSetChanged();
            }
        } else {
            birthdayRingAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 删除久坐提醒提醒声
     * @param gdid  删除第x个设备的提醒声
     */

    private void deleteLongSitRemindRecord(String gdid) {
        List<LongSitRemind> longSitRemindAll = DataSupport.where("gdid == ?", gdid).find(LongSitRemind.class);
        DataSupport.deleteAll(LongSitRemind.class,"remindRecordPath == ?",longSitRemindAll.get(pos).getRemindRecordPath());
        mPopupWindow.dismiss();
        mDatas.clear();
        List<LongSitRemind> longSitRemind = DataSupport.where("gdid == ?", gdid).find(LongSitRemind.class);
        if (longSitRemind.size() > 0) {
            for (int i = 0; i < longSitRemind.size(); i++) {
                mDatas.add(new LeaveMessageData("recorder" + longSitRemind.get(i).getRecordDuration(),
                        longSitRemind.get(i).getRecordDuration(), longSitRemind.get(i).getStartRemindTime()));//向listview中添加数据
                birthdayRingAdapter.notifyDataSetChanged();
            }
        } else {
            birthdayRingAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 长按录音的弹窗
     */

    private void deleteRecord() {
        lvBirthdayRing.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                mPopupWindow.showAtLocation(lvBirthdayRing, Gravity.BOTTOM
                        | Gravity.CENTER, 0, mScreenHeight - height / 2);
                return false;
            }
        });
    }

    private void initPopupWindow() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();

        LayoutInflater lf = (LayoutInflater) BirthdaySound.this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        contentView = lf.inflate(R.layout.delete_record_item, null);
        mPopupWindow = new PopupWindow(contentView, ActionMenuView.LayoutParams.WRAP_CONTENT,
                ActionMenuView.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mScreenHeight = getScreenHeight();
    }

    private int getScreenHeight() {
        // 获取屏幕实际像素
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = BirthdaySound.this.getWindowManager().getDefaultDisplay();
        display.getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * 点击按钮录音
     * @param v
     * @param event
     * @return
     */

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction()==MotionEvent.ACTION_DOWN){
        startRecordTime = getTime.getCurrentTime();
        time = 0;
        birthdayRingRecorder = new MediaRecorder();
        birthdayRingRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//声音来源麦克风
        birthdayRingRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);//输出格式3gp
        birthdayRingRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);//编码格式
        try {
            String paths = path
                    + new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
                    + ".amr";
            saveFilePath = new File(paths);
            birthdayRingRecorder.setOutputFile(saveFilePath.getAbsolutePath());
            birthdayRingRecorder.prepare();
            birthdayRingRecorder.start();
        } catch (IOException e) {
            Log.d("00000000", "prepare() failed");
            System.out.println(e);
        }
        isRecoding = true;
        showRecorderDialog();
        timedTask();
        animationDrawable.start();
    } else if(event.getAction()==MotionEvent.ACTION_UP) {
            mTimer.cancel();
            System.out.println("times--------------" + times);
            System.out.println("time--------------" + time);
            animationDrawable.stop();
            dialog.dismiss();
            birthdayRingRecorder.stop();//停止录音
            //将录音时间，录音时长，文件路径存储到数据库
            if (ringSign.equals("ring")) {
                BirthdayRingRecord ringRecord = new BirthdayRingRecord();
                ringRecord.setRingDuration(time + "s");
                ringRecord.setStartRingTime(startRecordTime);
                ringRecord.setRingRecordPath(saveFilePath.getAbsolutePath());
                ringRecord.setRingJudge("ring");
                if (ringRecord.save()) {
                    Toast.makeText(BirthdaySound.this, "存储成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BirthdaySound.this, "存储失败", Toast.LENGTH_SHORT).show();
                }
                ringLast = DataSupport.findLast(BirthdayRingRecord.class);
                mDatas.add(new LeaveMessageData("recorder" + ringLast.getRingDuration(),
                        ringLast.getRingDuration(), ringLast.getStartRingTime()));//向listview中添加数据
                birthdayRingAdapter.notifyDataSetChanged();
                int ringLastId = ringLast.getId();
//            birthdayRingPush(ringLastId);
            } else if (ringSign.equals("remind") && devSign == 1 && remindSign.equals("生日提醒")) {

                BirthdayRingRecord ringRecord = new BirthdayRingRecord();
                ringRecord.setRingDuration(time + "s");
                ringRecord.setStartRingTime(startRecordTime);
                ringRecord.setRingRecordPath(saveFilePath.getAbsolutePath());
                System.out.println(saveFilePath.getAbsolutePath());
                ringRecord.setRingJudge("remindRing");
                ringRecord.setGdid(gdid_1);
                if (ringRecord.save()) {
                    Toast.makeText(BirthdaySound.this, "存储成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BirthdaySound.this, "存储失败", Toast.LENGTH_SHORT).show();
                }
                BirthdayRingRecord remindLast = DataSupport.findLast(BirthdayRingRecord.class);
                mDatas.add(new LeaveMessageData("recorder" + remindLast.getRingDuration(),
                        remindLast.getRingDuration(), remindLast.getStartRingTime()));//向listview中添加数据
                birthdayRingAdapter.notifyDataSetChanged();
                pushRemind(remindLast.getId(),"bremind");
            } else if (ringSign.equals("remind") && devSign == 2 && remindSign.equals("生日提醒")) {

                BirthdayRingRecord ringRecord = new BirthdayRingRecord();
                ringRecord.setRingDuration(time + "s");
                ringRecord.setStartRingTime(startRecordTime);
                ringRecord.setRingRecordPath(saveFilePath.getAbsolutePath());
                System.out.println(saveFilePath.getAbsolutePath());
                ringRecord.setRingJudge("remindRing");
                ringRecord.setGdid(gdid_2);
                if (ringRecord.save()) {
                    Toast.makeText(BirthdaySound.this, "存储成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BirthdaySound.this, "存储失败", Toast.LENGTH_SHORT).show();
                }
                BirthdayRingRecord remindLast = DataSupport.findLast(BirthdayRingRecord.class);
                mDatas.add(new LeaveMessageData("recorder" + remindLast.getRingDuration(),
                        remindLast.getRingDuration(), remindLast.getStartRingTime()));//向listview中添加数据
                birthdayRingAdapter.notifyDataSetChanged();


            } else if (ringSign.equals("remind") && devSign == 1 && remindSign.equals("久坐提醒")) {
                LongSitRemind longSitRemind = new LongSitRemind();
                longSitRemind.setRecordDuration(time + "s");
                longSitRemind.setStartRemindTime(startRecordTime);
                longSitRemind.setRemindRecordPath(saveFilePath.getAbsolutePath());
                System.out.println(saveFilePath.getAbsolutePath());
                longSitRemind.setGdid("1");
                if (longSitRemind.save()) {
                    Toast.makeText(BirthdaySound.this, "存储成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BirthdaySound.this, "存储失败", Toast.LENGTH_SHORT).show();
                }
                LongSitRemind remindLast = DataSupport.findLast(LongSitRemind.class);
                mDatas.add(new LeaveMessageData("recorder" + remindLast.getRecordDuration(),
                        remindLast.getRecordDuration(), remindLast.getStartRemindTime()));//向listview中添加数据
                birthdayRingAdapter.notifyDataSetChanged();
            } else if (ringSign.equals("remind") && devSign == 2 && remindSign.equals("久坐提醒")) {
                LongSitRemind longSitRemind = new LongSitRemind();
                longSitRemind.setRecordDuration(time + "s");
                longSitRemind.setStartRemindTime(startRecordTime);
                longSitRemind.setRemindRecordPath(saveFilePath.getAbsolutePath());
                System.out.println(saveFilePath.getAbsolutePath());
                longSitRemind.setGdid("2");
                if (longSitRemind.save()) {
                    Toast.makeText(BirthdaySound.this, "存储成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BirthdaySound.this, "存储失败", Toast.LENGTH_SHORT).show();
                }
                LongSitRemind remindLast = DataSupport.findLast(LongSitRemind.class);
                mDatas.add(new LeaveMessageData("recorder" + remindLast.getRecordDuration(),
                        remindLast.getRecordDuration(), remindLast.getStartRemindTime()));//向listview中添加数据
                birthdayRingAdapter.notifyDataSetChanged();
            } else if (ringSign.equals("remind") && devSign == 1 && remindSign.equals("用药提醒")) {
                UseMedicineRemind useMedicineRemind = new UseMedicineRemind();
                useMedicineRemind.setRecordDuration(time + "s");
                useMedicineRemind.setStartRemindTime(startRecordTime);
                useMedicineRemind.setRemindRecordPath(saveFilePath.getAbsolutePath());
                System.out.println(saveFilePath.getAbsolutePath());
                useMedicineRemind.setGdid("1");
                if (useMedicineRemind.save()) {
                    Toast.makeText(BirthdaySound.this, "存储成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BirthdaySound.this, "存储失败", Toast.LENGTH_SHORT).show();
                }
                UseMedicineRemind remindLast = DataSupport.findLast(UseMedicineRemind.class);
                mDatas.add(new LeaveMessageData("recorder" + remindLast.getRecordDuration(),
                        remindLast.getRecordDuration(), remindLast.getStartRemindTime()));//向listview中添加数据
                birthdayRingAdapter.notifyDataSetChanged();
            } else if (ringSign.equals("remind") && devSign == 2 && remindSign.equals("用药提醒")) {
                UseMedicineRemind useMedicineRemind = new UseMedicineRemind();
                useMedicineRemind.setRecordDuration(time + "s");
                useMedicineRemind.setStartRemindTime(startRecordTime);
                useMedicineRemind.setRemindRecordPath(saveFilePath.getAbsolutePath());
                System.out.println(saveFilePath.getAbsolutePath());
                useMedicineRemind.setGdid("2");
                if (useMedicineRemind.save()) {
                    Toast.makeText(BirthdaySound.this, "存储成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BirthdaySound.this, "存储失败", Toast.LENGTH_SHORT).show();
                }
                UseMedicineRemind remindLast = DataSupport.findLast(UseMedicineRemind.class);
                mDatas.add(new LeaveMessageData("recorder" + remindLast.getRecordDuration(),
                        remindLast.getRecordDuration(), remindLast.getStartRemindTime()));//向listview中添加数据
                birthdayRingAdapter.notifyDataSetChanged();
            }
            birthdayRingRecorder.release();
        }
            return false;
      }
    private void birthdayRingPushRecord() {
        if (!_isExe) {
            task.execute(String.format("%s?prod=w65&id=1&fttype=ul_files&auth_user=w65_wdas&" +
                    "file_id=%s&fname=&auth_id=%s&auth_name=%s&type=json&bdx=prod", fub,fileId,auid,authName));
        }
        _isExe = true;
    }
    class uploadRecordTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            System.out.println("[downloadImageTask->]doInBackground "
                    + params[0]);
            String response = HttpUtils.uploadRing(params[0],"ring");
//            Log.i("返回的数据",response);
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
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
     * 生日提醒铃声的数据推送
     * @param id  请求上传本地文件ID
     */
    private void birthdayRingPush(int id) {
        try {
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lat", latitude);
            jsonGeo.put("lng", lnglatitude);
            Log.d("json经纬度", jsonGeo.toString());
            JSONObject jsonObject = new JSONObject(jsonGeo.toString());

            JSONObject jsReq = new JSONObject();
            jsReq.put("stype","ring");
            jsReq.put("action","push");
            jsReq.put("title","");
            jsReq.put("file_id","");
            jsReq.put("app_file_id",id);
            jsReq.put("dt_s","");
            jsReq.put("dt_e","");
            JSONObject req = new JSONObject(jsReq.toString());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("gdid","1-1001");
            jsonBody.put("geo", jsonObject);
            jsonBody.put("req",req);
            jsReq.put("dt",currentTime);

            JSONObject jsonReq = new JSONObject();
            jsonReq.put("msg", 113);
            jsonReq.put("token", Login.token);
            jsonReq.put("body",jsonBody);

            MyService.client.send(jsonReq.toString());

        } catch (Exception e) {
            Toast.makeText(BirthdaySound.this,"发送失败!",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 上传xx提醒声，获取fileId
     * @param id             本地文件id
     * @param remindType     xx提醒声
     */

    private void pushRemind(int id,String remindType) {
        try {
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lat", latitude);
            jsonGeo.put("lng", lnglatitude);
            Log.d("json经纬度", jsonGeo.toString());
            JSONObject jsonObject = new JSONObject(jsonGeo.toString());

            JSONObject jsReq = new JSONObject();
            jsReq.put("stype",remindType);
            jsReq.put("action","push");
            jsReq.put("title","");
            jsReq.put("file_id","");
            jsReq.put("app_file_id",id);
            jsReq.put("dt_s","");
            jsReq.put("dt_e","");
            JSONObject req = new JSONObject(jsReq.toString());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("gdid","1-1001");
            jsonBody.put("geo", jsonObject);
            jsonBody.put("req",req);
            jsReq.put("dt",currentTime);

            JSONObject jsonReq = new JSONObject();
            jsonReq.put("msg", 113);
            jsonReq.put("token", Login.token);
            jsonReq.put("body",jsonBody);

            MyService.client.send(jsonReq.toString());

        } catch (Exception e) {
            Toast.makeText(BirthdaySound.this,"发送失败！",Toast.LENGTH_SHORT).show();
        }

    }
    android.os.Handler handler=new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x09:
                    mTextViewRecordTime.setText("正在录音..."+time);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 计算录音时的时间
     */

    private void timedTask() {
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                Message message = handler.obtainMessage();
                message.obj = time;
                message.what = 0x09;
                handler.sendMessage(message);
                time++;
            }
        }, 0, 1000);
    }

    private void deltetTime() {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                while(isRecoding){
//                    Message message = handler.obtainMessage();
//                    message.obj =time;
//                    message.what = 0x09;
//                    handler.sendMessage(message);
//                    time++;
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
    }

    /**
     * 长按录音时展示的对话框
     */
    private void showRecorderDialog() {
        dialog = new Dialog(this, R.style.callserviceDialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View dialogSelf = inflater.inflate(R.layout.recorder_time,null);
        mTextViewRecordTime = (TextView) dialogSelf.findViewById(R.id.textview_recordertime);
        mImageViewShow = (ImageView) dialogSelf.findViewById(R.id.imageview_animation);
        animationDrawable = (AnimationDrawable) mImageViewShow.getBackground();
        dialog.setContentView(dialogSelf);
        dialog.show();
    }

    /**
     * 从本地读取图片
     * @param path
     * @return
     */
    private Bitmap getLocalImage(String path) {
        Bitmap deviceBitmap = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                deviceBitmap = BitmapFactory.decodeFile(path);
            }
        } catch (Exception e) {
        }
        return deviceBitmap;
    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        if (ringPlayer != null) {
            ringPlayer.release();
        }
        super.onDestroy();
    }
}
