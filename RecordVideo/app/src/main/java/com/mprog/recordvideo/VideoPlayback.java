package com.mprog.recordvideo;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.VideoView;

public class VideoPlayback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_playback);

        VideoView playbackView = findViewById(R.id.playbackView);

        String playbackPath = getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE);

        playbackView.setVideoPath(playbackPath);

        playbackView.start();
    }
}
