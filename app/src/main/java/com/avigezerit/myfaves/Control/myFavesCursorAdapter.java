package com.avigezerit.myfaves.Control;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avigezerit.myfaves.Model.dbContract;
import com.avigezerit.myfaves.Model.dbm;
import com.avigezerit.myfaves.Place;
import com.avigezerit.myfaves.R;
import com.avigezerit.myfaves.favPosition;
import com.squareup.picasso.Picasso;

/* * * * * * * * * * * * * FAV LIST CURSOR ADAPTER - MAIN * * * * * * * * * * * * */

public class myFavesCursorAdapter extends CursorAdapter {

    Context context;
    View view;

    Place p = new Place();

    private dbContract.mPlacesTable dbc;

    //saveMyLocation
    private double myLocationLati;
    private double MyLocationLongi;

    //
    ImageView favIcon;
    int mIsFav;

    static long itemFavedId;


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
        TextView flNameTV = (TextView) view.findViewById(R.id.flNameTV);
        //TextView flAddressTV = (TextView) view.findViewById(R.id.flDistanceTV);
        TextView flDistanceTV = (TextView) view.findViewById(R.id.flDistanceTV);
        ImageView flPicIV = (ImageView) view.findViewById(R.id.flPicIV);
        favIcon = (ImageView) view.findViewById(R.id.favIV);
        ImageView mapIcon = (ImageView) view.findViewById(R.id.mapIV);

        //TO DONE: binding data from cursor to view
        String mName = cursor.getString(cursor.getColumnIndex(dbc.COL_NAME_1));
        String mAddress = cursor.getString(cursor.getColumnIndex(dbc.COL_ADDRESS_4));
        int mId = cursor.getInt(cursor.getColumnIndex(dbc.COL_ID_0));
        String mImage = cursor.getString(cursor.getColumnIndex(dbm.COL_IMAGE_5));
        mIsFav = cursor.getInt(cursor.getColumnIndex(dbc.COL_ISFAV_6));

        //extract lati & longi to double[]
        double flLati = cursor.getDouble(cursor.getColumnIndex(dbm.COL_LATITUDE_2));
        double flLongi = cursor.getDouble(cursor.getColumnIndex(dbm.COL_LONGITUDE_3));

        if (mIsFav == 1) {
            favIcon.setImageResource(R.drawable.ic_favorite_filled);
        } else
            favIcon.setImageResource(R.drawable.ic_favorite_border);

        if (mImage != null) {

            Picasso.with(context).load(mImage).into(flPicIV);

        } else
            flPicIV.setImageResource(R.drawable.photo_ph);

        //??defining a place

        p.setId(mId);
        p.setFav(false);

        //onClick favIcon
        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO CLEAN LOGS
                Log.d("FAVORITES: ", " clicked on fav icon");
                Log.d("ID CLICKED: ", "" + p.getId());

                addToFav(p.getId());

            }
        });

        //TODO get current location once!

        getCurrentPositionToCalcDistance();

        //define double arrays
        Double[] favPositionCoords = new Double[]{flLati, flLongi};
        Double[] currentPositionCoords = new Double[]{myLocationLati, MyLocationLongi};

        //TO DONE: calculate distance
        String mDistance = favPosition.getDisfromAtoB(currentPositionCoords, favPositionCoords);

        //TODO: load image from url path

        //TO DONE: set values
        flNameTV.setText(mName);
        flDistanceTV.setText(mAddress);

    }


    public void addToFav(int _id){

        Intent intent = new Intent(dbc.ACTION_FAVED);
        intent.putExtra("_id",_id);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


    private void getCurrentPositionToCalcDistance() {

        favPosition currentPosition = new favPosition();
        currentPosition.setContext(context);
        Double[] coords = currentPosition.getPositionOfMyLocation();

        myLocationLati = coords[0];
        MyLocationLongi = coords[1];

    }
}
