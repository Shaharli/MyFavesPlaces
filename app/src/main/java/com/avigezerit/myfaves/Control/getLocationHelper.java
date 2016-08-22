package com.avigezerit.myfaves.Control;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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

    private boolean canAccessLocation= false;

    public getLocationHelper() {
    }

    public getLocationHelper(double mLati, double mLongi) {
        this.mLati = mLati;
        this.mLongi = mLongi;
    }

    public void setContext(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    //get location
    private void getCurrentLocation() {

        Log.d(TAG, "Getting Location");

        checkPermission();

        if (canAccessLocation){
            //get an instance of the location service
            locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

            //get the best location-provider that matches a certain criteria
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
            criteria.setPowerRequirement(Criteria.POWER_LOW);

            //set to provider
            provider = locationManager.getBestProvider(criteria, true);


            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 4000, 0, this);

            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                //setting location to favLocation
                mLati = location.getLatitude();
                mLongi = location.getLongitude();

                float mLatiFloat = (float) mLati;
                float mLongiFloat = (float) mLongi;

            /*
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("location_lati", String.valueOf(mLati));
            editor.putString("location_longi", String.valueOf(mLongi));
            editor.commit();
            */

                locationManager.removeUpdates(this);
        }
        }
    }

    //check for permission
    public void checkPermission() {

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            String[] locationPermissions = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(activity, locationPermissions, REQUEST_PERMISSION);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "Granted! ");
            canAccessLocation = true;
            getCurrentLocation();
        }

    }

    public Double[] getPositionOfMyLocation() {

        getCurrentLocation();

        Double[] coords = new Double[]{mLati, mLongi};

        Log.d(TAG, ""+mLati + " " + mLongi);

        return coords;
    }


    @Override
    public void onLocationChanged(Location location) {

        Log.d(TAG, ""+location.getLatitude() + " " + location.getLongitude());
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




