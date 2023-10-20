package com.uni.gpstracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    private static final int PERMISSIONS_FINE_LOCATIONS = 99;
    // references to the UI elements

//    TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_speed, tv_updates, tv_sensor, tv_address;
    Button  btn_showMap, btn_preferences;

    Switch sw_locations_updates, sw_gps;

    // Variable to remember if we are tracking location or not
    boolean updateOn = false;

    // current location
    Location currentLocation;

    // list of saved locations
    List<Location> savedLocations;

    // Google's API location service
    FusedLocationProviderClient fusedLocationProviderClient;

    // A config file for all settings related to FusedLocationProviderClient
    MyApplication myApplication = MyApplication.getInstance();
    LocationRequest locationRequest = myApplication.getLocationRequest();

    LocationCallback locationCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_showMap = findViewById(R.id.btn_showMap);
        btn_preferences = findViewById(R.id.btn_preferences);


        // set all properties of LocationRequest
        locationRequest = MyApplication.getLocationRequest();

        // event that is triggered whenever the update interval is met
        locationCallBack = new LocationCallback() {

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                // save the location
//                updateUIValues(locationResult.getLastLocation());
            }
        };


        btn_showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLocation.getAccuracy() > 40.0) {
                    Intent i = new Intent(MainActivity.this, MapsActivity.class);
                    i.putExtra("currentLocation", currentLocation);
                    startActivity(i);
                } else {
                    Intent i = new Intent(MainActivity.this, DecisionActivity.class);
                    startActivity(i);
                }

            }
        });

        btn_preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });

        updateGPS();
    } // end of onCreate method

    @Override
    protected void onResume() {
        super.onResume();
        updateGPS();
    }

    void stopLocationUpdates() {


        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
    }

    void startLocationUpdates() {
//        tv_updates.setText("Location is being tracked");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(MyApplication.getLocationRequest(), locationCallBack, null);
        }
        updateGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_FINE_LOCATIONS:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS();
                } else {
                    Toast.makeText(this, "This app requires permission to be granted to work properly", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    void updateGPS() {
        // get permissions from the user to track GPS
        // get the current location from the fused client
        // update the UI - i.e. set all properties in their associated text view items
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // user provided the permission
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // we got permissions. Put the values of location. XXX into the UI components
                    if(location != null) {
//                        updateUIValues(location);
                        currentLocation = location;
                        myApplication.setLocation(location);
                    }



                }
            });

        } else {
            // permissions not granted yet
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATIONS);
                }
    }
}


    }