package com.mprog.notifications;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "test";
    private int notifactionId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        setContentView(R.layout.activity_main);
    }

    public void showToast(View view){
        CharSequence toastText = "Hi I'm a Toast!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, toastText, duration);
        toast.show();
    }

    public void showDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        AlertDialog.Builder alertBuilder = builder.setMessage("Hi I'm a dialog")
                .setTitle("Dialog").setPositiveButton(R.string.close_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Noop
                    }
                });
        AlertDialog dialog = alertBuilder.create();

        dialog.show();
    }

    public void showNotification(View view){
        Intent fullScreenIntent = new Intent(this, MainActivity.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Notification")
                .setContentText("Hi I'm a Notification!").setSmallIcon(R.drawable.ic_stat_test)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(fullScreenPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(notifactionId++, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_channel_name);
            String description = getString(R.string.notification_channel_name);

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
