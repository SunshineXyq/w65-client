package com.bluedatax.w65.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.R;
import com.bluedatax.w65.fragment.UserAuthFragment.AuthInquire;
import com.bluedatax.w65.fragment.UserAuthFragment.AuthRegister;

/**
 * 用户授权
 * Created by bdx108 on 15/11/28.
 */
public class UserAuthorization extends BaseActivity implements View.OnClickListener{
    //包含注册和查询的两个启动fragment的按钮
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonRegister;
    private RadioButton mRadioButtonInquire;

    private FragmentManager mManger;
    private FragmentTransaction mTransaction;

    private AuthRegister mAuthRegister;
    private AuthInquire mAuthInquire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_authorization);
        mRadioGroup= (RadioGroup) findViewById(R.id.radiogroup_auth);
        mRadioButtonRegister= (RadioButton) findViewById(R.id.radiobutton_auth_register);
        mRadioButtonRegister.setOnClickListener(this);
        mRadioButtonInquire= (RadioButton) findViewById(R.id.radiobutton_auth_inquire);
        mRadioButtonInquire.setOnClickListener(this);

        mAuthRegister=new AuthRegister();
        mAuthInquire=new AuthInquire();

        mManger=getFragmentManager();
        mTransaction=mManger.beginTransaction();
        mTransaction.replace(R.id.framelayout_auth,mAuthRegister);
        mRadioGroup.check(R.id.radiobutton_auth_register);
        mTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        mTransaction=mManger.beginTransaction();
        switch (v.getId()){
            case R.id.radiobutton_auth_register:
                mTransaction.replace(R.id.framelayout_auth,mAuthRegister);
                mTransaction.commit();
                break;
            case R.id.radiobutton_auth_inquire:
                mTransaction.replace(R.id.framelayout_auth,mAuthInquire);
                mTransaction.commit();
                break;
            default:
                break;
        }
    }
}
