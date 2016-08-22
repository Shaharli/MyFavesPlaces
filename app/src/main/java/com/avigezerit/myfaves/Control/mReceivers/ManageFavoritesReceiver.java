package com.avigezerit.myfaves.Control.mReceivers;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.avigezerit.myfaves.Model.dbContract;
import com.avigezerit.myfaves.Model.dbm;

/**
 * Created by Shaharli on 18/08/2016.
 */
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
        id = intent.getIntExtra("_id", -1);

        if (id != -1) {
            // CLICKED ICON
            String[] whereArgs = new String[]{"" + id};
            c = context.getContentResolver().query(uri, null, dbc.COL_ID_0 + "=?", whereArgs, null);
            c.moveToNext();

        } else {
            // CLICKED CONTEXT MENU
            c = context.getContentResolver().query(uri, null, null, null, null);
            id = c.getInt(c.getColumnIndex(dbc.COL_ID_0));
        }

        String[] whereArgs = new String[]{"" + id};

        if (intent.getAction() == dbc.ACTION_FAVED) {

            cv.put(dbm.COL_ISFAV_6, 1);

            context.getContentResolver().update(uri, cv, dbc.COL_ID_0 + "=?", whereArgs);


        } else if (intent.getAction() == dbc.ACTION_FAVED) {

            context.getContentResolver().delete(uri, dbc.COL_ID_0 + "=?", whereArgs);

        }

        context.getContentResolver().notifyChange(uri, null);
        c.close();

        /*check if is currently fav
        int isFav = c.getInt(c.getColumnIndex(dbc.COL_ISFAV_6));

        if (isFav == 1) {

            //myFavesCursorAdapter.changeFavOption(0);
        } else {

            //myFavesCursorAdapter.changeFavOption(1);
        }
        */


    }
}
