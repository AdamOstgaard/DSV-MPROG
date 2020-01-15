package com.mprog.sendsms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_SMS_PERMISSION = 201;
    private String[] permissions = {Manifest.permission.SEND_SMS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Sends a Requests sms permissions and sends a sms.
     * @param view sender
     */
    public void sendSms(View view){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, REQUEST_SMS_PERMISSION);
            return;
        }

        SmsManager smsManager = SmsManager.getDefault();

        // get sms information.
        TextView toView = findViewById(R.id.toTextInput);
        TextView messageView = findViewById(R.id.messageTextInput);

        smsManager.sendTextMessage(toView.getText().toString(), null, messageView.getText().toString(), null, null);
    }
}
