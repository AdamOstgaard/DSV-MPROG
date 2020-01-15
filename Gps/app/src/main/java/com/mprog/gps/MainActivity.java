package com.mprog.gps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 541;

    /**
     * Handles the permissions results.
     * @param requestCode identifier for the permission request.
     * @param permissions permission(s) requested
     * @param grantResults An array of result codes determining if the permissions were granted or not.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_LOCATION_PERMISSION:
                startGps();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startGps();
    }

    /**
     * Creates and starts a new motion listener if we have location permission, otherwise asks for permissions and returns.
     */
    private void startGps(){
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }
        // Get location manager
        LocationManager locationManager = (LocationManager)
                getSystemService(LOCATION_SERVICE);

        // Create motionlistener
        LocationListener locationListener = new GpsMotionListener(this);

        // start listener
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 5, locationListener);
    }
}
