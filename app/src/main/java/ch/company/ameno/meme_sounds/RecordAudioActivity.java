package ch.company.ameno.meme_sounds;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class RecordAudioActivity extends Activity {

    private MediaRecorder mMediaRecorder;
    private MediaPlayer mPlayer;
    private String outputFile = null;
    private Button btnRecordStart;
    private Button btnRecordStop;
    private Button btnPlay;
    private Button btnStopPlay;
    private TextView mRecordStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        mRecordStatus = (TextView) findViewById(R.id.tv_recordStatus);

        // store it to sd card
        outputFile = Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/sampleAudioRecord.3gpp";

        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mMediaRecorder.setOutputFile(outputFile);

        btnRecordStart = (Button)findViewById(R.id.start);
        btnRecordStart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                start(v);
            }
        });

//        btnRecordStop = (Button)findViewById(R.id.stop);
//        btnRecordStop.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                stop(v);
//            }
//        });

        btnPlay = (Button)findViewById(R.id.play);
        btnPlay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                play(v);
            }
        });

        btnStopPlay = (Button)findViewById(R.id.stopPlay);
        btnStopPlay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                stopPlay(v);
            }
        });
    }

    public void start(View view){
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

        mRecordStatus.setText("Record status: now recording");
        btnRecordStart.setEnabled(false);
        btnRecordStop.setEnabled(true);


    }

    public void stop(View view){
        try {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder  = null;

            btnRecordStop.setEnabled(false);
            btnPlay.setEnabled(true);
            mRecordStatus.setText("Record status: Stopped recording");


        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void play(View view) {
        try{
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(outputFile);
            mPlayer.prepare();
            mPlayer.start();

            btnPlay.setEnabled(false);
            btnStopPlay.setEnabled(true);
            mRecordStatus.setText("Record status: Playing audio");


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void stopPlay(View view) {
        try {
            if (mPlayer != null) {
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
                btnPlay.setEnabled(true);
                btnStopPlay.setEnabled(false);
                mRecordStatus.setText("Record status: stopped playing");


            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}