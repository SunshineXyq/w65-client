package com.bluedatax.w65.fragment.firstPagerHeart;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluedatax.w65.R;
import com.bluedatax.w65.adapter.StartViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 行走距离的fragment
 * Created by bdx108 on 15/11/23.
 */
public class ViewPagerDistance extends Fragment {
    private ViewPager mViewPager;
    private List<View> mViews;
    private StartViewAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.viewpager_distance,null);
        //使用viewpager使得显示距离的三张图片可以左右滑动显示
        mViewPager= (ViewPager) view.findViewById(R.id.viewpager_distance);
        initViews();
        mAdapter=new StartViewAdapter(mViews);//viewpager的适配器
        mViewPager.setAdapter(mAdapter);
        return view;
    }

    /**
     * 初始化viewpager，加入三张图表的图片
     */
    private void initViews() {
        mViews=new ArrayList<View>();
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view1=inflater.inflate(R.layout.activity_distance_day,null);
        View view2=inflater.inflate(R.layout.activity_distance_week,null);
        View view3=inflater.inflate(R.layout.activity_distance_month,null);
//将三张图标加入到集合中
        mViews.add(view1);
        mViews.add(view2);
        mViews.add(view3);
    }
}
