package com.gdeer.maptest;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.gdeer.mapmock.MapMock;
import com.tbruyelle.rxpermissions2.RxPermissions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    TextView mTvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvMain = findViewById(R.id.tv_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        execLocation();
    }

    @SuppressLint("CheckResult")
    private void execLocation() {
        final RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
            .subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean granted) throws Exception {
                    if (granted) {
                        final AMapLocationClient client = new AMapLocationClient(MainActivity.this);
                        AMapLocationClientOption option = new AMapLocationClientOption();
                        option.setOnceLocation(true);
                        client.setLocationOption(option);
                        client.setLocationListener(new AMapLocationListener() {
                            @Override
                            public void onLocationChanged(AMapLocation aMapLocation) {
                                MapMock.handleRealLocation(aMapLocation);
                                mTvMain.setText(aMapLocation.toStr().replaceAll(",", ",\n"));
                                client.stopLocation();
                            }
                        });
                        client.startLocation();
                    }
                }
            });
    }
}
