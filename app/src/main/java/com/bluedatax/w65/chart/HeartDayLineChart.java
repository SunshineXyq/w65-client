package com.bluedatax.w65.chart;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.bluedatax.w65.fragment.FirstPager;
import com.bluedatax.w65.utils.timeCalculate.Calculate;

/**
 * 心率的一天的图表
 * Created by bdx108 on 15/11/23.
 */
public class HeartDayLineChart extends View {
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
    public HeartDayLineChart(Context context) {
        super(context);
    }

    public HeartDayLineChart(Context context, AttributeSet attrs) {
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
        mPaintText.setTextSize(20f);
        mPaintText.setStrokeWidth(4);

        mPaintDot=new Paint();
        mPaintDot.setColor(Color.RED);
        mPaintDot.setStyle(Paint.Style.FILL_AND_STROKE);     //设置画笔样式即填充和描边
        mDates= new String[]{Calculate.caculateHour(28),Calculate.caculateHour(24),Calculate.caculateHour(20),Calculate.caculateHour(16),
                Calculate.caculateHour(12),Calculate.caculateHour(8),Calculate.caculateHour(4),
                Calculate.caculateHour(0)};
        heartNums=new String[]{"40","45","50","55","60","65","70","75","80","85","90","95","100","105","110","115","120"};
//        mHeartDatas1=new int[]{FirstPager.average7,FirstPager.average1,FirstPager.average2,FirstPager.average3,
//                FirstPager.average4, FirstPager.average5, FirstPager.average6};
        mHeartDatas2=new int[]{80,90,85,70,72,63,75};
        mHeartDatas1=new int[]{90,70,65,80,62,73,80};

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

            canvas.drawCircle(30+(width-30)/8f*i,height-(((mHeartDatas2[i]-40)/5f)*((height-30)/16f)+30),5,mPaintText);
            canvas.drawCircle(30+(width-30)/8f*i,height-(((mHeartDatas1[i]-40)/5f)*((height-30)/16f)+30),5,mPaintDot);

//                canvas.drawCircle(30+(width-30)/8f*i,height-((mHeartDatas1[i]/180f)*(height-30)+30),5,mPaintDot);

//                canvas.drawCircle(30+(width-30)/8f*i,height-(((mHeartDatas2[i]-40)/5f)*((height-30)/16f)+30),5,mPaintAxle);

            if (i<6){
                canvas.drawLine(30+(width-30)/8f*i,height-(((mHeartDatas1[i]-40)/5f)*((height-30)/16f)+30),30+(width-30f)/8*(i+1),
                        height-(((mHeartDatas1[i+1]-40f)/5)*((height-30)/16f)+30),mPaintText);

//                canvas.drawLine(30+(width-30)/8f*i,height-((mHeartDatas2[i]/180f)*(height-30)+30),30+(width-30f)/8*(i+1),
//                        height-((mHeartDatas2[i+1]/180f)*(height-30)+30),mPaintText);
                canvas.drawLine(30+(width-30)/8f*i,height-(((mHeartDatas2[i]-40)/5f)*((height-30)/16f)+30),30+(width-30f)/8*(i+1),
                        height-(((mHeartDatas2[i+1]-40f)/5)*((height-30)/16f)+30),mPaintText);
            }
        }
    }
}