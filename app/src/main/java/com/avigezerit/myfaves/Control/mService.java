package com.avigezerit.myfaves.Control;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Shaharli on 18/08/2016.
 */
public class mService extends IntentService {

    public mService() {
        super("mService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("", "Service Activated");

        //use google access to get results
        String searchTerm = intent.getStringExtra("term");
        String searchResultsToParse = GoogleAPIRequest.searchPlace(searchTerm, this);

        //use helper to parse json
        JSONParserHelper parser = new JSONParserHelper();
        parser.parseJSON(searchResultsToParse, getApplicationContext());
    }
}
