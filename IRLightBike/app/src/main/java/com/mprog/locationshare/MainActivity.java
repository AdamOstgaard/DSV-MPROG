package com.mprog.locationshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Scdreen for selecting a name to use in the application.
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Create basic resources for the activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Start tha application if the username is valid.
     * @param view
     */
    public void start(View view){
        TextView textView = findViewById(R.id.usernameTextView);

        String userId = textView.getText().toString();

        // Make sure there are no special characters that would interfere with serialization.
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(userId);
        boolean b = m.find();

        if(b){
            Toast.makeText(
                    this,
                    "Display name may only contain characters a-z and 0-9",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent(this, UserMap.class);

        i.putExtra(Intent.EXTRA_USER, userId);

        startActivity(i);
    }
}
