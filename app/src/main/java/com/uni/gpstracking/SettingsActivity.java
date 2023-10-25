package com.uni.gpstracking;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.gms.location.Priority;

public class SettingsActivity extends MainActivity {

    Switch sw1_locations_updates, sw1_gps;
    RadioButton rb_high_power, rb_balanced_power, rb_low_power;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        MyApplication Context = (MyApplication) getApplicationContext();

        sw_locations_updates = findViewById(R.id.sw_locations_updates);
//        sw_gps = findViewById(R.id.sw_gps);
//        sw_gps.setChecked(Context.getGpsOn());
        rb_high_power = findViewById(R.id.rb_high_power);
        rb_balanced_power = findViewById(R.id.rb_balanced_power);
        rb_low_power = findViewById(R.id.rb_low_power);

//        sw_gps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (sw_gps.isChecked()) {
//
//                    locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
//
//
//                } else {
//                    locationRequest.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY);
//
//
//                }
//
//                Context.setGpsOn(sw_gps.isChecked());
//            }
//        });

        rb_high_power.setChecked(Context.getHighAccuracyOn());
        rb_high_power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
                myApplication.setSensor("GPS, Wi-fi and cell towers");
                Context.setLowMode(false);
                Context.setHighAccuracyMode(true);
                Context.setBalancedMode(false);

                String message = "High accuracy mode is enabled!";
                int duration = Toast.LENGTH_SHORT; // or Toast.LENGTH_LONG for a longer duration
                Toast.makeText(SettingsActivity.this, message, duration).show();
            }
        });

        rb_balanced_power.setChecked(Context.getBalancedAccuracy());
        rb_balanced_power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationRequest.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY);
                myApplication.setSensor("Wi-fi and cell towers");
                Context.setLowMode(false);
                Context.setHighAccuracyMode(false);
                Context.setBalancedMode(true);

                String message = "Balanced power accuracy mode is enabled!";
                int duration = Toast.LENGTH_SHORT; // or Toast.LENGTH_LONG for a longer duration
                Toast.makeText(SettingsActivity.this, message, duration).show();

            }
        });

        rb_low_power.setChecked(Context.getLowAccuracy());
        rb_low_power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationRequest.setPriority(Priority.PRIORITY_LOW_POWER);
                myApplication.setSensor("Cell towers");
                Context.setLowMode(true);
                Context.setHighAccuracyMode(false);
                Context.setBalancedMode(false);

                String message = "Low power mode is enabled!";
                int duration = Toast.LENGTH_SHORT; // or Toast.LENGTH_LONG for a longer duration
                Toast.makeText(SettingsActivity.this, message, duration).show();

            }
        });

//        rb_no_power.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                locationRequest.setPriority(Priority.PRIORITY_NO_POWER);
//                myApplication.setSensor("No location updates (locations triggered by other apps)");
//            }
//        });

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