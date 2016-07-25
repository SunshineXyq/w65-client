package com.bluedatax.w65.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.R;
import com.bluedatax.w65.fragment.FirstPager;
import com.bluedatax.w65.utils.DateTimePickDialogUtil;
import com.bluedatax.w65.utils.SharedPrefsUtil;
import com.bluedatax.w65.utils.bitmapTransparency.TransparentBitmap;
import com.bluedatax.w65.utils.getTime;

import java.util.Map;

/**
 * Created by bdx108 on 15/12/2.
 */
public class LongSideRemind extends BaseActivity implements View.OnClickListener{
    public TextView textViewMiddle;
    private RelativeLayout setBirthdayTime;
    private TextView devicePersonalMessage;
    private TextView birthdayDate;
    private String initStartDateTime = "2013年9月3日 14:44";
    private String initEndDateTime = "2016年3月11日 17:29";
    private RelativeLayout birthdayRing;
    private RelativeLayout birthdayRemindRing;
    private RelativeLayout medicineRemind;
    private ImageView birthdayImageOne;
    private ImageView birthdayImageTwo;
    private String pathOne;
    private String pathTwo;
    public static int deviceSign;
    private String current;
    private String pathThree;
    private String titleName;
    private String gdid_1;
    private String gdid_2;
    private String gdid_1_name;
    private String gdid_1_name1;
    private String gdid_2_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_longsite_remind);
        deviceSign = 1;
        current = getTime.getCurrentTime();
        setBirthdayTime = (RelativeLayout) findViewById(R.id.set_birthday_date);
        devicePersonalMessage = (TextView) findViewById(R.id.device_personal_message);
        birthdayDate = (TextView) findViewById(R.id.birthday_date);
        textViewMiddle= (TextView) findViewById(R.id.textview_title_middle);
        birthdayRing = (RelativeLayout) findViewById(R.id.birthday_ring);
        birthdayRemindRing = (RelativeLayout) findViewById(R.id.birthday_remind_ring);
        medicineRemind = (RelativeLayout) findViewById(R.id.use_medicine_remind);
        System.out.println("默认设备deviceSign" + deviceSign);

        Map map_1 = (Map) FirstPager.list.get(0);
        Map map_2 = (Map)FirstPager.list.get(1);
        gdid_1 = (String)map_1.get("gdid");
        gdid_2 = (String)map_2.get("gdid");
        gdid_1_name = (String) map_1.get("name");
        gdid_2_name = (String) map_2.get("name");

        birthdayImageOne = (ImageView) findViewById(R.id.birthday_image_one);
        birthdayImageTwo = (ImageView) findViewById(R.id.birthday_image_two);
        pathOne = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage1.jpg";
        pathTwo = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage2.jpg";
        pathThree = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage3.jpg";
        Bitmap birthdayTwo = TransparentBitmap.getTransparentBitmap(FamilyNumber.getLocalImage(pathTwo), 50);
        if (pathOne != null) {
            birthdayImageOne.setVisibility(View.VISIBLE);
            birthdayImageOne.setImageBitmap(FamilyNumber.getLocalImage(pathOne));
        }
        if (pathThree != null) {
            birthdayImageTwo.setVisibility(View.VISIBLE);
            birthdayImageTwo.setImageBitmap(birthdayTwo);
        }
        birthdayRing.setOnClickListener(this);
        birthdayRemindRing.setOnClickListener(this);
        birthdayImageOne.setOnClickListener(this);
        birthdayImageTwo.setOnClickListener(this);
        medicineRemind.setOnClickListener(this);
        Intent intent=getIntent();
        titleName = intent.getStringExtra("titleName");
        textViewMiddle.setText(titleName);
        if (titleName.equals("生日提醒")) {
            setBirthdayTime.setVisibility(View.VISIBLE);
        } else if (titleName.equals("用药提醒")) {
            medicineRemind.setVisibility(View.VISIBLE);
        }
//        birthdayDate.setText(current);

        setBirthdayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        LongSideRemind.this, initEndDateTime);
                dateTimePicKDialog.dateTimePicKDialog(LongSideRemind.this,birthdayDate);
                System.out.println(birthdayDate.getText().toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        devicePersonalMessage.setText(gdid_1_name+"(设备ID:"+gdid_1+ ")");
        if (SharedPrefsUtil.getValue(LongSideRemind.this,"birthday_one","1") != null && deviceSign == 1) {
            String date = SharedPrefsUtil.getValue(LongSideRemind.this,"birthday_one",current);
            System.out.println("取出生日日期"+date);
            birthdayDate.setText(date);
        } else if (SharedPrefsUtil.getValue(LongSideRemind.this,"birthday_two","1") != null && deviceSign == 2){
            String date = SharedPrefsUtil.getValue(LongSideRemind.this,"birthday_two",current);
            System.out.println("取出生日日期"+date);
            birthdayDate.setText(date);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.birthday_ring:
                Intent ringIntent = new Intent(LongSideRemind.this,BirthdaySound.class);
                ringIntent.putExtra("ringSign","ring");
                ringIntent.putExtra("deviceRing",deviceSign);
                startActivity(ringIntent);
                break;
            case R.id.birthday_remind_ring:
                Intent remindIntent = new Intent(LongSideRemind.this,BirthdaySound.class);
                remindIntent.putExtra("ringSign","remind");
                remindIntent.putExtra("remindSign",titleName);
                remindIntent.putExtra("deviceSign",deviceSign);
                System.out.println("您传递的第x个设备标记------" + deviceSign );
                startActivity(remindIntent);
                break;
            case R.id.birthday_image_one:
                devicePersonalMessage.setText(gdid_1_name+"(设备ID:"+gdid_1+ ")");
                Bitmap birthday_one = TransparentBitmap.getTransparentBitmap(FamilyNumber.getLocalImage(pathOne), 100);
                Bitmap birthday_two = TransparentBitmap.getTransparentBitmap(FamilyNumber.getLocalImage(pathTwo), 50);
                birthdayImageOne.setImageBitmap(birthday_one);
                birthdayImageTwo.setImageBitmap(birthday_two);
                if (SharedPrefsUtil.getValue(LongSideRemind.this,"birthday_one","1") != null) {
                    String date = SharedPrefsUtil.getValue(LongSideRemind.this, "birthday_one", current);
                    System.out.println("取出生日日期" + date);
                    birthdayDate.setText(date);
                }
                deviceSign = 1;
                System.out.println("点击第一个设备时deviceSign" + deviceSign);
                break;
            case R.id.birthday_image_two:
                devicePersonalMessage.setText(gdid_2_name + "(设备ID:"+gdid_2+ ")");
                Bitmap birthdayOne = TransparentBitmap.getTransparentBitmap(FamilyNumber.getLocalImage(pathOne), 50);
                Bitmap birthdayTwo = TransparentBitmap.getTransparentBitmap(FamilyNumber.getLocalImage(pathTwo), 100);
                birthdayImageOne.setImageBitmap(birthdayOne);
                birthdayImageTwo.setImageBitmap(birthdayTwo);
                deviceSign = 2;
                if (SharedPrefsUtil.getValue(LongSideRemind.this,"birthday_two","1") != null){
                    String date = SharedPrefsUtil.getValue(LongSideRemind.this,"birthday_two",current);
                    System.out.println("取出生日日期"+date);
                    birthdayDate.setText(date);
                }
                System.out.println("点击第二个设备时deviceSign" + deviceSign);
                break;
            case R.id.use_medicine_remind:
               Intent intent = new Intent(LongSideRemind.this,UseMedicineMessage.class);
               startActivity(intent);
            default:
                break;
        }
    }
}