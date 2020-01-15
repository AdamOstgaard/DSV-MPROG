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

    /**
     * Handles the permissions results.
     * @param requestCode identifier for the permission request.
     * @param permissions permission(s) requested
     * @param grantResults An array of result codes determining if the permissions were granted or not.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                // start recording if permission was granted.
                startRecording();
                break;
        }
        if (!permissionToRecordAccepted) finish();
    }

    /**
     * Initialize components for recording.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordingFileName = getExternalCacheDir().getAbsolutePath();
        recordingFileName += "/audiorecordtest.3gp";

        recordButton = findViewById(R.id.recordButton);
        playButton = findViewById(R.id.playButton);
    }

    /**
     * Handle click for recording button.
     * @param view sender
     */
    public void recordButtonOnClick(View view){
        if(isRecording){
            stopRecording();
            return;
        }

        startRecording();
    }

    /**
     * Start playback of recording.
     * @param view sender
     */
    public void playButtonClick(View view){
        if(isPlaying){
            stopPlaying();
            return;
        }

        startPlaying();
    }

    /**
     * Stops the recording
     */
    private void stopRecording(){
        isRecording = false;
        recorder.stop();
        recorder.release();
        recorder = null;
        setRecordButtonText();
    }

    /**
     * Sets the text of the recording toggle button to the correct state.
     */
    private void setRecordButtonText(){
        recordButton.setText(isRecording ? R.string.click_to_stop_record : R.string.click_to_record);
    }

    /**
     * Sets the text of the playback toggle button to the correct state.
     */
    private void setPlayButtonText(){
        playButton.setText(isPlaying ? R.string.click_to_stop_playback : R.string.click_to_play);
    }

    /**
     * Starts a new recording if permission is accepted otherwise ask for permission.
     */
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

    /**
     * Starts playback of latest recording.
     */
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

    /**
     * Stops playback of latest recording.
     */
    private void stopPlaying() {
        player.release();
        player = null;
        isPlaying = false;

        setPlayButtonText();
    }
}
