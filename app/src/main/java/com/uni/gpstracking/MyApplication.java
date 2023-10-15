package com.uni.gpstracking;
import android.app.Application;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application{
    private static MyApplication singleton;
    public Boolean location_updated = true;
    public Boolean gps_on = false;

    private List<Location> myLocations;

    public List<Location> getMyLocations() {
        return myLocations;
    }

    public void setMyLocations(List<Location> myLocations) {
        this.myLocations = myLocations;
    }

    public MyApplication getInstance() {
        return singleton;
    }

    public void onCreate() {
        super.onCreate();
        singleton = this;
        myLocations = new ArrayList<>();
    }

    public Boolean getLocationUpdate() {
        return location_updated;
    }

    public void setLocationUpdate(Boolean location_updated1) {
        location_updated = location_updated1;
    }

    public Boolean getGpsOn() {
        return gps_on;
    }

    public void setGpsOn(Boolean gps_on1) {
        gps_on = gps_on1;
    }

}
