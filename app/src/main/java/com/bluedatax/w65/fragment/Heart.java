package com.bluedatax.w65.fragment;



import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluedatax.w65.R;
import com.bluedatax.w65.Service.MyService;
import com.bluedatax.w65.activity.FamilyNumber;
import com.bluedatax.w65.activity.LeaveMessage;
import com.bluedatax.w65.activity.LongSideRemind;
import com.bluedatax.w65.utils.SharedPrefsUtil;
import com.bluedatax.w65.utils.getTime;

import org.json.JSONObject;

/**
 *  爱心定制
 */
public class Heart extends Fragment implements View.OnClickListener{
    private RelativeLayout mLeavetMessage;
    private RelativeLayout mLayoutBirthday;
    private RelativeLayout mLayoutMedicine;
    private TextView mLayoutLongSeat;
    private RelativeLayout mFamilyNumber;
    private SharedPreferences mSharedPreference;
    private String CurrentTime;
    private String latitude;
    private String lnglatitude;
    private long token;
    private ImageView longSwitch;
    private ImageView birthdaySwitch;
    private ImageView medicineSwitch;
    private boolean status = false;
    private int count;
    private int mSwitch;
    private int bSwitch;
    private Drawable open;
    private Drawable close;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.activity_heart, null);
        longSwitch = (ImageView) view.findViewById(R.id.longseat_switch);
        birthdaySwitch = (ImageView) view.findViewById(R.id.birthday_switch);
        medicineSwitch = (ImageView) view.findViewById(R.id.medicine_switch);
        longSwitch.setOnClickListener(this);
        birthdaySwitch.setOnClickListener(this);
        medicineSwitch.setOnClickListener(this);
        mFamilyNumber = (RelativeLayout) view.findViewById(R.id.family_number);
        mFamilyNumber.setOnClickListener(this);
        mLeavetMessage= (RelativeLayout) view.findViewById(R.id.heart_leave_message);
        mLeavetMessage.setOnClickListener(this);
        mLayoutLongSeat= (TextView) view.findViewById(R.id.layout_longseat);
        mLayoutLongSeat.setOnClickListener(this);
        mLayoutBirthday= (RelativeLayout) view.findViewById(R.id.layout_birthdayremind);
        mLayoutBirthday.setOnClickListener(this);
        mLayoutMedicine= (RelativeLayout) view.findViewById(R.id.layout_medicineremind);
        mLayoutMedicine.setOnClickListener(this);
        CurrentTime = getTime.getCurrentTime();
        token = SharedPrefsUtil.getLong(getActivity(), "loginToken", 0L);
        mSharedPreference = getActivity().getSharedPreferences("count", Context.MODE_PRIVATE);
        latitude = mSharedPreference.getString("lat", "");
        lnglatitude = mSharedPreference.getString("lng", "");
        count = mSharedPreference.getInt("flag", 0);
        bSwitch = mSharedPreference.getInt("birthdaySwitch", 0);
        mSwitch = mSharedPreference.getInt("medicineSwitch", 0);
        close = getResources().getDrawable(R.mipmap.heart_switch_close);
        open = getResources().getDrawable(R.mipmap.heart_switch_open);
        close.setBounds(0, 0, 30, 55);
        open.setBounds(0, 0, 30, 55);
        if (count == 1) {
            longSwitch.setImageDrawable(open);
        } else {
            longSwitch.setImageDrawable(close);
        }
        if (bSwitch == 1){
            birthdaySwitch.setImageDrawable(open);
        } else {
            birthdaySwitch.setImageDrawable(close);
        }
        if (mSwitch == 1) {
            medicineSwitch.setImageDrawable(open);
        } else {
            medicineSwitch.setImageDrawable(close);
        }
        return view;
	}


    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.family_number:
                intent =new Intent(getActivity(), FamilyNumber.class);
                startActivity(intent);
                break;
            case R.id.heart_leave_message:
                //电话留言
                intent =new Intent(getActivity(), LeaveMessage.class);
                startActivity(intent);
                break;
            case R.id.layout_longseat:
                //久坐提醒
                intent=new Intent(getActivity(), LongSideRemind.class);
                intent.putExtra("titleName","久坐提醒");
                startActivity(intent);
                break;
            case R.id.layout_birthdayremind:
                intent=new Intent(getActivity(), LongSideRemind.class);
                intent.putExtra("titleName","生日提醒");
//                queryBirthday();
                startActivity(intent);
                break;
            case R.id.layout_medicineremind:
                intent=new Intent(getActivity(), LongSideRemind.class);
                intent.putExtra("titleName","用药提醒");
                startActivity(intent);
                break;
            case R.id.longseat_switch:
                if (!status) {
                    longSwitch.setImageDrawable(open);
                    status = true;
                    mSharedPreference.edit().putInt("flag",1).commit();
                } else {
                    longSwitch.setImageDrawable(close);
                    status = false;
                    mSharedPreference.edit().putInt("flag",0).commit();
            }
                break;
            case R.id.birthday_switch:
                if (!status) {
                    birthdaySwitch.setImageDrawable(open);
                    status = true;
                    mSharedPreference.edit().putInt("birthdaySwitch",1).commit();
                } else {
                    birthdaySwitch.setImageDrawable(close);
                    status = false;
                    mSharedPreference.edit().putInt("birthdaySwitch",0).commit();
                }
                break;
            case R.id.medicine_switch:
                if (!status) {
                    medicineSwitch.setImageDrawable(open);
                    status = true;
                    mSharedPreference.edit().putInt("medicineSwitch",1).commit();
                } else {
                    medicineSwitch.setImageDrawable(close);
                    status = false;
                    mSharedPreference.edit().putInt("medicineSwitch",0).commit();
                }
                break;
            default:
                break;
        }
    }


    /**
     *
     * 查询生日提醒铃声
     *
     */
    private void queryBirthday() {

        try {
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lat", latitude);
            jsonGeo.put("lng", lnglatitude);
            Log.d("json经纬度", jsonGeo.toString());
            JSONObject jsonObject = new JSONObject(jsonGeo.toString());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("gdid","1-1001");
            jsonBody.put("action", "query");
            jsonBody.put("dt", CurrentTime);
            jsonBody.put("geo", jsonObject);


            JSONObject jsonType = new JSONObject();
            JSONObject jsonBremind = new JSONObject();
//            jsonBremind.put("file_id","");
//            jsonBremind.put("title","录音1");
//            jsonBremind.put("ring_title","ring1");
//            jsonBremind.put("ring_id",1);
//            jsonBremind.put("play_dt","2016-01-30 10:30:00");
//            jsonBremind.put("loop",1);
            jsonType.put("bremind",jsonBremind);
            jsonBody.put("type",jsonType);

            JSONObject jsonReq = new JSONObject();
            jsonReq.put("msg", 114);
            jsonReq.put("token",token);
            jsonReq.put("body",jsonBody);

            MyService.client.send(jsonReq.toString());

        } catch (Exception e) {

        }
    }
}


