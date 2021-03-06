package com.avigezerit.myfaves.View;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.avigezerit.myfaves.Control.mHelpers.LoadMapIF;
import com.avigezerit.myfaves.Control.mHelpers.StartSearchingService;
import com.avigezerit.myfaves.Control.mHelpers.getLocationHelper;
import com.avigezerit.myfaves.Control.mReceivers.InternetConnectionReceiver;
import com.avigezerit.myfaves.Control.mReceivers.PowerConnectionReceiver;
import com.avigezerit.myfaves.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URLEncoder;

/* * * * * * * * * * * * * * * * *  SEARCH PLACES ACTIVITY - HOSTING FRAGMENTS  * * * * * * * * * * * * * * * * * */

public class SearchActivity extends AppCompatActivity implements LoadMapIF, View.OnClickListener, CheckBox.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = SearchActivity.class.getSimpleName();

    //frags obj related
    FragmentManager manager;
    FragmentTransaction transaction;

    //xml ref
    EditText searchTermET;
    String searchTermFromET;
    Button startSearchBtn;
    TextInputLayout searchTIL;

    //cb near by
    CheckBox cb;
    SeekBar sb;
    LinearLayout rsbLL;
    int rs = 1;
    getLocationHelper helper;

    //charging & internet alert
    PowerConnectionReceiver powerReceiver;
    InternetConnectionReceiver internetReceiver;
    boolean receiversRegistered = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //define related frags
        PlaceListFragment list = new PlaceListFragment();

        //init frags obj
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

        //bind to ET
        searchTermET = (EditText) findViewById(R.id.searchPlaceET);
        searchTIL = (TextInputLayout) findViewById(R.id.searchTIL);

        //bind to btn
        startSearchBtn = (Button) findViewById(R.id.activateSearchBtn);
        startSearchBtn.setOnClickListener(this);

        //bind cb
        cb = (CheckBox) findViewById(R.id.nearByCB);
        cb.setOnCheckedChangeListener(this);

        //bind to sb
        sb = (SeekBar) findViewById(R.id.radiusSB);
        sb.setOnSeekBarChangeListener(this);

        //set sb params and initially hide layout
        initRadiusSB();
        rsbLL = (LinearLayout) findViewById(R.id.rsbLL);
        rsbLL.setVisibility(View.GONE);

        //register powerReceiver
        initReceivers();
    }

    //// INITIALIZING METHODS ////

    private void initReceivers() {

        //power connection
        powerReceiver = new PowerConnectionReceiver();
        IntentFilter powerConnectedIF = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(powerReceiver, powerConnectedIF);

        //internet connection
        internetReceiver = new InternetConnectionReceiver();
        IntentFilter internetDisconnectedIF = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(internetReceiver, internetDisconnectedIF);

        receiversRegistered = true;

    }

    private void initRadiusSB() {
        sb.setMax(1000);
        sb.setProgress(100);
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

    private void writeToSharedPref(int rs) {
        //saving radius for app-level use
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("rs", rs);
        editor.commit();

    }

    //// LOAD MAP FRAG INTERFACE ////

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

    //// OPTIONS MENU ////

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
                Intent gotoFaves = new Intent(SearchActivity.this, FavesListActivity.class);
                startActivity(gotoFaves);
                break;
        }

        return true;
    }

    //// ACTIVITY EVENTS ////

    @Override
    public void onBackPressed() {

        manager.popBackStack();

        if (manager.getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        //unregister receiver to avoid harassment!
        if (receiversRegistered && powerReceiver!=null && internetReceiver!=null) {
            Log.d(TAG, "unregistered power & internet");
            unregisterReceiver(powerReceiver);
            unregisterReceiver(internetReceiver);
            powerReceiver=null;
            powerReceiver=null;
        }

        super.onPause();
    }

    //// UI EVENTS ////

    @Override
    public void onClick(View v) {

        //get text from edit text
        searchTermFromET = searchTermET.getText().toString();

        boolean STisValid = false;

        if (searchTermFromET.length() >= 2 && !searchTermFromET.isEmpty()) {
            STisValid = true;
        }

        //validate search term: short(1 char) or empty
        if (STisValid) {

            searchTIL.setErrorEnabled(false);

            //encode url
            String searchTermEncoded = URLEncoder.encode(searchTermFromET);

            Intent intent = new Intent(this, StartSearchingService.class);

            //send query params to service
            intent.putExtra("term", searchTermEncoded);

            //get cb status
            if (cb.isChecked()) {
                intent.putExtra("cb", true);
            } else {
                intent.putExtra("cb", false);
            }

            //go to search
            startService(intent);

            Toast.makeText(this, R.string.fetching_results_alert, Toast.LENGTH_SHORT).show();

        } else {
            searchTIL.setError(getString(R.string.search_error));
        }


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int stepSize = 100;
        progress = (Math.round(progress/stepSize))*stepSize;
        seekBar.setProgress(progress);
        rs = progress;
        Log.d(TAG, "" + progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        Toast.makeText(this, getString(R.string.radius_set_alert) + rs + getString(R.string.meters), Toast.LENGTH_SHORT).show();
        writeToSharedPref(rs);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (!isChecked) {
            rsbLL.setVisibility(View.GONE);
        } else if (isChecked) {
            //get location
            helper = new getLocationHelper();
            helper.setContext(this, this);
            checkPermission();
            //helper.getCurrentLocation();
            rsbLL.setVisibility(View.VISIBLE);
        }

    }

    //// PERMISSIONS - M ////

    public void checkPermission() {

        //check and request for location permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                String[] locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

                ActivityCompat.requestPermissions(this, locationPermissions, helper.REQUEST_LOCATION_PERMISSION);

            } else helper.getCurrentLocation();
        } else helper.getCurrentLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Granted! ");
            helper.getCurrentLocation();
        }


    }
}




