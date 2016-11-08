package com.bluedatax.w65;


import android.annotation.TargetApi;
import android.app.Activity;
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
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bluedatax.w65.Service.MyService;
import com.bluedatax.w65.activity.AccidentReport;
import com.bluedatax.w65.activity.Login;
import com.bluedatax.w65.fragment.Family;
import com.bluedatax.w65.fragment.FirstPager;
import com.bluedatax.w65.fragment.Heart;
import com.bluedatax.w65.fragment.Mine;
import com.bluedatax.w65.utils.SharedPrefsUtil;
import com.bluedatax.w65.utils.getTime;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements OnClickListener {

    private FragmentManager mManger;
    private FragmentTransaction mTransation;
    private final String ACTION_NAME = "发送广播";

    private static boolean isExit = false;

    private Family mFamily;
    private FirstPager mFirstPager;
    private Heart mHeart;
    private Mine mMine;


    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonFirst;
    private RadioButton mRadioButtonLove;
    private RadioButton mRadioButtonFamily;
    private RadioButton mRadioButtonMine;
    private SharedPreferences mSharedPreference;
    private String latitude;
    private String lnglatitude;
    private String currentTime;
    private long token;
    private Notification mNotification;
    private NotificationManager mNotificationManger;
    private String noticeJson;
    private PushMessageReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        mManger = getFragmentManager();
        setTabSelection(0);
        receiver = new PushMessageReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NAME);
        registerReceiver(receiver,intentFilter);



        mSharedPreference = getSharedPreferences("count", MODE_PRIVATE);
        latitude = mSharedPreference.getString("lat", "");
        lnglatitude = mSharedPreference.getString("lng", "");
        Log.d("取出存入的经纬度", latitude + "#" + lnglatitude);
        currentTime = getTime.getCurrentTime();
        token = SharedPrefsUtil.getLong(this, "loginToken", 0L);
        mNotificationManger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void initWidgets() {
        //主界面下的四个按钮
        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        mRadioButtonFirst = (RadioButton) findViewById(R.id.radiobutton_firstpager);
        mRadioButtonLove = (RadioButton) findViewById(R.id.radiobutton_love);
        mRadioButtonFamily = (RadioButton) findViewById(R.id.radiobutton_family);
        mRadioButtonMine = (RadioButton) findViewById(R.id.radiobutton_mine);
        mRadioButtonFirst.setOnClickListener(this);
        mRadioButtonLove.setOnClickListener(this);
        mRadioButtonFamily.setOnClickListener(this);
        mRadioButtonMine.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public class PushMessageReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            noticeJson = intent.getStringExtra("noticeJson");
            if (noticeJson != null) {
                StartNotification();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void StartNotification() {
        PendingIntent pend = PendingIntent.getActivities(MainActivity.this, 1, makeIntentStack(MainActivity.this)
                , PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification = new Notification.Builder(this).
                 setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("消息来了")
                .setContentTitle("注意啦！")
                .setContentText("这是一个用来显示提醒的notification")
                .setContentInfo("可以点击")
                .setContentIntent(pend).build();
        //将notification设置为自动取消，即点击完之后自动消失
        mNotification.defaults = Notification.DEFAULT_VIBRATE;
        //设置notification的提示音为默认和震动
        mNotification.defaults = Notification.DEFAULT_SOUND;
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotificationManger.notify(2, mNotification);
    }

    Intent[] makeIntentStack(Context context) {
        Intent[] intents = new Intent[2];
        intents[0] = Intent.makeRestartActivityTask(new ComponentName(context, MainActivity.class));
        intents[1] = new Intent(context, Mine.class);
        intents[1].setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("accidentJson", noticeJson);
        return intents;
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.radiobutton_firstpager:
                setTabSelection(0);
                break;
            case R.id.radiobutton_love:
                setTabSelection(1);
                break;
            case R.id.radiobutton_family:
                setTabSelection(2);
                break;
            case R.id.radiobutton_mine:
                setTabSelection(3);
                break;
            default:
                break;
        }
    }
    private void setTabSelection(int index)
    {
        resetBtn();
        mTransation = mManger.beginTransaction();
        hideFragments(mTransation);
        switch (index) {
            //首页中的图标变亮
            case 0:
                Drawable homePagePress = getResources().getDrawable(R.mipmap.home_page_press);
                homePagePress.setBounds(0,0,70,70);
                mRadioButtonFirst.setCompoundDrawables(null,homePagePress,null,null);
                if (mFirstPager == null ) {
                    mFirstPager = new FirstPager();
                    mTransation.add(R.id.framelayout_main, mFirstPager);
                    mRadioGroup.check(R.id.radiobutton_firstpager);
                } else {
                    mTransation.show(mFirstPager);
                    System.out.println("首页展示了");
                }
                break;
            case 1:
                Drawable lovePress = getResources().getDrawable(R.mipmap.love_press);
                lovePress.setBounds(0,0,70,70);
                mRadioButtonLove.setCompoundDrawables(null,lovePress,null,null);
                if (mHeart == null) {
                    mHeart = new Heart();
                    mTransation.add(R.id.framelayout_main, mHeart);
                } else {
                    mTransation.show(mHeart);
                }
                break;
            case 2:
                Drawable familyPress = getResources().getDrawable(R.mipmap.family_press);
                familyPress.setBounds(0,0,70,70);
                mRadioButtonFamily.setCompoundDrawables(null,familyPress,null,null);
                if (mFamily == null) {
                    mFamily = new Family();
                    mTransation.add(R.id.framelayout_main, mFamily);
                } else {
                    mTransation.show(mFamily);
                }
                break;
            case 3:
                Drawable minePress = getResources().getDrawable(R.mipmap.mine_press);
                minePress.setBounds(0,0,70,70);
                mRadioButtonMine.setCompoundDrawables(null,minePress,null,null);
                if (mMine == null) {
                    mMine = new Mine();
                    mTransation.add(R.id.framelayout_main, mMine);
                } else {
                    mTransation.show(mMine);
                }
                break;
            }
                mTransation.commit();
    }

    private void resetBtn() {
        Drawable homePage = getResources().getDrawable(R.mipmap.home_page_gray);
        Drawable love = getResources().getDrawable(R.mipmap.love_gray);
        Drawable family = getResources().getDrawable(R.mipmap.family_gray);
        Drawable mine = getResources().getDrawable(R.mipmap.mine_gray);
        homePage.setBounds(0,0,70,70);
        love.setBounds(0,0,70,70);
        family.setBounds(0, 0, 70, 70);
        mine.setBounds(0, 0, 70, 70);
        mRadioButtonFirst.setCompoundDrawables(null,homePage,null,null);
        mRadioButtonLove.setCompoundDrawables(null,love,null,null);
        mRadioButtonFamily.setCompoundDrawables(null,family,null,null);
        mRadioButtonMine.setCompoundDrawables(null,mine,null,null);
    }

    private void hideFragments(FragmentTransaction mTransation) {
        if (mFirstPager != null)
        {
            mTransation.hide(mFirstPager);
            System.out.println("首页隐藏了");
        }
        if (mHeart != null)
        {
            mTransation.hide(mHeart);
        }
        if (mFamily != null)
        {
            mTransation.hide(mFamily);
        }
        if (mMine != null)
        {
            mTransation.hide(mMine);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            //需要处理
//            Login.mTimer.cancel();
            exit();
        }
        return false;
    }
    private void exit() {
        if(!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
//            sendExit();
            Intent in = new Intent(this, MyService.class);
            stopService(in);
            ActivityControler.finishAll();
        }
    }
    private void sendExit() {
        try {
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lat", latitude);
            jsonGeo.put("lng", lnglatitude);
            Log.d("json经纬度", jsonGeo.toString());
            JSONObject jsonObject = new JSONObject(jsonGeo.toString());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("ts", currentTime);
            jsonBody.put("geo", jsonObject);

            JSONObject jsonReq = new JSONObject();
            jsonReq.put("msg", 111);
            jsonReq.put("token", Login.token);
            jsonReq.put("body", jsonBody);

            MyService.client.send(jsonReq.toString());

        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
