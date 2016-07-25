package com.bluedatax.w65.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.VideoView;

import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.R;
import com.bluedatax.w65.adapter.AccidentStateAdapter;
import com.bluedatax.w65.data.AccidentStateData;
import com.bluedatax.w65.utils.map.BdMapApplication;
import com.bluedatax.w65.utils.pullRefresh.XListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 事故状态
 *
 * Created by bdx108 on 15/12/7.
 */
public class AccidentState extends BaseActivity implements View.OnClickListener,XListView.IXListViewListener{
    private XListView mListView;//事故状态listview
    private List<AccidentStateData>mDatas;//listview上要显示的数据
    private LayoutInflater mInflater;//用来加载布局
    private AccidentStateAdapter mAdapter;//事故状态的适配器
    private CircleImageView mCircleImageAccidentState;
    private VideoView mVideoView;//视频播放
    private RadioButton mButtonPlayVideo;//点击播放视频
    private TextView stateStartTime;
    private TextView stateHappenTime;
    private ArrayList accidentName;
    private ArrayList accidentTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accident_state);
        String startTime = getIntent().getStringExtra("accident_start_time");
        String happenTime = getIntent().getStringExtra("accident_happen_time");
        accidentName = getIntent().getStringArrayListExtra("name");

        Iterator its = accidentName.iterator();
        while (its.hasNext()) {
            Object obj = its.next();
            Log.d("******name", obj + "");
        }
        accidentTime = getIntent().getStringArrayListExtra("time");
        Iterator it = accidentTime.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            Log.d("******time", obj + "");
        }
        stateStartTime = (TextView) findViewById(R.id.state_start_time);
        stateHappenTime = (TextView) findViewById(R.id.state_happen_time);
        stateStartTime.setText(startTime);
        stateHappenTime.setText(happenTime);
        mButtonPlayVideo= (RadioButton) findViewById(R.id.radiobutton_playvideo);
        mButtonPlayVideo.setOnClickListener(this);
        mVideoView= (VideoView) findViewById(R.id.videoview);
        mCircleImageAccidentState= (CircleImageView) findViewById(R.id.circleimage_accident_state);
        mCircleImageAccidentState.setOnClickListener(this);
        mListView= (XListView) findViewById(R.id.listView_accident);
        mInflater=getLayoutInflater();
        initDatas();
        mAdapter=new AccidentStateAdapter(mDatas,mInflater,this);
        mListView.setAdapter(mAdapter);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        mDatas=new ArrayList<AccidentStateData>();
        for (int i = 0; i <accidentTime.size() ; i++) {
            mDatas.add(new AccidentStateData(R.drawable.accident_circle, accidentTime.get(i)+"", accidentName.get(i)+""));
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.circleimage_accident_state:
                Intent intent=new Intent(AccidentState.this, BdMapApplication.class);
                startActivity(intent);
                break;
            case R.id.radiobutton_playvideo:
                String path=Environment.getExternalStorageDirectory()+"/DCIM/Camera/video_20150803_164216.mp4";
                mVideoView.setVisibility(View.VISIBLE);
                //找到视频地址，还可以通过setVideoUri来找到
                mVideoView.setVideoPath(path);
                mVideoView.setMediaController(new MediaController(AccidentState.this));
                mVideoView.start();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        mListView.stopRefresh();
    }

    @Override
    public void onLoadMore() {
        mListView.stopLoadMore();
    }
}
