package com.bluedatax.w65.utils.sql_query_dev_dt;

import com.bluedatax.w65.litepal.HeartRateStep;
import com.bluedatax.w65.utils.timeCalculate.Calculate;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by xuyuanqiang on 4/12/16.
 */
public class queryHour {

    public static int period(int a,int b){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int heartRateSum = 0;
        List<HeartRateStep> heartRateSteps = DataSupport.where("dev_dt between ? and ?", String.format("%d", Calculate.caculateHours(a)),
                String.format("%d", Calculate.caculateHours(b))).find(HeartRateStep.class);
        for (int i=0;i<heartRateSteps.size();i++) {
            Date date = new Date(heartRateSteps.get(i).getDev_dt());
            System.out.println("查到的时间"+df.format(date));
            System.out.println("id" + heartRateSteps.get(i).getId());
            System.out.println("心率"+heartRateSteps.get(i).getHeartRate());
            System.out.println("步数"+heartRateSteps.get(i).getSteps());
            System.out.println("--------****************-------");
            heartRateSum = heartRateSum + heartRateSteps.get(i).getHeartRate();
        }
        int heartAverage = heartRateSum/heartRateSteps.size();
        System.out.println("心率平均数"+heartAverage);
        return heartAverage;
    }
    public static int step(int a,int b){
        int stepsSum = 0;
        List<HeartRateStep> heartRateSteps = DataSupport.where("dev_dt between ? and ?", String.format("%d", Calculate.caculateHours(a)),
                String.format("%d", Calculate.caculateHours(b))).find(HeartRateStep.class);
        System.out.println(Calculate.caculateCurrentDay() + "今天的时间范围" +
                Calculate.caculateWeekday(1));
        for (int i=0;i<heartRateSteps.size();i++) {
            System.out.println("步数"+heartRateSteps.get(i).getSteps());
            System.out.println("--------****************-------");
            stepsSum = stepsSum + heartRateSteps.get(i).getSteps();
        }
        int stepAverage = stepsSum/heartRateSteps.size();
        System.out.println("步数平均数"+stepAverage);
        return stepAverage;
    }
    public static int currentDayHeartRate(int a){
        int currentHeartRateSum = 0;
        List<HeartRateStep> heartRateSteps = DataSupport.where("dev_dt between ? and ?", String.format("%d", Calculate.caculateWeekday(a)),
                String.format("%d", Calculate.caculateCurrentDay())).find(HeartRateStep.class);
        System.out.println(Calculate.caculateCurrentDay() + "今天的时间范围" +
                Calculate.caculateWeekday(1));
        for (int i=0;i<heartRateSteps.size();i++) {

            currentHeartRateSum = currentHeartRateSum + heartRateSteps.get(i).getHeartRate();

        }
        int curentHeartRateAverage = currentHeartRateSum/heartRateSteps.size();
        System.out.println("当天心率平均数"+curentHeartRateAverage);
        return curentHeartRateAverage;
    }

    public static int currentDayStep(int a){
        int curentStepsSum = 0;
        List<HeartRateStep> heartRateSteps = DataSupport.where("dev_dt between ? and ?", String.format("%d", Calculate.caculateWeekday(a)),
                String.format("%d", Calculate.caculateCurrentDay())).find(HeartRateStep.class);
        for (int i=0;i<heartRateSteps.size();i++) {

            curentStepsSum = curentStepsSum + heartRateSteps.get(i).getSteps();
        }
        int stepAverage = curentStepsSum/heartRateSteps.size();
        System.out.println("当天步数平均数"+stepAverage);
        return stepAverage;
    }

    public static int everyDayStep(int a,int b){
        int everyStepsSum = 0;
        List<HeartRateStep> heartRateSteps = DataSupport.where("dev_dt between ? and ?", String.format("%d", Calculate.caculateWeekday(a)),
                String.format("%d", Calculate.caculateWeekday(b))).find(HeartRateStep.class);

        for (int i=0;i<heartRateSteps.size();i++) {

            everyStepsSum = everyStepsSum + heartRateSteps.get(i).getSteps();
        }
        int everyStepAverage = everyStepsSum/heartRateSteps.size();
        System.out.println("每天步数平均数"+everyStepAverage);
        return everyStepAverage;
    }
    public static int everyHeartRate(int a,int b){
        int everyHeartRateSum = 0;
        List<HeartRateStep> heartRateSteps = DataSupport.where("dev_dt between ? and ?", String.format("%d", Calculate.caculateWeekday(a)),
                String.format("%d", Calculate.caculateWeekday(b))).find(HeartRateStep.class);

        for (int i=0;i<heartRateSteps.size();i++) {

            everyHeartRateSum = everyHeartRateSum + heartRateSteps.get(i).getSteps();
        }
        int everyHeartRateAverage = everyHeartRateSum/heartRateSteps.size();
        System.out.println("每天步数平均数"+everyHeartRateAverage);
        return everyHeartRateAverage;
    }
 }