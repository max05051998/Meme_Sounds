package ch.company.ameno.meme_sounds;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        mRecordStatus = (TextView) findViewById(R.id.tv_recordStatus);
        mRecordStatus.setText("Waiting for input");

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

//        mMediaRecorder = new MediaRecorder();
//        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//        mMediaRecorder.setOutputFile(outputPath);

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
        if (mMediaRecorder == null) {
            try {
                initializeMR();
                mMediaRecorder.prepare();
                mMediaRecorder.start();
                btnPlay.setEnabled(false);
                btnSave.setEnabled(false);
                counter++;
                mRecordStatus.setText("Record status: Recording!");
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (counter == 1) {
                mMediaRecorder.stop();
                mRecordStatus.setText("Record status: Stopped recording");
                counter++;
            } else {
                mMediaRecorder = null;
                counter = 0;
                mRecordStatus.setText("Cleared previous recording!");

                try {
                    File dir = new File(outputPath);
                    if (dir.exists()) {
                        dir.delete();
                    }
                } catch (Exception e) {
                    Log.e("tag", e.getMessage());
                }
            }
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
                mRecordStatus.setText("Playing audio");
                isPlaying = true;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mRecordStatus.setText("Stopped playing audio");
            isPlaying = false;
        }
        mRecordStatus.setText("Waiting for input");

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                mRecordStatus.setText("Finished playing audio");
            }
        });
    }

    public void save(View view) {

        EditText buttonName = (EditText) findViewById(R.id.buttonName);
        if (buttonName.length() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RecordAudioActivity.this);
            builder.setTitle("You did not enter a Button Name!");
            builder.show();
        }
        else {
            movedFile = getFilesDir().getAbsolutePath() + "/sounds/" + buttonName.getText() + ".mp4";

            try {
                saveFile();
                mRecordStatus.setText("Your Button was saved!");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void saveFile() {
        File from = new File(outputPath);
        File to = new File(movedFile);
        from.renameTo(to);
        isSaved = true;
    }

    public void initializeMR() {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mMediaRecorder.setOutputFile(outputPath);
    }

    @Override
    public void onBackPressed() {
        if (!isSaved) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RecordAudioActivity.this);
            builder.setTitle("Are you sure you want to quit without saving?");
            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {

        if (mMediaPlayer != null) mMediaPlayer.stop();
        super.onDestroy();

    }
}