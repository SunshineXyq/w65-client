package com.bluedatax.w65.utils.map;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.GroundOverlayOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.R;


/**
 * Created by bdx108 on 15/12/9.
 */
  public class ApplicationMap extends BaseActivity implements LocationSource,AMapLocationListener,
        GeocodeSearch.OnGeocodeSearchListener,View.OnClickListener{
    private MapView mMapView;
    private Button mButtonLocation;
    private EditText mEditTextDest;
    private AMap mMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baimap);
        mMapView= (MapView) findViewById(R.id.gaodemap);
        mButtonLocation= (Button) findViewById(R.id.button_location);
        mButtonLocation.setOnClickListener(this);
        mEditTextDest= (EditText) findViewById(R.id.edittext_destination);
        mMapView.onCreate(savedInstanceState);
        mMap=mMapView.getMap();
        CameraUpdate cameraUpdate= CameraUpdateFactory.zoomTo(16);
        mMap.moveCamera(cameraUpdate);
        setUpMap();
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.mipmap.ic_launcher));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        mMap.setMyLocationStyle(myLocationStyle);

        mMap.setLocationSource(this);// 设置定位监听
        mMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        mMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // aMap.setMyLocationType()
    }
    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
            Log.d("gaode123456","开始定位");
        }
    }
    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        //获取解析得到第一个地址
        Log.d("yunxingdaoci","yunxingdaoci");
        //Log.d("yunxingdaoci",""+geocodeResult.getGeocodeAddressList().get(i));
        GeocodeAddress geocodeAddress=geocodeResult.getGeocodeAddressList().get(0);
        Log.d("yunxingdaoci","yunxingdaoci222");
        LatLonPoint point=geocodeAddress.getLatLonPoint();
        Log.d("yunxingdaoci","yunxingdaoci333");
        LatLng targetPos=new LatLng(point.getLatitude(),point.getLongitude());
        Toast.makeText(getApplicationContext(),"经纬度为"+point.getLatitude(),Toast.LENGTH_LONG).show();
        Log.d("dizhiwei",point.getLatitude()+"");
        //创建一个设置经纬度的CameraUpdate
        CameraUpdate cu=CameraUpdateFactory.changeLatLng(targetPos);
        //更新地图的显示区域
        mMap.moveCamera(cu);
        GroundOverlayOptions options=new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)).position(targetPos,64);
        mMap.addGroundOverlay(options);
        CircleOptions circleOptions=new CircleOptions().center(targetPos).fillColor(0x80ffff00).radius(80)
                .strokeWidth(1).strokeColor(0xff000000);
        mMap.addCircle(circleOptions);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_location:
                String address=mEditTextDest.getText().toString();
                if ("".equals(address)){
                    Toast.makeText(getApplicationContext(),"请输入有效的地址",Toast.LENGTH_LONG).show();
                }else {
                    GeocodeSearch search=new GeocodeSearch(this);
                    search.setOnGeocodeSearchListener(this);
                    GeocodeQuery query=new GeocodeQuery(address,"广州");
                    search.getFromLocationNameAsyn(query);
                    Toast.makeText(getApplicationContext(),"开始搜索",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
}
