package com.avigezerit.myfaves.Control.mHelpers;

import com.google.android.gms.maps.model.LatLng;

/* * * * * * * * * * * * * MAP FRAGMENT LOADING - INTERFACE * * * * * * * * * * * * */


public interface LoadMapIF {

    public void loadMapOfSelectedPlace(String placeName, LatLng placeCoordinates);

}
