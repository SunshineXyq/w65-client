package com.bluedatax.w65.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.MainActivity;
import com.bluedatax.w65.R;
import com.bluedatax.w65.Service.MyService;
import com.bluedatax.w65.net.InternetConnection;
import com.bluedatax.w65.utils.getTime;
import com.bluedatax.w65.utils.loadDialog.LoadingDialog;
import com.bluedatax.w65.utils.sendHandler.SendHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class TempPassword extends BaseActivity implements OnClickListener {


    private int i = 120;
    private ImageView mImageDotVerify;
    private ImageView mImageDotLogin;

    private TextView mTextViewVerify;
    private TextView mTextViewLogin;

    private TextView mTextViewPhone;
    private EditText mEditTextVerify;
    private Button mButtonSend;
    private Button mButtonNext;
    private TextView mTextViewHelpInfo;

    private LoadingDialog dialog;
    private SharedPreferences mSharedPreference;
    private String latitude;
    private String lnglatitude;
    private String currentTime;
    private String verifyNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temppassword);
        mSharedPreference = getSharedPreferences("count", MODE_PRIVATE);
        latitude = mSharedPreference.getString("lat", "");
        lnglatitude = mSharedPreference.getString("lng", "");
        currentTime = getTime.getCurrentTime();
        mImageDotVerify = (ImageView) findViewById(R.id.image_dot_verify);
        mImageDotLogin = (ImageView) findViewById(R.id.image_dot_login);
        mTextViewVerify = (TextView) findViewById(R.id.text_verify);
        mTextViewLogin = (TextView) findViewById(R.id.text_temp_login);
        mTextViewPhone = (TextView) findViewById(R.id.text_phone);
        mEditTextVerify = (EditText) findViewById(R.id.edittext_verify_number);
        mTextViewHelpInfo = (TextView) findViewById(R.id.textview_helpinfo);
        mButtonSend = (Button) findViewById(R.id.button_send_number);
        mButtonSend.setOnClickListener(this);
        mButtonNext = (Button) findViewById(R.id.button_next);
        mButtonNext.setOnClickListener(this);
        mButtonNext.setClickable(false);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case CLOCK_SECOND:
                    mButtonSend.setText(msg.obj.toString());
                    if (i == 0) {
                        mButtonSend.setText("重新发送");
                    }
                    break;
                case SEND_PHONE:
//                    getVerificationCode();
                    mButtonNext.setBackgroundResource(R.drawable.button_commit);
                    mButtonNext.setTextColor(Color.RED);
                    mTextViewHelpInfo.setVisibility(View.VISIBLE);
                    mTextViewHelpInfo.setText(String.format(getResources().getString
                            (R.string.temppassword_text_info),verifyNumber));
                    mButtonNext.setClickable(true);
                    break;
                case SEND_CODE:
                    responseOfCode();
                    break;
                default:
                    break;
            }
        }

    };

    private void getVerificationCode() {

        mButtonNext.setBackgroundResource(R.drawable.button_commit);
        mButtonNext.setTextColor(Color.RED);
        mTextViewHelpInfo.setVisibility(View.VISIBLE);
        mTextViewHelpInfo.setText(String.format(getResources().getString
                (R.string.temppassword_text_info),verifyNumber));
        mButtonNext.setClickable(true);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", 123);
            jsonObject.put("token",Login.initToken);
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("upn", "13552972613");
            jsonBody.put("ts",currentTime);
            jsonBody.put("cc",86);
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lng", lnglatitude);
            jsonGeo.put("lat", latitude);

            jsonBody.put("geo", jsonGeo);
            jsonObject.put("body", jsonBody);

            MyService.client.send(jsonObject.toString());

            System.out.println("发送的json串" + jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void responseOfCode() {
        InternetConnection.getInstance().addRequest("https://www.baidu.com", null, new InternetConnection.OnConnectionListerner() {
            @Override
            public void isConnection(boolean connection, String type) {

            }

            @Override
            public void onSuccessConnection(String response) {
                Toast.makeText(getApplicationContext(), "验证码已经成功发送，可以登录", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                Intent intent = new Intent(TempPassword.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailConnection(String response, int statusCode) {
                Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_send_number:
                verifyNumber = mEditTextVerify.getText().toString().trim();
                if (!TextUtils.isEmpty(verifyNumber)) {
                    if (mButtonSend.getText().equals("点击发送")) {
                        deltetTime();
                        new SendHandler().sendHandler(SEND_PHONE, handler);
                        mImageDotLogin.setImageResource(R.mipmap.temppass_dot_checked);
                        mTextViewLogin.setTextColor(Color.RED);
                        mImageDotVerify.setImageResource(R.mipmap.temppass_dot_normal);
                        mTextViewVerify.setTextColor(Color.GRAY);
                    } else if (mButtonSend.getText().equals("重新发送")) {
                        i = 120;
                        deltetTime();
                        new SendHandler().sendHandler(SEND_PHONE, handler);
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"手机号不能为空",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.button_next:
                //点击向服务器发送验证码，并弹出dialog
                new SendHandler().sendHandler(SEND_CODE, handler);
                dialog = new LoadingDialog(this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                break;
            default:
                break;
        }
    }

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
                    message.what = CLOCK_SECOND;
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
}
