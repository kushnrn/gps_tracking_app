package com.uni.gpstracking;
import android.app.Application;
import android.location.Location;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.Priority;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application{
    private static MyApplication singleton;
    public Boolean location_updated = true;
    public Boolean gps_on = false;
    public Boolean high_accuracy_on = true;
    public Boolean balanced_accuracy_on = false;
    public Boolean low_power_on = false;
    public String sensor;

    private List<Location> myLocations;
    private static LocationRequest locationRequest;
    private static Location location;

    public static Location getLocation(){
        return location;
    }

    public void setLocation(Location locationUpdated) {
        location = locationUpdated;
    }


    public List<Location> getMyLocations() {
        return myLocations;
    }

    public void setMyLocations(List<Location> myLocations) {
        this.myLocations = myLocations;
    }

    public static MyApplication getInstance() {
        return singleton;
    }

    public void onCreate() {
        super.onCreate();
        singleton = this;
        myLocations = new ArrayList<>();

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(1000 * 30)
                .setFastestInterval(1000 * 5);
    }

    public Boolean getLocationUpdate() {
        return location_updated;
    }

    public void setLocationUpdate(Boolean location_updated1) {
        location_updated = location_updated1;
    }

    public Boolean getHighAccuracyOn() {
        return high_accuracy_on;
    }

    public void setHighAccuracyMode(Boolean highAccuracy) {
        high_accuracy_on = highAccuracy;
    }

    public Boolean getBalancedAccuracy() {
        return balanced_accuracy_on;
    }

    public void setBalancedMode(Boolean balancedAccuracy) {
        balanced_accuracy_on = balancedAccuracy;
    }

    public Boolean getLowAccuracy() {
        return low_power_on;
    }

    public void setLowMode(Boolean lowAccuracy) {
        low_power_on = lowAccuracy;
    }

    public static void setLocationRequest(LocationRequest location_request){
        locationRequest = location_request;
    }
    public static LocationRequest getLocationRequest(){
        return locationRequest;
    }
    public Boolean getGpsOn() {
        return gps_on;
    }

    public void setGpsOn(Boolean gps_on1) {
        gps_on = gps_on1;
    }

    public String getSensor() { return sensor; }

    public void setSensor(String newSensor) { sensor = newSensor; };

}
