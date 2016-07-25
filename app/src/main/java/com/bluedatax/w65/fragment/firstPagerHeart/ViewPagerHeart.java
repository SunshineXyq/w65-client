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
 * Created by bdx108 on 15/11/23.
 */
public class ViewPagerHeart extends Fragment {
    private ViewPager mViewPager;
    private List<View> mViews;
    private StartViewAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.viewpager_heart,null);
        mViewPager= (ViewPager) view.findViewById(R.id.viewpager_heart);
        initViews();
        mAdapter=new StartViewAdapter(mViews);
        mViewPager.setAdapter(mAdapter);
        return view;
    }

    /**
     * 初始化三张心率的图表
     */
    private void initViews() {
        mViews=new ArrayList<View>();
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view1=inflater.inflate(R.layout.activity_heart_line_day,null);
        View view2=inflater.inflate(R.layout.activity_heart_week,null);
        View view3=inflater.inflate(R.layout.activity_heart_month,null);

        mViews.add(view1);
        mViews.add(view2);
        mViews.add(view3);
    }
}
