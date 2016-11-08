package com.bluedatax.w65.fragment;



import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bluedatax.w65.R;
import com.bluedatax.w65.activity.MineDeviceDetail;
import com.bluedatax.w65.utils.loadDialog.LoadingDialog;
import com.bluedatax.w65.activity.HealthInfo;
import com.bluedatax.w65.activity.Setting;
import com.bluedatax.w65.activity.UserAuthorization;

import java.io.File;
import java.util.Map;

/**
 * 我的
 */
public class Mine extends Fragment implements View.OnClickListener{
    private RelativeLayout mLayoutAuth;//用户授权
    private RelativeLayout mLayoutSetting;//设置
    private RelativeLayout mLayoutHistory;//历史信息
    private LoadingDialog mDialog;
    private ImageView deviceTwo1;
    private ImageView deviceTwo2;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//        getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view=inflater.inflate(R.layout.activity_mine, null);
        //初始化控件
        initWidget(view);

        ImageView deviceTwo1 = (ImageView) view.findViewById(R.id.device_message_detail).findViewById(R.id.devicetwo1);
        ImageView deviceTwo2 = (ImageView) view.findViewById(R.id.device_message_detail).findViewById(R.id.devicetwo2);
        ImageView deviceTwoMine = (ImageView) view.findViewById(R.id.device_message_detail).findViewById(R.id.device_two_mine);
        deviceTwoMine.setImageBitmap(FirstPager.mDownloadImageMine);
        String pathOne = Environment.getExternalStorageDirectory()+"/w65/icon_download/"+ "downloadImage1.jpg";
        String pathTwo = Environment.getExternalStorageDirectory()+"/w65/icon_download/"+ "downloadImage2.jpg";
        if(pathOne.length()!= 0) {
            deviceTwo1.setImageBitmap(getLocalImage(pathOne));
        }
        if (pathTwo.length() != 0) {
            deviceTwo2.setImageBitmap(getLocalImage(pathTwo));
        }
        return view;
	}

    private void initWidget(View view) {
        mLayoutAuth= (RelativeLayout) view.findViewById(R.id.layout_auth);
        mLayoutAuth.setOnClickListener(this);
        mLayoutSetting= (RelativeLayout) view.findViewById(R.id.layout_setting);
        mLayoutSetting.setOnClickListener(this);
        mLayoutHistory= (RelativeLayout) view.findViewById(R.id.layout_history);
        deviceTwo1 = (ImageView) view.findViewById(R.id.device_message_detail).findViewById(R.id.devicetwo1);
        deviceTwo2 = (ImageView) view.findViewById(R.id.device_message_detail).findViewById(R.id.devicetwo2);
        mLayoutHistory.setOnClickListener(this);

//        Map map = (Map)FirstPager.list.get(0);
//        final String gdid = (String)map.get("gdid");
//        final String name = (String)map.get("name");
//        final String srv_status = (String)map.get("srv_status");
//        final String upn = (String)map.get("upn");
//
//        Map mapLi = (Map)FirstPager.list.get(1);
//        final String gdidLi = (String)mapLi.get("gdid");
//        final String nameLi = (String)mapLi.get("name");
//        final String srv_statusLi = (String)mapLi.get("srv_status");
//        final String upnLi = (String)mapLi.get("upn");


        deviceTwo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in_one = new Intent(getActivity(), MineDeviceDetail.class);
//                in_one.putExtra("gdid",gdid);
//                in_one.putExtra("name",name);
//                in_one.putExtra("srv_status",srv_status);
//                in_one.putExtra("upn",upn);
                startActivity(in_one);
            }
        });
        deviceTwo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in_two = new Intent(getActivity(), MineDeviceDetail.class);
//                in_two.putExtra("gdid",gdidLi);
//                in_two.putExtra("name",nameLi);
//                in_two.putExtra("srv_status",srv_statusLi);
//                in_two.putExtra("upn",upnLi);
                startActivity(in_two);
            }
        });
    }



    /**
     * 点击启动用户授权和设置的界面
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_auth:
                Intent intentAuth=new Intent(getActivity(), UserAuthorization.class);
                startActivity(intentAuth);
                break;
            case R.id.layout_setting:
                Intent intentSetting=new Intent(getActivity(), Setting.class);
                startActivity(intentSetting);
                break;
            case R.id.layout_history:
                Intent intentHistory=new Intent(getActivity(), HealthInfo.class);
                startActivity(intentHistory);
                break;
            default:
                break;
        }
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
