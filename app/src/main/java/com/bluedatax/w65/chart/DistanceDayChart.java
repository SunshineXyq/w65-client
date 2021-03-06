package com.bluedatax.w65.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.bluedatax.w65.fragment.FirstPager;
import com.bluedatax.w65.utils.sql_query_dev_dt.queryHour;
import com.bluedatax.w65.utils.timeCalculate.Calculate;

/**
 * 行走距离的今天的图表
 * Created by bdx108 on 15/11/23.
 */
public class DistanceDayChart extends View {
    private int width;
    private int height;
    private Paint mPaintAxle;//坐标轴的画笔
    private Paint mPaintBack;//背景画笔
    private Paint mPaintTable;//背景网格
    private Paint mPaintText;//文本颜色
    private Paint mPaintDot;//点
    private String[]mDates;
    private String[]heartNums;
    private int[]mHeartDatas1;
    private int[]mHeartDatas2;
    public DistanceDayChart(Context context) {
        super(context);
    }

    public DistanceDayChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaintAxle =new Paint();
        mPaintAxle.setColor(Color.BLACK);
        mPaintAxle.setStrokeWidth(5);

        //背景画笔
        mPaintBack =new Paint();
        mPaintBack.setColor(Color.GREEN);  //设置画笔颜色
        mPaintBack.setAlpha(50);           //设置画笔透明度

        mPaintTable=new Paint();
        mPaintTable.setColor(Color.GRAY);
        mPaintAxle.setStrokeWidth(3);      //当画笔是空心样式时，设置画笔空心的宽度

        mPaintText=new Paint();
        mPaintText.setColor(Color.BLUE);
        mPaintText.setStrokeWidth(4);
        mPaintText.setTextSize(20f);

        mPaintDot=new Paint();
        mPaintDot.setColor(Color.RED);
        mPaintDot.setStyle(Paint.Style.FILL_AND_STROKE);     //设置画笔样式即填充和描边
        mDates= new String[]{Calculate.caculateHour(28),Calculate.caculateHour(24),Calculate.caculateHour(20),Calculate.caculateHour(16),
                Calculate.caculateHour(12),Calculate.caculateHour(8),Calculate.caculateHour(4),
                Calculate.caculateHour(0)};
        heartNums=new String[]{"50","100","150","200","250","300","350","400","450","500","550","600","650","700","750","800"};
//        mHeartDatas1=new int[]{FirstPager.averageStep7,FirstPager.averageStep1,
//                FirstPager.averageStep2,FirstPager.averageStep3,
//                FirstPager.averageStep4,FirstPager.averageStep5,FirstPager.averageStep6};
//        System.out.println(FirstPager.averageStep7+"--"+FirstPager.averageStep1);
        mHeartDatas2=new int[]{200,300,400,375,460,550,475};
        mHeartDatas1=new int[]{500,550,735,600,650,600,625};
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width=getDefaultSize(getSuggestedMinimumWidth(),widthMeasureSpec);       //获取最小宽度作为默认值
        height=getDefaultSize(getSuggestedMinimumHeight(),heightMeasureSpec);    //获取最小高度作为默认值
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(30,height-30,width,height-30, mPaintAxle);//x轴
        canvas.drawLine(30,height-30,30,0, mPaintAxle);//y轴
        canvas.drawRect(30,0,width,height-30,mPaintBack);//背景

        for (int i=0;i<8;i++){
            canvas.drawLine(30+(width-30)/8*(i+1),height-30,30+(width-30)/8*(i+1),0,mPaintTable);//竖格
            canvas.drawText(mDates[i],30+(width-30)/8*i-mPaintText.measureText(mDates[i])/2,height-10,mPaintText);
        }
        for (int i=0;i<16;i++){
            canvas.drawLine(30,(i+1)*(height-30)/16,width-10,(i+1)*(height-30)/16,mPaintTable);//横格
            canvas.drawText(heartNums[i],15,height-30-i*((height-30)/16),mPaintText);
        }
        for (int i=0;i<7;i++){
            canvas.drawCircle(30+(width-30)/8f*i,height-(((mHeartDatas1[i]-50)/50f)*((height-30)/16f)+30),5,mPaintDot);
//            canvas.drawCircle(30+(width-30)/8f*i,height-((mHeartDatas2[i]/2100f)*(height-30)+30),5,mPaintDot);
            canvas.drawCircle(30+(width-30)/8f*i,height-(((mHeartDatas2[i]-50)/50f)*((height-30)/16f)+30),5,mPaintAxle);

            if (i<6){
//                canvas.drawLine(30+(width-30)/8f*i,height-((mHeartDatas2[i]/2100f)*(height-30)+30),30+(width-30f)/8*(i+1),
//                        height-((mHeartDatas2[i+1]/2100f)*(height-30)+30),mPaintText);
//                canvas.drawLine(30+(width-30)/8f*i,height-((mHeartDatas1[i]/(height-30))*(height-30)+30),30+(width-30f)/8*(i+1),
//                        height-((mHeartDatas1[i+1]/(height-30))*(height-30)+30),mPaintText);
                canvas.drawLine(30+(width-30)/8f*i,height-(((mHeartDatas1[i]-50)/50f)*((height-30)/16f)+30),30+(width-30f)/8*(i+1),
                        height-(((mHeartDatas1[i+1]-50f)/50)*((height-30)/16f)+30),mPaintText);
                canvas.drawLine(30+(width-30)/8f*i,height-(((mHeartDatas2[i]-50)/50f)*((height-30)/16f)+30),30+(width-30f)/8*(i+1),
                        height-(((mHeartDatas2[i+1]-50f)/50)*((height-30)/16f)+30),mPaintText);
            }
        }
    }
}