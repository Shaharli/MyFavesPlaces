package com.avigezerit.myfaves.Control.mHelpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/* * * * * * * * * * * * * * * * * * * *  LOCATION PROVIDER - HELPER  * * * * * * * * * * * * * * * * * * * */


public class getLocationHelper extends FragmentActivity implements android.location.LocationListener {

    private static final String TAG = getLocationHelper.class.getSimpleName();

    //permission req code
    public static final int REQUEST_LOCATION_PERMISSION = 101;

    //get location
    private LocationManager locationManager;
    private String provider;

    //context holder
    Context context;
    Activity activity;

    public getLocationHelper() {
    }

    //// INITIALIZING METHODS ////

    public void setContext(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    //init manager & get current location
    public void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //get an instance of the location service
            locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

            //get the best location-provider that matches a certain criteria
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
            criteria.setPowerRequirement(Criteria.POWER_LOW);

            //set to provider
            provider = locationManager.getBestProvider(criteria, true);

            Log.d(TAG, "provider: " + provider);

            locationManager.requestLocationUpdates(provider, 4000, 1, this);

            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                Log.d(TAG, "lat: " + location.getLatitude() + " lng: " + location.getLongitude());
                writeToSharedPref(location.getLatitude(), location.getLongitude());
            }
        }
    }

    private void writeToSharedPref(double mLt, double mLng) {

        //saving location for app-level use
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("lt", (float) mLt);
        editor.putFloat("lg", (float) mLng);
        editor.commit();

    }

    //// LOCATION METHODS ////

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            Log.d(TAG, "lat: " + location.getLatitude() + " lng: " + location.getLongitude());
            writeToSharedPref(location.getLatitude(), location.getLongitude());
        }
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




