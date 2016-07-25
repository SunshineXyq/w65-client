package com.bluedatax.w65;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.bluedatax.w65.net.InternetConnection;

/**
 * Created by bdx108 on 15/12/1.
 */
public class ActivityTest extends Activity {
    private Button mButtonTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
//        mButtonTest= (Button) findViewById(R.id.button_test);
//        mButtonTest.setBackgroundResource(R.mipmap.ic_launcher);
        InternetConnection.getInstance().addRequest("https://www.baidu.com",null,new InternetConnection.OnConnectionListerner() {
            @Override
            public void isConnection(boolean connection, String type) {

            }

            @Override
            public void onSuccessConnection(String response) {

            }

            @Override
            public void onFailConnection(String response, int statusCode) {

            }
        });
    }
}
