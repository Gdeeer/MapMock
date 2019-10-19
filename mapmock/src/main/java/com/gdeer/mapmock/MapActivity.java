package com.gdeer.mapmock;


import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import android.app.Activity;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class MapActivity extends Activity implements AMap.OnMapClickListener, GeocodeSearch.OnGeocodeSearchListener {
    private AMap aMap;
    private MapView mapView;

    /**
     * 自定义定位小蓝点的Marker
     */
    private Marker locationMarker;

    /**
     * 搜索
     */
    private GeocodeSearch geocodeSearch;

    /**
     * 纬度、经度
     */
    private double mLat, mLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.map);
        // 此方法必须重写
        mapView.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);
        MyLocationStyle locationStyle = new MyLocationStyle();
        // 定位一次，且将视角移动到地图中心点。
        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;
        aMap.setMyLocationStyle(locationStyle);
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
                getAddress(latLonPoint);
                locationMarker = aMap.addMarker((new MarkerOptions())
                    .position(latLng)
                    .title("origin"));
            }
        });

        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
    }

    private void setUpMap() {
        aMap.setOnMapClickListener(this);
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
        MapMock.setMockAMapLocation(null);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        locationMarker.setPosition(latLng);
        mLat = latLng.latitude;
        mLng = latLng.longitude;
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        getAddress(latLonPoint);
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        // 第一个参数表示一个 Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是 GPS 原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        // 设置异步逆地理编码请求
        geocodeSearch.getFromLocationAsyn(query);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                && result.getRegeocodeAddress().getFormatAddress() != null) {
                String addressName = result.getRegeocodeAddress().getFormatAddress() + "附近";

                RegeocodeAddress regeocodeAddress = result.getRegeocodeAddress();
                AMapLocation aMapLocation = generateAMapLocation(regeocodeAddress);
                MapMock.setMockAMapLocation(aMapLocation);
                Log.d("MapActivity", aMapLocation.toString());

                showToast(addressName);
            } else {
                showToast("error not valid");
            }
        } else {
            showToast("error " + rCode);
        }
    }

    public void showToast(String info) {
        Toast.makeText(this.getApplicationContext(), info, Toast.LENGTH_LONG).show();
    }

    private AMapLocation generateAMapLocation(RegeocodeAddress regeocodeAddress) {
        if (regeocodeAddress == null) {
            return null;
        }
        AMapLocation aMapLocation = new AMapLocation("mock");
        aMapLocation.setLatitude(mLat);
        aMapLocation.setLongitude(mLng);
        aMapLocation.setProvince(regeocodeAddress.getProvince());
        aMapLocation.setCity(regeocodeAddress.getCity());
        aMapLocation.setCityCode(regeocodeAddress.getCityCode());
        aMapLocation.setDistrict(regeocodeAddress.getDistrict());
        aMapLocation.setAddress(regeocodeAddress.getFormatAddress());
        aMapLocation.setCountry(regeocodeAddress.getCountry());
        aMapLocation.setAdCode(regeocodeAddress.getAdCode());
        aMapLocation.setTime(System.currentTimeMillis());

        try {
            aMapLocation.setNumber(regeocodeAddress.getStreetNumber().getNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            aMapLocation.setRoad(regeocodeAddress.getRoads().get(0).getName());
            aMapLocation.setStreet(regeocodeAddress.getRoads().get(0).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            aMapLocation.setPoiName(regeocodeAddress.getPois().get(0).getAdName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            aMapLocation.setAoiName(regeocodeAddress.getAois().get(0).getAoiName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return aMapLocation;
    }

    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
    }
}
