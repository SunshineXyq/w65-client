package com.bluedatax.w65.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bluedatax.w65.R;
import com.bluedatax.w65.data.UseMedicineData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xuyuanqiang on 5/29/16.
 */
public class UseMedicineAdapter extends BaseAdapter{

    private ArrayList<UseMedicineData> mData;
    private LayoutInflater mInflater;


    public UseMedicineAdapter(ArrayList<UseMedicineData> mData,LayoutInflater mInflater) {
        this.mData = mData;
        this.mInflater = mInflater;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.listview_usemedicine_item,null);
        TextView tvRemarks = (TextView) convertView.findViewById(R.id.tv_remarks);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tv_time);
        tvRemarks.setText(mData.get(position).getNote());
        tvTime.setText(mData.get(position).getTime());
        return convertView;
    }
}
