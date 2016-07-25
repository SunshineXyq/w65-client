package com.bluedatax.w65.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluedatax.w65.R;
import com.bluedatax.w65.data.HealthInfoData;

import java.util.List;

/**
 * Created by bdx108 on 15/11/24.
 */
public class HealthAdapter extends BaseAdapter {
    private List<HealthInfoData>mDatas;
    private LayoutInflater mInflater;

    public HealthAdapter(List<HealthInfoData> mDatas, LayoutInflater mInflater) {
        this.mDatas = mDatas;
        this.mInflater = mInflater;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HealthInfoData data=mDatas.get(position);
        ViewHolder viewHolder;
        if (convertView==null){
            convertView=mInflater.inflate(R.layout.listview_health_info,null);
            viewHolder=new ViewHolder();
            viewHolder.textViewDate= (TextView) convertView.findViewById(R.id.textview_date);
            viewHolder.imageViewSecurity=(ImageView) convertView.findViewById(R.id.imageview_security);
            viewHolder.textViewSecurity= (TextView) convertView.findViewById(R.id.textview_issecurity);
            viewHolder.TextViewSecurityResult= (TextView) convertView.findViewById(R.id.textview_security_result);
            viewHolder.imageViewHeart=(ImageView) convertView.findViewById(R.id.imageview_heart);
            viewHolder.textViewHeartNum= (TextView) convertView.findViewById(R.id.textview_heart_num);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.textViewDate.setText(data.getDate());
        viewHolder.imageViewSecurity.setImageResource(R.mipmap.people);
        viewHolder.textViewSecurity.setText(data.getTextIsSecurity());
        viewHolder.TextViewSecurityResult.setText(data.getTextSecurityResult());
        viewHolder.imageViewHeart.setImageResource(R.mipmap.heart);
        viewHolder.textViewHeartNum.setText(data.getTextHeartNum()+"次");
        return convertView;
    }
    class ViewHolder{
        TextView textViewDate;
        ImageView imageViewIcon;
        ImageView imageViewSecurity;
        TextView textViewSecurity;
        TextView TextViewSecurityResult;
        ImageView imageViewHeart;
        TextView textViewHeartNum;
    }
}
