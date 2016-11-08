package com.bluedatax.w65.activity;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import android.view.WindowManager;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.R;
import com.bluedatax.w65.Service.MyService;
import com.bluedatax.w65.Service.OnConnectListener;
import com.bluedatax.w65.adapter.LeaveMessageAdapter;
import com.bluedatax.w65.data.LeaveMessageData;
import com.bluedatax.w65.fragment.FirstPager;
import com.bluedatax.w65.litepal.Record;
import com.bluedatax.w65.utils.SharedPrefsUtil;
import com.bluedatax.w65.utils.bitmapTransparency.TransparentBitmap;
import com.bluedatax.w65.utils.downloadimage.HttpUtils;
import com.bluedatax.w65.utils.getTime;
import com.bluedatax.w65.utils.pullRefresh.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 电话留言
 * Created by bdx108 on 15/11/27.
 */
public class LeaveMessage extends BaseActivity implements View.OnTouchListener, XListView.IXListViewListener, View.OnClickListener {
    private TextView mTextViewMiddle;
    private TextView mTextViewRight;
    private TextView tvLeavemessagePerson;
    private XListView mListViewLeaveMessage;
    private Button mButtonMic;
    private LeaveMessageAdapter mAdapter;
    private LayoutInflater mInflater;
    private List<LeaveMessageData> mDatas;//存放listview数据的集合
    private int time = 0;//录音时间
    private Boolean isRecoding;
    private Dialog dialog;//用来显示录音时间
    private TextView mTextViewRecordTime;
    //语音操作对象
    private MediaPlayer mPlayer;
    private MediaRecorder mRecorder = null;
    //语音文件保存路径

    private List<String> allFiles;//存放录音文件名字的集合
    private File mRecAudioFile;        // 录制的音频文件
    private File mRecAudioPath;        // 录制的音频文件路徑
    private String strTempFile = "audio";//  临时文件的前缀

    //定义显示动画的ImageView
    private ImageView mImageViewShow;
    //定义逐帧动画
    private AnimationDrawable animationDrawable;
    private String path;
    private File pathRecord;
    private File files;
    public static String[] listFile = null;
    private Record record;
    private String startRecordTime;
    private File saveFilePath;
    private List<Record> recordList;
    private Timer mTimer;
    private Button deleteLeaveMessage;
    private int width;
    private int height;
    private View contentView;
    private PopupWindow mPopupWindow;
    private int mScreenHeight;
    private int pos;
    private ImageView lmImageOne;
    private ImageView lmImageTwo;
    private String pathOne;
    private String pathTwo;
    private int lmSign = 1;
    private String gdid_1 = "1-1001";
    private String gdid_2 = "2-1001";
    private String gdid_1_name = "李思奶奶";
    private String gdid_2_name = "张三大爷";
    private MyService myService;
    private String fileId;
    private String appfileId;
    private String auidId;
    private String authName;
    private boolean _isExe = false;
    private String latitude;
    private String lnglatitude;
    private String currentTime;
    private String fub;
    private String auid;


    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = ((MyService.MyBinder)service).getService();
            myService.setOnConnectListener(new OnConnectListener() {
                @Override
                public void onJSonObject(JSONObject json) {
                }

                @Override
                public void onError(Exception msg) {

                }

                @Override
                public void onConnected(String notice) {

                }

                @Override
                public void onRingJSONObject(JSONObject ringJSON) {
                    try {
                        JSONArray jsonArray = ringJSON.getJSONObject("body").getJSONArray("files");
                        fileId = jsonArray.getJSONObject(0).getString("file_id");
                        appfileId = jsonArray.getJSONObject(0).getString("app_file_id");
                        auidId = ringJSON.getJSONObject("body").getString("auth_id");
                        authName = ringJSON.getJSONObject("body").getString("auth_name");
                        System.out.println(fileId + "\n" + auidId + "\n" + authName + appfileId + "\n");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (!_isExe) {
                        UploadRecordTask task = new UploadRecordTask();
                        task.execute(String.format("%s?prod=w65&id=1&fttype=ul_files&auth_user=w65_wdas&" +
                                "file_id=%s&fname=&auth_id=%s&auth_name=%s&type=json&bdx=prod", fub,fileId, auid,authName));
                    }
                    _isExe = true;
                }

                @Override
                public void onFamilyNumber(JSONObject familyJSON) {

                }

                @Override
                public void onDisconnect(String message) {

                }

                @Override
                public void onHeartbeat(JSONObject heartbeatJSON) {

                }
            });
        }
    };
    public static Record databaseLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leavemessage);
        initResources();
        initPopupWindow();
        Intent intent = new Intent(LeaveMessage.this,MyService.class);
