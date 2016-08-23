package com.avigezerit.myfaves.Control;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Shaharli on 18/08/2016.
 */
public class StartSearchingService extends IntentService {

    public StartSearchingService() {
        super("StartSearchingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("", "Service Activated");

        //get parameters from intent
        String searchTerm = intent.getStringExtra("term");
        boolean searchNearBy = intent.getBooleanExtra("cb", false);

        //use google access to get results
        String searchResultsToParse = GoogleAPIRequest.searchPlace(searchTerm, searchNearBy, this);

        //use helper to parse json
        JSONParserHelper parser = new JSONParserHelper();
        parser.parseJSON(searchResultsToParse, searchNearBy, getApplicationContext());
    }
}