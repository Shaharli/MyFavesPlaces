package com.avigezerit.myfaves.Model;

import android.net.Uri;

/**
 * Created by Shaharli on 16/08/2016.
 */
public class dbContract {

    public static class mPlacesTable {

        //authority
        public static final String DB = "com.avigezerit.myfaves";

        //name of table
        public static final String PLACES = "places";

        //version
        public static final int DB_VERSION = 1;

        //uri
        public static final Uri CONTENT_URI = Uri.parse("content://"+DB+"/"+PLACES);

        //columns names
        public static final String COL_ID_0 = "_id";
        public static final String COL_NAME_1 = "Name";
        public static final String COL_LATITUDE_2 = "Latitude";
        public static final String COL_LONGITUDE_3 = "Longitude";
        public static final String COL_ADDRESS_4 = "Address";
        public static final String COL_IMAGE_5 = "Image";
        public static final String COL_ISFAV_6 = "isFav";

        //add to fave receiver action
        public static final String ACTION_FAVED = "com.avigezerit.myfaves.FAVED";

        //remove from fave receiver action
        public static final String ACTION_UNFAVED = "com.avigezerit.myfaves.UNFAVED";

    }

}