//        bindService(intent,conn,Context.BIND_AUTO_CREATE);
        SharedPreferences sharedPreferences = getSharedPreferences("count", Context.MODE_PRIVATE);
        latitude = sharedPreferences.getString("lat", "");
        lnglatitude = sharedPreferences.getString("lng", "");
        fub = SharedPrefsUtil.getValue(this, "fub", "");
        auid = SharedPrefsUtil.getValue(this,"auid","");
        currentTime = getTime.getCurrentTime();
        deleteLeaveMessage = (Button) contentView.findViewById(R.id.delete_record);
        tvLeavemessagePerson = (TextView) findViewById(R.id.tv_leavemessage_person);
        deleteLeaveMessage.setOnClickListener(this);
        mDatas = new ArrayList<LeaveMessageData>();
        mInflater = getLayoutInflater();
        mAdapter = new LeaveMessageAdapter(mDatas, mInflater);
        mListViewLeaveMessage.setAdapter(mAdapter);
        mListViewLeaveMessage.setPullLoadEnable(true);
        mListViewLeaveMessage.setXListViewListener(this);
        mPlayer = new MediaPlayer();
//        Map map_1 = (Map) FirstPager.list.get(0);
//        Map map_2 = (Map)FirstPager.list.get(1);
//        gdid_1 = (String)map_1.get("gdid");
//        gdid_2 = (String)map_2.get("gdid");
//        gdid_1_name = (String) map_1.get("name");
//        gdid_2_name = (String) map_2.get("name");
        tvLeavemessagePerson.setText(gdid_1_name + "(设备ID:" + gdid_1 + ")");
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/w65/recorder/";
        files = new File(path);
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
            listFile = files.list();
        }

        final List<Record> roleOne = DataSupport.where("RecordRole == ?", "roleOne").find(Record.class);
        if (roleOne.size() > 0) {
            System.out.println("初始化查询");
            for (int i = 0; i < roleOne.size(); i++) {
                mDatas.add(new LeaveMessageData("recorder" + roleOne.get(i).getDuration(),
                        roleOne.get(i).getDuration(), roleOne.get(i).getStartTime()));//向listview中添加数据
                mAdapter.notifyDataSetChanged();
            }
        }

        /**
         * 播放设备电话留言
         */
        mListViewLeaveMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lmSign == 1) {
                    System.out.println("您播放的电话留言的设备是:" + lmSign);
                    try {
                        mPlayer.reset();
                        List<Record> ItemPath = DataSupport.where("recordRole == ?","roleOne").find(Record.class);
                        System.out.println(position);
                        System.out.println(id);
                        mPlayer.setDataSource(ItemPath.get(position - 1).getRecordPath());
                        System.out.println(ItemPath.get(position - 1).getRecordPath());
                        if (!mPlayer.isPlaying()) {
                            mPlayer.prepare();
                            mPlayer.start();
                        } else {
                            mPlayer.pause();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (lmSign == 2) {
                    System.out.println("您播放的电话留言的设备是:" + lmSign);
                    try {
                        mPlayer.reset();
                        List<Record> ItemPath = DataSupport.where("recordRole == ?","roleTwo").find(Record.class);
                        System.out.println(position);
                        System.out.println(id);
                        mPlayer.setDataSource(ItemPath.get(position - 1).getRecordPath());
                        System.out.println(ItemPath.get(position - 1).getRecordPath());
                        if (!mPlayer.isPlaying()) {
                            mPlayer.prepare();
                            mPlayer.start();
                        } else {
                            mPlayer.pause();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mListViewLeaveMessage.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("你长按了第" + position);
                pos = position;
                mPopupWindow.showAtLocation(mListViewLeaveMessage, Gravity.BOTTOM
                        | Gravity.CENTER, 0, mScreenHeight - height / 2);
                return false;
            }
        });
    }

    private void initPopupWindow() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        LayoutInflater lf = (LayoutInflater) LeaveMessage.this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        contentView = lf.inflate(R.layout.delete_record_item, null);
        mPopupWindow = new PopupWindow(contentView, ActionMenuView.LayoutParams.WRAP_CONTENT,
                ActionMenuView.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mScreenHeight = getScreenHeight();
    }

    private int getScreenHeight() {
        // 获取屏幕实际像素
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = LeaveMessage.this.getWindowManager().getDefaultDisplay();
        display.getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * 界面控件的初始化
     */
    private void initResources() {
        mButtonMic = (Button) findViewById(R.id.imageview_mic);
        lmImageOne = (ImageView) findViewById(R.id.lm_image_one);
        lmImageTwo = (ImageView) findViewById(R.id.lm_image_two);
        lmImageOne.setOnClickListener(this);
        lmImageTwo.setOnClickListener(this);
        mButtonMic.setOnTouchListener(this);
        mTextViewMiddle = (TextView) findViewById(R.id.textview_title_middle);
        mTextViewMiddle.setText("电话留言");
        mTextViewRight = (TextView) findViewById(R.id.textview_title_right);
        mListViewLeaveMessage = (XListView) findViewById(R.id.listview_leavemessage);
        pathOne = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage1.jpg";
        pathTwo = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage2.jpg";
        Bitmap birthdayTwo = TransparentBitmap.getTransparentBitmap(FamilyNumber.getLocalImage(pathTwo), 50);
        if (pathOne != null) {
            lmImageOne.setImageBitmap(FamilyNumber.getLocalImage(pathOne));
        }
        if (pathTwo != null) {
            lmImageTwo.setImageBitmap(birthdayTwo);
        }

    }

    /**
     * 话筒按钮的点击事件，按下时进行录音，松开则停止录音，并把录音保存到本地
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startRecordTime = getTime.getCurrentTime();
            time = 0;
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);           //声音来源麦克风
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);   //输出格式3gp
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);     //编码格式
            try {
                String paths = path                                            //文件存储目录
                        + new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())  //文件命名格式
                        + ".amr";
                saveFilePath = new File(paths);
                mRecorder.setOutputFile(saveFilePath.getAbsolutePath());

//                    mRecAudioFile = File.createTempFile(String.format("audio%d",i++), ".amr", file);//创建临时文件
//                    mRecorder.setOutputFile(mRecAudioFile.getAbsolutePath());
//                    System.out.println(mRecAudioFile.getAbsolutePath());
                mRecorder.prepare();
                mRecorder.start();

            } catch (IOException e) {
                Log.d("00000000", "prepare() failed");
                System.out.println(e);
            }
            isRecoding = true;
            showRecorderDialog();
            timedTask();
            animationDrawable.start();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            mTimer.cancel();
            animationDrawable.stop();
            dialog.dismiss();
            mRecorder.stop();//停止录音
            //将录音时间，录音时长，文件路径存储到数据库
            if (lmSign == 1) {
                System.out.println("保存设备一电话留言");
                int id = saveRecord("roleOne");
                pushTelephoneMessage(id);
            } else if (lmSign == 2) {
                System.out.println("保存设备二电话留言");
                saveRecord("roleTwo");
            }
            mRecorder.release();//释放资源
            mRecorder = null;
        }
        return false;
    }

    /**
     * 请求推送电话留言，获取fileId
     * @param id  本地文件的id
     */
    private void pushTelephoneMessage(int id) {
        try {
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lat", latitude);
            jsonGeo.put("lng", lnglatitude);
            Log.d("json经纬度", jsonGeo.toString());
            JSONObject jsonObject = new JSONObject(jsonGeo.toString());

            JSONObject jsReq = new JSONObject();
            jsReq.put("stype","voice_msg");
            jsReq.put("action","push");
            jsReq.put("title","");
            jsReq.put("file_id","");
            jsReq.put("app_file_id",id);
            jsReq.put("dt_s","");
            jsReq.put("dt_e","");
            JSONObject req = new JSONObject(jsReq.toString());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("gdid","1-1001");
            jsonBody.put("geo", jsonObject);
            jsonBody.put("req",req);
            jsReq.put("dt",currentTime);

            JSONObject jsonReq = new JSONObject();
            jsonReq.put("msg", 113);
            jsonReq.put("token", Login.token);
            jsonReq.put("body",jsonBody);

            MyService.client.send(jsonReq.toString());

        } catch (Exception e) {

        }
    }

    class UploadRecordTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            System.out.println("[downloadImageTask->]doInBackground " + params[0]);
            String response = HttpUtils.uploadRing(params[0],"telephoneMessage");
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            System.out.println("result = " + result);
            super.onPostExecute(result);
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }
    }



    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x09:
                    mTextViewRecordTime.setText("正在录音..." + time);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 计算录音时的时间
     */

    private void timedTask() {
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                Message message = handler.obtainMessage();
                message.obj = time;
                message.what = 0x09;
                handler.sendMessage(message);
                time++;
            }
        }, 0, 1000);
    }


    @Override
    public void onRefresh() {
        mListViewLeaveMessage.stopRefresh();
    }

    @Override
    public void onLoadMore() {
        mListViewLeaveMessage.stopLoadMore();
    }


    /**
     * 录音的时候弹出一个diglog来显示录音的时间
     */
    private void showRecorderDialog() {
        dialog = new Dialog(this, R.style.callserviceDialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View dialogSelf = inflater.inflate(R.layout.recorder_time, null);
        mTextViewRecordTime = (TextView) dialogSelf.findViewById(R.id.textview_recordertime);
        mImageViewShow = (ImageView) dialogSelf.findViewById(R.id.imageview_animation);
        animationDrawable = (AnimationDrawable) mImageViewShow.getBackground();
        dialog.setContentView(dialogSelf);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_record:
                if (lmSign == 1) {
                    List<Record> roleOne = DataSupport.where("RecordRole = ?", "roleOne").find(Record.class);
                    DataSupport.deleteAll(Record.class, "recordPath == ?", roleOne.get(pos - 1).getRecordPath());
                    mDatas.clear();
                    queryLeaveMessageOne();
                } else if (lmSign == 2) {
                    List<Record> roleTwo = DataSupport.where("RecordRole = ?", "roleTwo").find(Record.class);
                    DataSupport.deleteAll(Record.class, "recordPath == ?", roleTwo.get(pos - 1).getRecordPath());
                    mDatas.clear();
                    queryLeaveMessageTwo();
                }
                mPopupWindow.dismiss();
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.lm_image_one:
                tvLeavemessagePerson.setText(gdid_1_name + "(设备ID:" + gdid_1 + ")");
                lmSign = 1;
                mDatas.clear();
                queryLeaveMessageOne();
                mAdapter.notifyDataSetChanged();
                Bitmap birthday_one = TransparentBitmap.getTransparentBitmap(FamilyNumber.getLocalImage(pathOne), 100);
                Bitmap birthday_two = TransparentBitmap.getTransparentBitmap(FamilyNumber.getLocalImage(pathTwo), 50);
                lmImageOne.setImageBitmap(birthday_one);
                lmImageTwo.setImageBitmap(birthday_two);
                break;
            case R.id.lm_image_two:
                tvLeavemessagePerson.setText(gdid_2_name + "(设备ID:" + gdid_2 + ")");
                lmSign = 2;
                mDatas.clear();
                queryLeaveMessageTwo();
                mAdapter.notifyDataSetChanged();
                Bitmap birthdayOne = TransparentBitmap.getTransparentBitmap(FamilyNumber.getLocalImage(pathOne), 50);
                Bitmap birthdayTwo = TransparentBitmap.getTransparentBitmap(FamilyNumber.getLocalImage(pathTwo), 100);
                lmImageOne.setImageBitmap(birthdayOne);
                lmImageTwo.setImageBitmap(birthdayTwo);
                break;
        }
    }

    /**
     * 保存录音到数据库
     * @param role  保存录音的role
     */

    private int saveRecord(String role) {
        record = new Record();
        record.setDuration(time + "s");
        record.setStartTime(startRecordTime);
        record.setRecordPath(saveFilePath.getAbsolutePath());
        record.setRecordRole(role);
        if (record.save()) {
            Toast.makeText(LeaveMessage.this, "存储成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LeaveMessage.this, "存储失败", Toast.LENGTH_SHORT).show();
        }
        databaseLast = DataSupport.findLast(Record.class);
        mDatas.add(new LeaveMessageData("recorder" + databaseLast.getDuration(),
                databaseLast.getDuration(), databaseLast.getStartTime()));//向listview中添加数据
        mAdapter.notifyDataSetChanged();
        return databaseLast.getId();
    }

    /**
     * 查询第一个设备电话留言
     */

    private void queryLeaveMessageOne() {
        List<Record> roleOne = DataSupport.where("RecordRole == ?", "roleOne").find(Record.class);
        if (roleOne.size() > 0) {
            System.out.println("点击查询角色1");
            for (int i = 0; i < roleOne.size(); i++) {
                mDatas.add(new LeaveMessageData("recorder" + roleOne.get(i).getDuration(),
                        roleOne.get(i).getDuration(), roleOne.get(i).getStartTime()));//向listview中添加数据
            }
        }
    }

    /**
     * 查询第二个设备电话留言
     */

    private void queryLeaveMessageTwo() {
        List<Record> roleTwo = DataSupport.where("RecordRole == ?", "roleTwo").find(Record.class);
        if (roleTwo.size() > 0) {
            System.out.println("查询角色2保存的留言");
            for (int i = 0; i < roleTwo.size(); i++) {
                mDatas.add(new LeaveMessageData("recorder" + roleTwo.get(i).getDuration(),
                        roleTwo.get(i).getDuration(), roleTwo.get(i).getStartTime()));//向listview中添加数据
            }
        }
    }
}