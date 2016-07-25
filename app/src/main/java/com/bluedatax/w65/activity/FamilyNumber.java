package com.bluedatax.w65.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.R;
import com.bluedatax.w65.Service.MyService;
import com.bluedatax.w65.Service.OnConnectListener;
import com.bluedatax.w65.fragment.Family;
import com.bluedatax.w65.utils.SharedPrefsUtil;
import com.bluedatax.w65.utils.bitmapTransparency.TransparentBitmap;
import com.bluedatax.w65.utils.getTime;
import com.google.gson.JsonArray;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by xuyuanqiang on 16/3/11.
 */
public class FamilyNumber extends Activity {

    private Button saveNumber;
    private Activity mContext = this;
    private ListView lvShow;
    private ImageView tvLeftExit;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Button del;
    private MyAdapter adapter;
    private ArrayList<HashMap<String, Object>> listItem;
    private HashMap<String, Object> map;
    private View contentView;
    private PopupWindow mPopupWindow;
    private int mScreenHeight;
    private int width;
    private int height;
    private int pos;
    private Button ibtn1;
    private Button ibtn2;
    private Button ibtn3;
    private String CurrentTime;
    private SharedPreferences mSharedPreference;
    private String latitude;
    private String lnglatitude;
    private long token;
    private ImageView ibDeviceOne;
    private ImageView ibDeviceTwo;
    private String pathTwo;
    private String pathOne;
    private int modify;


    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService myservice = ((MyService.MyBinder) service).getService();
            myservice.setOnConnectListener(new OnConnectListener() {
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
                }

                @Override
                public void onFamilyNumber(JSONObject familyJSON) {
                    System.out.println("回调方法接收到的数据" + familyJSON);
                    try {
                        JSONObject body = familyJSON.getJSONObject("body");
                        String status = body.getString("status");
                        System.out.println(status);
                        if (status.equals("ok")) {
                            Toast.makeText(FamilyNumber.this, "保存成功!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(FamilyNumber.this, "保存失败!", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onDisconnect(String message) {
                }
                @Override
                public void onHeartbeat(JSONObject heartbeatJSON) {
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_family_number);
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        saveNumber = (Button) findViewById(R.id.save_family_number);
        tvLeftExit = (ImageView) findViewById(R.id.imageview_title_back);
        ibDeviceOne = (ImageView) findViewById(R.id.family_image_one);
        ibDeviceTwo = (ImageView) findViewById(R.id.family_image_two);


        CurrentTime = getTime.getCurrentTime();
        token = SharedPrefsUtil.getLong(this, "loginToken", 0L);
        mSharedPreference = getSharedPreferences("count", Context.MODE_PRIVATE);
        latitude = mSharedPreference.getString("lat", "");
        lnglatitude = mSharedPreference.getString("lng", "");
        Log.d("取出存入的经纬度", latitude + "#" + lnglatitude);
        pathOne = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage1.jpg";
        pathTwo = Environment.getExternalStorageDirectory() + "/w65/icon_download/" + "downloadImage2.jpg";
        Bitmap devTwoBitmap = TransparentBitmap.getTransparentBitmap(getLocalImage(pathTwo), 50);
        ibDeviceTwo.setImageBitmap(devTwoBitmap);
        if (pathOne != null) {
            ibDeviceOne.setImageBitmap(getLocalImage(pathOne));
        }
        if (pathTwo != null) {
            ibDeviceTwo.setImageBitmap(devTwoBitmap);
        }
        lvShow = (ListView) findViewById(R.id.lv_show);
        listItem = new ArrayList<HashMap<String, Object>>();
        adapter = new MyAdapter(this);
        lvShow.setAdapter(adapter);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();

        LayoutInflater lf = (LayoutInflater) FamilyNumber.this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        contentView = lf.inflate(R.layout.popupwindow, null);
        mPopupWindow = new PopupWindow(contentView, ActionMenuView.LayoutParams.WRAP_CONTENT,
                ActionMenuView.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);           //获取焦点，否则无法点击
        mPopupWindow.setOutsideTouchable(true);    //点击窗体外边窗口可消失
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());  //点击外边可消失
        mScreenHeight = getScreenHeight();


        saveNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        tvLeftExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FamilyNumber.this.finish();
            }
        });
        // 列表项长按事件
        lvShow.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Toast.makeText(FamilyNumber.this, "你长按了第" + position + "项",
                        Toast.LENGTH_SHORT).show();
                mPopupWindow.showAtLocation(lvShow, Gravity.BOTTOM
                        | Gravity.CENTER, 0, mScreenHeight - height / 2);
                pos = position;
                return false;
            }
        });
        // 弹窗内的按钮点击事件
        ibtn1 = (Button) contentView.findViewById(R.id.bt1);
        ibtn2 = (Button) contentView.findViewById(R.id.bt2);
        ibtn3 = (Button) contentView.findViewById(R.id.bt3);
        MyOnClickListener myOnClickListener = new MyOnClickListener();
        ibtn1.setOnClickListener(myOnClickListener);
        ibtn2.setOnClickListener(myOnClickListener);
        ibtn3.setOnClickListener(myOnClickListener);

        ibDeviceOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("您点击第一个设备");
                Bitmap devOneBitmap = TransparentBitmap.getTransparentBitmap(getLocalImage(pathOne), 100);
                ibDeviceOne.setImageBitmap(devOneBitmap);
                Bitmap devTwoBitmap = TransparentBitmap.getTransparentBitmap(getLocalImage(pathTwo), 50);
                ibDeviceTwo.setImageBitmap(devTwoBitmap);
                listItem.clear();
                adapter.notifyDataSetChanged();
            }
        });
        ibDeviceTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("您点击了第二个设备");
                Bitmap devTwoBitmap = TransparentBitmap.getTransparentBitmap(getLocalImage(pathTwo), 100);
                ibDeviceTwo.setImageBitmap(devTwoBitmap);
                Bitmap devOneBitmap = TransparentBitmap.getTransparentBitmap(getLocalImage(pathOne), 50);
                ibDeviceOne.setImageBitmap(devOneBitmap);
                listItem.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }


    private void showDialog() {
        if (listItem.size() == 3) {
            Toast.makeText(FamilyNumber.this, "你好！亲情号码只能保存三个", Toast.LENGTH_SHORT).show();
        } else if (listItem.size() < 4) {
            System.out.println(listItem.toString() + "号码个数");
            AlertDialog.Builder builder = new AlertDialog.Builder(FamilyNumber.this);
            final AlertDialog dialog = builder.create();
            View dialog_view = View.inflate(FamilyNumber.this, R.layout.activity_save_famiy_number, null);
            final EditText etName = (EditText) dialog_view.findViewById(R.id.et_name);
            final EditText etPhoneNumber = (EditText) dialog_view.findViewById(R.id.et_phone_number);
            etPhoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
            Button btExit = (Button) dialog_view.findViewById(R.id.bt_exit);
            Button btSave = (Button) dialog_view.findViewById(R.id.bt_save);
            btExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str_name = etName.getText().toString().trim();
                    String str_phone_number = etPhoneNumber.getText().toString().trim();

                    if (TextUtils.isEmpty(str_name)) {
                        Toast.makeText(FamilyNumber.this, "请输入姓名", Toast.LENGTH_SHORT).show();
                    } else if (str_phone_number.isEmpty()) {
                        Toast.makeText(FamilyNumber.this, "手机号码不能为空!", Toast.LENGTH_SHORT).show();
                    } else if (etPhoneNumber.getText().toString().length() == 11) {
                        String num = "[1][3458]\\d{9}";
                        Pattern p = Pattern.compile(num);       //编译
                        Matcher m = p.matcher(etPhoneNumber.getText().toString().trim());
                        if (m.matches()) {
                            map = new HashMap<String, Object>();
                            if (modify == 1) {
                                map.put("name", str_name);
                                map.put("upn", str_phone_number);
                                listItem.set(pos, map);
                                modify = 0;
                                adapter.notifyDataSetChanged();
                                System.out.println(listItem+"修改后的");
                            } else {
                                map.put("name", str_name);
                                map.put("upn", str_phone_number);
                                listItem.add(map);
                                Log.i("*********", listItem.toString());
                            }
                            setFamilyRequest();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(FamilyNumber.this, "请输入正确的手机号码!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(FamilyNumber.this, "请输入11位手机号码!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.setView(dialog_view);
            dialog.show();
        }
    }

    private void setFamilyRequest() {
        try {
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lat", latitude);
            jsonGeo.put("lng", lnglatitude);
            Log.d("json经纬度", jsonGeo.toString());
            JSONObject jsonObject = new JSONObject(jsonGeo.toString());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("gdid", "1-1001");
            jsonBody.put("action", "config");
            jsonBody.put("dt", CurrentTime);
            jsonBody.put("geo", jsonObject);

            JSONArray jsonWpn = new JSONArray();
            JSONObject jsonItem = new JSONObject();

            jsonItem.put("file_id", listItem.size() - 1);
            jsonItem.put("name", listItem.get(listItem.size() - 1).get("name").toString());
            jsonItem.put("upn", listItem.get(listItem.size() - 1).get("upn").toString());
            jsonWpn.put(jsonItem);
            JSONObject jsonType = new JSONObject();
            jsonType.put("wpn", jsonWpn);
            jsonBody.put("type", jsonType);

            JSONObject jsonReq = new JSONObject();
            jsonReq.put("msg", 114);
            jsonReq.put("token", token);
            jsonReq.put("body", jsonBody);
            MyService.client.send(jsonReq.toString());
        } catch (Exception e) {

        }
    }

    public static Bitmap getLocalImage(String path) {
        Bitmap deviceBitmap = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                deviceBitmap = BitmapFactory.decodeFile(path);
            }
        } catch (Exception e) {
        }
        return deviceBitmap;
    }

    // 弹窗的点击事件
    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt1:
                    modify = 1;
                    showDialog();
                    mPopupWindow.dismiss();
                    break;
                case R.id.bt2:
                    listItem.remove(pos);
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.bt3:
                    mPopupWindow.dismiss();
                    adapter.notifyDataSetChanged();
                    break;
            }
            Toast.makeText(FamilyNumber.this, "您点击了", Toast.LENGTH_SHORT).show();
        }
    }

    private int getScreenHeight() {
        // 获取屏幕实际像素
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = FamilyNumber.this.getWindowManager()
                .getDefaultDisplay();
        display.getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private class MyAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater inflater;

        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listItem.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            // TODO Auto-generated method stub
            if (view == null) {
                view = inflater.inflate(R.layout.list_item, null);
            }
            TextView itemName = (TextView) view.findViewById(R.id.item_name);
            TextView itemNumber = (TextView) view.findViewById(R.id.item_number);
            itemName.setText(listItem.get(position).get("name").toString());
            itemNumber.setText(listItem.get(position).get("upn").toString());
//            adapter.notifyDataSetChanged();

            return view;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}