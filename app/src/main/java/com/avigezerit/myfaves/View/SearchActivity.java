package com.avigezerit.myfaves.View;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avigezerit.myfaves.Control.LoadMapIF;
import com.avigezerit.myfaves.Control.mService;
import com.avigezerit.myfaves.FavesListActivity;
import com.avigezerit.myfaves.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URLEncoder;

public class SearchActivity extends AppCompatActivity implements LoadMapIF, View.OnClickListener {

    //fragments
    FragmentManager manager;
    FragmentTransaction transaction;

    //xml ref
    EditText searchTermET;
    String searchTermFromET;
    Button startSearchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //define related frags
        PlaceListFragment list = new PlaceListFragment();

        manager = getFragmentManager();
        transaction = manager.beginTransaction();

        //check orientation
        if (isLanscapeMode()){
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

    }

    public boolean isLanscapeMode() {

        boolean isLanscapeMode = false;

        int orientation = getResources().getConfiguration().orientation;

        switch (orientation){
            case  Configuration.ORIENTATION_LANDSCAPE:
                Log.d("my orient" ,"ORIENTATION_LANDSCAPE");
                isLanscapeMode = true;
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                Log.d("my orient" ,"ORIENTATION_PORTRAIT");
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

        if (isLanscapeMode()){
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
                Intent gotoFaves = new Intent(SearchActivity.this, FavesListActivity.class);
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
        }
        else  Toast.makeText(this, "Search with at least 2 letters", Toast.LENGTH_SHORT);
    }
}
