package com.avigezerit.myfaves.Control;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avigezerit.myfaves.Model.dbContract;
import com.avigezerit.myfaves.Model.dbm;
import com.avigezerit.myfaves.R;
import com.squareup.picasso.Picasso;

/* * * * * * * * * * * * * FAV LIST CURSOR ADAPTER - MAIN * * * * * * * * * * * * */

public class myFavesCursorAdapter extends CursorAdapter {

    private static final String TAG = myFavesCursorAdapter.class.getSimpleName();

    Context context;
    View view;

    Place p = new Place();

    private dbContract.mPlacesTable dbc;
    Uri uri = dbContract.mPlacesTable.CONTENT_URI;

    //saveMyLocation
    private double myLocationLati;
    private double myLocationLongi;

    //
    static ImageView favIcon;
    int mIsFav;


    public myFavesCursorAdapter(Context context, Cursor c) {
        super(context, c);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        //TO DONE: inflate view
        view = LayoutInflater.from(context).inflate(R.layout.place_item, null);
        return view;

    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        //TO DONE: ref to xml
        TextView nameTV = (TextView) view.findViewById(R.id.nameTV);
        TextView addressTV = (TextView) view.findViewById(R.id.addressTV);
        TextView distanceTV = (TextView) view.findViewById(R.id.distanceTV);
        ImageView flPicIV = (ImageView) view.findViewById(R.id.flPicIV);
        favIcon = (ImageView) view.findViewById(R.id.favIV);

        //TO DONE: binding data from cursor to view
        String mName = cursor.getString(cursor.getColumnIndex(dbc.COL_NAME_1));
        String mAddress = cursor.getString(cursor.getColumnIndex(dbc.COL_ADDRESS_4));
        int mId = cursor.getInt(cursor.getColumnIndex(dbc.COL_ID_0));
        String mImage = cursor.getString(cursor.getColumnIndex(dbm.COL_IMAGE_5));
        mIsFav = cursor.getInt(cursor.getColumnIndex(dbc.COL_ISFAV_6));

        //extract lati & longi to double[]
        double placeLati = cursor.getDouble(cursor.getColumnIndex(dbm.COL_LATITUDE_2));
        double placeLongi = cursor.getDouble(cursor.getColumnIndex(dbm.COL_LONGITUDE_3));

        if (mIsFav == 1) {
            favIcon.setImageResource(R.drawable.ic_favorite_filled);
        } else if (mIsFav == 0) {
            favIcon.setImageResource(R.drawable.ic_favorite_border);
        }

        //changeFavOption(mIsFav);

        if (mImage != null) {

            Picasso.with(context).load(mImage).into(flPicIV);

        } else
            flPicIV.setImageResource(R.drawable.photo_ph);

        //??defining a place


        String mDistance = getDisfromMyLocationToPlace(placeLati, placeLongi);

        p.setId(mId);
        p.setFav(false);
        //onClick favIcon
        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addToFav(p.getId());
            }
        });

        //set values
        nameTV.setText(mName);
        addressTV.setText(mAddress);
        distanceTV.setText(mDistance);

    }


    public void addToFav(int _id) {

        Intent intent = new Intent(dbc.ACTION_FAVED);
        intent.putExtra("_id", _id);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


    private String getDisfromMyLocationToPlace(double placeLati, double placeLongi) {

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

        float distanceInMeters = myLocation.distanceTo(placeLocation);

        return "" + distanceInMeters;

    }
}
