package com.bluedatax.w65.chart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bdx108 on 15/12/10.
 */
public class SignView extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder mSurfaceHolder = null;
    private Paint mPaint;
    //当前所选画笔的形状
    private MyPath myPath = null;
    //记录画笔的列表
    private List<MyPath> myPaths;
    //默认画笔为黑色
    private int currentColor = Color.BLACK;
    //画笔的粗细
    private int currentSize = 5;
    private Bitmap bmp;
    public SignView(Context context) {
        super(context);
        init();
    }

    public SignView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = mSurfaceHolder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        mSurfaceHolder.unlockCanvasAndPost(canvas);
        myPaths = new ArrayList<MyPath>();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    private void init() {
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        this.setFocusable(true);

        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(6);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_CANCEL) {
            return false;
        }

        float touchX = event.getRawX();
        float touchY = event.getRawY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setCurAction(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                Canvas canvas = mSurfaceHolder.lockCanvas();
                canvas.drawColor(Color.WHITE);
                for (MyPath a : myPaths) {
                    a.draw(canvas);
                }
                myPath.move(touchX, touchY);
                myPath.draw(canvas);
                mSurfaceHolder.unlockCanvasAndPost(canvas);
                break;
            case MotionEvent.ACTION_UP:
                myPaths.add(myPath);
                myPath = null;
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }
    // 得到当前画笔的类型，并进行实例
    public void setCurAction(float x, float y) {
        myPath = new MyPath(x, y, currentSize, currentColor);
    }
    public boolean back(){
        myPaths.clear();
        Canvas canvas = mSurfaceHolder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        mSurfaceHolder.unlockCanvasAndPost(canvas);
        return true;
    }
    /**
     * 获取画布的截图
     * @return
     */
    public Bitmap getBitmap() {
        bmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        draw(canvas);
        return bmp;
    }
//    public void doDraw(Canvas canvas) {
//        canvas.drawColor(Color.TRANSPARENT);
//        for (Action a : mActions) {
//            a.draw(canvas);
//        }
//        canvas.drawBitmap(bmp, 0, 0, mPaint);
//    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        for (MyPath a : myPaths) {
            a.draw(canvas);
        }
        canvas.drawBitmap(bmp, 0, 0, mPaint);
    }
}
// 自由曲线
class MyPath {
    Path path;
    int size;
    Paint paint;

//    MyPath() {
//        path = new Path();
//
//        size = 1;
//    }

    MyPath(float x, float y, int size, int color) {
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(size);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        this.size = size;
        path.moveTo(x, y);
        path.lineTo(x, y);
    }

    public void draw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    public void move(float mx, float my) {
        path.lineTo(mx, my);
    }
}
