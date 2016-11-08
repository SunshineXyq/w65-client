package com.bluedatax.w65.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bluedatax.w65.R;
import com.bluedatax.w65.adapter.UseMedicineAdapter;
import com.bluedatax.w65.data.UseMedicineData;

import java.util.ArrayList;
import java.util.HashMap;

public class UseMedicineMessage extends Activity {

    private ListView useMedicineMes;
    private LayoutInflater mInflater;
    private Button addUseMedicine;
    private ArrayList<UseMedicineData> listRemind;
    private UseMedicineAdapter useMedicineAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_use_medicine_message);
        useMedicineMes = (ListView) findViewById(R.id.lv_use_medicine);
        addUseMedicine = (Button) findViewById(R.id.add_use_medicine);
        ImageView imageview_title_back = (ImageView) findViewById(R.id.imageview_title_back);
        imageview_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mInflater = getLayoutInflater();
        listRemind = new ArrayList<UseMedicineData>();
        useMedicineAdapter = new UseMedicineAdapter(listRemind,mInflater);
        useMedicineMes.setAdapter(useMedicineAdapter);
        useMedicineMes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
       addUseMedicine.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(UseMedicineMessage.this, UseMedicineTime.class);
               startActivityForResult(intent, 1);
           }
       });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

       switch (requestCode) {
           case 1:
               if (resultCode == RESULT_OK) {
                   String remark = data.getStringExtra("remark");
                   String time = data.getStringExtra("time");
                   System.out.println("返回到这个activity的数据" + remark + time);
                   listRemind.add(new UseMedicineData(remark,time));
                   useMedicineAdapter.notifyDataSetChanged();
               }
               break;
           default:
       }
    }
}
