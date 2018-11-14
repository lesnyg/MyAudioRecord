package com.example.edu.myaudiorecord;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button play,stop,record;
    MediaRecorder myAudioRecoder;
    String outputFile=null;
    final int REQUEST_CODE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permission = ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_CODE);
        }


        record = findViewById(R.id.buttonRecord);
        record.setOnClickListener(this);
        stop = findViewById(R.id.buttonStop);
        stop.setOnClickListener(this);
        play = findViewById(R.id.buttonPlay);
        play.setOnClickListener(this);

        stop.setEnabled(false);
        play.setEnabled(false);
        outputFile = getExternalFilesDir(null).getAbsolutePath()+"/recording.3gp";
        myAudioRecoder = new MediaRecorder();
        myAudioRecoder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecoder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecoder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecoder.setOutputFile(outputFile);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE :
                if(grantResults.length > 0 ||
                        grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.i("", "Permission has been granted by user");
                }
        }
    }

    @Override
    public void onClick(View v) {
//        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_AUDIO_PRO)){
//            Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_SEARCH);
//            startActivityForResult(intent, 201);
//        }
        switch (v.getId()) {
            case R.id.buttonRecord:
                try {
                    myAudioRecoder.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myAudioRecoder.start();
                record.setEnabled(false);
                stop.setEnabled(true);
                break;
            case R.id.buttonStop:
                stop.setEnabled(false);
                play.setEnabled(true);
                myAudioRecoder.stop();
                myAudioRecoder.release();
                myAudioRecoder=null;
                break;
            case R.id.buttonPlay:
                MediaPlayer m =new MediaPlayer();
                try {
                    m.setDataSource(outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                m.start();
                break;
        }
    }
}
