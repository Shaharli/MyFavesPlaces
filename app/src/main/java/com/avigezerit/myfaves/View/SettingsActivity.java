package com.avigezerit.myfaves.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.avigezerit.myfaves.FavesListActivity;
import com.avigezerit.myfaves.Model.dbContract;
import com.avigezerit.myfaves.R;

/**
 * Created by Shaharli on 19/08/2016.
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    //init contract and define uri
    private dbContract.mPlacesTable dbc;
    Uri uri = dbContract.mPlacesTable.CONTENT_URI;

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
        prefUnit.setEntries(new String[]{"Kilometers", "Miles"});
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

        if (key.equals("exit")) {
            //exit
            finish();
        } else if (key.equals("clear")) {
            //clear: activate clear list listener
            Toast.makeText(this, "Faves List Cleared!", Toast.LENGTH_SHORT).show();
            //delete all faves
            String[] whereArgs = new String[]{""+1};
            getContentResolver().delete(uri, dbc.COL_ISFAV_6+"=?", whereArgs);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings:
                Intent gotoSettings = new Intent(SettingsActivity.this, SettingsActivity.class);
                startActivity(gotoSettings);
                break;
            case R.id.faves:
                Intent gotoFaves = new Intent(SettingsActivity.this, FavesListActivity.class);
                startActivity(gotoFaves);
                break;
        }

        return true;
    }


}