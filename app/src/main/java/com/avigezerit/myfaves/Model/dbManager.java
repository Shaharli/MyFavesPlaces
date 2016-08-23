package com.avigezerit.myfaves.Model;

import android.content.Context;

/* * * * * * * * * * * * * PLACES DB MANAGER COMMANDS  * * * * * * * * * * * * */

public class dbManager {

    //bind to database helper
    private static com.avigezerit.myfaves.Model.dbHelper dbHelper;
    private static Context context;

    ///////////////////////////////////////// CONSTANTS /////////////////////////////////////////////

    //constants name of table
    public static final String PLACES = "places";

    //columns names
    public static final String COL_ID_0 = "_id";
    public static final String COL_NAME_1 = "Name";
    public static final String COL_LATITUDE_2 = "Latitude";
    public static final String COL_LONGITUDE_3 = "Longitude";
    public static final String COL_ADDRESS_4 = "Address";
    public static final String COL_IMAGE_5 = "Image";
    public static final String COL_ISFAV_6 = "isFav";



    ///////////////////////////////////////// CREATION /////////////////////////////////////////////

    public dbManager(Context c) {
        dbHelper = new dbHelper(c);
        this.context = c;
    }

    ///////////////////////////////////////// COMMANDS //////////////////////////////////////////////

    /*

    public static Cursor getAllDataAsCursor(String tableName) {

        Cursor c = dbHelper.getReadableDatabase().query(tableName, null, null, null, null, null, null, null);
        return c;

    }

    public Cursor getFLDataAsCursorByID(PlaceClass FL) {

        String[] whereArgs = new String[]{"" + FL.getId()};

        Cursor c = dbHelper.getReadableDatabase().query(MY_FAVES, null, COL_ID_0 + "=?", whereArgs, null, null, null);
        return c;
    }

    public static void addNewFL(PlaceClass FL) {

        ContentValues cv = new ContentValues();

        cv.put(COL_NAME_1, FL.getName());
        cv.put(COL_LATITUDE_2, FL.getLati());
        cv.put(COL_LONGITUDE_3, FL.getLongi());
        cv.put(COL_ADDRESS_4, FL.getAddress());
        cv.put(COL_IMAGE_5, FL.getImage());

        dbHelper.getWritableDatabase().insert(MY_FAVES, null, cv);

    }

    public static void addNewEntryToDB(ContentValues cv, String tableName) {

        dbHelper.getWritableDatabase().insert(tableName, null, cv);

    }

    public void updateFL(PlaceClass FL) {

        ContentValues cv = new ContentValues();

        cv.put(COL_NAME_1, FL.getName());
        cv.put(COL_LATITUDE_2, FL.getLati());
        cv.put(COL_LONGITUDE_3, FL.getLongi());
        cv.put(COL_ADDRESS_4, FL.getAddress());
        cv.put(COL_IMAGE_5, FL.getImage());

        String[] whereArgs = new String[]{"" + FL.getId()};

        dbHelper.getWritableDatabase().update(MY_FAVES, cv, COL_ID_0 + "=?", whereArgs);

    }

    public void deleteFLByID(PlaceClass FL) {

        String[] whereArgs = new String[]{"" + FL.getId()};

        dbHelper.getWritableDatabase().delete(MY_FAVES, COL_ID_0 + "=?", whereArgs);

    }

    public static String getAddressByLL(PlaceClass FL, Context c) {

        List<Address> addresses;
        String address = "default", city = "default", state = "default", country = "default";
        Geocoder gc = new Geocoder(c);
        try {
            addresses = gc.getFromLocation(FL.getLati(), FL.getLongi(), 1);
            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address + ", " + city + ", " + state + ", " + country;
    }
    */

}
