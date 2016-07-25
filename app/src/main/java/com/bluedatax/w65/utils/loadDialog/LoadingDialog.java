package com.bluedatax.w65.utils.loadDialog;

import android.app.Dialog;
import android.content.Context;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.bluedatax.w65.R;

/**
 * Created by bdx108 on 12/14/15.
 */
public class LoadingDialog extends Dialog {

    private TextView mTextLoadDialog;
    public LoadingDialog(Context context) {
        super(context, R.style.loadingDialogStyle);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
        mTextLoadDialog = (TextView)findViewById(R.id.tv);
        mTextLoadDialog.setText("正在加载...");
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch(event.getKeyCode()){
            case KeyEvent.KEYCODE_BACK:{
                return false;
            }
        }
        return true;
    }
}
