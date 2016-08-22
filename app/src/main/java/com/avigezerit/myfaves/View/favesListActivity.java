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

import com.avigezerit.myfaves.Control.myFavesCursorAdapter;
import com.avigezerit.myfaves.Model.dbContract;
import com.avigezerit.myfaves.R;

/* * * * * * * * * * * * * FAVORITE PLACES LIST ACTIVITY - MAIN * * * * * * * * * * * * */

public class FavesListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private static final String TAG = FavesListActivity.class.getSimpleName();

    myFavesCursorAdapter adapter;
    Uri uri = dbContract.mPlacesTable.CONTENT_URI;
    private dbContract.mPlacesTable dbc;

    Cursor c;

    static final int FAV_CURSOR_ID = 202;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favs_list);

        //ask location

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToSearch = new Intent(FavesListActivity.this, SearchActivity.class);
                startActivity(goToSearch);
            }
        });

        c = null;
        adapter = new myFavesCursorAdapter(this, c);
        ListView favesLV = (ListView) findViewById(R.id.favesLV);
        favesLV.setAdapter(adapter);
        registerForContextMenu(favesLV);

        //using cursor Loader to access db
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(FAV_CURSOR_ID, null, this);

    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        String[] whereArgs = new String[]{"" + 1};
        return new CursorLoader(this, uri, null, dbc.COL_ISFAV_6 + "=?", whereArgs, null);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        c = (Cursor) data;
        adapter.swapCursor(c);
        c.setNotificationUri(getContentResolver(), uri);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.place_context_menu, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.removeItem(R.id.favorite);
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

        switch (item.getItemId()) {

            /*
            case R.id.favorite:
                //TODO FIX BUG REMOVE FAV
                Intent intent = new Intent(dbc.ACTION_UNFAVED);
                intent.putExtra("_id", selectedPlaceId);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                break;
            */
            case R.id.navigate:
                //navigate with google maps intent
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + selectedPlaceLati + ", " + selectedPlaceLongi);
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
}
