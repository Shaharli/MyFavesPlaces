package com.avigezerit.myfaves.Control;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avigezerit.myfaves.Model.dbContract;
import com.avigezerit.myfaves.R;
import com.squareup.picasso.Picasso;

/* * * * * * * * * * * * * * * * *  PLACES LIST - CURSOR ADAPTER  * * * * * * * * * * * * * * * * * */

public class myFavesCursorAdapter extends CursorAdapter {

    private static final String TAG = myFavesCursorAdapter.class.getSimpleName();

    //context holder
    Context context;
    View view;

    //instance of place obj
    PlaceClass p = new PlaceClass();

    //db related
    private dbContract.mPlacesTable dbc;
    Uri uri = dbContract.mPlacesTable.CONTENT_URI;

    //xml ref re-use
    TextView nameTV;
    TextView addressTV;
    TextView distanceTV;
    ImageView picIV;
    ImageView favIcon;

    //km or mi status
    String distanceAsString;
    public static boolean isKM = true;


    public myFavesCursorAdapter(Context context, Cursor c) {
        super(context, c);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        //inflate view
        view = LayoutInflater.from(context).inflate(R.layout.place_item, null);
        return view;

    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        //ref to xml
        nameTV = (TextView) view.findViewById(R.id.nameTV);
        addressTV = (TextView) view.findViewById(R.id.addressTV);
        distanceTV = (TextView) view.findViewById(R.id.distanceTV);
        picIV = (ImageView) view.findViewById(R.id.flPicIV);
        favIcon = (ImageView) view.findViewById(R.id.favIV);

        //binding data from cursor to view
        int mId = cursor.getInt(cursor.getColumnIndex(dbc.COL_ID_0));
        String mName = cursor.getString(cursor.getColumnIndex(dbc.COL_NAME_1));
        String mAddress = cursor.getString(cursor.getColumnIndex(dbc.COL_ADDRESS_4));
        String mImage = cursor.getString(cursor.getColumnIndex(dbc.COL_IMAGE_5));
        int mIsFav = cursor.getInt(cursor.getColumnIndex(dbc.COL_ISFAV_6));

        //extract lati & longi to double[] to get dist
        double placeLati = cursor.getDouble(cursor.getColumnIndex(dbc.COL_LATITUDE_2));
        double placeLongi = cursor.getDouble(cursor.getColumnIndex(dbc.COL_LONGITUDE_3));
        String mDistance = getDisFromMyLocationToPlace(placeLati, placeLongi);

        //fav handler
        if (mIsFav == 1) {
            favIcon.setImageResource(R.drawable.ic_favorite_filled);
        } else if (mIsFav == 0) {
            favIcon.setImageResource(R.drawable.ic_favorite_border);
        }

        //image handler
        if (mImage != null) {
            Picasso.with(context).load(mImage).into(picIV);
        } else {
            picIV.setImageResource(R.drawable.photo_ph);
        }

        p.setId(mId);
        p.setFav(false);

        //set values to views
        nameTV.setText(mName);
        addressTV.setText(mAddress);
        distanceTV.setText(mDistance);

    }

    //// DISTANCE ADAPTATION ////

    private String getDisFromMyLocationToPlace(double placeLati, double placeLongi) {

        //get my current location from shared pref
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        double mLati = pref.getFloat("lt", 0f);
        double mLongi = pref.getFloat("lg", 0f);

        //define locations
        Location myLocation = new Location("");
        myLocation.setLatitude(mLati);
        myLocation.setLongitude(mLongi);

        Location placeLocation = new Location("");
        placeLocation.setLatitude(placeLati);
        placeLocation.setLongitude(placeLongi);

        //get distance and determine km or miles
        float disAsFloat = myLocation.distanceTo(placeLocation) / 1000;
        int distance = (int) disAsFloat;

        if (!isKM) {

            distance = convertKMtoMiles(distance);
            distanceAsString = "" + distance + context.getString(R.string.dis_miles);

        } else {

            distanceAsString = "" + distance + context.getString(R.string.dis_km);
        }

        textIndicationForReallyCloseOrReallyFarPlaces(distance);

        return distanceAsString;

    }

    private void textIndicationForReallyCloseOrReallyFarPlaces(int distance) {

        if (distance > 250) {
            distanceAsString += context.getString(R.string.dis_far_extra);
        } else if (distance == 0) {
            distanceAsString += context.getString(R.string.dis_near_extra);
        }
    }

    private int convertKMtoMiles(int disKM) {
        int disMiles = (int) (disKM / 1.609344);
        return disMiles;
    }


}
