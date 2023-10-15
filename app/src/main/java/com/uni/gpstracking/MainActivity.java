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
import android.widget.ImageButton;
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

    TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_speed, tv_updates, tv_sensor, tv_address, tv_wayPointCounts;
    Button btn_newWayPoint, btn_showWayPointList, btn_showMap, btn_preferences;
//    ImageButton btn_settings;
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
    LocationRequest locationRequest;
    LocationCallback locationCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //give each UI variable a value

        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_speed = findViewById(R.id.tv_speed);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_updates = findViewById(R.id.tv_updates);
        tv_address = findViewById(R.id.tv_address);
        sw_gps = findViewById(R.id.sw_gps);
        sw_locations_updates = findViewById(R.id.sw_locations_updates);
        btn_newWayPoint = findViewById(R.id.btn_newWayPoint);
        btn_showWayPointList = findViewById(R.id.btn_showWayPointList);
        tv_wayPointCounts = findViewById(R.id.tv_countOfCrumbs);
        btn_showMap = findViewById(R.id.btn_showMap);
        btn_preferences = findViewById(R.id.btn_preferences);



        // set all properties of LocationRequest

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 1000 * DEFAULT_UPDATE_INTERVAL)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000 * FAST_UPDATE_INTERVAL)
                .build();

        // event that is triggered whenever the update interval is met
        locationCallBack = new LocationCallback() {

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                // save the location
                updateUIValues(locationResult.getLastLocation());
            }
        };

        btn_newWayPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the gps location

                //add lhe new location to the global list
                MyApplication myApplication = (MyApplication)getApplicationContext();
                savedLocations = myApplication.getMyLocations();
                savedLocations.add(currentLocation);
            }
        });

        btn_showWayPointList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ShowSavedLocationsList.class);
                startActivity(i);
            }
        });

        btn_showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });

        btn_preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });

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
            }
        });

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
            }
        });
        updateGPS();
    } // end of onCreate method

    private void stopLocationUpdates() {
        tv_updates.setText("Location is NOT being tracked");
        tv_lat.setText("Not tracking location");
        tv_lon.setText("Not tracking location");
        tv_speed.setText("Not tracking location");
        tv_address.setText("Not tracking location");
        tv_altitude.setText("Not tracking location");
        tv_sensor.setText("Not tracking location");

        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
    }

    private void startLocationUpdates() {
        tv_updates.setText("Location is being tracked");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
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

    private void updateGPS() {
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
                        updateUIValues(location);
                        currentLocation = location;
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

    private void updateUIValues(Location location) {
        // update all of the text view objects with a location
        tv_lat.setText(String.valueOf(location.getLatitude()));
        tv_lon.setText(String.valueOf(location.getLongitude()));
        tv_accuracy.setText(String.valueOf(location.getAccuracy()));

        if(location.hasAltitude()) {
            tv_altitude.setText(String.valueOf(location.getAltitude()));
        } else {
            tv_altitude.setText("Not available");
        }

        if(location.hasSpeed()) {
            tv_speed.setText(String.valueOf(location.getSpeed()));
        } else {
            tv_speed.setText("Not available");
        }

        Geocoder geocoder = new Geocoder(MainActivity.this);

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            tv_address.setText((addresses.get(0).getAddressLine(0)));
        }
        catch (Exception e){
            tv_address.setText("Unable to get street address");
        }

        MyApplication myApplication = (MyApplication)getApplicationContext();
        savedLocations = myApplication.getMyLocations();

        // show the number of waypoints saved
        tv_wayPointCounts.setText(Integer.toString(savedLocations.size()));
    }
    }