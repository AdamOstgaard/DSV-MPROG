package com.mprog.web;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String WEBVIEW_EXTRAS_URL = "com.mprog.web.URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Starts a new activity with the url as data.
     * @param view sender
     */
    public void onGo(View view){
        // Get the url
        TextView textView = findViewById(R.id.urlTextView);

        String url = textView.getText().toString();
        Intent intent = new Intent(this, Website.class);
        intent.putExtra(WEBVIEW_EXTRAS_URL, url);

        startActivity(intent);
    }
}
