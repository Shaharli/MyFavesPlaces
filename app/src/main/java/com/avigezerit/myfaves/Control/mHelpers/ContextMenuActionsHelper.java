package com.avigezerit.myfaves.Control.mHelpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import com.avigezerit.myfaves.Model.dbContract;
import com.avigezerit.myfaves.R;

/* * * * * * * * * * * * * * * * *  PLACE ACTIONS - HELPER  * * * * * * * * * * * * * * * * * */

public class ContextMenuActionsHelper {

    Context context;

    //db related
    private dbContract.mPlacesTable dbc;

    public ContextMenuActionsHelper(Context context) {
        this.context = context;
    }

    public void sharePlaceNameAddress(String selectedPlaceName, String selectedPlaceAddress) {

        Intent sharePlace = new Intent();
        sharePlace.setAction(Intent.ACTION_SEND);
        sharePlace.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_check_out) + selectedPlaceName + "!" + context.getString(R.string.share_located_on) + selectedPlaceAddress);
        sharePlace.setType("text/plain");
        context.startActivity(sharePlace);
    }

    public void navigateToPlaceUsingGoogleMaps(double selectedPlaceLati, double selectedPlaceLongi) {

        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + selectedPlaceLati + ", " + selectedPlaceLongi);
        Intent navigateToPlace = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        navigateToPlace.setPackage("com.google.android.apps.maps");
        context.startActivity(navigateToPlace);
    }

    public void removePlaceFromListByID(int selectedPlaceId) {

        Intent intent = new Intent(dbc.ACTION_FAVED);
        intent.putExtra("action", "remove");
        intent.putExtra("_id", selectedPlaceId);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void addPlaceToFavesByID(int selectedPlaceId) {

        Intent intent = new Intent(dbc.ACTION_FAVED);
        intent.putExtra("action", "add");
        intent.putExtra("_id", selectedPlaceId);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

}
