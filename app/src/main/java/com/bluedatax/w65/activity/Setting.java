package com.bluedatax.w65.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bluedatax.w65.ActivityControler;
import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.R;
import com.bluedatax.w65.Service.MyService;
import com.bluedatax.w65.utils.SharedPrefsUtil;
import com.bluedatax.w65.utils.getTime;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by bdx108 on 15/11/25.
 */
public class Setting extends BaseActivity implements View.OnClickListener{
    private TextView mTextViewMiddle;
    private RelativeLayout mLayoutSetIcon;
    private ImageView mImageViewIcon;
    private SetIcon mSetIcon;
    private RelativeLayout mLayoutFixpassword;
    private RelativeLayout mLayoutFindpassword;
    private Button mButtonQuit;
    private SharedPreferences mSharedPreference;
    private String latitude;
    private String lnglatitude;
    private String currentTime;
    private long token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mTextViewMiddle= (TextView) findViewById(R.id.textview_title_middle);
        mLayoutFixpassword= (RelativeLayout) findViewById(R.id.layout_fixpassword);
        mLayoutFixpassword.setOnClickListener(this);
        mButtonQuit= (Button) findViewById(R.id.button_securityquit);
        mButtonQuit.setOnClickListener(this);
//        mLayoutFindpassword= (RelativeLayout) findViewById(R.id.layout_findpassword);
//        mLayoutFindpassword.setOnClickListener(this);
        mLayoutSetIcon= (RelativeLayout) findViewById(R.id.layout_seticon);
        mLayoutSetIcon.setOnClickListener(this);
        mImageViewIcon= (ImageView) findViewById(R.id.circleimage_icon);
        mSetIcon=new SetIcon();
        mTextViewMiddle.setText("设置");


        mSharedPreference = getSharedPreferences("count", MODE_PRIVATE);
        latitude = mSharedPreference.getString("lat", "");
        lnglatitude = mSharedPreference.getString("lng", "");
        Log.d("取出存入的经纬度", latitude + "#" + lnglatitude);
        currentTime = getTime.getCurrentTime();
        token = SharedPrefsUtil.getLong(this, "loginToken", 0L);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String path=Environment.getExternalStorageDirectory()+"/w65/icon_bitmap/"+ "myicon.jpg";
        if (path!=null){
            mImageViewIcon.setImageBitmap(getDiskBitmap(path));
        }else {
            Toast.makeText(getApplicationContext(),"zanshi没有照片",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.layout_seticon:
                intent=new Intent(Setting.this,SetIcon.class);
                startActivity(intent);
                break;
            case R.id.layout_fixpassword:
                intent=new Intent(Setting.this,FixPassword.class);
                startActivity(intent);
                break;
            case R.id.button_securityquit:
                sendExit();
                ActivityControler.finishAll();
                Intent in = new Intent(this, MyService.class);
                stopService(in);
                break;
            default:
                break;
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
            jsonReq.put("token", token);
            jsonReq.put("body", jsonBody);

            MyService.client.send(jsonReq.toString());

        } catch (Exception e) {

        }
    }


    /**
     * 从本地获取图片
     * @param pathString 文件路径
     * @return 图片
     */
    public Bitmap getDiskBitmap(String pathString)
    {
        Bitmap bitmap = null;
        try
        {
            File file = new File(pathString);
            if(file.exists())
            {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e)
        {
            // TODO: handle exception
        }
        return bitmap;
    }
}