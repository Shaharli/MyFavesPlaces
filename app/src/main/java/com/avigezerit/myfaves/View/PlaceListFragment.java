package com.avigezerit.myfaves.View;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.avigezerit.myfaves.Control.LoadMapIF;
import com.avigezerit.myfaves.Control.mReceivers.ManageFavoritesReceiver;
import com.avigezerit.myfaves.Control.myFavesCursorAdapter;
import com.avigezerit.myfaves.Model.dbContract;
import com.avigezerit.myfaves.R;
import com.google.android.gms.maps.model.LatLng;


public class PlaceListFragment extends Fragment implements ListView.OnItemClickListener, LoaderManager.LoaderCallbacks {

    private static final String TAG = PlaceListFragment.class.getSimpleName();

    //xml ref
    EditText searchTermET;
    String searchTermFromET;
    Button startSearchBtn;
    ListView resultsLV;

    //db related
    myFavesCursorAdapter adapter;
    Uri uri = dbContract.mPlacesTable.CONTENT_URI;
    private dbContract.mPlacesTable dbc;

    Cursor c;

    static final int SEARCH_RESULT_CURSOR_ID = 101;

    public PlaceListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_place_list, container, false);

        Cursor cursor = null;
        adapter = new myFavesCursorAdapter(getActivity(), cursor);
        resultsLV = (ListView) v.findViewById(R.id.searchPlacesResultLV);
        resultsLV.setAdapter(adapter);
        resultsLV.setOnItemClickListener(this);
        registerForContextMenu(resultsLV);

        //using cursor Loader to access db
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(SEARCH_RESULT_CURSOR_ID, null, this);

        //receiver for adding to favorites
        ManageFavoritesReceiver receiver = new ManageFavoritesReceiver();
        IntentFilter filter = new IntentFilter(dbc.ACTION_FAVED);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);


        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.place_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        //get position in list view
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        //moving the cursor to a info.position
        c.moveToPosition(info.position);

        String selectedPlaceName = c.getString(c.getColumnIndex(dbc.COL_NAME_1));
        int selectedPlaceId = c.getInt(c.getColumnIndex(dbc.COL_ID_0));
        String selectedPlaceAddress = c.getString(c.getColumnIndex(dbc.COL_ADDRESS_4));
        double selectedPlaceLati = c.getDouble(c.getColumnIndex(dbc.COL_LATITUDE_2));
        double selectedPlaceLongi = c.getDouble(c.getColumnIndex(dbc.COL_LONGITUDE_3));

        switch (item.getItemId()){

            case R.id.favorite:
                Intent intent = new Intent(dbc.ACTION_FAVED);
                intent.putExtra("_id", selectedPlaceId);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                break;
            case R.id.navigate:
                //navigate with google maps intent
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+selectedPlaceLati+", "+selectedPlaceLongi);
                Intent navigateToPlace = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                navigateToPlace.setPackage("com.google.android.apps.maps");
                startActivity(navigateToPlace);
                break;
            case R.id.share:
                //Share name and address Intent
                Intent sharePlace = new Intent();
                sharePlace.setAction(Intent.ACTION_SEND);
                sharePlace.putExtra(Intent.EXTRA_TEXT, "Check out this place I found: " + selectedPlaceName + "!" + "\nIt's located on: " + selectedPlaceAddress);
                sharePlace.setType("text/plain");
                startActivity(sharePlace);
                break;
        }

        return true;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        //selected place for map
        return new CursorLoader(getActivity(), uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        c = (Cursor) data;
        c.setNotificationUri(getActivity().getContentResolver(), uri);
        adapter.swapCursor(c);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //get position and init cursor
        c.moveToPosition(position);

        String selectedPlaceName = c.getString(c.getColumnIndex(dbc.COL_NAME_1));
        double selectedPlaceLati = c.getDouble(c.getColumnIndex(dbc.COL_LATITUDE_2));
        double selectedPlaceLongi = c.getDouble(c.getColumnIndex(dbc.COL_LONGITUDE_3));

        try {
            //load map using interface in hosting activity
            LoadMapIF loadMapIF = (LoadMapIF) getActivity();
            LatLng coordinates = new LatLng(selectedPlaceLati, selectedPlaceLongi);
            loadMapIF.loadMapOfSelectedPlace(selectedPlaceName, coordinates);

        } catch (ClassCastException ee) {
            Log.e("My Faves", "Please make sure the host activity implements the interface LoadMap");
        }


    }

}

