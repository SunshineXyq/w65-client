package com.bluedatax.w65.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
//                    commitModifyPsw();
                    dialog.dismiss();
                    mButtonFindpassCommit.setText("修改成功，点击登录");
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
    private TextView mTextViewServiceNumber;
    private Dialog dialog1;

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
        TextView tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowServiceDialog();
            }
        });
    }

    private void ShowServiceDialog() {
        dialog1 = new Dialog(FindPassword.this, R.style.callserviceDialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View dialogSelf = inflater.inflate(R.layout.activity_callservice, null);
        Button button_ok = (Button) dialogSelf.findViewById(R.id.callservice_button_yes);
        Button button_cancel = (Button) dialogSelf.findViewById(R.id.callservice_button_cancel);
        mTextViewServiceNumber = (TextView) dialogSelf.findViewById(R.id.text_setvice_number);
        dialog1.setContentView(dialogSelf);
        dialog1.show();
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_CALL);
                intent1.setData(Uri.parse("tel:" + mTextViewServiceNumber.getText()));
                startActivity(intent1);
                dialog1.dismiss();
            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_findpass_commit:
                etPassword = etModifyPsw.getText().toString().trim();
                if (!TextUtils.isEmpty(etPassword)) {
                    if ("提交".equals(mButtonFindpassCommit.getText())) {
                        mLayoutEditpassword.setVisibility(View.GONE);
                        new SendHandler().sendHandler(FORGET_PASS, handler);
                        dialog = new LoadingDialog(this);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                }
                if ("修改成功，点击登录".equals(mButtonFindpassCommit.getText())) {
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
