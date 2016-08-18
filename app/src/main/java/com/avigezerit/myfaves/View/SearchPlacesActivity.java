package com.avigezerit.myfaves.View;

import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.avigezerit.myfaves.Control.addToFavoritesReceiver;
import com.avigezerit.myfaves.Control.mService;
import com.avigezerit.myfaves.Control.myFavesCursorAdapter;
import com.avigezerit.myfaves.Model.dbContract;
import com.avigezerit.myfaves.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.net.URLEncoder;

public class SearchPlacesActivity extends AppCompatActivity implements View.OnClickListener, ListView.OnItemClickListener, LoaderManager.LoaderCallbacks {

    EditText searchTermET;
    String searchTermFromET;
    myFavesCursorAdapter adapter;

    Uri uri = dbContract.mPlacesTable.CONTENT_URI;
    private dbContract.mPlacesTable dbc;

    static final int SEARCH_RESULT_CURSOR_ID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_places);

        //bind to xml
        searchTermET = (EditText) findViewById(R.id.searchTermET);
        Button searchBtn = (Button) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);

        Cursor cursor = null;
        adapter = new myFavesCursorAdapter(this, cursor);
        ListView resultsLV = (ListView) findViewById(R.id.searchResultLV);
        resultsLV.setAdapter(adapter);
        resultsLV.setOnItemClickListener(this);

        //using cursor Loader to access db
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(SEARCH_RESULT_CURSOR_ID, null, this);

        //receiver for adding to favorites
        addToFavoritesReceiver receiver = new addToFavoritesReceiver();
        IntentFilter filter = new IntentFilter(dbc.ACTION_FAVED);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);

    }

    @Override
    public void onClick(View v) {

        //get text from edit text
        searchTermFromET = searchTermET.getText().toString();

        //validate search term: short(1 char) or empty
        if (searchTermFromET.length() < 2 || searchTermFromET.isEmpty()) {
            Toast.makeText(this, "Search with at least 2 letters", Toast.LENGTH_SHORT);
        }

        //encode url
        String searchTermEncoded = URLEncoder.encode(searchTermFromET);

        Log.d("SEARCH: ", searchTermEncoded);

        Toast.makeText(this, "clicked search", Toast.LENGTH_LONG).show();

        //send query to service
        Intent intent = new Intent(this, mService.class);
        intent.putExtra("term", searchTermEncoded);
        startService(intent);

    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        //use content provider to access search results
        return new CursorLoader(this, uri, null, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        Cursor c = (Cursor) data;
        c.setNotificationUri(getContentResolver(), uri);
        adapter.swapCursor(c);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        FragmentManager manager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) manager.findFragmentById(R.id.placeMF);

        mapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {

                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

                // setup map position and zoom
                LatLng position = new LatLng(40.781085, -73.967104);
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(position, 15);
                googleMap.moveCamera(update);
            }
        });

    }
}
