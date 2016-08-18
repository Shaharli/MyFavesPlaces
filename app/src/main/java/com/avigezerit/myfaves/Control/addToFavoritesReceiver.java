package com.avigezerit.myfaves.Control;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.avigezerit.myfaves.Model.dbContract;
import com.avigezerit.myfaves.Model.dbm;

/**
 * Created by Shaharli on 18/08/2016.
 */
public class addToFavoritesReceiver extends BroadcastReceiver {

    private dbContract.mPlacesTable dbc;

    @Override
    public void onReceive(Context context, Intent intent) {

        //get the id of the place that will be added to fave
        int id = intent.getIntExtra("_id", 0);

        //prepare to update db
        Uri uri = dbContract.mPlacesTable.CONTENT_URI;
        ContentValues cv = new ContentValues();


        String[] projection = new String[]{dbc.COL_ISFAV_6};
        String[] whereArgs = new String[]{"" + id};

        //check if is currently fav
        Cursor c = context.getContentResolver().query(uri, projection, dbc.COL_ID_0 + "=?", whereArgs, null);

        c.moveToNext();
        int isFav = c.getInt(c.getColumnIndex(dbc.COL_ISFAV_6));

        if (isFav == 1) {
            cv.put(dbm.COL_ISFAV_6, 0);
        } else {
            cv.put(dbm.COL_ISFAV_6, 1);
        }

        context.getContentResolver().update(uri, cv, dbc.COL_ID_0 + "=?", whereArgs);
        context.getContentResolver().notifyChange(uri, null);
    }
}
