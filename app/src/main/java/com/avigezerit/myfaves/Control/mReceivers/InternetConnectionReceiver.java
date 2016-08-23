package com.avigezerit.myfaves.Control.mReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/* * * * * * * * * * * * * * * * *  INTERNET CONNECTION - RECEIVER  * * * * * * * * * * * * * * * * * */

public class InternetConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //TODO make it work

        if (isNetworkAvailable(context)) {
            Toast.makeText(context, "connect to the internet", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(context, "Connection faild", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
