package com.bluedatax.timealgorithm;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate!!!!!!!!!!!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestory!!!!!!!!!!");
    }
}
