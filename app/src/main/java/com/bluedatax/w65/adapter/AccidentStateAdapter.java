package com.bluedatax.w65.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluedatax.w65.R;
import com.bluedatax.w65.data.AccidentStateData;
import com.bluedatax.w65.utils.sign.Sign;

import java.util.HashMap;
import java.util.List;

/**
 * 事故状态中的listview的adapter
 * Created by bdx108 on 15/12/7.
 */
public class AccidentStateAdapter extends BaseAdapter {
    private List<AccidentStateData>mDatas;
    private LayoutInflater mInflater;
    private Context mContext;
    private ViewHolder viewHolder=null;
    private HashMap<Integer, Boolean> isCanRenew = new HashMap<Integer, Boolean>();

    public AccidentStateAdapter(List<AccidentStateData> mDatas, LayoutInflater mInflater,Context context) {
        this.mDatas = mDatas;
        this.mInflater = mInflater;
        this.mContext=context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        AccidentStateData data=mDatas.get(position);

        if (convertView==null){
            convertView=mInflater.inflate(R.layout.listview_accidentstate,null);
            viewHolder=new ViewHolder();
            viewHolder.imageView= (ImageView) convertView.findViewById(R.id.imageview_list_accident);
            viewHolder.textViewDate= (TextView) convertView.findViewById(R.id.textview_list_accidentdata);
            viewHolder.textViewInfo= (TextView) convertView.findViewById(R.id.textview_list_accidentinfo);
            viewHolder.buttonSign= (Button) convertView.findViewById(R.id.button_sign);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.setImageResource(data.getDotStyle());
        viewHolder.textViewDate.setText(data.getDate());
        viewHolder.textViewInfo.setText(data.getInfo());
        viewHolder.buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mContext,Sign.class);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        ImageView imageView;//前边的红色点和绿色点
        TextView textViewDate;//显示时间的tv
        TextView textViewInfo;//显示具体信息的tv
        Button buttonSign;//签名的按钮
    }
}
