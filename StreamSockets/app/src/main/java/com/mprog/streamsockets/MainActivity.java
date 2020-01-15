package com.mprog.streamsockets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple chgat client that connects to
 */
public class MainActivity extends AppCompatActivity implements TcpMessageListener {
    final private List<String> messages;
    private TcpClient connection;
    private ArrayAdapter<String> adapter;
    private static final String SERVER_IP = "atlas.dsv.su.se";

    public MainActivity() {
        this.messages = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView chatHistory = findViewById(R.id.chatHistory);

        // create connection
        connection = new TcpClient(SERVER_IP, 9494);

        try {
            connection.Connect();
            connection.startReadMessageLoop();
            connection.addListener(this);
        }catch (IOException e) {
            e.printStackTrace();
        }

        // create adapter for message log.
        adapter = new ArrayAdapter<>(this, R.layout.listview_item, R.id.textView, messages);

        chatHistory.setAdapter(adapter);
    }

    /**
     * Handle click and send message
     * @param view
     */
    public void onSendClick(View view){
        TextView textView = findViewById(R.id.editText);

        String text = textView.getText().toString();

        connection.sendMessage(text);
    }

    /**
     * Handles incoming messages from the server.
     * @param message incoming message
     */
    @Override
    public void handleMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.add(message);
            }
        });
    }
}
