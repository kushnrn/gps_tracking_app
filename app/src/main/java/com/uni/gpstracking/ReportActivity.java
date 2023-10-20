package com.uni.gpstracking;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class ReportActivity extends MainActivity {
    TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_speed, tv_sensor, tv_address;

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


        if (myApplication.location_updated ){
            currentLocation = MyApplication.getLocation();

            tv_lat.setText(String.valueOf(currentLocation.getLatitude()));
            tv_lon.setText(String.valueOf(currentLocation.getLongitude()));
            tv_accuracy.setText(String.valueOf(currentLocation.getAccuracy()));
            tv_sensor.setText((myApplication.getSensor()));

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

            Geocoder geocoder = new Geocoder(ReportActivity.this);

            try {
                List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                tv_address.setText((addresses.get(0).getAddressLine(0)));
            }
            catch (Exception e){
                tv_address.setText("Unable to get street address");
            }


        }
        else {

            tv_lat.setText("Not tracking location");
            tv_lon.setText("Not tracking location");
            tv_speed.setText("Not tracking location");
            tv_address.setText("Not tracking location");
            tv_altitude.setText("Not tracking location");
            tv_sensor.setText("Not tracking location");
        }



    }
}