package com.avigezerit.myfaves.View;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.avigezerit.myfaves.R;

/**
 * Created by Shaharli on 19/08/2016.
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private static final String TAG = "PrefsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref);

        // -- get current saved prefs
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String unit = prefs.getString("unit", "km");

        // find the prefs (by key)
        ListPreference prefUnit = (ListPreference) findPreference("unit");
        Preference prefClear = findPreference("clear");
        Preference prefExit = findPreference("exit");

        //set the list values:
        prefUnit.setEntries(new String[]{"km", "ml"});
        prefUnit.setEntryValues(new String[]{"Kilometers", "Miles"});

        //set the summaries:
        prefUnit.setSummary(unit);

        // SET CHANGE LISTENERS
        prefUnit.setOnPreferenceChangeListener(this);

        // SET CLICK LISTENERS
        prefClear.setOnPreferenceClickListener(this);
        prefExit.setOnPreferenceClickListener(this);

    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {


        //get the key:
        String key = preference.getKey();
        Log.d(TAG, "changed " + key + " -> " + newValue);

        if (key.equals("unit")) {
            //unit: change the summary
            preference.setSummary((String) newValue);
        }

        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {


        String key = preference.getKey();

        if (key.equals("unit")) {
            //clear: activate clear list listener
            Toast.makeText(this, "Clicked Clear", Toast.LENGTH_SHORT).show();
            //TODO Clear List
        } else if (key.equals("exit")) {
            //exit
            finish();
        }


        return true;
    }


}