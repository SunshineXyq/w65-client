package com.bluedatax.w65.fragment.UserAuthFragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.MainActivity;
import com.bluedatax.w65.R;
import com.bluedatax.w65.adapter.AuthInquireAdapter;
import com.bluedatax.w65.data.AuthInquireData;
import com.bluedatax.w65.net.InternetConnection;
import com.bluedatax.w65.utils.loadDialog.LoadingDialog;
import com.bluedatax.w65.utils.pullRefresh.XListView;
import com.bluedatax.w65.utils.sendHandler.SendHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户授权查询
 * Created by bdx108 on 15/11/28.
 */
public class AuthInquire extends Fragment implements XListView.IXListViewListener{
    private List<AuthInquireData>mDatas;
    private LayoutInflater mInflater;
    private AuthInquireAdapter mAdapter;
    private XListView mListView;
    private LoadingDialog dialog;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            responseOfInquire();
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_auth_inquire,null);
        //这个fragment中是一个listview
        mListView= (XListView) view.findViewById(R.id.listview_auth_inquire);
        mInflater=getActivity().getLayoutInflater();
        ConnectInit();
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        return view;

    }

    private void ConnectInit() {
        new SendHandler().sendHandler(BaseActivity.AUTH_INQUIRE,handler);
        dialog=new LoadingDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void initDatas() {
        mDatas=new ArrayList<AuthInquireData>();
        AuthInquireData data1=new AuthInquireData("2012-1-13","母亲（新疆）","13001150012","徐如画","2013-1-11");
        AuthInquireData data2=new AuthInquireData("2012-1-14","父亲（郑州）","13001150222","胥文琦那个","2013-1-1");
        AuthInquireData data3=new AuthInquireData("2012-1-15","爷爷（内蒙古）","13001150013","徐sdf画","2013-1-31");
        mDatas.add(data1);
        mDatas.add(data2);
        mDatas.add(data3);
    }

    @Override
    public void onRefresh() {
        mListView.stopRefresh();
    }

    @Override
    public void onLoadMore() {
        mListView.stopLoadMore();
    }
    private void responseOfInquire() {
        InternetConnection.getInstance().addRequest("https://www.baidu.com",null,new InternetConnection.OnConnectionListerner() {
            @Override
            public void isConnection(boolean connection, String type) {

            }
            @Override
            public void onSuccessConnection(String response) {

                dialog.dismiss();
                initDatas();
                mAdapter=new AuthInquireAdapter(mDatas,mInflater);
                mListView.setAdapter(mAdapter);
            }

            @Override
            public void onFailConnection(String response, int statusCode) {
                Toast.makeText(getActivity().getApplicationContext(),"网络连接失败",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }
}
