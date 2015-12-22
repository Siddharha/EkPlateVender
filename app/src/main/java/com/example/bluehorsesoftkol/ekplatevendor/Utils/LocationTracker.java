package com.example.bluehorsesoftkol.ekplatevendor.Utils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by Rahul on 9/29/2015.
 */
public class LocationTracker {

    private Context context;
    private LocationManager locationManager;
    private Location location;
    private Criteria criteria;
    private double latitude=0.0, longitude=0.0;
    private String provider;

    public LocationTracker(Context context){
        this.context = context;
        criteria = new Criteria();
        setUpLocationSetting();
    }

    private void setUpLocationSetting(){
        this.locationManager =
                (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(criteria, false);
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            location = locationManager.getLastKnownLocation(provider);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        } else {
            System.out.println("Location not available.");
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
