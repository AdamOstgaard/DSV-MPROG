package com.mprog.sparatillfil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    private static final String PRIME_FILE = "prime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start();
    }

    private void start(){
        final Context ctx = getApplicationContext();
        new Thread(new Runnable() {
            public void run() {
                int number = loadNumber(ctx);

                while (true) {
                    number += 2;
                    if(isPrime(number)){
                        saveNumber(number, ctx);
                        setDisplayedNumber(number);
                    }
                }
            }
        }).start();
    }


    private void setDisplayedNumber(final int number){
        final TextView view = findViewById(R.id.textView);
        view.post(new Runnable() {
                      @Override
                      public void run() {
                          view.setText(String.valueOf(number));
                      }
                  });
    }

    private int loadNumber(Context ctx){
        int number = 1;


        try(InputStream inputStream = ctx.openFileInput(PRIME_FILE);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream)){
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                if(line != null) {
                    number = Integer.parseInt(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Error occurred when opening raw file for reading.
            }
        } catch (FileNotFoundException e){
            saveNumber(number, ctx);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return number;
    }

    private void saveNumber(int number, Context context){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(PRIME_FILE, Context.MODE_PRIVATE));
            outputStreamWriter.write(String.valueOf(number));
            outputStreamWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isPrime(long candidate) {
        long sqrt = (long)Math.sqrt(candidate);

        for(long i = 3; i <= sqrt; i += 2) {
            if (candidate % i == 0) {
                return false;
            }
        }

        return true;
    }
}
