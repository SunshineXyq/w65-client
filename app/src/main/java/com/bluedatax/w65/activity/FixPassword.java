package com.bluedatax.w65.activity;

import android.content.Intent;
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
import com.bluedatax.w65.net.InternetConnection;
import com.bluedatax.w65.utils.loadDialog.LoadingDialog;
import com.bluedatax.w65.utils.sendHandler.SendHandler;

/**
 *
 * Created by bdx108 on 15/11/25.
 */
public class FixPassword extends BaseActivity implements View.OnClickListener{
    private TextView mTextViewMiddle;
    private TextView mTextViewRight;
    private Button mButtonCommit;
    private EditText mEditOldPass;
    private EditText mEditNewPass;
    private LoadingDialog mDialog;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==FIXPASS_COMMIT){
                responseOfFixPass();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_fixpassword);
        mTextViewMiddle= (TextView) findViewById(R.id.textview_title_middle);
        mTextViewMiddle.setText("修改密码");
        mTextViewRight= (TextView) findViewById(R.id.textview_title_right);
        mTextViewRight.setText("关闭");
        mButtonCommit= (Button) findViewById(R.id.button_fixpass_commit);
        mButtonCommit.setOnClickListener(this);
        mEditOldPass= (EditText) findViewById(R.id.edittext_oldpass_fix);
        mEditNewPass= (EditText) findViewById(R.id.edittext_newpass_fix);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_fixpass_commit:
                if (mEditOldPass.getText().toString().equals(mEditNewPass.getText().toString())){
                    Toast.makeText(getApplicationContext(),"新密码与旧密码相同",Toast.LENGTH_SHORT).show();
                    break;
                }else {
                    new SendHandler().sendHandler(FIXPASS_COMMIT,handler);
                    mDialog=new LoadingDialog(this);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                }
                break;
            default:
                break;
        }
    }
    private void responseOfFixPass() {
        InternetConnection.getInstance().addRequest("https://www.baidu.com",null,new InternetConnection.OnConnectionListerner() {
            @Override
            public void isConnection(boolean connection, String type) {

            }
            @Override
            public void onSuccessConnection(String response) {
                mDialog.dismiss();
                Toast.makeText(getApplicationContext(),"修改成功，请重新登录",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(FixPassword.this,Login.class);
                startActivity(intent);


            }

            @Override
            public void onFailConnection(String response, int statusCode) {
                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_LONG).show();
                mDialog.dismiss();
            }
        });
    }
}
