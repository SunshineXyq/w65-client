package com.bluedatax.w65.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 最初进入应用的三张图片的adapter
 * Created by bdx108 on 15/11/21.
 */
public class StartViewAdapter extends PagerAdapter {

    private List<View> mViews;

    public StartViewAdapter(List<View> mViews) {
        this.mViews = mViews;
    }

    @Override
    public int getCount() {
        return mViews.size();//viewpager中所有的图片的数量
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view==o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //当某个页面被划进屏幕时，显示该页面
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        //当某个页面被划出屏幕时，销毁
        View view=mViews.get(position);
        container.removeView(view);
    }
}