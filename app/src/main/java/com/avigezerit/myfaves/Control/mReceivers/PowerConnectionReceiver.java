package com.avigezerit.myfaves.Control.mReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/* * * * * * * * * * * * * * * * *  POWER CONNECTION - RECEIVER  * * * * * * * * * * * * * * * * * */


public class PowerConnectionReceiver extends BroadcastReceiver {

    private static final String TAG = PowerConnectionReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        //TODO CHECK THAT IT WORKS

        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            Log.d(TAG, "charging");
            Toast.makeText(context, "Charging...!", Toast.LENGTH_SHORT).show();


        } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            Log.d(TAG, "not charging");
            Toast.makeText(context, "OH NO! You're not charging anymore!", Toast.LENGTH_SHORT).show();
        }
    }
}
