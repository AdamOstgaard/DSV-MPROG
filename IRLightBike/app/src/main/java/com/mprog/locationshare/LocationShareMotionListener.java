package com.mprog.locationshare;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Listens for location updates to send server.
 */
public class LocationShareMotionListener implements LocationListener {
    private final Context context;
    private final TcpClient client;
    private String name;

    public LocationShareMotionListener(Context context, TcpClient client, String name){
        this.context = context;
        this.client = client;
        this.name = name;
    }

    /**
     * Handles gps updates and sends them to the server.
     * @param loc the new location update to handle
     */
    @Override
    public void onLocationChanged(Location loc) {
        if(!client.isConnected()){
            return;
        }

        SerializableUserLocation serializableUserLocation =
                new SerializableUserLocation(this.name, System.currentTimeMillis(), (float) loc.getLatitude(), (float)loc.getLongitude());

        client.sendMessage(serializableUserLocation.toString());

        Toast.makeText(
                this.context,
                "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                        + loc.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}