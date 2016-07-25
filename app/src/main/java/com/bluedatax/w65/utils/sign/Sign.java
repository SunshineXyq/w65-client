package com.bluedatax.w65.utils.sign;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.R;
import com.bluedatax.w65.chart.SignView;

/**
 * Created by bdx108 on 15/12/11.
 */



public class Sign extends BaseActivity implements View.OnClickListener{

    private SignView mMyView;
    private Button mButtonClear;
    private Button mButtonSave;
    private ImageView mImageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);
        mMyView= (SignView) findViewById(R.id.myview);
        mButtonClear= (Button) findViewById(R.id.clear);
        mButtonClear.setOnClickListener(this);
        mButtonSave= (Button) findViewById(R.id.save);
        mButtonSave.setOnClickListener(this);
        mImageView= (ImageView) findViewById(R.id.image_sign);
        String path=Environment.getExternalStorageDirectory()+ "/w65/sign/"+ "sign.png";
        if (path!=null){
            mImageView.setImageBitmap(getDiskBitmap(path));
            Toast.makeText(getApplicationContext(),path,Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getApplicationContext(),"zanshi没有照片",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mMyView.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clear:
                mMyView.back();
                break;
            case R.id.save:
                saveSign();
                mImageView.setImageBitmap(mMyView.getBitmap());
                break;
            default:
                break;
        }
    }

    private File saveSign() {
        String path = Environment.getExternalStorageDirectory().toString()+ "/w65/sign/";
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        File signFile=new File(path+"sign.png");
//        try {//另一种写法
//            saveBitmapToJPG(mMyView.getBitmap(),signFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Intent mediaScanIntent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        Uri contentUri=Uri.fromFile(signFile);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
        try {

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(signFile));
            mMyView.getBitmap().compress(Bitmap.CompressFormat.PNG, 80, bos);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "图片保存成功，路径为" + path, Toast.LENGTH_LONG).show();
        Log.d("12345678",path.toString());
        return signFile;
    }
    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }
    private Bitmap getDiskBitmap(String pathString)
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

