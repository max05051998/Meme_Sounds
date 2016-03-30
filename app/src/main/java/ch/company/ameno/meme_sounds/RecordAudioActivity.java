package ch.company.ameno.meme_sounds;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class RecordAudioActivity extends Activity {

    private MediaRecorder mMediaRecorder;
    private MediaPlayer mMediaPlayer;
    private String outputPath = null;
    private String movedFile = null;
    private ImageButton btnRecordStart;
    private ImageButton btnPlay;
    private ImageButton btnSave;
    private TextView mRecordStatus;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private boolean isSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        mRecordStatus = (TextView) findViewById(R.id.tv_recordStatus);
        mRecordStatus.setText("Record status: Waiting for input");

        // store it to sd card
        outputPath = getFilesDir().getAbsolutePath() + "/sounds";
        try {
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

        outputPath = outputPath + "/currentButton.mp4";


        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mMediaRecorder.setOutputFile(outputPath);

        btnRecordStart = (ImageButton) findViewById(R.id.record);
        btnRecordStart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                record(v);
            }
        });

        btnPlay = (ImageButton) findViewById(R.id.play);
        btnPlay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                play(v);
            }
        });

        btnSave = (ImageButton) findViewById(R.id.saveButton);
        btnSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                save(v);
            }
        });
    }

    public void record(View view) {
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
        } else {
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
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setDataSource(outputPath);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                mRecordStatus.setText("Record status: Playing audio");
                isPlaying = true;
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else {
            mMediaPlayer.stop();
            mRecordStatus.setText("Record status: Stopped playing");
            isPlaying = false;
        }
        mRecordStatus.setText("");
    }

    public void save(View view) {

        EditText buttonName = (EditText) findViewById(R.id.buttonName);
        movedFile = getFilesDir().getAbsolutePath() + "/sounds/" + buttonName.getText() + ".mp4";
        try {
            saveFile();
            mRecordStatus.setText("Record status: saved Button");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void saveFile() {
        File from = new File(outputPath);
        File to = new File(movedFile);
        from.renameTo(to);
        isSaved = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();

        if (!isSaved) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RecordAudioActivity.this);
            builder.setTitle("Are you sure you want to quit without saving?");
        }
    }
}