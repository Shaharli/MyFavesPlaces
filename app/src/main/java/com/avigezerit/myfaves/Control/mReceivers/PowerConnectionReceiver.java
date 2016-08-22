package com.avigezerit.myfaves.Control.mReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Shaharli on 20/08/2016.
 */
public class PowerConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            Toast.makeText(context, "Charging...!", Toast.LENGTH_SHORT).show();
        } else {
            intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED);
            Toast.makeText(context, "OH NO! You're not charging anymore!", Toast.LENGTH_SHORT).show();
        }
    }
}
