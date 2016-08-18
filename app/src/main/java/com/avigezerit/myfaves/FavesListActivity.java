package com.avigezerit.myfaves;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.avigezerit.myfaves.Control.myFavesCursorAdapter;
import com.avigezerit.myfaves.Model.dbContract;
import com.avigezerit.myfaves.View.SearchActivity;

/* * * * * * * * * * * * * FAVORITE PLACES LIST ACTIVITY - MAIN * * * * * * * * * * * * */

public class FavesListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    myFavesCursorAdapter adapter;
    Uri uri = dbContract.mPlacesTable.CONTENT_URI;
    private dbContract.mPlacesTable dbc;

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

        //TODO Show only faves

        Cursor cursor = null;
        adapter = new myFavesCursorAdapter(this, cursor);
        ListView favesLV = (ListView) findViewById(R.id.favesLV);
        favesLV.setAdapter(adapter);

        //using cursor Loader to access db
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(FAV_CURSOR_ID, null, this);

    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        String[] whereArgs = new String[]{""+1};
        return new CursorLoader(this, uri, null, dbc.COL_ISFAV_6+"=?", whereArgs, null);
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.fav_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.navigate:
                //TODO navigate
                break;
            case R.id.share:
                //Share Intent
                break;
        }

        return true;
    }
}