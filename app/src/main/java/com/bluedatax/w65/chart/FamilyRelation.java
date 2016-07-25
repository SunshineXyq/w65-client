package com.bluedatax.w65.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by bdx108 on 15/11/24.
 */
public class FamilyRelation extends View {
    private int width;
    private int height;
    private Paint mPaintCircleMiddle;
    private Paint mPaintCircleOther;
    private Paint mPaintLine;
    public FamilyRelation(Context context) {
        super(context);
    }

    public FamilyRelation(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaintCircleMiddle=new Paint();
        mPaintCircleMiddle.setColor(Color.GRAY);
        mPaintCircleMiddle.setStrokeWidth(5);
        mPaintCircleMiddle.setStyle(Paint.Style.STROKE);

        mPaintCircleOther=new Paint();
        mPaintCircleOther.setColor(Color.GREEN);
        mPaintCircleOther.setStrokeWidth(5);
        mPaintCircleOther.setStyle(Paint.Style.STROKE);

        mPaintLine=new Paint();
        mPaintLine.setColor(Color.BLACK);
        mPaintLine.setStrokeWidth(5);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width=getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height=getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(width/2,height/2,80,mPaintCircleMiddle);
        for (int i=0;i<2;i++){
            canvas.rotate(180,width/2,height/2);
            canvas.drawCircle(width/2,height/2+175,70,mPaintCircleOther);
            canvas.drawLine(width/2,height/2+85,width/2,height/2+105,mPaintLine);
        }
        for (int i=0;i<2;i++){
            canvas.rotate(60,width/2,height/2);
            canvas.drawCircle(width/2,height/2+250,70,mPaintCircleOther);
            canvas.drawLine(width/2,height/2+90,width/2,height/2+170,mPaintLine);
        }

        for (int i=0;i<2;i++){
            canvas.rotate(60*(2-i),width/2,height/2);
            canvas.drawCircle(width/2,height/2+250,70,mPaintCircleOther);
            canvas.drawLine(width/2,height/2+90,width/2,height/2+170,mPaintLine);
        }

    }
}
