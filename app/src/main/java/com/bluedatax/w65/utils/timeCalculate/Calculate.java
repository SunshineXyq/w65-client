package com.bluedatax.w65.utils.timeCalculate;

import com.baidu.mapapi.common.SysOSUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xuyuanqiang on 4/8/16.
 */
public class Calculate {

    private static long dt;

    public static String caculateHour (int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - i);
        int date = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String dateHour = date+"日"+hour+"时";
        return dateHour;
    }
    public static String caculateDay (int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH) -i);
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH)+1;
        String monthDate = month+"月"+date+"日";
        return monthDate;
    }
    public static long caculateHours (int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016,02,01,00,00,00);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - i);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatTime = df.format(calendar.getTime());
        System.out.println("需要查询的时间段"+formatTime);
        try {
            Date dt1 = df.parse(formatTime);
            dt = dt1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    public static String calculateWeek (int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH) -i);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println("获取一天中的小时"+hour);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String weekday = df.format(calendar.getTime());
        System.out.println("平日"+weekday);
        return weekday;
    }

    public static long caculateCurrentDay () {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatTime = df.format(calendar.getTime());
        System.out.println("当前时间"+formatTime);
        try {
            Date dt1 = df.parse(formatTime);
            dt = dt1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }



    public static long caculateWeekday (int i) {
        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH) -i);
//        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - Calendar.HOUR_OF_DAY);
//        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - Calendar.MINUTE);
//        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) - Calendar.SECOND);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH) -i);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatTime = df.format(calendar.getTime());
        System.out.println("今天的零点时刻"+formatTime);
        try {
            Date dt1 = df.parse(formatTime);
            dt = dt1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

}