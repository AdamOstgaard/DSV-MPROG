package com.mprog.locationshare;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The main map part of the application.
 */
public class UserMap extends FragmentActivity implements OnMapReadyCallback, TcpMessageListener, GoogleMap.OnPolylineClickListener {
    private GoogleMap mMap;
    private Map<String, Polyline> userPolylines;
    private LocationCache cache;
    private TcpClient client;
    private String displayName;
    private int color;

    /**
     * Creates a new UserMap instance
     */
    public UserMap() {
        this.userPolylines = new HashMap<>();
    }

    /**
     * Create and start all necessary objects.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        color = ContextCompat.getColor(this, R.color.colorAccent);

        Intent intent = getIntent();
        displayName = intent.getStringExtra(Intent.EXTRA_USER);

        setContentView(R.layout.activity_game_map);

        this.cache = new LocationCache(this);
        this.client = startSharingClient();
        this.client.addListener(this);

        startGps(client);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Clean up resources
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.client.removeListener(this);

        try {
            this.client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startGps(TcpClient tcp){
        LocationManager locationManager = (LocationManager)
                getSystemService(LOCATION_SERVICE);

        LocationListener locationListener = new LocationShareMotionListener(this, tcp, displayName);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 541);
            return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 5, locationListener);
    }

    private TcpClient startSharingClient(){
        TcpClient connection = new TcpClient("atlas.dsv.su.se", 9494);

        connection.start();
        connection.startReadMessageLoop();

        return connection;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        loadCachedLocations();
        mMap.setOnPolylineClickListener(this);
    }

    /**
     * Saves location and adds it to the map when a new message is received.
     * @param message the newly received message
     */
    @Override
    public void handleMessage(String message) {
        final SerializableUserLocation loc;

        try {
            loc = SerializableUserLocation.parse(message);
        } catch (InvalidObjectException e) {
            e.printStackTrace();
            return;
        }

        this.cache.saveLocation(loc);

        if(mMap != null){
            addLocationToMap(loc);
        }
    }

    /**
     * get and load cached locations from previous sessions
     */
    private void loadCachedLocations(){
        List<SerializableUserLocation> locations = this.cache.getLocations();

        for (SerializableUserLocation location : locations) {
            addLocationToMap(location);
        }
    }

    /**
     * Add location to a polyline on the map.
     * @param location to be added.
     */
    private void addLocationToMap(final SerializableUserLocation location){
        final LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
        final Polyline polyline = userPolylines.get(location.getUser());

        if(polyline == null){
            // create a new polyline
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PolylineOptions polylineOptions = new PolylineOptions().add(pos).clickable(true).color(color);
                    Polyline polyline1 = mMap.addPolyline(polylineOptions);
                    polyline1.setTag(location.getUser());
                    userPolylines.put(location.getUser(), polyline1);
                }
            });
        } else {
            // add point to existing polyline
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List posList = polyline.getPoints();
                    posList.add(pos);
                    polyline.setPoints(posList);
                }
            });
        }
    }

    /**
     * handle onClick event for polyline by displaying what user it belongs to.
     * @param polyline the clicked polyline
     */
    @Override
    public void onPolylineClick(Polyline polyline) {
        Toast.makeText(
                this,
                "Polyline belongs to " + polyline.getTag().toString(),
                Toast.LENGTH_SHORT).show();
    }
}
