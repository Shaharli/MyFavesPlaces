package com.avigezerit.myfaves.Control;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Shaharli on 18/08/2016.
 */
public interface LoadMapIF {

    public void loadMapOfSelectedPlace(String placeName, LatLng placeCoordinates);

}
