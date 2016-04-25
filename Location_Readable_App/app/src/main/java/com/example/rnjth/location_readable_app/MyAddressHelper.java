package com.example.rnjth.location_readable_app;

import android.location.Address;
import android.util.Log;

/**
 * Created by rnjth on 04-04-2016 successfully at 13:25
 */
public class MyAddressHelper {

    public static void logMyAddress(String logMyTag, Address address) {
        // Printable addresses
        int maxAddressLineIndex = address.getMaxAddressLineIndex();
        for(int addressLineIndex = 0; addressLineIndex <= maxAddressLineIndex;addressLineIndex++){
            String addressLine = address.getAddressLine(addressLineIndex);
            Log.d(logMyTag, String.format("%d: %s", addressLineIndex, addressLine));
        }
        double latitude = 0.0d;
        double longitude = 0.0d;

        boolean hasLatitude = address.hasLatitude();
        if (hasLatitude)
            latitude = address.getLatitude();
        boolean hasLongitude = address.hasLongitude();
        if(hasLongitude)
            longitude = address.getLongitude();

        String adminArea = address.getAdminArea();
        String featureName = address.getFeatureName();
        String locality = address.getLocality();
        String postalCode = address.getPostalCode();
        String premises = address.getPremises();
        String subAdminArea = address.getSubAdminArea();
        String subLocality = address.getSubLocality();
        String subThoroughfare = address.getSubThoroughfare();
        String thoroughfare = address.getThoroughfare();
        String phone = address.getPhone();
        String url = address.getUrl();

        logMyValue(logMyTag, "latitude", hasLatitude, latitude);
        logMyValue(logMyTag, "longitude", hasLongitude, longitude);

        logMyValue(logMyTag, "adminArea", adminArea);
        logMyValue(logMyTag, "featureName", featureName);
        logMyValue(logMyTag, "locality", locality);
        logMyValue(logMyTag, "subLocality", subLocality);
        logMyValue(logMyTag, "postalCode", postalCode);
        logMyValue(logMyTag, "premises", premises);
        logMyValue(logMyTag, "subAdminArea", subAdminArea);
        logMyValue(logMyTag, "subLocality", subLocality);
        logMyValue(logMyTag, "thoroughfare", thoroughfare);
        logMyValue(logMyTag, "subThoroughfare", subThoroughfare);
        logMyValue(logMyTag, "phone", phone);
        logMyValue(logMyTag, "url", url);

    }

    private static void logMyValue(String logMyTag, String valueName, String value) {
        Log.d(logMyTag, valueName + "=" + (value != null ? value : "<NULL>"));
    }

    private static void logMyValue(String logMyTag, String valueName, boolean hasValue, double value) {
        Log.d(logMyTag, valueName + "=" + (hasValue ? value : "<NO_VALUE>"));
    }


}

