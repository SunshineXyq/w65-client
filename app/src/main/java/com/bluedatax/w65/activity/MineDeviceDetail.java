package com.bluedatax.w65.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.R;
import com.bluedatax.w65.fragment.FirstPager;

import java.io.File;

public class MineDeviceDetail extends BaseActivity {

    private TextView mTextViewMiddle;
    private ImageView image;
    private TextView tvEquipment;
    private TextView tvUser;
    private TextView tvRoot;
    private TextView tvCharge;
    private TextView tvLeftName;
    private String srv_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mine_device_detail);
        String pathOne = Environment.getExternalStorageDirectory()+"/w65/icon_download/"+ "downloadImage1.jpg";
        String pathTwo = Environment.getExternalStorageDirectory()+"/w65/icon_download/"+ "downloadImage2.jpg";
        mTextViewMiddle= (TextView) findViewById(R.id.textview_title_middle);
        image= (ImageView) findViewById(R.id.image_device_detail);
        mTextViewMiddle.setText("设备详情");
        tvEquipment = (TextView) findViewById(R.id.tv_equipment);
        tvUser = (TextView) findViewById(R.id.tv_user);
        tvRoot = (TextView) findViewById(R.id.tv_root);
        tvCharge = (TextView) findViewById(R.id.tv_charge);
        tvLeftName = (TextView) findViewById(R.id.tv_left_name);
        String gdid = getIntent().getStringExtra("gdid");
        String name = getIntent().getStringExtra("name");
        srv_status = getIntent().getStringExtra("srv_status");
        if (srv_status.equals("0")) {
            srv_status = "否";
        } else {
            srv_status = "是";
        }
        Log.d("12345", srv_status);
        String upn = getIntent().getStringExtra("upn");
        if (pathOne.length()!= 0 && gdid.equals("21-1")) {
            image.setImageBitmap(getLocalImage(pathOne));
        }else if(pathOne.length()!= 0 && gdid.equals("21-2")) {
            image.setImageBitmap(getLocalImage(pathTwo));
        }
        tvEquipment.setText(gdid);
        tvUser.setText(name);
        tvRoot.setText(upn);
        tvCharge.setText(srv_status);
        tvLeftName.setText(name);
    }
    private Bitmap getLocalImage(String path) {
        Bitmap deviceBitmap = null;
        try
        {
            File file = new File(path);
            if(file.exists())
            {
                deviceBitmap = BitmapFactory.decodeFile(path);
            }
        } catch (Exception e)
        {
            // TODO: handle exception
        }
        return deviceBitmap;

    }
}
