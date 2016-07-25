package com.bluedatax.w65.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bluedatax.w65.R;
import com.bluedatax.w65.data.AuthInquireData;

import java.util.List;

/**
 * 用户授权页面的listview的adapter
 * Created by bdx108 on 15/11/28.
 */
public class AuthInquireAdapter extends BaseAdapter {
    private List<AuthInquireData>mDatas;
    private LayoutInflater mInflater;

    public AuthInquireAdapter(List<AuthInquireData> mDatas, LayoutInflater mInflater) {
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
        AuthInquireData data=mDatas.get(position);
        ViewHolder viewHolder;
        if (convertView==null){
            convertView=mInflater.inflate(R.layout.listview_auth_inquire,null);
            viewHolder=new ViewHolder();
            viewHolder.textViewDate= (TextView) convertView.findViewById(R.id.text_auth_inquire_date);
            viewHolder.textViewEquipment= (TextView) convertView.findViewById(R.id.text_auth_inquire_equipment);
            viewHolder.textViewPhoneNum= (TextView) convertView.findViewById(R.id.text_auth_inquire_phonenum);
            viewHolder.textViewUserName= (TextView) convertView.findViewById(R.id.text_auth_inquire_username);
            viewHolder.textViewLastDate= (TextView) convertView.findViewById(R.id.text_auth_inquire_lastdate);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.textViewDate.setText(data.getDate());
        viewHolder.textViewEquipment.setText(data.getEquipment());
        viewHolder.textViewPhoneNum.setText(data.getPhoneNum());
        viewHolder.textViewUserName.setText(data.getUserName());
        viewHolder.textViewLastDate.setText(data.getLastDate());
        return convertView;
    }
    class ViewHolder{
        TextView textViewDate;//授权用户最后登录时间
        TextView textViewEquipment;//授权设备
        TextView textViewPhoneNum;//用户手机号
        TextView textViewUserName;//用户名称
        TextView textViewLastDate;//用户最后登录日期
    }
}
