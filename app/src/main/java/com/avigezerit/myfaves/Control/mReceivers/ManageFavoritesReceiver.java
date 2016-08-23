package com.avigezerit.myfaves.Control.mReceivers;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.avigezerit.myfaves.Model.dbContract;
import com.avigezerit.myfaves.Model.dbManager;

/* * * * * * * * * * * * * * * * *  FAVORITES MANAGER - RECEIVER  * * * * * * * * * * * * * * * * * */

public class ManageFavoritesReceiver extends BroadcastReceiver {

    private static final String TAG = ManageFavoritesReceiver.class.getSimpleName();

    private dbContract.mPlacesTable dbc;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, intent.getAction());

        //prepare to update db
        Uri uri = dbContract.mPlacesTable.CONTENT_URI;
        ContentValues cv = new ContentValues();
        Cursor c = null;
        int id;

        //get the id of the place that will be added to fave
        c = context.getContentResolver().query(uri, null, null, null, null);
        id = c.getInt(c.getColumnIndex(dbc.COL_ID_0));

        String[] whereArgs = new String[]{"" + id};

        if (intent.getAction() == dbc.ACTION_FAVED) {

            cv.put(dbManager.COL_ISFAV_6, 1);

            context.getContentResolver().update(uri, cv, dbc.COL_ID_0 + "=?", whereArgs);


        } else if (intent.getAction() == dbc.ACTION_UNFAVED) {

            context.getContentResolver().delete(uri, dbc.COL_ID_0 + "=?", whereArgs);

        }

        context.getContentResolver().notifyChange(uri, null);
        c.close();

    }
}
