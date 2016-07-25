package com.bluedatax.w65.utils.sendHandler;

import android.os.Handler;
import android.os.Message;

/**
 * Created by bdx108 on 15/12/30.
 */
public class SendHandler {
    public void sendHandler(int what,Handler handler) {
        Message message = handler.obtainMessage();
        message.what = what;
        handler.sendMessage(message);
    }
}
