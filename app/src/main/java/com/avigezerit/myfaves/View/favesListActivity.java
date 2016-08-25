package com.avigezerit.myfaves.View;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avigezerit.myfaves.Control.mHelpers.ContextMenuActionsHelper;
import com.avigezerit.myfaves.Control.myFavesCursorAdapter;
import com.avigezerit.myfaves.Model.dbContract;
import com.avigezerit.myfaves.R;

/* * * * * * * * * * * * * * * * *  FAVORITE PLACES LIST - ACTIVITY  * * * * * * * * * * * * * * * * * */

public class FavesListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks, View.OnClickListener {

    private static final String TAG = FavesListActivity.class.getSimpleName();

    //db related
    private dbContract.mPlacesTable dbc;
    private myFavesCursorAdapter adapter;
    private Uri uri = dbContract.mPlacesTable.CONTENT_URI;
    private Cursor c;
    static final int FAV_CURSOR_ID = 202;

    //menu item adaptation
    Menu favMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favs_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        c = null;
        adapter = new myFavesCursorAdapter(this, c);
        ListView favesLV = (ListView) findViewById(R.id.favesLV);
        favesLV.setAdapter(adapter);
        registerForContextMenu(favesLV);

        //using cursor Loader to access db
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(FAV_CURSOR_ID, null, this);
    }

    //// INITIALIZING METHODS ////

    private void changeItemTitleInMenu() {
        MenuItem favItemMenu = favMenu.findItem(R.id.favorite);
        favItemMenu.setTitle(R.string.menu_remove_fav);
    }

    private void checkIfNoFavesYet(myFavesCursorAdapter currentAdapter) {

        if (currentAdapter.getCount() == 0) {
            findViewById(R.id.noFavesTV).setVisibility(View.VISIBLE);
        } else
            findViewById(R.id.noFavesTV).setVisibility(View.INVISIBLE);
    }

    //// LOADING DATABASE METHODS ////

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        String[] whereArgs = new String[]{"" + 1};
        return new CursorLoader(this, uri, null, dbc.COL_ISFAV_6 + "=?", whereArgs, null);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        c = (Cursor) data;
        c.setNotificationUri(getContentResolver(), uri);
        adapter.swapCursor(c);
        checkIfNoFavesYet(adapter);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    //// OPTIONS & CONTEXT MENU ////

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.place_context_menu, menu);
        favMenu = menu;
        changeItemTitleInMenu();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        //menu helper
        ContextMenuActionsHelper ch = new ContextMenuActionsHelper(FavesListActivity.this);

        //get position in list view
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        //moving the cursor to a info.position
        c.moveToPosition(info.position);

        //extract information about a place
        String selectedPlaceName = c.getString(c.getColumnIndex(dbc.COL_NAME_1));
        int selectedPlaceId = c.getInt(c.getColumnIndex(dbc.COL_ID_0));
        String selectedPlaceAddress = c.getString(c.getColumnIndex(dbc.COL_ADDRESS_4));
        double selectedPlaceLati = c.getDouble(c.getColumnIndex(dbc.COL_LATITUDE_2));
        double selectedPlaceLongi = c.getDouble(c.getColumnIndex(dbc.COL_LONGITUDE_3));

        switch (item.getItemId()) {
            case R.id.favorite:
                ch.removePlaceFromListByID(selectedPlaceId);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings:
                Intent gotoSettings = new Intent(FavesListActivity.this, SettingsActivity.class);
                startActivity(gotoSettings);
                break;
            case R.id.faves:
                Intent gotoFaves = new Intent(FavesListActivity.this, FavesListActivity.class);
                startActivity(gotoFaves);
                break;
        }

        return true;
    }

    //// UI EVENTS ////

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.fab) {
            Intent goToSearch = new Intent(FavesListActivity.this, SearchActivity.class);
            startActivity(goToSearch);
        }
    }
}
