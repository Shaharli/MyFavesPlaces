package com.avigezerit.myfaves.Control;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by Shaharli on 20/08/2016.
 */
public class getLocationHelper extends Activity implements android.location.LocationListener {


    private static final String TAG = getLocationHelper.class.getSimpleName();
//permission

    private static final int REQUEST_PERMISSION = 101;
    //get location
    private LocationManager locationManager;
    private String provider;
    Context context;
    Activity activity;
    private double mLati;
    private double mLongi;

    public getLocationHelper() {

    }

    public void setContext(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    //get location
    public void getCurrentLocation() {

        Log.d(TAG, "Getting Location");


        //get an instance of the location service
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

        //get the best location-provider that matches a certain criteria
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        //set to provider
        provider = locationManager.getBestProvider(criteria, true);

        checkPermission();

        locationManager.requestLocationUpdates(provider, 4000, 1, this);

        Location location = locationManager.getLastKnownLocation(provider);

        writeToSharedPref(location.getLatitude(), location.getLongitude());

    }

    //check for permission
    public void checkPermission() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            String[] locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(locationPermissions, REQUEST_PERMISSION);
            }

        }
    }

    private void writeToSharedPref(double mLt, double mLng) {

        //setting location to Place
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("lt", (float) mLt);
        editor.putFloat("lg", (float) mLng);
        editor.commit();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "Granted! ");
            getCurrentLocation();
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
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




