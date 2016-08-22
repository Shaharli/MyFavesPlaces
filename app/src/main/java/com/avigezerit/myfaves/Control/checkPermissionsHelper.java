package com.avigezerit.myfaves.Control;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Shaharli on 12/08/2016.
 */
public class checkPermissionsHelper extends FragmentActivity {

    private static final String TAG = checkPermissionsHelper.class.getSimpleName();

    Context context;

    static Activity activity;

    public static boolean canAccessLocation = false;

    //reqcode
    private static final int REQUEST_GPS_PERMISSION = 101;

    public checkPermissionsHelper(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void checkPermission() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            String[] gpsPermmisions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

            ActivityCompat.requestPermissions(activity, gpsPermmisions, REQUEST_GPS_PERMISSION);

        }
    }


    public void alarmUser() {
        Toast.makeText(context, "This App Requires Location Permission", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case REQUEST_GPS_PERMISSION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(TAG, "Granted! ");
                    canAccessLocation = true;
                    break;

                } else {
                    alarmUser();
                }
            }
        }
    }
}


