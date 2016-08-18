package com.avigezerit.myfaves.Control;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.avigezerit.myfaves.Model.dbContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* * * * * * * * * * * * * JSON PARSER - USED BY SERVICE * * * * * * * * * * * * */


public class JSONParserHelper {

    private dbContract.mPlacesTable dbc;

    public static final String TAG = JSONParserHelper.class.getSimpleName();

    public void parseJSON(String searchResultsToParse, Context cx) {

        Log.d(TAG, "JSON Parser activated");
        Log.d(TAG, searchResultsToParse);


        //init cv and define uri
        Uri uri = dbContract.mPlacesTable.CONTENT_URI;

        //delete all current results beside favs
        //String[] whereArgs = new String[]{""+0};
        cx.getContentResolver().delete(uri, null, null);

        try {

            //extracting all the data using JSON objects
            JSONObject resultsMainObject = new JSONObject(searchResultsToParse);

            //getting JSON Array from main object
            JSONArray resultsArray = resultsMainObject.getJSONArray("results");

            Log.d(TAG, "Array Results Size: " + resultsArray.length());

            //check if result ok
            String resultsStatus = resultsMainObject.getString("status");
            if (!resultsStatus.equals("OK")) {

                //Log.d(TAG, "Error with results");

                //TODO broadcast result not ok;
            }

            //extracting every result data using JSON array
            for (int i = 0; i < resultsArray.length(); i++) {

                //extracting name and address from every result
                JSONObject resultObject = resultsArray.getJSONObject(i);
                String mName = resultObject.getString("name");
                String mAddress = resultObject.getString("formatted_address");

                //get lati longi
                JSONObject resultObjectGeometry = resultObject.getJSONObject("geometry");
                JSONObject resultObjectLocation = resultObjectGeometry.getJSONObject("location");
                double mLati = resultObjectLocation.getDouble("lat");
                double mLongi = resultObjectLocation.getDouble("lng");

                //TODO IMAGE


                String mImage = null;

                try {

                    JSONArray photosArray = resultObject.getJSONArray("photos");
                    Log.d(TAG, "photosArray: " + photosArray.length());
                    JSONObject photoObject = photosArray.getJSONObject(0);
                    String photo_ref = photoObject.getString("photo_reference");
                    mImage = GoogleAPIRequest.getPhotoUrl(photo_ref);

                } catch (JSONException e) {
                }


                //print to log
                Log.d(TAG, "This is a place: " + mName + " " + mAddress + " " + mLati + " " + mLongi);

                ContentValues cv = new ContentValues();

                //using contact values to insert db
                cv.put(dbc.COL_NAME_1, mName);
                cv.put(dbc.COL_LATITUDE_2, mLati);
                cv.put(dbc.COL_LONGITUDE_3, mLongi);
                cv.put(dbc.COL_ADDRESS_4, mAddress);
                cv.put(dbc.COL_IMAGE_5, mImage);
                cv.put(dbc.COL_ISFAV_6, 0);

                cx.getContentResolver().insert(uri, cv);
                cx.getContentResolver().notifyChange(uri, null);

            }

            Log.d(TAG, "Finished Json");


        } catch (JSONException e)

        {
            Log.d(TAG, e.getMessage());
        }

    }


    private void addImage() {


    }
}
