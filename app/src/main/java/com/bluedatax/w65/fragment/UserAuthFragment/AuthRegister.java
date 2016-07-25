package com.bluedatax.w65.fragment.UserAuthFragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.MainActivity;
import com.bluedatax.w65.R;
import com.bluedatax.w65.net.InternetConnection;
import com.bluedatax.w65.utils.loadDialog.LoadingDialog;
import com.bluedatax.w65.utils.sendHandler.SendHandler;

/**
 * 用户授权注册
 * Created by bdx108 on 15/11/28.
 */
public class AuthRegister extends Fragment {
    private Button mButtonAuthRegister;
    private EditText mEtEquipment,mEtPhone,mEtName,mEtPassword;
    private LoadingDialog dialog;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            responseOfAuthRegister();
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_auth_register,null);
        mButtonAuthRegister= (Button) view.findViewById(R.id.button_authregister);
        mEtEquipment= (EditText) view.findViewById(R.id.et_equipment);
        mEtPhone= (EditText) view.findViewById(R.id.et_phone);
        mEtName= (EditText) view.findViewById(R.id.et_name);
        mEtPassword= (EditText) view.findViewById(R.id.et_password);
        Log.d("1234567",mEtEquipment.getText().toString());
        mButtonAuthRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("1234567",mEtEquipment.getText().toString());
                if (mEtEquipment.getText().toString().length()==0){
                    Toast.makeText(getActivity().getApplicationContext(),"授权设备不能为空",Toast.LENGTH_LONG).show();
                }else if(mEtPhone.getText().toString().length()==0){
                    Toast.makeText(getActivity().getApplicationContext(),"授权设备"+mEtEquipment.getText().toString().length(),Toast.LENGTH_LONG).show();

                    Toast.makeText(getActivity().getApplicationContext(),"用户手机号不能为空",Toast.LENGTH_LONG).show();
                }else if (mEtName.getText().toString().length()==0){
                    Toast.makeText(getActivity().getApplicationContext(),"用户名称不能为空",Toast.LENGTH_LONG).show();
                }else if (mEtPassword.getText().toString().length()==0){
                    Toast.makeText(getActivity().getApplicationContext(),"登录密码不能为空",Toast.LENGTH_LONG).show();
                }else {
                    new SendHandler().sendHandler(BaseActivity.AUTH_REGISTER,handler);
                    dialog=new LoadingDialog(getActivity());
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }

            }
        });
        return view;
    }
    private void responseOfAuthRegister() {
        InternetConnection.getInstance().addRequest("https://www.baidu.com",null,new InternetConnection.OnConnectionListerner() {
            @Override
            public void isConnection(boolean connection, String type) {

            }
            @Override
            public void onSuccessConnection(String response) {
                dialog.dismiss();
                Toast.makeText(getActivity().getApplicationContext(),"用户授权注册成功",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailConnection(String response, int statusCode) {
                Toast.makeText(getActivity().getApplicationContext(),"网络连接失败",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }
}
