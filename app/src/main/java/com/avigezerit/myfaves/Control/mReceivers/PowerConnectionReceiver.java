package com.avigezerit.myfaves.Control.mReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

import com.avigezerit.myfaves.R;

/* * * * * * * * * * * * * * * * *  POWER CONNECTION - RECEIVER  * * * * * * * * * * * * * * * * * */


public class PowerConnectionReceiver extends BroadcastReceiver {

    private static final String TAG = PowerConnectionReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        if (plugged == BatteryManager.BATTERY_PLUGGED_AC) {

            Toast.makeText(context, R.string.charging_alert, Toast.LENGTH_SHORT).show();
        }
    }
}
