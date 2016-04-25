package com.example.rnjth.location_readable_app;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by rnjth on 04-04-2016 successfully at 18:53
 */
public class MyListener implements LocationListener {
    //Location loc=

    @Override
    public void onLocationChanged(Location location) {
        String provider = location.getProvider();
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        float accuracy = location.getAccuracy();
        long time = location.getTime();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
