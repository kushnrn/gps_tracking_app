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



}
