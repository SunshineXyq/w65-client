package com.bluedatax.w65.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 心率的今天的图表
 * Created by bdx108 on 15/11/23.
 */
//public class HeartDayChart extends View{
//    private int width;
//    private int height;
//    private Paint mPaintAxle;
//    private Paint mPaintRect1;
//    private Paint mPaintRect2;
//    private Paint mPaintRect3;
//    private Paint mPaintRect4;
//    private Paint[]mPaintArray;
//    public HeartDayChart(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        mPaintAxle=new Paint();
//        mPaintAxle.setColor(Color.GRAY);
//        mPaintAxle.setStrokeWidth(5);
//        mPaintArray=new Paint[4];
//        mPaintRect1=new Paint();
//        mPaintRect1.setColor(Color.RED);
//        mPaintArray[0]=mPaintRect1;
//        mPaintRect2=new Paint();
//        mPaintRect2.setColor(Color.GREEN);
//        mPaintArray[1]=mPaintRect2;
//        mPaintRect3=new Paint();
//        mPaintRect3.setColor(Color.BLACK);
//        mPaintArray[2]=mPaintRect3;
//        mPaintRect4=new Paint();
//        mPaintRect4.setColor(Color.BLUE);
//        mPaintArray[3]=mPaintRect4;
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        width=getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
//        height=getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        canvas.drawLine(40f,height-40,width,height-40,mPaintAxle);//横坐标
//        canvas.drawLine(40f,height-40,40f,40f,mPaintAxle);//纵坐标
//        float sui=50f;
//        for (int i=0;i<4;i++){
//            canvas.drawText(100*(i+1)+"",sui,height-100*(i+1)-10,mPaintAxle);
//            canvas.drawRect(sui,height-100*(i+1),sui+=30,height-40,mPaintArray[i]);
//            canvas.save();
//
//            canvas.rotate(-45,sui-20,height-40);//旋转的中心点自己来调整
//            //由于文字的末端对应着柱状图中心点，所以文字开始写的位置要从中心点剪掉文字的长度
//            canvas.drawText("cdefg",sui-20-mPaintAxle.measureText("cdefg"),height-20,mPaintAxle);
//            canvas.restore();
//        }
//    }
//}
