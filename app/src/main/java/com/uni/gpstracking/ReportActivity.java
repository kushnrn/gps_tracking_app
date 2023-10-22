package com.uni.gpstracking;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class ReportActivity extends MainActivity {
    TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_speed, tv_sensor, tv_address, tv_postal_code, tv_locality, tv_country;
    ImageButton reload;
    private static final int PERMISSIONS_FINE_LOCATIONS = 99;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_speed = findViewById(R.id.tv_speed);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_address = findViewById(R.id.tv_address);
        tv_postal_code = findViewById(R.id.tv_postal_code);
        tv_country = findViewById(R.id.tv_country);
        tv_locality = findViewById(R.id.tv_locality);

        reload = findViewById(R.id.reload);

        if (myApplication.location_updated ){
            currentLocation = MyApplication.getLocation();

            tv_lat.setText(String.valueOf(currentLocation.getLatitude()));
            tv_lon.setText(String.valueOf(currentLocation.getLongitude()));
            tv_accuracy.setText(String.valueOf(currentLocation.getAccuracy()));
            if (myApplication.getSensor() == null) {
                tv_sensor.setText(currentLocation.getProvider());
            } else {
                tv_sensor.setText((myApplication.getSensor()));
            }

//            if(myApplication.getGpsOn()) {
//                tv_sensor.setText("Using GPS sensors");
//            } else {
//                tv_sensor.setText("Using Towers + WIFI");
//            }

            if(currentLocation.hasAltitude()) {
                Double alt = currentLocation.getAltitude();
                Log.e("alt", String.valueOf(alt));
                tv_altitude.setText(String.valueOf(currentLocation.getAltitude()));
            } else {
                tv_altitude.setText("Not available");
            }

            if(currentLocation.hasSpeed()) {
                tv_speed.setText(String.valueOf(currentLocation.getSpeed()));
            } else {
                tv_speed.setText("Not available");
            }

            geocoder = new Geocoder(ReportActivity.this);
            performGeocodingInBackground();

//            try {
//                List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
//                tv_address.setText((addresses.get(0).getAddressLine(0)));
//            }
//            catch (Exception e){
//                Log.e("address error", e.getMessage(), e);
//                tv_address.setText("Unable to get street address");
//            }

            reload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_address.setText(myApplication.getAddress());
                    tv_postal_code.setText(myApplication.getZipCode());
                    tv_locality.setText(myApplication.getLocality());
                    tv_country.setText(myApplication.getCountry());
                }
            });


        }
        else {

            tv_lat.setText("Not tracking location");
            tv_lon.setText("Not tracking location");
            tv_speed.setText("Not tracking location");
            tv_address.setText("Not tracking location");
            tv_altitude.setText("Not tracking location");
            tv_sensor.setText("Not tracking location");
            tv_postal_code.setText("Not tracking location");
            tv_locality.setText("Not tracking location");
            tv_country.setText("Not tracking location");
            tv_accuracy.setText("Not tracking location");
        }

    }

    private void performGeocodingInBackground() {
        new AsyncTask<Void, Void, List<Address>>() {
            @Override
            protected List<Address> doInBackground(Void... params) {
                try {
                    return geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<Address> addresses) {
                if (addresses != null && !addresses.isEmpty()) {
//                    tv_address.setText((addresses.get(0).getAddressLine(0)));
//                    myApplication.setAddress((addresses.get(0).getAddressLine(0)));
                    myApplication.setAddress(addresses.get(0).getThoroughfare() +", " + addresses.get(0).getSubThoroughfare());
                    myApplication.setZipCode(addresses.get(0).getPostalCode());
                    myApplication.setLocality(addresses.get(0).getLocality());
                    myApplication.setCountry(addresses.get(0).getCountryName());

                }
            }
        }.execute();
    }



}