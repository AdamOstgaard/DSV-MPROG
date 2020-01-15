package com.mprog.web;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Displays a web page in a web view.
 */
public class Website extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = new WebView(this);
        setContentView(webView);

        String url = getIntent().getStringExtra(MainActivity.WEBVIEW_EXTRAS_URL);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }
}
