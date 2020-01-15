package com.mprog.youtube;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private LinearLayout layout = null;
    private YoutubeVideo[] videos = {
            new YoutubeVideo("Never Gonna Give You Up", "https://www.youtube.com/watch?v=dQw4w9WgXcQ"),
            new YoutubeVideo("I'm Gonna Be", "https://www.youtube.com/watch?v=tbNlMtqrYS0"),
            new YoutubeVideo("Africa", "https://www.youtube.com/watch?v=FTQbiNvZqaY")
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = new LinearLayout(this );
        populateVideos();
        setContentView(layout);
    }

    /**
     * Create buttons for all videos.
     */
    private void populateVideos(){
        for (YoutubeVideo video : videos) {
            addVideoButton(video);
        }
    }

    /**
     * Add a new button that opens the provided YouTube video on press.
     *
     * @param video Corresponding youtube video.
     */
    private void addVideoButton(YoutubeVideo video){
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video.url));

        Button videoButton = new Button(this);

        videoButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        videoButton.setText(video.name);

        layout.addView(videoButton);
    }

    /**
     * simple representation of a youtube video.
     */
    private class YoutubeVideo {
        private String name;
        private String url;

        private YoutubeVideo(String name, String url){
            this.name = name;
            this.url = url;
        }
    }
}
