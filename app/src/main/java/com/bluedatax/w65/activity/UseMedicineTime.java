package com.bluedatax.w65.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.R;

public class UseMedicineTime extends BaseActivity {

    private TextView textViewMiddle;
    private EditText remark;
    private EditText time;
    private Button btIncreate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_use_medicine_time);
        remark = (EditText) findViewById(R.id.et_remark);
        time = (EditText) findViewById(R.id.et_time);
        btIncreate = (Button) findViewById(R.id.confirm_increate);
        btIncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remarkMes = remark.getText().toString().trim();
                String timeMes = time.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra("remark", remarkMes);
                intent.putExtra("time",timeMes);
                System.out.println(remarkMes+timeMes+"获取的备注与时间");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
