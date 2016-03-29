package ch.company.ameno.meme_sounds;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.v("memeSounds", "Camera pressed");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            LayoutInflater inflater = getLayoutInflater();
            RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
            container.removeAllViews();
            inflater.inflate(R.layout.content_home, container);
            generateButtons();
        }
        else if (id == R.id.nav_myButtons) {
            LayoutInflater inflater = getLayoutInflater();
            RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
            container.removeAllViews();
            inflater.inflate(R.layout.content_mybuttons, container);
            generateMyButtons();
        }
        else if (id == R.id.nav_about) {
            LayoutInflater inflater = getLayoutInflater();
            RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
            container.removeAllViews();
            inflater.inflate(R.layout.content_about, container);
            loadDataFromAsset("Koala.jpg", "Penguins.jpg");

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void generateButtons(){
        ArrayList<String> soundList = listAssetFiles("sounds");
        String[] list = soundList.toArray(new String[soundList.size()]);
        final MediaPlayer mp = new MediaPlayer();
        for (final String file: list) {

            Button myButton = new Button(this);
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mp.isPlaying())
                    {
                        mp.stop();
                    }
                    try {
                        mp.reset();
                        AssetFileDescriptor afd;
                        afd = getAssets().openFd("sounds/" +file+".mp3");
                        mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                        mp.prepare();
                        mp.start();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
            myButton.setText(file);

            LinearLayout ll = (LinearLayout)findViewById(R.id.buttonlayout);

            ll.addView(myButton);
        }


    }



    private ArrayList listAssetFiles(String path) {
        ArrayList<String> soundList = new ArrayList<>();
        String[] list = null;
        try {
            list = getAssets().list(path);

            soundList = new ArrayList<>();
            for (int i = 0; i< list.length; i++)
            {
                if(list[i].endsWith("mp3")) {
                    soundList.add(list[i].substring(0, list[i].lastIndexOf('.')));
                }
            }
        }
         catch (IOException e) {

         }
        return soundList;
    }

    public void generateMyButtons(){
        ArrayList<String> soundList = listAssetFilesMyButtons("sounds");
        String[] list = soundList.toArray(new String[soundList.size()]);
        final MediaPlayer mp = new MediaPlayer();
        for (final String file: list) {

            Button myButton = new Button(this);
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mp.isPlaying())
                    {
                        mp.stop();
                    }
                    try {
                        mp.reset();
                        AssetFileDescriptor afd;
                        afd = getAssets().openFd("sounds/" +file+".mp3");
                        mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                        mp.prepare();
                        mp.start();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
            myButton.setText(file);

            LinearLayout ll = (LinearLayout)findViewById(R.id.buttonlayout);

            ll.addView(myButton);
        }


    }



    private ArrayList listAssetFilesMyButtons(String path){
        ArrayList<String> soundList = new ArrayList<>();
        String[] list = null;
        try {
            list = getAssets().list(path);

            soundList = new ArrayList<>();
            for (int i = 0; i< list.length; i++)
            {
                if(list[i].endsWith("_myButtons.mp3")) {
                    soundList.add(list[i].substring(0, list[i].lastIndexOf('.')));
                }
            }
        }
        catch (IOException e) {

        }
        return soundList;
    }


    public void loadDataFromAsset(String PictureNameHaP, String PictureNameStM ) {
        ImageView ImageHaP = (ImageView) findViewById(R.id.HaP_Image);
        ImageView ImageStM = (ImageView) findViewById(R.id.StM_Image);

        // load image
        try {
            // get input stream
            InputStream ims = getAssets().open(PictureNameHaP);
            // load image as Drawable
            Drawable dHaP = Drawable.createFromStream(ims, null);
            //close imput stream
            ims.close();
            // set image to ImageView
            ImageHaP.setImageDrawable(dHaP);

            // get input stream
            InputStream ims2 = getAssets().open(PictureNameStM);
            // load image as Drawable
            Drawable dStM = Drawable.createFromStream(ims2, null);
            //close imput stream
            ims2.close();
            // set image to ImageView
            ImageStM.setImageDrawable(dStM);

        } catch (IOException ex) {
            return;
        }

    }

    public void addButton(View view) {
    }
}
