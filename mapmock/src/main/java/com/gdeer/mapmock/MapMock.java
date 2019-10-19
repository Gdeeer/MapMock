package com.gdeer.mapmock;

import com.amap.api.location.AMapLocation;

public class MapMock {
    private static AMapLocation sMockAMapLocation;

    static void setMockAMapLocation(AMapLocation aMapLocation) {
        sMockAMapLocation = aMapLocation;
    }

    public static void handleRealLocation(AMapLocation aMapLocation) {
        if (aMapLocation == null || sMockAMapLocation == null) {
            return;
        }
        aMapLocation.setLatitude(sMockAMapLocation.getLatitude());
        aMapLocation.setLongitude(sMockAMapLocation.getLongitude());
        aMapLocation.setProvince(sMockAMapLocation.getProvince());
        aMapLocation.setCity(sMockAMapLocation.getCity());
        aMapLocation.setCityCode(sMockAMapLocation.getCityCode());
        aMapLocation.setDistrict(sMockAMapLocation.getDistrict());
        aMapLocation.setAddress(sMockAMapLocation.getAddress());
        aMapLocation.setCountry(sMockAMapLocation.getCountry());
        aMapLocation.setRoad(sMockAMapLocation.getRoad());
        aMapLocation.setPoiName(sMockAMapLocation.getPoiName());
        aMapLocation.setStreet(sMockAMapLocation.getStreet());
        aMapLocation.setNumber(sMockAMapLocation.getStreetNum());
        aMapLocation.setAoiName(sMockAMapLocation.getAoiName());
        aMapLocation.setDescription(sMockAMapLocation.getDescription());
        aMapLocation.setTime(sMockAMapLocation.getTime());
    }
}
