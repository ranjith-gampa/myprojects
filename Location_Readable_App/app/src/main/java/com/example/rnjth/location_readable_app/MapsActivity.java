package com.example.rnjth.location_readable_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap _myMap;
    Location l;
    LocationManager lm;
    double lat, longi;
    double[] lati;
    double[] longit;
    double latitude, longitude;
    Intent i;
    Bundle b;
    String[] localities;
    String locality;
    LocationListener gpsListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //setUpMapIfNeeded();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "On Map Ready ", Toast.LENGTH_LONG).show();
        _myMap = googleMap;
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        gpsListener=new MyListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);

        if(gpsListener==null) {

            l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if(l==null){
            Toast.makeText(this,"Present Location Unavailable",Toast.LENGTH_LONG).show();
        }
        else {
            lat = l.getLatitude();
            longi = l.getLongitude();
        }

        i = getIntent();
        b = i.getExtras();
        if (b != null) {

            lati = (double[]) b.get("latitudes");

            longit = (double[]) b.get("longitudes");
            Object[] loc = (Object[]) b.get("localities");


            for (int k = 0; k < lati.length; k++) {

                latitude = lati[k];
                longitude = longit[k];
                locality = (String) loc[k];

                //Toast.makeText(this, "Lat-Longi " + lati + "-" + longit, Toast.LENGTH_LONG).show();
                _myMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(locality));
                _myMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));

                _myMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
                _myMap.addCircle(new CircleOptions().center(new LatLng(latitude, longitude)).visible(true));
                _myMap.getFocusedBuilding();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
    }


}
