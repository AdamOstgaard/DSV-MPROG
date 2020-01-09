package com.mprog.networkinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setNetworkInforText();
    }

    private void setNetworkInforText(){
        TextView text = findViewById(R.id.networkInfo);

        NetworkInfo networkInfo = getNetworkInfo();

        if(networkInfo != null){
            String infoText = "Connected to " + getNetworkType(networkInfo);

            text.setText(infoText);
        }else {
            text.setText("Not connected!");
        }
    }

    private String getNetworkType(NetworkInfo info){
        switch (info.getType()) {
            case ConnectivityManager.TYPE_WIFI:
                return "wifi";
            case ConnectivityManager.TYPE_MOBILE:
                return "cellular";
        }
        return "unknown";
    }

    private NetworkInfo getNetworkInfo(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork;
    }
}
