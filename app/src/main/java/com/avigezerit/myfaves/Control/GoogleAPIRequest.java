package com.avigezerit.myfaves.Control;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/* * * * * * * * * * * * * GOOGLE PLACES API REQUEST * * * * * * * * * * * * */

public class GoogleAPIRequest {

    private final static String SEARCH_API_KEY = "key=AIzaSyB2eNueotW9eTU2scgoiGd-KjpvRfWmKHM";

    private final static String SEARCH_PLACE_PREFIX = "https://maps.googleapis.com/maps/api/place/";
    private final static String SEARCH_PLACE_TYPE = "textsearch/json?";
    private final static String SEARCH_PHOTO_TYPE = "photo?maxwidth=400";
    private final static String SEARCH_PHOTO_REF = "photoreference=";
    private final static String SEARCH_PLACE_SENSOR = "sensor=false";
    private final static String SEARCH_PLACE_QUERY = "query=";
    private final static String SEARCH_PLACE_CURRENT_LOCATION = "location=";
    private final static String SEARCH_PLACE_RADIUS = "radius=";

    public static String getPhotoUrl(String photo_ref) {

        String photoUrl = SEARCH_PLACE_PREFIX
                + SEARCH_PHOTO_TYPE
                + "&" + SEARCH_API_KEY
                + "&" + SEARCH_PHOTO_REF + photo_ref;

        return photoUrl;

    }

    @NonNull
    public static String searchPlace(String searchTerm, boolean nearBy, Context context) {


        //TODO Add near me location??

        //build query
        String query = SEARCH_PLACE_PREFIX
                + SEARCH_PLACE_TYPE
                + SEARCH_API_KEY
                + "&" + SEARCH_PLACE_SENSOR
                + "&" + SEARCH_PLACE_QUERY + searchTerm;


        if (nearBy) {

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            double mLati = pref.getFloat("lt", 0f);
            double mLongi = pref.getFloat("lg", 0f);
            int radius = pref.getInt("rs", 500);

            //build query
            query +=  "&" + SEARCH_PLACE_CURRENT_LOCATION + mLati + "," + mLongi
                    + "&" +  SEARCH_PLACE_RADIUS + radius;
        }

        Log.d("", query);

        return connectAndDownload(query);
    }

    public static String connectAndDownload(String query) {

        BufferedReader input = null;
        HttpURLConnection connection = null;
        StringBuilder response = new StringBuilder();

        try {
            //create a url:
            URL url = new URL(query);
            //create a connection and open it:
            connection = (HttpURLConnection) url.openConnection();

            //status check:
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                //connection not good - return.
            }

            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            //go over the input, line by line
            String line = "";
            while ((line = input.readLine()) != null) {
                //append it to a StringBuilder to hold the
                //resulting string
                response.append(line + "\n");
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    //must close the reader
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                //must disconnect the connection
                connection.disconnect();
            }
        }

        return response.toString();
    }
}
