package com.uni.gpstracking;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.gms.location.Priority;

public class SettingsActivity extends MainActivity {

    Switch sw1_locations_updates, sw1_gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);


        MyApplication Context = (MyApplication) getApplicationContext();


        sw_locations_updates = findViewById(R.id.sw_locations_updates);
        sw_gps = findViewById(R.id.sw_gps);
        sw_gps.setChecked(Context.getGpsOn());
        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sw_gps.isChecked()) {
                    locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
                    tv_sensor.setText("Using GPS sensors");
                } else {
                    locationRequest.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY);
                    tv_sensor.setText("Using Towers + WIFI");

                }
                Context.setGpsOn(sw_gps.isChecked());
            }
        });
        sw_locations_updates.setChecked(Context.getLocationUpdate());
        sw_locations_updates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sw_locations_updates.isChecked()) {
                    // turn on location tracking
                    startLocationUpdates();
                } else {
                    // turn off tracking
                    stopLocationUpdates();
                }
                Context.setLocationUpdate(sw_locations_updates.isChecked());
            }
        });
        updateGPS();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the counter value when the activity is about to be paused or destroyed
        outState.putBoolean("sw_locations_updates", sw_locations_updates.isChecked());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            boolean switchState = savedInstanceState.getBoolean("sw_locations_updates");
            sw_locations_updates.setChecked(switchState);
        }
    }
}