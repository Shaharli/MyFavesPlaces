package com.avigezerit.myfaves.Model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Shaharli on 16/08/2016.
 */
public class dbProvider extends ContentProvider {

    private static final String TAG = dbProvider.class.getSimpleName();

    private dbContract.mPlacesTable dbc;

    dbHelper dbh;

    @Override
    public boolean onCreate() {

        dbh = new dbHelper(getContext());

        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor = dbh.getReadableDatabase().query(dbContract.mPlacesTable.PLACES, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        //adding the given place to db
        dbh.getWritableDatabase().insert(dbContract.mPlacesTable.PLACES, null, values);
        Log.d(TAG, "DB added place : "+  values.getAsString(dbc.COL_NAME_1) + " / " + values.get(dbc.COL_ADDRESS_4));
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int rowsDeleted = dbh.getWritableDatabase().delete(dbContract.mPlacesTable.PLACES, selection, selectionArgs);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int rowsUpdated = dbh.getWritableDatabase().update(dbContract.mPlacesTable.PLACES, values, selection, selectionArgs);
        Log.d(TAG, "DB Faved : "+""+values.get(dbc.COL_ISFAV_6));
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
