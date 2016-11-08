package com.bluedatax.w65.activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bluedatax.w65.ActivityControler;
import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.MainActivity;
import com.bluedatax.w65.R;
import com.bluedatax.w65.Service.MyService;
import com.bluedatax.w65.Service.OnConnectListener;
import com.bluedatax.w65.net.InternetConnection;
import com.bluedatax.w65.utils.GetPhoneMes.GetAppVersion;
import com.bluedatax.w65.utils.MD5Utils;
import com.bluedatax.w65.utils.SharedPrefsUtil;
import com.bluedatax.w65.utils.android_websockets.ConnectActivity;
import com.bluedatax.w65.utils.android_websockets.WebSocketClient;
import com.bluedatax.w65.utils.getTime;
import com.bluedatax.w65.utils.loadDialog.LoadingDialog;
import com.bluedatax.w65.activity.StartAnimation;
import com.bluedatax.w65.utils.timeCalculate.Calculate;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.tablemanager.Connector;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 登录界面
 */

public class Login extends BaseActivity implements OnClickListener {

    private TextView mTextViewService;//客服
    private TextView mTextViewTemppass;//临时密码
    private TextView mTextViewForgetpass;//忘记密码
    private EditText mEditTextUsername;//输入用户名
    private EditText mEditTextPassword;//输入密码
    private TextView mTextViewServiceNumber;//拨打客服dialog上的电话号码
    private Button mButtonLogin;//登录按钮
    private Button button_ok;//dialog上的yes按钮
    private Button button_cancel;//dialog上的cancel按钮
    private Intent intent;//用来启动activity
    private Dialog dialog = null;//拨打客服的dialog
    public static SharedPreferences mSharedPreference;
    private SharedPreferences.Editor mEditor;//将账号密码保存
    private final String ACTION_NAME = "发送广播";
    private String userName;
    private String password;
    private String duid;
    private StringBuffer sb;
    private TelephonyManager tm;
    private String currentTime;
    private String name;
    private String sver;
    private String model;
    private String aver;
    private Activity mContext = this;
    public String DEVICE_ID;
    private MyService myService;
    //    private MyReceiver myReceiver;
    public static String latitude;
    public static String lnglatitude;
    public static long token;
    private LoadingDialog mDialog;
    public static long initToken = 0;
    private String md5Password;
    private long loginToken;
    private String fub;
    private String auid;
    private boolean connected = true;
    private String loginSign;
    private Timer mTimer;


    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("123456", "*************");
            //返回一个MsgService对象
            myService = ((MyService.MyBinder) service).getService();
            System.out.println("绑定服务成功");
            startConnect();
            //注册回调接口来接收变化
            myService.setOnConnectListener(new OnConnectListener() {
                @Override
                public void onHeartbeat(JSONObject heartbeatJSON) {
                    if (heartbeatJSON != null) {
                        parseHeartbeatJOSNObject(heartbeatJSON);
                    } else {
                        startConnect();
                    }
                }
                @Override
                public void onJSonObject(JSONObject json) {
                    try {
                        if (json.getInt("msg") == 10) {
                            parseInitJSONObject(json);
                        } else if (json.getInt("msg") == 110) {
                            loginSign = "登录成功";
//                            openTimedTask();
                            parseLoginJSONObject(json);
                        }
                    } catch (Exception e) {
                    }
                }

                public void onError(Exception msg) {
                    error = msg;
                    startReconnect();
                    System.out.println("你好，你走这了吗" + msg.getMessage());
                }

                public void onConnected(String notice) {
                    Log.d("传递过来的notice", notice);
                    System.out.println(notice);
                    if (notice.equals("Connected")) {
                        initServer();
                    } else if (notice.equals("")) {
                        Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onRingJSONObject(JSONObject ringJSON) {
                }

                public void onFamilyNumber(JSONObject familyJSON) {
                }

                @Override
                public void onDisconnect(String message) {
                    startService(in);
                }
            });
        }
    };
    private Timer reconnectTimer;

    private void parseHeartbeatJOSNObject(JSONObject heartbeatJSON) {
        try {
            token = heartbeatJSON.getLong("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openTimedTask() {
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendHeartMsg();
            }
        }, 1000, 5000);
    }

    private void startConnect() {
        startService(in);
    }


    private void startReconnect() {
        reconnectTimer = new Timer();
        reconnectTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                startConnect();
            }
        }, 5000);
    }

    private Exception error;
    private Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        System.out.println(String.format("%d", Calculate.caculateHours(20)));
        initWidget();

        currentTime = getTime.getCurrentTime();
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        name = android.os.Build.MANUFACTURER;       //name
        Log.d("loginActivity", name);
        sver = android.os.Build.VERSION.RELEASE;    //sver
        Log.d("loginActivity", sver);
        model = Build.MODEL;                        //device model 型号
        Log.d("loginActivity", model);
        GetAppVersion mGetAppVersion = new GetAppVersion(mContext);
        aver = mGetAppVersion.getVersion();         //aver
        Log.d("loginActivity", aver);
        DEVICE_ID = tm.getDeviceId();
        Log.d("loginActivity", DEVICE_ID);
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

        in = new Intent(this, MyService.class);
        in.putExtra("duid", duid);
