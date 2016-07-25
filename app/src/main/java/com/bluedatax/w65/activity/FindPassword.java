package com.bluedatax.w65.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.R;
import com.bluedatax.w65.Service.MyService;
import com.bluedatax.w65.net.InternetConnection;
import com.bluedatax.w65.utils.getTime;
import com.bluedatax.w65.utils.loadDialog.LoadingDialog;
import com.bluedatax.w65.utils.sendHandler.SendHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 忘记密码点击下一步之后到此页面
 * Created by bdx108 on 12/14/15.
 */
public class FindPassword extends BaseActivity implements View.OnClickListener {
    private LinearLayout mLayoutEditpassword;//编辑重置登录密码的edittext的LinearLayout
    private Button mButtonFindpassCommit;
    private EditText etModifyPsw;
    private TextView title;
    private LoadingDialog dialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FORGET_PASS:
                    commitModifyPsw();
                    break;
            }
        }
    };
    private SharedPreferences mSharedPreference;
    private String latitude;
    private String lnglatitude;
    private String currentTime;
    private String etPassword;
    private String verifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword_next);
        currentTime = getTime.getCurrentTime();
        verifyCode = getIntent().getStringExtra("verifyCode");
        mSharedPreference = getSharedPreferences("count", MODE_PRIVATE);
        latitude = mSharedPreference.getString("lat", "");
        lnglatitude = mSharedPreference.getString("lng","");
        mLayoutEditpassword = (LinearLayout) findViewById(R.id.layout_editpassword);
        etModifyPsw = (EditText) findViewById(R.id.et_modify_psw);
        title = (TextView) findViewById(R.id.textview_title_middle);
        title.setText("设置登录密码");
        mButtonFindpassCommit = (Button) findViewById(R.id.button_findpass_commit);
        mButtonFindpassCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_findpass_commit:
                etPassword = etModifyPsw.getText().toString().trim();
                mLayoutEditpassword.setVisibility(View.GONE);
                if ("提交".equals(mButtonFindpassCommit.getText())) {
                    new SendHandler().sendHandler(FORGET_PASS, handler);
                    dialog = new LoadingDialog(this);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                } else if ("修改成功，点击登录".equals(mButtonFindpassCommit.getText())) {
                    Intent intent = new Intent(FindPassword.this, Login.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    private void commitModifyPsw() {
        dialog.dismiss();
        mButtonFindpassCommit.setText("修改成功，点击登录");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", 122);
            jsonObject.put("token","");

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("sms_code",verifyCode);
            jsonBody.put("new_psw",etPassword);
            jsonBody.put("ts",currentTime);

            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lng",lnglatitude);
            jsonGeo.put("lat",latitude);
            jsonBody.put("body",jsonGeo);

            MyService.client.send(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
