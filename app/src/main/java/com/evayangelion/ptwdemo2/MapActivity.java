package com.evayangelion.ptwdemo2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;


public class MapActivity extends AppCompatActivity implements LocationSource,
        AMapLocationListener {


    private AMap aMap;
    //创建一个高德地图对象
    private MapView mapView;
    //MapView 是AndroidView的子类，是一个地图容器，用于加载地图。LHT

    private boolean isFirstLatLng;
    //判断是不是第一次定位
    private LatLng oldLatLng;
    //多次定位中以前的定位点：



    private OnLocationChangedListener mListener;//定位监听器
    private AMapLocationClient mlocationClient;//定位发起端
    private AMapLocationClientOption mLocationOption;//定位参数
    private RadioGroup mGPSModeGroup;

    private TextView mLocationErrText;
    private TextView getDistance;

    float distance=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //隐藏标题栏
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_map);
        //获取地图控件的引用。
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        isFirstLatLng=true;

        init();
    }




    /**
     * 初始化
     */
    private void init() {
        //判断aMap对象情况

        if (aMap == null) {
            aMap = mapView.getMap();
            //设置地图样式
            //aMap.setMapType(AMap.MAP_TYPE_NIGHT);
            //隐藏文字标注
            aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {
                    aMap.showMapText(false);
                }
            });
        }
        //复选框组合
       // mGPSModeGroup = (RadioGroup) findViewById(R.id.gps_radio_group);
        //为复选框按钮注册监听器
       // mGPSModeGroup.setOnCheckedChangeListener(this);

        //文本框内容
        //报错文本
        mLocationErrText = (TextView)findViewById(R.id.location_errInfo_text);
        mLocationErrText.setVisibility(View.GONE);
        //距离计算文本
        getDistance=(TextView)findViewById(R.id.distance);
        getDistance.setVisibility(View.GONE);

        //设置一些aMap属性
        //放缩开关
        aMap.setMaxZoomLevel(17);
        aMap.setMinZoomLevel(15);

        aMap.setLocationSource(this);// 设置定位监听

        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置右上角定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        // 设置定位的类型为定位模式
        MyLocationStyle myLocationStyle;
        myLocationStyle=new MyLocationStyle();
        myLocationStyle.interval(200);
        //aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        aMap.setMyLocationStyle(myLocationStyle);
    }

    /**
     * 画线
     */
    private void setUpMap(LatLng oldl,LatLng newl) {
        //PolylineOptions line;

        //设置纹理
        //BitmapDescriptorFactory 是bitmap 描述信息工厂类
        //用于创建BitmapDescriptor 对象。
        //而BitmapDescriptor 对象则是setCustomTexture要求的参数
        BitmapDescriptor blueroad= BitmapDescriptorFactory
                .fromAsset("blue_road.png");
        PolylineOptions polylineOptions=new PolylineOptions();
        polylineOptions.setCustomTexture(blueroad);

        polylineOptions.width(100);
        //polylineOptions.color(Color.CYAN);

        //划线
        aMap.addPolyline(polylineOptions
                //另一种设定路径贴图的方法
                //.setCustomTexture(BitmapDescriptorFactory.fromAsset("blue_road.png"))
                .add(oldl,newl).geodesic(true));


        //计算距离
        distance += AMapUtils.calculateLineDistance(oldl,newl);

        getDistance.setVisibility(View.VISIBLE);
        String dis="now distance is"+distance;
        getDistance.setText(dis);

    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mLocationErrText.setVisibility(View.GONE);

                //Toast.makeText(LoginActivity.this,"SHISHI",Toast.LENGTH_SHORT).show();
                Log.d("===纬度：",""+amapLocation.getLatitude());
                Log.d("===经度：",""+amapLocation.getLongitude());

                //新建一个经纬度对象 保存当前的经纬度内容
                LatLng newLatLng =new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点

                if(isFirstLatLng){
                    oldLatLng=newLatLng;
                    isFirstLatLng=false;
                }
                if(oldLatLng!=newLatLng){
                    Log.e("Amap",+amapLocation.getLatitude()+","+amapLocation.getLongitude());
                    setUpMap(oldLatLng,newLatLng);

                    //每进行一次定位 生成一个marker做一次标记
                    Marker marker=aMap.addMarker(new MarkerOptions().position(newLatLng).title("new").snippet("DefaultMarker"));
                    oldLatLng=newLatLng;

                }

            }
            else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
                mLocationErrText.setVisibility(View.VISIBLE);
                mLocationErrText.setText(errText);
            }
        }
    }


    /**
     * activate和deactivate是
     * LocationSource 的接口
     *
     */

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            //设置定位间隔
            mLocationOption.setInterval(1000);

            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
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




}



