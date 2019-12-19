package com.mprog.audiorecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private String recordingFileName = null;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    private MediaRecorder recorder = null;
    private MediaPlayer player = null;

    private boolean isRecording = false;
    private boolean isPlaying = false;

    private Button recordButton = null;
    private Button playButton = null;

    private boolean permissionToRecordAccepted = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                startRecording();
                break;
        }
        if (!permissionToRecordAccepted) finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordingFileName = getExternalCacheDir().getAbsolutePath();
        recordingFileName += "/audiorecordtest.3gp";

        recordButton = findViewById(R.id.recordButton);
        playButton = findViewById(R.id.playButton);
    }

    public void recordButtonOnClick(View view){
        if(isRecording){
            stopRecording();
            return;
        }

        startRecording();
    }

    public void playButtonClick(View view){
        if(isPlaying){
            stopPlaying();
            return;
        }

        startPlaying();
    }

    private void stopRecording(){
        isRecording = false;
        recorder.stop();
        recorder.release();
        recorder = null;
        setRecordButtonText();
    }

    private void setRecordButtonText(){
        recordButton.setText(isRecording ? R.string.click_to_stop_record : R.string.click_to_record);
    }

    private void setPlayButtonText(){
        playButton.setText(isPlaying ? R.string.click_to_stop_playback : R.string.click_to_play);
    }

    private void startRecording(){
        if(!permissionToRecordAccepted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
            return;
        }

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordingFileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        recorder.start();
        isRecording = true;

        setRecordButtonText();
    }

    private void startPlaying() {
        player = new MediaPlayer();

        try {
            player.setDataSource(recordingFileName);
            player.prepare();
            player.start();
            isPlaying = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        setPlayButtonText();
    }

    private void stopPlaying() {
        player.release();
        player = null;
        isPlaying = false;

        setPlayButtonText();
    }
}
