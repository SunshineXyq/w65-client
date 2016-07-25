package com.bluedatax.w65.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.bluedatax.w65.R;

/**
 * Created by xuyuanqiang on 16/3/23.
 */
public class SaveFamilyNumber extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_save_famiy_number);
    }
}
