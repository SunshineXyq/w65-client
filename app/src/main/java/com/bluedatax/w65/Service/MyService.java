package com.bluedatax.w65.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.bluedatax.w65.utils.android_websockets.WebSocketClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class MyService extends Service {

    public OnConnectListener onConnectListener;
    public static WebSocketClient client;
    private final String ACTION_NAME = "发送广播";
    private Intent intent = new Intent(ACTION_NAME);
    private String duid;

    /**
     * 注册回调接口的方法，供外部调用
     * @param
     */
    public void setOnConnectListener(OnConnectListener onConnectListener) {
        this.onConnectListener = onConnectListener;
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        duid = intent.getStringExtra("duid");
        startConnect();
        return super.onStartCommand(intent, flags, startId);
    }
    public void startConnect(){
        List<BasicNameValuePair> extraHeaders = Arrays.asList(new BasicNameValuePair("Cookie", "session=abcd"));

        client = new WebSocketClient(URI.create(String.format("ws://192.168.0.9:19000?connect&prod=w65" +
                "&auid=89b5129f7d5f447562b632d724e4c7a0&duid=%s&apc=2",duid)), new WebSocketClient.Listener() {
            @Override
            public void onConnect() {
                Log.d("onConnect", "Connected!");
                String notice = "Connected";
                Log.d("判断连接状态", notice);
                if(onConnectListener != null ) {
                    Log.d("发送的通知", notice);
                    onConnectListener.onConnected(notice);
                }
            }
            @Override
            public void onMessage(String message) {
                Log.d("onMessage", String.format("Got string message! %s", message));
                try {
                    Log.d("*******", "1234456");
                    JSONObject parseJson = new JSONObject(message);
                    System.out.println( "---------------" + parseJson.getInt("msg"));
                    if (onConnectListener != null && parseJson.getInt("msg") == 10) {          //初始化
                        Log.d("发送给UI的json数据:", parseJson + "");
                        onConnectListener.onJSonObject(parseJson);
                    } else if (onConnectListener != null && parseJson.getInt("msg") == 11) {   //心跳
                        onConnectListener.onHeartbeat(parseJson);
                    } else if (onConnectListener != null && parseJson.getInt("msg") == 110) {  //登录接口
                        onConnectListener.onJSonObject(parseJson);
                    } else if (onConnectListener != null && parseJson.getInt("msg") == 112) {  //主动查询设备event
                        intent.putExtra("accidentJson",message);
                        Log.d("发送广播设备事件通知", message);
                        sendBroadcast(intent);
                    } else if (parseJson.getInt("msg") == 200 ) {                              //设备event消息推送
                        intent.putExtra("noticeJson",message);
                        Log.d("推送设备事件通知", message);
                        sendBroadcast(intent);
                    } else if (onConnectListener != null && parseJson.getInt("msg") == 113) {  //推送语音文件
                        onConnectListener.onRingJSONObject(parseJson);
                    } else if (onConnectListener != null && parseJson.getInt("msg") == 114) {
                        onConnectListener.onFamilyNumber(parseJson);
                    }
                }catch (Exception e){
                }
            }
            @Override
            public void onMessage(byte[] data) {
                Log.d("onMessage", String.format("Got binary message! %s", new String(data)));

            }
            @Override
            public void onDisconnect(int code, String reason) {
                Log.d("onDisconnect", String.format("Disconnected! Code: %d Reason: %s", code, reason));
                if (reason != null) {
                    if (onConnectListener != null) {
                        onConnectListener.onDisconnect(reason);
                    }
                }
            }
            @Override
            public void onError(Exception error) {
                Log.e("onError", "Error!", error);
                if (error.getMessage().equals("Socket closed")) {
                    System.out.println("退出成功");
                } else if (onConnectListener != null) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "网络连接错误，正在尝试重新连接!", Toast.LENGTH_LONG).show();
                        }
                    });
                    onConnectListener.onError(error);
                }
            }
        }, extraHeaders);

        client.connect();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        return new MyBinder();
    }
    public class MyBinder extends Binder {
        /**
         * 获取当前Service的实例
         *
         * @return
         */
        public MyService getService() {

            return MyService.this;
        }
    }
}