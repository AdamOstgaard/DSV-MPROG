package com.mprog.recordvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_VIDEO_CAPTURE = 1;
    static final String EXTRA_MESSAGE = "com.mprog.recordvideo.VIDEO";

    private Uri videoUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Gets the uri of newly recorded video from activity result.
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = intent.getData();
        }
    }

    /**
     * Start a intent to record a video using a camera activity.
     * @param view
     */
    public void onClickRecord(View view){
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    /**
     * Playback the latest video in a VideoPlayback activity.
     * @param view
     */
    public void onClickPlayBack(View view){
        if(videoUri == null){
            return;
        }

        Intent playBackIntent = new Intent(this, VideoPlayback.class);
        playBackIntent.putExtra(EXTRA_MESSAGE, videoUri.toString());
        startActivity(playBackIntent);
    }
}
