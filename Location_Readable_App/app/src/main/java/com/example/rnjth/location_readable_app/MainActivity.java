package com.example.rnjth.location_readable_app;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "Location Human Readable";
    LocationListener networkListener;
    LocationListener gpsListener;
    Location location;
    Location loc;
    float dist;
    String s2;
    String locName;
   double lat,longi;
    double[] lati=new double[100];
    //lati[0]=0.0;

    double[] longit=new double[100];
    //longit[0]=0.0;
    String s;
    DecimalFormat df=new DecimalFormat("0.00");
    Intent i=new Intent();
    int k=1;
    Criteria criteria;
    String[] locality=new String[100];
    List<Address> addressList;
    Looper looper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        long uiThreadId = Thread.currentThread().getId();
        Log.d(LOG_TAG, String.format("UI Thread ID: %d", uiThreadId));
    }


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


        return super.onOptionsItemSelected(item);
    }


    public void findPresentLocation(View view) {
        Log.d(LOG_TAG, "From Location menu selected");
        clearDisplay();

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        gpsListener = new MyListener();
        networkListener = new MyListener();


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
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkListener);
        if (gpsListener == null) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        //Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //Location nwlocation=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location == null) {
            Log.d(LOG_TAG, "No 'Last Known Location' available");
            Toast.makeText(this, "No 'Last Known Location' available", Toast.LENGTH_LONG).show();
            AlertDialog.Builder ad=new AlertDialog.Builder(this);
            ad.setMessage("Wi-Fi or Location Services aren't turned on");
            ad.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_LONG).show();
                }
            });
            AlertDialog alertDialog = ad.create();
            alertDialog.show();
            return;
        }

        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList =
                    geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);


            int addressesReturned = addressList.size();
            Log.d(LOG_TAG, String.format("Number of addresses returned: %d", addressesReturned));

            for (Address address : addressList) {
                displayAddressLines(address);
                MyAddressHelper.logMyAddress(LOG_TAG, address);
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

        }
    }

   /* public void onStopListening(View view) {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (gpsListener != null) {
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
            lm.removeUpdates(gpsListener);
            gpsListener = null;
        }
        if (networkListener != null) {
            lm.removeUpdates(networkListener);
            networkListener = null;
        }
    }
 */
    public void onPlaceName(View view) {
        long threadId = Thread.currentThread().getId();
        /*Toast.makeText(MainActivity.this,"Current Thread: "+threadId,Toast.LENGTH_LONG).show();
        EditText et1=(EditText)findViewById(R.id.editText);
        String locationName=et1.getText().toString();
        MyAsyncTask mat= new MyAsyncTask();
        mat.execute(locationName);*/
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        gpsListener = new MyListener();

        networkListener = new MyListener();
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
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkListener);

        if(gpsListener==null) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        else {
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        Log.d(LOG_TAG, "From Place Name menu selected");
        clearDisplay();
        EditText et1=(EditText)findViewById(R.id.editText);
        String locationName=et1.getText().toString();
        // String locationName = "Empire State Building, NYC";

        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList =
                    geocoder.getFromLocationName(locationName, 21);


            //getIntent().putExtra("location name", locationName);

            int addressesReturned = addressList.size();
            Log.d(LOG_TAG, String.format("Number of addresses returned: %d", addressesReturned));
            Toast.makeText(this,"Number of address returned: "+addressesReturned,Toast.LENGTH_LONG).show();
            for(Address address:addressList) {
                lat=address.getLatitude();
                longi=address.getLongitude();
                locality[k]=address.getAddressLine(0)+","+address.getAddressLine(1);


               lati[k]=lat;
               longit[k]=longi;

                loc.setLatitude(lat);
                loc.setLongitude(longi);
                dist=(location.distanceTo(loc))/1000;
               // if(dist<20.00){
                    //addLineToDisplay();
                    displayAddressLines(address);
                    s=df.format(dist)+" "+"mi";
                    addLineToDisplay(s );
                    addLineToDisplay("");
                    MyAddressHelper.logMyAddress(LOG_TAG, address);
              //  }
                k++;
            }
         // i=new Intent(this,MapsActivity.class);
            i.setAction(Intent.ACTION_SEND);
//            i.getExtras().clear();
            i.putExtra("latitudes", lati);
            i.putExtra("longitudes", longit);
            i.putExtra("localities", locality);
            startActivity(Intent.createChooser(i, getResources().getText(R.string.send_to)));


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
    public void findNearby(MenuItem item){
        EditText et1=(EditText)findViewById(R.id.editText);
        String locationName=et1.getText().toString();
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        gpsListener = new MyListener();
        networkListener = new MyListener();


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
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkListener);
        if (gpsListener == null) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        //Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //Location nwlocation=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location == null) {
            Log.d(LOG_TAG, "No 'Last Known Location' available");
            //Toast.makeText(this, "No 'Last Known Location' available", Toast.LENGTH_LONG).show();
            AlertDialog.Builder ad=new AlertDialog.Builder(this);
            ad.setMessage("Wi-Fi or Location Services aren't turned on");
            ad.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_LONG).show();
                }
            });
            AlertDialog alertDialog = ad.create();
            alertDialog.show();

            return;
        }

        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> address1 =
                    geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);


            int addressesReturned = address1.size();
            Log.d(LOG_TAG, String.format("Number of addresses returned: %d", addressesReturned));

            for (Address address : address1) {
                s=address.getAddressLine(1);
                break;
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

        }
        locName=locationName+s;
        clearDisplay();
        addLineToDisplay("");
        MyAsyncTask asyncTask = new MyAsyncTask();
        asyncTask.execute(locName);

    }

    public void onMenuUseAsyncTask(MenuItem item) {
        Log.d(LOG_TAG, "Use AsyncTask menu selected");

        //String placeName = "10 Downing Street, London";
        EditText et1=(EditText)findViewById(R.id.editText);
        String locationName=et1.getText().toString();

        MyAsyncTask asyncTask = new MyAsyncTask();
        asyncTask.execute(locationName);

    }

    public void onExit(MenuItem item) {
        Log.d(LOG_TAG, "Exit menu selected");

        finish();
    }

    private void displayAddressLines(Address address) {
        int lastIndex = address.getMaxAddressLineIndex();
        for(int index = 0; index <= lastIndex; index++) {
            String addressLine = address.getAddressLine(index);
            addLineToDisplay(addressLine);
        }
        //addLineToDisplay("");

    }
    public void doClearDisplay(View view){
        clearDisplay();
    }

    private void clearDisplay() {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("");
    }

    private void addLineToDisplay(CharSequence displayLine) {
        TextView textView = (TextView) findViewById(R.id.textView);

        CharSequence existingText = textView.getText();
        CharSequence newText = existingText + "\n" + displayLine;

        textView.setText(newText);
    }




    @Override
    protected void onStop(){
        super.onStop();
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (gpsListener != null) {
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
            lm.removeUpdates(gpsListener);
            gpsListener = null;
        }
    }

    class MyAsyncTask extends AsyncTask<String, Void, List<Address>>{

        @Override
        protected List<Address> doInBackground(String... str) {
            Looper.prepare();
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            gpsListener = new MyListener();

            networkListener = new MyListener();
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                //return addressList;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);

            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkListener);

            if(gpsListener==null) {
                location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            else {
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if(location==null || loc==null){
                AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this);
                ad.setMessage("Wi-Fi or Location Services aren't turned on");
                ad.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog alertDialog = ad.create();
                alertDialog.show();
            }


            Geocoder geocoder = new Geocoder(MainActivity.this);
            try {
                addressList =
                        geocoder.getFromLocationName(str[0], 21);

            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            return addressList;
        }
        @Override
        public void onPostExecute(List<Address> addressList){

                long threadId = Thread.currentThread().getId();
                int addressesReturned = addressList.size();
                Log.d(LOG_TAG, String.format("Number of addresses returned: %d", addressesReturned));
                Toast.makeText(MainActivity.this,"Number of address returned: "+addressesReturned,Toast.LENGTH_LONG).show();
                for(Address address:addressList) {
                    lat=address.getLatitude();
                    longi=address.getLongitude();
                    locality[k]=address.getAddressLine(0)+","+address.getAddressLine(1);


                    lati[k]=lat;
                    longit[k]=longi;

                    loc.setLatitude(lat);
                    loc.setLongitude(longi);
                    dist=(location.distanceTo(loc))/1000;
                    // if(dist<20.00){
                    //addLineToDisplay();
                   // MainActivity.this.clearDisplay();
                    displayAddressLines(address);
                    s=df.format(dist)+" "+"mi";
                    addLineToDisplay(s );
                    addLineToDisplay("");
                    MyAddressHelper.logMyAddress(LOG_TAG, address);
                    //  }
                    k++;
                }
                // i=new Intent(this,MapsActivity.class);
                i.setAction(Intent.ACTION_SEND);
//            i.getExtras().clear();
                i.putExtra("latitudes", lati);
                i.putExtra("longitudes", longit);
                i.putExtra("localities", locality);
                startActivity(Intent.createChooser(i, getResources().getText(R.string.send_to)));
                Toast.makeText(MainActivity.this,"Current Thread: "+threadId,Toast.LENGTH_LONG).show();
            
            
        }
    }

}
