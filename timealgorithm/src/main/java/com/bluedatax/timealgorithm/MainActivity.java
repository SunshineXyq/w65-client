package com.bluedatax.timealgorithm;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends BaseActivity {

    private TextView Hour;
    private TextView week;
    private String CurrentTime;
    private String time;
    private Button button;
    private ImageView ic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Hour = (TextView) findViewById(R.id.hour);
        week = (TextView) findViewById(R.id.week);
        button = (Button) findViewById(R.id.bt_bt);
//        CurrentTime = getCurrentTime();
        ic = (ImageView) findViewById(R.id.ic);
        ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("你好，你点击了");
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try
//        {
//            java.util.Date now = df.parse(CurrentTime);
////            java.util.Date date=df.parse(tm_s);
//            long l=now.getTime() + (10*24*60*60*1000);
//            System.out.println(l);
//            long day=l/(24*60*60*1000);
//            long hour=(l/(60*60*1000)-day*24);
//            long min=((l/(60*1000))-day*24*60-hour*60);
//            long s=(l/1000-day*24*60*60-hour*60*60-min*60);
//            time = ""+day+"天"+hour+"小时"+min+"分"+s+"秒";
//            Log.d("时间", time);
//            System.out.println(""+day+"天"+hour+"小时"+min+"分"+s+"秒");
//            Hour.setText(l+"");
//
//        }
//        catch (Exception e)
//        {
//        }

        Hour.setText(caculate(1));
//        week.setText(month+"月"+date+"日"+"");
        compare_date("2016-04-04","2016-04-03");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("MainActivity onDestory!!!");
    }

//    private String getCurrentTime() {
//        Date date = new Date();
//
//        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
//
//        String CurrentTime = sdformat.format(date);
//
//        Log.d("AccidentReport获取的当前的时间", CurrentTime);
//
//        return CurrentTime;
//    }
    public static String caculate (int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - i);
//        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH) -7);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int date = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int month = calendar.get(Calendar.MONTH)+1;
        String day = date+"日"+hour+"时";
        System.out.println("一个小时前的时间："+ df.format(calendar.getTime()));
        System.out.println("当前的时间："+ df.format(new Date()));
        String time = df.format(calendar.getTime());
        return day;
    }
    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            System.out.println(dt1+"-----------"+dt2);
            if (dt1.getTime() >= dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                System.out.println(dt1.getTime());
                return 1;
            } else if (dt1.getTime() <= dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) { exception.printStackTrace();
        }
        return 0;
    }
}