//        bindService(in, conn, Context.BIND_AUTO_CREATE);

        userName = mEditTextUsername.getText().toString();
        mEditTextUsername.setText(SharedPrefsUtil.getValue(mContext, "userPhone", ""));
        password = mEditTextPassword.getText().toString();
        md5Password = MD5Utils.encode(password);
        Log.d("MD5密码", md5Password);
    }


    public String getUserPhone() {
        String phone = mEditTextUsername.getText().toString();
        if (phone != null) {
            return phone;
        }
        return "";
    }

    public void saveUserPhone() {
        String phone = getUserPhone();
        SharedPrefsUtil.putValue(mContext, "userPhone", phone);
    }

    public void checkLogin() {
        if (mEditTextUsername.getText().toString().trim().equals("")
                || mEditTextPassword.getText().toString().trim().equals("")) {
            Toast.makeText(Login.this, "手机号或登录密码为空",
                    Toast.LENGTH_LONG).show();
            mEditTextUsername.setFocusable(true);
        } else {

            if (mEditTextUsername.getText().toString().length() == 11) {
                String num = "[1][3458]\\d{9}";
                Pattern p = Pattern.compile(num);       //编译
                Matcher m = p.matcher(mEditTextUsername.getText().toString().trim());     //匹配程序
                if (m.matches()) {
                    saveUserPhone();
                    intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
//                    if (error != null) {
//                        Toast.makeText(this, "网络连接错误", Toast.LENGTH_SHORT).show();
//                    } else {
//                        initLogin();
//                    }
                } else {
                    Toast.makeText(Login.this, "请输入正确格式的手机号",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(Login.this, "请输入11位手机号",
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    private void initLogin() {
        String user = mEditTextUsername.getText().toString();

        try {
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lat", latitude);
            jsonGeo.put("lng", lnglatitude);
            Log.d("json经纬度", jsonGeo.toString());
            JSONObject jsonObject = new JSONObject(jsonGeo.toString());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("upn", user);
            jsonBody.put("psw", md5Password);
            jsonBody.put("ts", currentTime);
            jsonBody.put("geo", jsonObject);

            JSONObject jsonReq = new JSONObject();
            jsonReq.put("msg", 110);
            jsonReq.put("token", initToken);
            jsonReq.put("body", jsonBody);

            MyService.client.send(jsonReq.toString());

        } catch (Exception e) {

        }
    }

    /**
     * 初始化数据
     */

    private void initServer() {
        mSharedPreference = getSharedPreferences("count", MODE_PRIVATE);
        latitude = mSharedPreference.getString("lat", "");
        lnglatitude = mSharedPreference.getString("lng", "");
        Log.d("取出存入的经纬度", latitude + "#" + lnglatitude);

        try {
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lat", latitude);
            jsonGeo.put("lng", lnglatitude);
            Log.d("json经纬度", jsonGeo.toString());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("duid", duid);
            jsonBody.put("name", name);
            jsonBody.put("model", model);
            jsonBody.put("sver", sver);
            jsonBody.put("aver", aver);
            jsonBody.put("type", "2");
            jsonBody.put("ts", currentTime);
            jsonBody.put("geo", jsonGeo);

            JSONObject jsonReq = new JSONObject();
            jsonReq.put("msg", 10);
            jsonReq.put("body", jsonBody);

            MyService.client.send(jsonReq.toString());

        } catch (Exception e) {

        }
    }

    /**
     * 解析登录返回的数据
     */
    private void parseLoginJSONObject(JSONObject json) {
        try {
            token = json.getLong("token");
            Log.d("解析登录返回的token数据", loginToken + "");
            JSONObject body = json.getJSONObject("body");
            Log.d("解析后的body数据", body + "");
            int id = body.getInt("id");
            Log.d("id", id + "");
            String name = body.getString("name");
            Log.d("解析后的name数据", name);
            auid = body.getString("auid");
            Log.d("auid", auid);
            int ast = body.getInt("ast");
            Log.d("解析后的ast数据", ast + "");
            fub = body.getString("fub");
            Log.d("解析后的fub数据", fub);
            String ts = body.getString("ts");
            Log.d("解析后的tm数据", ts);
        } catch (Exception e) {
        }
        SharedPrefsUtil.putValue(mContext, "fub", fub);
        SharedPrefsUtil.putValue(mContext, "auid", auid);
        SharedPrefsUtil.putLong(mContext, "loginToken", loginToken);
        intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * 发送心跳
     */

    private void sendHeartMsg() {

        try {
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lat", latitude);
            jsonGeo.put("lng", lnglatitude);
            Log.d("json经纬度", jsonGeo.toString());
            JSONObject jsonObject = new JSONObject(jsonGeo.toString());

            JSONObject jsonBody = new JSONObject();

            jsonBody.put("geo", jsonObject);
            jsonBody.put("ts", currentTime);
            jsonBody.put("ht", currentTime);

            JSONObject jsonReq = new JSONObject();
            jsonReq.put("msg", 11);
            jsonReq.put("body", jsonBody);

            MyService.client.send(jsonReq.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析初始化的json数据
     */
    private void parseInitJSONObject(JSONObject json) {
        try {
            initToken = json.getLong("token");
            Log.d("解析后的token数据", initToken + "");
            JSONObject body = json.getJSONObject("body");
            Log.d("解析后的body数据", body + "");
            String htp = body.getString("htp");
            Log.d("解析后的htp数据", htp + "");
            String spn = body.getString("spn");
            Log.d("解析后的spn数据", spn + "");
            String laver = body.getString("laver");
            Log.d("解析后的laver数据", laver + "");
            String upgurl = body.getString("upgurl");
            Log.d("解析后的upgurl数据", upgurl + "");
            String appurl = body.getString("appurl");
            Log.d("解析后的appurl数据", appurl + "");
            String sturl = body.getString("sturl");
            Log.d("解析后的sturl数据", sturl + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化界面上的某些控件
     */
    private void initWidget() {
        mTextViewService = (TextView) findViewById(R.id.login_service);
        mTextViewService.setOnClickListener(this);//客服
        mTextViewTemppass = (TextView) findViewById(R.id.login_temppass);
        mTextViewTemppass.setOnClickListener(this);//临时密码
        mTextViewForgetpass = (TextView) findViewById(R.id.login_forgetpass);
        mTextViewForgetpass.setOnClickListener(this);//忘记密码
        mButtonLogin = (Button) findViewById(R.id.button_login);
        mButtonLogin.setOnClickListener(this);//登录
        mEditTextUsername = (EditText) findViewById(R.id.edittext_username);
        mEditTextPassword = (EditText) findViewById(R.id.edittext_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_service:
                //客服电话按钮，启动dialog
                ShowServiceDialog();
                isCallClick();
                break;
            case R.id.login_temppass:
                //临时登陆，启动临时登陆界面
                intent = new Intent(Login.this, TempPassword.class);
                startActivity(intent);
                break;
            case R.id.login_forgetpass:
                //忘记密码，点击启动找回密码界面
//            mEditor.putString("username",mEditTextUsername.getText().toString());
//            mEditor.putString("password",mEditTextPassword.getText().toString());
//            mEditor.commit();
                intent = new Intent(Login.this, ForgetPassword.class);
                startActivity(intent);
                break;
            case R.id.button_login:
                //登录按钮，启动主界面
//                if (initToken != 0) {
//                    SQLiteDatabase db = Connector.getDatabase();
//                    checkLogin();
//                    intent = new Intent(Login.this, MainActivity.class);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(Login.this,"网络不给力，请检查网络设置",Toast.LENGTH_LONG).show();
//                }
                checkLogin();

                break;
            default:
                break;
        }
    }

    /**
     * 拨打客服电话dialog上的两个按钮的点击事件
     */
    private void isCallClick() {
        button_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_CALL);
                intent1.setData(Uri.parse("tel:" + mTextViewServiceNumber.getText()));
                startActivity(intent1);
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "启动打电话界面", Toast.LENGTH_LONG).show();
            }
        });
        button_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 显示拨打客服的dialog
     */
    private void ShowServiceDialog() {
        dialog = new Dialog(Login.this, R.style.callserviceDialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View dialogSelf = inflater.inflate(R.layout.activity_callservice, null);
        button_ok = (Button) dialogSelf.findViewById(R.id.callservice_button_yes);
        button_cancel = (Button) dialogSelf.findViewById(R.id.callservice_button_cancel);
        mTextViewServiceNumber = (TextView) dialogSelf.findViewById(R.id.text_setvice_number);
        dialog.setContentView(dialogSelf);
        dialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //需要处理
            ActivityControler.finishAll();
//            MyService.client.disconnect();
//            if (error == null && loginSign != null) {
//                sendExit();
//            }
        }
        return false;
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
            jsonReq.put("token", initToken);
            jsonReq.put("body", jsonBody);

            MyService.client.send(jsonReq.toString());

        } catch (Exception e) {
        }
    }

    protected void onDestroy() {
//        unbindService(conn);
        if (reconnectTimer != null) {
            reconnectTimer.cancel();
        }
        Log.d("LoginActivity", "取消了绑定服务");
        super.onDestroy();
        ActivityControler.removeActivity(this);
    }
}
