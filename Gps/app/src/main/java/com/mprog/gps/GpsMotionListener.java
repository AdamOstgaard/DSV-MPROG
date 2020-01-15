package com.mprog.gps;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * A listener for GPs motion
 */
public class GpsMotionListener implements LocationListener {
    private static final String TAG = "MotionListener";
    private final Context context;

    /**
     * Creates a new GpsMotionListener instance with the provided context
     * @param context to be used for showing toasts.
     */
    public GpsMotionListener(Context context) {
        this.context = context;
    }

    /**
     * Shows a toast on every new recorded Gps Movement.
     * @param loc The location data of the Gps.
     */
    @Override
    public void onLocationChanged(Location loc) {
        Toast.makeText(
                this.context,
                "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                        + loc.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}