package com.bluedatax.w65.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.R;
import com.bluedatax.w65.adapter.HealthAdapter;
import com.bluedatax.w65.data.HealthInfoData;
import com.bluedatax.w65.net.InternetConnection;
import com.bluedatax.w65.utils.loadDialog.LoadingDialog;
import com.bluedatax.w65.utils.pullRefresh.XListView;
import com.bluedatax.w65.utils.sendHandler.SendHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by bdx108 on 15/11/24.
 */
public class HealthInfo extends BaseActivity implements XListView.IXListViewListener{

    private ImageView mImageViewPeron1;
    private ImageView mImageViewPeron2;
    private ImageView mImageViewPeron3;
    private ImageView mImageViewPeron4;
    private ImageView mImageViewPeron5;
    private ImageView mImageViewPeron6;

    private XListView mListView;
    private List<HealthInfoData>mDatas;
    private HealthAdapter mAdapter;
    private LayoutInflater mInflater;

    private LoadingDialog mDialog;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==GET_HEALTH_INFO){
                responseOfHealthInfo();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_info);
        initWidgets();
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        initData();
        mInflater=getLayoutInflater();
        mAdapter=new HealthAdapter(mDatas,mInflater);
        //mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"可以弹出一个对话框用来显示具体信息情况",Toast.LENGTH_LONG).show();
            }
        });
        new SendHandler().sendHandler(GET_HEALTH_INFO,handler);
        mDialog=new LoadingDialog(this);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    private void initWidgets() {
        mImageViewPeron1= (ImageView) findViewById(R.id.history_image_one);
        mImageViewPeron2= (ImageView) findViewById(R.id.history_image_two);
        mImageViewPeron3= (ImageView) findViewById(R.id.history_image_three);
        mImageViewPeron4= (ImageView) findViewById(R.id.history_image_four);
        mImageViewPeron5= (ImageView) findViewById(R.id.history_image_five);
        mImageViewPeron6= (ImageView) findViewById(R.id.history_image_six);
        mListView= (XListView) findViewById(R.id.listview_health);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showDeviceCount();
    }

    private void showDeviceCount() {
        String pathOne = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage1.jpg";
        String pathTwo = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage2.jpg";
        String pathThree = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage3.jpg";
        String pathFour = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage4.jpg";
        String pathFive = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage5.jpg";
        String pathSix = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage6.jpg";
        if (pathOne.length() != 0) {
            mImageViewPeron1.setVisibility(View.VISIBLE);
            mImageViewPeron1.setImageBitmap(getLocalImage(pathOne));
        }
        if (pathTwo.length() != 0) {
            mImageViewPeron2.setVisibility(View.VISIBLE);
            mImageViewPeron2.setImageBitmap(getLocalImage(pathTwo));
        }
        if (pathThree.length() != 0) {

        }
    }

    private Bitmap getLocalImage(String path) {
        Bitmap deviceBitmap = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                deviceBitmap = BitmapFactory.decodeFile(path);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return deviceBitmap;

    }

    private void initData() {
        mDatas=new ArrayList<HealthInfoData>();
        mDatas.add(new HealthInfoData("2013", R.mipmap.ic_launcher,R.mipmap.people,"安全","",R.mipmap.ic_launcher,3));
        mDatas.add(new HealthInfoData("2013",R.mipmap.ic_launcher,R.mipmap.people,"摔倒","已经处理",R.mipmap.ic_launcher,10));
        mDatas.add(new HealthInfoData("2014",R.mipmap.ic_launcher,R.mipmap.people,"摔倒","已经处理",R.mipmap.ic_launcher,20));
        mDatas.add(new HealthInfoData("2015",R.mipmap.ic_launcher,R.mipmap.people,"摔倒","已经处理",R.mipmap.ic_launcher,7));
        mDatas.add(new HealthInfoData("2015",R.mipmap.ic_launcher,R.mipmap.people,"摔倒","已经处理",R.mipmap.ic_launcher,15));
    }

    @Override
    public void onRefresh() {
        mListView.stopRefresh();
    }

    @Override
    public void onLoadMore() {
        mListView.stopLoadMore();
    }
    private void responseOfHealthInfo() {
        InternetConnection.getInstance().addRequest("https://www.baidu.com",null,new InternetConnection.OnConnectionListerner() {
            @Override
            public void isConnection(boolean connection, String type) {

            }
            @Override
            public void onSuccessConnection(String response) {
                //获取数据
                Toast.makeText(getApplicationContext(),"获取到列表信息",Toast.LENGTH_LONG).show();
                mListView.setAdapter(mAdapter);
                mDialog.dismiss();
            }

            @Override
            public void onFailConnection(String response, int statusCode) {
                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_LONG).show();
                mDialog.dismiss();
            }
        });
    }
}
