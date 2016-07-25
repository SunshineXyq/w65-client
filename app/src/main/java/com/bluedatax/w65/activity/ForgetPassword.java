package com.bluedatax.w65.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.R;
import com.bluedatax.w65.Service.MyService;
import com.bluedatax.w65.net.InternetConnection;
import com.bluedatax.w65.utils.loadDialog.LoadingDialog;
import com.bluedatax.w65.utils.sendHandler.SendHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 忘记密码
 */
public class ForgetPassword extends BaseActivity implements View.OnClickListener {
    private TextView mTextViewMiddle;//标题中间的内容
    private Button mButtonGetMessage;//获取短信验证码的按钮
    private TextView mTextViewMessage;//用来显示发送的具体情况描述
    private Button mButtonNext;//下一步
    private EditText mEditTextMessage;//输入验证码的edittext
    private int i = 120;//发送时间
    private LoadingDialog dialog;
    private SharedPreferences mSharedPreference;
    private String latitude;
    private String lnglatitude;
    private String verifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword);
        mSharedPreference = getSharedPreferences("count", MODE_PRIVATE);
        latitude = mSharedPreference.getString("lat", "");
        lnglatitude = mSharedPreference.getString("lng", "");
        initWidgets();
    }

    private void initWidgets() {
        mTextViewMiddle = (TextView) findViewById(R.id.textview_title_middle);
        mButtonGetMessage = (Button) findViewById(R.id.button_get_message);
        mTextViewMessage = (TextView) findViewById(R.id.text_find_message);
        mButtonNext = (Button) findViewById(R.id.button_find_next);
        mButtonNext.setOnClickListener(this);
        mButtonNext.setClickable(false);
        mEditTextMessage = (EditText) findViewById(R.id.et_find_password);
        mButtonGetMessage.setOnClickListener(this);
        mTextViewMiddle.setText("获取手机短信验证码");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_get_message:
                verifyCode = mEditTextMessage.getText().toString().trim();
                new SendHandler().sendHandler(FORGET_PHONE, handler);
                dialog = new LoadingDialog(this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                deltetTime();
                break;
            case R.id.button_find_next:
                new SendHandler().sendHandler(FORGET_CODE, handler);
                dialog = new LoadingDialog(this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                break;
            default:
                break;
        }
    }

    /**
     * 用handler来对界面刷新
     */
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case CLOCK_SECOND_DELETE:
                    mButtonGetMessage.setText(msg.obj.toString());
                    if (mEditTextMessage.getText().toString() != null) {
                        mButtonNext.setBackgroundResource(R.drawable.button_commit);
                        mButtonNext.setTextColor(Color.RED);
                    }
                    if (i == 0) {
                        mButtonGetMessage.setText("重新发送");
                    }
                    break;
                case FORGET_PHONE:
                    getPhoneVerificationCode();            //获取手机验证码
                    break;
                case FORGET_CODE:
                    setLoginPassword();
                    break;
                default:
                    break;
            }
        }

    };

    /**
     * 设置发送验证码的剩余时间
     */
    private void deltetTime() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                while (i > 0) {
                    Message message = handler.obtainMessage();
                    message.obj = i;
                    message.what = CLOCK_SECOND_DELETE;
                    handler.sendMessage(message);
                    i--;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }

    private void setLoginPassword() {

        dialog.dismiss();
        String verifyCode = mEditTextMessage.getText().toString().trim();
        Intent intent = new Intent(ForgetPassword.this, FindPassword.class);
        intent.putExtra("verifyCode",verifyCode);
        startActivity(intent);

    }

    private void getPhoneVerificationCode() {
        dialog.dismiss();
        mTextViewMessage.setVisibility(View.VISIBLE);
        mTextViewMessage.setText(String.format(getResources().
                getString(R.string.temppassword_text_info),verifyCode));
        mButtonNext.setVisibility(View.VISIBLE);
        mButtonNext.setClickable(true);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", 12);
            jsonObject.put("token",Login.initToken);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("cc", 86);
            jsonBody.put("upn", "13552972613");

            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lng", lnglatitude);
            jsonGeo.put("lat", latitude);
            System.out.println("组成的jsonGeo" + jsonGeo);
            System.out.println("jsonGeo toString" + jsonGeo.toString());
            JSONObject geoJson = new JSONObject(jsonGeo.toString());
            System.out.println("new jsonGeo" + geoJson);

            jsonBody.put("geo", jsonGeo);
            jsonObject.put("body", jsonBody);

            MyService.client.send(jsonObject.toString());

            System.out.println("发送的json串" + jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
