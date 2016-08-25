package com.avigezerit.myfaves.Control.mReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.avigezerit.myfaves.R;

/* * * * * * * * * * * * * * * * *  POWER CONNECTION - RECEIVER  * * * * * * * * * * * * * * * * * */


public class PowerConnectionReceiver extends BroadcastReceiver {

    private static final String TAG = PowerConnectionReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, R.string.charging_alert, Toast.LENGTH_SHORT).show();
    }
}
