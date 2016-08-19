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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.avigezerit.myfaves.Control.LoadMapIF;
import com.avigezerit.myfaves.Control.addToFavoritesReceiver;
import com.avigezerit.myfaves.Control.myFavesCursorAdapter;
import com.avigezerit.myfaves.Model.dbContract;
import com.avigezerit.myfaves.R;
import com.google.android.gms.maps.model.LatLng;


public class PlaceListFragment extends Fragment implements View.OnClickListener, ListView.OnItemClickListener, LoaderManager.LoaderCallbacks {

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

        //using cursor Loader to access db
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(SEARCH_RESULT_CURSOR_ID, null, this);

        //receiver for adding to favorites
        addToFavoritesReceiver receiver = new addToFavoritesReceiver();
        IntentFilter filter = new IntentFilter(dbc.ACTION_FAVED);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);

        return v;
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
    public void onClick(View v) {

        /*
        //get text from edit text
        searchTermFromET = searchTermET.getText().toString();

        //validate search term: short(1 char) or empty
        if (searchTermFromET.length() < 2 || searchTermFromET.isEmpty()) {
            Toast.makeText(getActivity(), "Search with at least 2 letters", Toast.LENGTH_SHORT);
        }
        //encode url
        String searchTermEncoded = URLEncoder.encode(searchTermFromET);

        //send query to service
        Intent intent = new Intent(getActivity(), mService.class);
        intent.putExtra("term", searchTermEncoded);
        getActivity().startService(intent);
        */

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        c.moveToPosition(position);

        String selectedPlaceName = c.getString(c.getColumnIndex(dbc.COL_NAME_1));
        double selectedPlaceLati = c.getDouble(c.getColumnIndex(dbc.COL_LATITUDE_2));
        double selectedPlaceLongi = c.getDouble(c.getColumnIndex(dbc.COL_LONGITUDE_3));

        try {
            LoadMapIF loadMapIF = (LoadMapIF) getActivity();
            LatLng coordinates = new LatLng(selectedPlaceLati, selectedPlaceLongi);
            loadMapIF.loadMapOfSelectedPlace(selectedPlaceName, coordinates);

        } catch (ClassCastException ee) {
            Log.e("My Faves", "Please make sure the host activity implements the interface LoadMap");
        }


    }

}

