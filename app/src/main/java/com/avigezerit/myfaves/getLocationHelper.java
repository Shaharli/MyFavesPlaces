package com.avigezerit.myfaves;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/* * * * * * * * * * * * * FAV POSISITION MANAGER POJO * * * * * * * * * * * * */

public class getLocationHelper implements LocationListener {

    private static final String TAG = getLocationHelper.class.getSimpleName();

    //permission
    private static final int REQUEST_GPS_PERMISSION = 101;

    //get location
    private LocationManager locationManager;
    private String provider;

    Context context;

    private double mLati;
    private double mLongi;

    public getLocationHelper() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    //get location
    private void getCurrentLocation() {

        //get an instance of the location service
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

        //get the best location-provider that matches a certain criteria
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        //set to provider
        provider = locationManager.getBestProvider(criteria, true);


        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 40000, 0, this);

        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            //setting location to Place
            mLati = location.getLatitude();
            mLongi = location.getLongitude();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("location_lati", String.valueOf(mLati));
            editor.putString("location_longi", String.valueOf(mLongi));
            editor.commit();

            locationManager.removeUpdates(this);
        }
    }

    //check for permission
    public void checkPermission() {

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //TODO : permission Dialog
            //String[] locationPermissions = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
            //ActivityCompat.requestPermissions(activity, locationPermissions, REQUEST_PERMISSION);

        }
    }

    public Double[] getPositionOfMyLocation() {

        getCurrentLocation();

        Double[] coords = new Double[]{mLati, mLongi};

        return coords;

    }

    //TODO use api
    public String getAddressFromLL(getLocationHelper getLocationHelper){

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(getLocationHelper.getLati(), getLocationHelper.getLongi(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String country = addresses.get(0).getCountryName();

        return  address + ", " + city + ", " + country;
    }

    public static String getDisfromAtoB(Double[] currentPosition, Double[] favPosition){

        String mDistance = "";

        Location currentLocation = new Location("");
        currentLocation.setLatitude(currentPosition[0]);
        currentLocation.setLongitude(currentPosition[1]);

        Location favLocation = new Location("");
        favLocation.setLatitude(favPosition[0]);
        favLocation.setLongitude(favPosition[1]);

        int distanceInMeters = Math.round(currentLocation.distanceTo(favLocation));

        if (distanceInMeters<50){
            mDistance = "Right around the corner";
        }

        else if (distanceInMeters>200){
            mDistance = "" + distanceInMeters + "meters away";
        }

        return mDistance;
    }

    public double getLati() {
        return mLati;
    }

    public double getLongi() {
        return mLongi;
    }

    @Override
    public void onLocationChanged(Location location) {
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



