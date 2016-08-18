package com.avigezerit.myfaves.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* * * * * * * * * * * * * PLACES DATABASE * * * * * * * * * * * * */

public class dbHelper extends SQLiteOpenHelper {

    //constants db name & table name
    public static final String DB_PLACES_NAME = "my_places.db";
    public static final int DB_VERSION = 1;

    private dbContract.mPlacesTable dbc;

    //get context
    Context context;

    public dbHelper(Context context) {
        super(context, DB_PLACES_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //creating places table
        String createPlacesTable = "CREATE TABLE "
                + dbc.PLACES + " ("
                + dbc.COL_ID_0 + " INTEGER PRIMARY KEY, "
                + dbc.COL_NAME_1 + " TEXT, "
                + dbc.COL_LATITUDE_2 + " DECIMAL(9,6), "
                + dbc.COL_LONGITUDE_3 + " DECIMAL(9,6), "
                + dbc.COL_ADDRESS_4 + " TEXT, "
                + dbc.COL_IMAGE_5 + " TEXT, "
                + dbc.COL_ISFAV_6 + " INTEGER);";

        db.execSQL(createPlacesTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
