package com.mprog.networkinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setNetworkInfoText();
    }

    /**
     * Populates the textview with network information.
     */
    private void setNetworkInfoText(){
        TextView text = findViewById(R.id.networkInfo);

        NetworkInfo networkInfo = getNetworkInfo();

        if(networkInfo != null){
            String infoText = "Connected to " + getNetworkType(networkInfo);

            text.setText(infoText);
        }else {
            text.setText("Not connected!");
        }
    }

    /**
     * Extracts the connection type from NetworkInfo object.
     * @param info Object containing network information
     * @return a string representation of the connection type.
     */
    private String getNetworkType(NetworkInfo info){
        switch (info.getType()) {
            case ConnectivityManager.TYPE_WIFI:
                return "wifi";
            case ConnectivityManager.TYPE_MOBILE:
                return "cellular";
        }
        return "unknown";
    }

    /**
     * Get information about the curtrent network of the device.
     * @return
     */
    private NetworkInfo getNetworkInfo(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork;
    }
}
