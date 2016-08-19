package com.avigezerit.myfaves.View;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.avigezerit.myfaves.Control.LoadMapIF;
import com.avigezerit.myfaves.FavesListActivity;
import com.avigezerit.myfaves.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SearchActivity extends AppCompatActivity implements LoadMapIF {

    FragmentManager manager;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        PlaceListFragment list = new PlaceListFragment();
        transaction.add(R.id.containerSearchLL, list);
        transaction.commit();

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

        transaction.replace(R.id.containerSearchLL, mapFragment, "map").addToBackStack("loadMap").commit();
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

        switch (item.getItemId()){
            case R.id.settings:
                Intent gotoSettings = new Intent(SearchActivity.this, SettingsActivity.class);
                startActivity(gotoSettings);
                break;
            case R.id.faves:
                //TODO faves
                Intent gotoFaves = new Intent(SearchActivity.this, FavesListActivity.class);
                startActivity(gotoFaves);
                break;
        }

        return true;
    }
}
