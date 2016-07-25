package com.bluedatax.w65.utils.map;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.R;

/**
 * Created by bdx108 on 15/12/16.
 */
public class BdMapApplication extends BaseActivity implements View.OnClickListener {
    private Button mButtonSearch;
    private EditText mEditDestination;
    private EditText mEditCity;
    MapView mMapView;
    BaiduMap mBaiduMap;
    private GeoCoder mSearch;
    //public LocationClient mLocationClient = null;
    //public BDLocationListener myListener = new MyLocationListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.baidu_map);

        mButtonSearch= (Button) findViewById(R.id.button_search);
        mButtonSearch.setOnClickListener(this);
        mEditDestination= (EditText) findViewById(R.id.edittext_destination);
        mEditCity= (EditText) findViewById(R.id.edittext_city);

//        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
//        mLocationClient.registerLocationListener( myListener );    //注册监听函数

        //initLocation();
        //mLocationClient.start();

        mSearch = GeoCoder.newInstance();

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //普通地图
        //mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//卫星地图
        //mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
    }
//    private void initLocation(){
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
//        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span=1000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
//        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
//        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
//        mLocationClient.setLocOption(option);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_search:
                mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    public void onGetGeoCodeResult(GeoCodeResult result) {
                        Log.d("1111111",result.toString());
                        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                            //没有检索到结果
                            Toast.makeText(getApplicationContext(),"未能找到结果",Toast.LENGTH_LONG).show();
                            return;
                        }else if (result.error == SearchResult.ERRORNO.PERMISSION_UNFINISHED){//百度地图鉴定失败 重新鉴定
                            Log.d("2222222","jianquanshibai");
                           Toast.makeText(getApplicationContext(),"jianquanshibai",Toast.LENGTH_LONG).show();
                            }
                        //获取地理编码结果
                        mBaiduMap.clear();
                        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
                        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result.getLocation()));
                        String addLocate=String.format("weidu:%fjiangdu:%f",result.getLocation().latitude,result.getLocation().longitude);
//                        //创建InfoWindow展示的view
                        Button button = new Button(getApplicationContext());
                        button.setText("这里用来显示具体位置");
                        button.setTextColor(Color.BLACK);
                        button.setBackgroundResource(R.drawable.button_commit);
//定义用于显示该InfoWindow的坐标点
                        LatLng pt = new LatLng(result.getLocation().latitude, result.getLocation().longitude);
//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                        InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);
//显示InfoWindow
                        mBaiduMap.showInfoWindow(mInfoWindow);
// 定义文字所显示的坐标点
//                        LatLng llText = new LatLng(result.getLocation().latitude, result.getLocation().longitude);
////构建文字Option对象，用于在地图上添加文字
//                        OverlayOptions textOption = new TextOptions()
//                                .bgColor(0xAAFFFF00)
//                                .fontSize(24)
//                                .fontColor(0xFFFF00FF)
//                                .text("百度地图SDK")
//                                .position(llText);
////在地图上添加该文字对象并显示
//                        mBaiduMap.addOverlay(textOption);
                        Toast.makeText(getApplicationContext(),addLocate,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                            //没有找到检索结果
                        }
                        //获取反向地理编码结果
                    }
                });
                Log.d("123456",mEditDestination.getText().toString());
                mSearch.geocode(new GeoCodeOption().city(mEditCity.getText().toString()).address(mEditDestination.getText().toString()));

                break;
            default:
                break;
        }
    }
    //    public class MyLocationListener implements BDLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            //Receive Location
//            StringBuffer sb = new StringBuffer(256);
//            sb.append("time : ");
//            sb.append(location.getTime());
//            sb.append("\nerror code : ");
//            sb.append(location.getLocType());
//            sb.append("\nlatitude : ");
//            sb.append(location.getLatitude());
//            sb.append("\nlontitude : ");
//            sb.append(location.getLongitude());
//            sb.append("\nradius : ");
//            sb.append(location.getRadius());
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//                sb.append("\nspeed : ");
//                sb.append(location.getSpeed());// 单位：公里每小时
//                sb.append("\nsatellite : ");
//                sb.append(location.getSatelliteNumber());
//                sb.append("\nheight : ");
//                sb.append(location.getAltitude());// 单位：米
//                sb.append("\ndirection : ");
//                sb.append(location.getDirection());// 单位度
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                sb.append("\ndescribe : ");
//                sb.append("gps定位成功");
//
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                //Toast.makeText(getApplicationContext(),location.getAddrStr(),Toast.LENGTH_LONG).show();
//                //运营商信息
//                sb.append("\noperationers : ");
//                sb.append(location.getOperators());
//                sb.append("\ndescribe : ");
//                sb.append("网络定位成功");
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                sb.append("\ndescribe : ");
//                sb.append("离线定位成功，离线定位结果也是有效的");
//            } else if (location.getLocType() == BDLocation.TypeServerError) {
//                sb.append("\ndescribe : ");
//                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                sb.append("\ndescribe : ");
//                sb.append("网络不同导致定位失败，请检查网络是否通畅");
//            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                sb.append("\ndescribe : ");
//                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//            }
//            sb.append("\nlocationdescribe : ");
//            sb.append(location.getLocationDescribe());// 位置语义化信息
//            List<Poi> list = location.getPoiList();// POI数据
//            if (list != null) {
//                sb.append("\npoilist size = : ");
//                sb.append(list.size());
//                for (Poi p : list) {
//                    sb.append("\npoi= : ");
//                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
//                }
//            }
//            Log.i("BaiduLocationApiDem", sb.toString());
//        }
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
