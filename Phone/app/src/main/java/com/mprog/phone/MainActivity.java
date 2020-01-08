package com.mprog.phone;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CALL_PERMISSION = 441;
    private static final int REQUEST_CALL_LOG_PERMISSION = 200;
    private String[] permissions = {Manifest.permission.READ_CALL_LOG};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CALL_LOG_PERMISSION);
        super.onCreate(savedInstanceState);
    }

    private void startApp() {
        setContentView(R.layout.activity_main);

        RecyclerView rvCallLog = findViewById(R.id.rvCalls);

        CallLogEntryAdapter adapter = new CallLogEntryAdapter(new CallLogManager(this), this);
        rvCallLog.setAdapter(adapter);
        rvCallLog.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_LOG_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startApp();
                }
            }
        }
    }
}
