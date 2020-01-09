package com.mprog.copypaste;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void copyClick(View view){
        TextView textView = findViewById(R.id.editText);

        copyToClipBoard("text", textView.getText().toString());
    }

    public void copyToClipBoard(String label, String text){
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);

        CharSequence toastText = "Copied to clipboard!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, toastText, duration);
        toast.show();
    }
}
