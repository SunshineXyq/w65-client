package com.bluedatax.w65.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bluedatax.w65.R;
import com.bluedatax.w65.activity.LeaveMessage;
import com.bluedatax.w65.data.LeaveMessageData;

import java.util.List;

/**
 * 电话留言的listview的adapter
 * Created by bdx108 on 15/11/27.
 */
public class LeaveMessageAdapter extends BaseAdapter {
    private List<LeaveMessageData>mDatas;
    private LayoutInflater mInflater;

    public LeaveMessageAdapter(List<LeaveMessageData> mDatas, LayoutInflater mInflater) {
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
        LeaveMessageData data=mDatas.get(position);
        ViewHolder viewHolder;
        if (convertView==null){
            convertView=mInflater.inflate(R.layout.listview_leave_message,null);
            viewHolder=new ViewHolder();
            viewHolder.textViewStartTime = (TextView) convertView.findViewById(R.id.textview_leavemessage_stime);
            viewHolder.textViewTime= (TextView) convertView.findViewById(R.id.textview_leavemessage_time);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.textViewTime.setText(data.getTime());
        viewHolder.textViewStartTime.setText(data.getStartTime());
        return convertView;
    }
    class ViewHolder{

        TextView textViewTime;
        TextView textViewStartTime;
    }
}
