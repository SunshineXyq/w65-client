package com.bluedatax.w65.activity;

import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.R;

/**
 * Created by bdx108 on 16/1/5.
 */
public class ImageLarge extends BaseActivity {
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_icon_large);
        mImageView= (ImageView) findViewById(R.id.image_large);
        String path= Environment.getExternalStorageDirectory()+"/w65/icon_bitmap/"+ "myicon.jpg";
        if (path!=null){
            mImageView.setImageBitmap(new Setting().getDiskBitmap(path));
        }else {
            mImageView.setImageResource(R.mipmap.ic_launcher);
            Toast.makeText(getApplicationContext(), "zanshi没有照片", Toast.LENGTH_LONG).show();
        }
    }
}
