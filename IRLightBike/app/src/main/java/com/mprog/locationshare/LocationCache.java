package com.mprog.locationshare;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores locations on internal storage to make them persistent throughout sessions.
 */
public class LocationCache {
    private static final String CACHE_FILE = "loc_cache";
    private final Context context;

    /**
     * Initialize a new LocationCache instance
     * @param context the context to be used for saving and loading locations.
     */
    public LocationCache(Context context) {
        this.context = context;
    }

    /**
     * Save location to internal storage.
     * @param location location to be persisted.
     */
    public void saveLocation(SerializableUserLocation location){
        try(OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(CACHE_FILE, Context.MODE_APPEND))) {
            try(BufferedWriter bWriter = new BufferedWriter(outputStreamWriter)){
                bWriter.append(location.toString());
                bWriter.newLine();
                bWriter.flush();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load all locations from internal storage.
     * @return A list containing locations from internal storage
     */
    public List<SerializableUserLocation> getLocations(){
        ArrayList<SerializableUserLocation> locations = new ArrayList<>();

        try(InputStream inputStream = context.openFileInput(CACHE_FILE);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream)){
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line;

                while ((line = reader.readLine()) != null){
                    if(line.length() <= 0){
                        continue;
                    }
                    SerializableUserLocation loc = SerializableUserLocation.parse(line);
                    locations.add(loc);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Error occurred when opening raw file for reading.
            }
        } catch (FileNotFoundException e){
            // no locations.
        } catch (IOException e) {
            e.printStackTrace();
        }

        return locations;
    }
}
