package com.avigezerit.myfaves.Control.mReceivers;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import com.avigezerit.myfaves.Model.dbContract;
import com.avigezerit.myfaves.Model.dbManager;

/* * * * * * * * * * * * * * * * *  FAVORITES MANAGER - RECEIVER  * * * * * * * * * * * * * * * * * */

public class ManageFavoritesReceiver extends BroadcastReceiver {

    private static final String TAG = ManageFavoritesReceiver.class.getSimpleName();

    private dbContract.mPlacesTable dbc;

    @Override
    public void onReceive(Context context, Intent intent) {

        //prepare to update db
        Uri uri = dbContract.mPlacesTable.CONTENT_URI;
        ContentValues cv = new ContentValues();
        Cursor c = null;
        int id = intent.getIntExtra("_id", 0);
        String act = intent.getStringExtra("action");

        //get the id of the place that will be added to fave
        c = context.getContentResolver().query(uri, null, null, null, null);
        c.moveToFirst();
        //id = c.getInt(c.getColumnIndex(dbc.COL_ID_0));
        String[] whereArgs = new String[]{"" + id};

        if (act.equals("add")) {

            cv.put(dbManager.COL_ISFAV_6, 1);
            Toast.makeText(context, "Place added to Faves", Toast.LENGTH_SHORT).show();


        } else if (act.equals("remove")) {

            cv.put(dbManager.COL_ISFAV_6, 0);
            Toast.makeText(context, "Place removed from Faves", Toast.LENGTH_SHORT).show();

        }

        context.getContentResolver().update(uri, cv, dbc.COL_ID_0 + "=?", whereArgs);
        context.getContentResolver().notifyChange(uri, null);
        c.close();

    }
}
