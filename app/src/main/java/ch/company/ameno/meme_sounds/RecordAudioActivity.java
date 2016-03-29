package ch.company.ameno.meme_sounds;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;

public class RecordAudioActivity extends Activity {

    private MediaRecorder mMediaRecorder;
    private MediaPlayer mPlayer;
    private String outputFile = null;
    private ImageButton btnRecordStart;
    private ImageButton btnPlay;
    private ImageButton btnSave;
    private TextView mRecordStatus;
    private boolean isRecording = false;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        mRecordStatus = (TextView) findViewById(R.id.tv_recordStatus);
        mRecordStatus.setText("Record status: Waiting for input");

        // store it to sd card
        outputFile =  getFilesDir().getAbsolutePath() + "/sampleAudioRecord.3gpp";

        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mMediaRecorder.setOutputFile(outputFile);

        btnRecordStart = (ImageButton)findViewById(R.id.record);
        btnRecordStart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                record(v);
            }
        });

        btnPlay = (ImageButton)findViewById(R.id.play);
        btnPlay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                play(v);
            }
        });

        btnSave = (ImageButton)findViewById(R.id.saveButton);
        btnSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                save(v);
            }
        });
    }

    public void record(View view){
        if (!isRecording) {
            try {
                mMediaRecorder.prepare();
                mMediaRecorder.start();
                isRecording = true;
                btnPlay.setEnabled(false);
                btnSave.setEnabled(false);

            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecordStatus.setText("Record status: Recording!");
        }

        else {
            try {
                mMediaRecorder.stop();
                mRecordStatus.setText("Record status: Stopped recording");
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            isRecording = false;
            btnPlay.setEnabled(true);
            btnSave.setEnabled(true);
        }


    }

    public void play(View view) {
        if (!isPlaying) {
            try {
                mPlayer = new MediaPlayer();
                mPlayer.setDataSource(outputFile);
                mPlayer.prepare();
                mPlayer.start();

                mRecordStatus.setText("Record status: Playing audio");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            mPlayer.stop();
            mRecordStatus.setText("Record status: Stopped playing");
        }
        mRecordStatus.setText("Record status: Waiting for input");
    }

    public void save(View view) {
        try {
            if (mPlayer != null) {
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
                mRecordStatus.setText("Record status: saved Button");


            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}