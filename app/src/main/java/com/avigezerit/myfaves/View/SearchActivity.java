package com.avigezerit.myfaves.View;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avigezerit.myfaves.Control.LoadMapIF;
import com.avigezerit.myfaves.Control.checkPermissionsHelper;
import com.avigezerit.myfaves.Control.mService;
import com.avigezerit.myfaves.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URLEncoder;

public class SearchActivity extends AppCompatActivity implements LoadMapIF, View.OnClickListener, android.location.LocationListener {

    private static final String TAG = SearchActivity.class.getSimpleName();

    //fragments
    FragmentManager manager;
    FragmentTransaction transaction;

    //xml ref
    EditText searchTermET;
    String searchTermFromET;
    Button startSearchBtn;

    //location

    checkPermissionsHelper helper;
    private static final int REQUEST_GPS_PERMISSION = 101;
    private LocationManager locationManager;
    private String provider;
    Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //define related frags
        PlaceListFragment list = new PlaceListFragment();

        manager = getFragmentManager();
        transaction = manager.beginTransaction();

        //check orientation
        if (isLanscapeMode()) {
            MapFragment map = new MapFragment();
            transaction.add(R.id.MapFragContainerLL, map);
            transaction.add(R.id.listFragContainerLL, list);
        } else {
            transaction.add(R.id.containerSearchLL, list);
        }
        transaction.commit();

        searchTermET = (EditText) findViewById(R.id.searchPlaceET);
        startSearchBtn = (Button) findViewById(R.id.activateSearchBtn);
        startSearchBtn.setOnClickListener(this);


        //requestPermission();

        //requestLocationUpdates();

    }

    public boolean isLanscapeMode() {

        boolean isLanscapeMode = false;

        int orientation = getResources().getConfiguration().orientation;

        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                Log.d("my orient", "ORIENTATION_LANDSCAPE");
                isLanscapeMode = true;
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                Log.d("my orient", "ORIENTATION_PORTRAIT");
                isLanscapeMode = false;
                break;
        }

        return isLanscapeMode;
    }

    @Override
    public void loadMapOfSelectedPlace(final String placeName, final LatLng placeCoordinates) {

        manager = getFragmentManager();
        transaction = manager.beginTransaction();

        MapFragment mapFragment = new MapFragment();
        mapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {

                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                googleMap.addMarker(new MarkerOptions().title("PLACE").position(placeCoordinates));

                // setup map position and zoom
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(placeCoordinates, 15);
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                googleMap.moveCamera(update);
            }
        });

        if (isLanscapeMode()) {
            transaction.replace(R.id.MapFragContainerLL, mapFragment).commit();
        } else {
            transaction.replace(R.id.containerSearchLL, mapFragment, "map").addToBackStack("loadMap").commit();
        }
    }

    @Override
    public void onBackPressed() {

        manager.popBackStack();

        if (manager.getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings:
                Intent gotoSettings = new Intent(SearchActivity.this, SettingsActivity.class);
                startActivity(gotoSettings);
                break;
            case R.id.faves:
                Intent gotoFaves = new Intent(SearchActivity.this, favesListActivity.class);
                startActivity(gotoFaves);
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        //get text from edit text
        searchTermFromET = searchTermET.getText().toString();

        //validate search term: short(1 char) or empty
        if (searchTermFromET.length() > 2 || !searchTermFromET.equals("")) {

            //encode url
            String searchTermEncoded = URLEncoder.encode(searchTermFromET);

            //send query to service
            Intent intent = new Intent(this, mService.class);
            intent.putExtra("term", searchTermEncoded);
            startService(intent);
        } else Toast.makeText(this, "Search with at least 2 letters", Toast.LENGTH_SHORT);
    }


    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_GPS_PERMISSION);
        }
    }

    private void requestLocationUpdates() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);

            //get the best location-provider that matches a certain criteria
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_HIGH);

            //set to provider
            provider = locationManager.getBestProvider(criteria, true);

            Log.d(TAG, "Provider: " + provider);

            locationManager.requestLocationUpdates(provider, 40000, 0, this);

        }

    }

    private void removeLocationUpdates() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_GPS_PERMISSION);
        }
        //locationManager.removeUpdates(this);
        Log.d(TAG, "Removed updates");

    }

    @Override
    protected void onPause() {
        super.onPause();
        //removeLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {

        mLocation = location;
        Log.d(TAG, "lati: " + location.getLatitude() + " longi: " + location.getLongitude());

        //setting location to Place
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lati", String.valueOf(location.getLatitude()));
        editor.putString("longi", String.valueOf(location.getLongitude()));
        editor.apply();
        editor.commit();

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_GPS_PERMISSION) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Granted! ");
                requestLocationUpdates();

            } else {
                //helper.alarmUser();
            }
        }
    }
}


