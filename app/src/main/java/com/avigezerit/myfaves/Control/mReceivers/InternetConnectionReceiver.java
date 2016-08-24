package com.avigezerit.myfaves.Control.mReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/* * * * * * * * * * * * * * * * *  INTERNET CONNECTION - RECEIVER  * * * * * * * * * * * * * * * * * */

public class InternetConnectionReceiver extends BroadcastReceiver {

    public static final String TAG = InternetConnectionReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            Toast.makeText(context, "OH NO! Internet disconnected!", Toast.LENGTH_LONG).show();
        }
    }
}
