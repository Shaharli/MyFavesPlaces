package com.avigezerit.myfaves.View;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
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
import android.widget.ListView;

import com.avigezerit.myfaves.Control.mHelpers.ContextMenuActionsHelper;
import com.avigezerit.myfaves.Control.mHelpers.LoadMapIF;
import com.avigezerit.myfaves.Control.mReceivers.ManageFavoritesReceiver;
import com.avigezerit.myfaves.Control.myFavesCursorAdapter;
import com.avigezerit.myfaves.Model.dbContract;
import com.avigezerit.myfaves.R;
import com.google.android.gms.maps.model.LatLng;

/* * * * * * * * * * * * * * * * *  SEARCH PLACES LIST - FRAGMENT  * * * * * * * * * * * * * * * * * */

public class PlaceListFragment extends Fragment implements ListView.OnItemClickListener, LoaderManager.LoaderCallbacks {

    private static final String TAG = PlaceListFragment.class.getSimpleName();

    //xml ref
    ListView resultsLV;

    //db related
    private dbContract.mPlacesTable dbc;
    private myFavesCursorAdapter adapter;
    private Uri uri = dbContract.mPlacesTable.CONTENT_URI;
    private Cursor c;
    static final int SEARCH_RESULT_CURSOR_ID = 101;

    public PlaceListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_place_list, container, false);

        //init cursor
        Cursor cursor = null;
        adapter = new myFavesCursorAdapter(getActivity(), cursor);

        //init listview
        resultsLV = (ListView) v.findViewById(R.id.searchPlacesResultLV);
        resultsLV.setAdapter(adapter);
        resultsLV.setOnItemClickListener(this);
        registerForContextMenu(resultsLV);

        //using cursor Loader to access db
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(SEARCH_RESULT_CURSOR_ID, null, this);

        //init receiver for adding places to favorites
        ManageFavoritesReceiver receiver = new ManageFavoritesReceiver();
        IntentFilter filter = new IntentFilter(dbc.ACTION_FAVED);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);

        return v;
    }

    //// OPTIONS & CONTEXT MENU ////

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.place_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        //menu helper
        ContextMenuActionsHelper ch = new ContextMenuActionsHelper(getActivity());

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
                ch.addPlaceToFavesByID(selectedPlaceId);
                break;
            case R.id.navigate:
                ch.navigateToPlaceUsingGoogleMaps(selectedPlaceLati, selectedPlaceLongi);
                break;
            case R.id.share:
                ch.sharePlaceNameAddress(selectedPlaceName, selectedPlaceAddress);
                break;
        }

        return true;
    }

    //// LOADING DATABASE METHODS ////

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        String[] whereArgs = new String[]{"" + 0};
        return new CursorLoader(getActivity(), uri, null, dbc.COL_ISFAV_6 + "=?", whereArgs, null);
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

    //// UI EVENTS ////

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

