package com.bluedatax.w65.chart;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bluedatax.w65.R;

/**
 * Created by bdx108 on 15/12/18.
 */
public class TitleLayout extends LinearLayout {
    public TitleLayout(Context context) {
        super(context);
    }

    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.activity_title,this);
        ImageView imageViewBack= (ImageView) findViewById(R.id.imageview_title_back);
        imageViewBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)getContext()).finish();
            }
        });
    }
}
