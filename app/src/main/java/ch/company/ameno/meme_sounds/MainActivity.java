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
import android.view.KeyEvent;
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
import android.widget.SearchView;
import android.widget.TableLayout;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String outputPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        outputPath = getFilesDir().getAbsolutePath() + "/sounds";

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        container.removeAllViews();
        inflater.inflate(R.layout.content_home, container);
        generateButtons();
        generateSearchView();
    }

    private void generateSearchView() {
        final SearchView searchView = (SearchView) findViewById(R.id.searchViewHome);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                generateButtons(newText);
                return true;
            }
        });
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

    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                if(file.getName().endsWith(".csv")){
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }

    //Generate all Buttons
    public void generateButtons(){
        ArrayList<String> soundList = listAssetFiles("sounds");
        File myFilesDir = new File(outputPath);
        List<File> mySoundsList = getListFiles(myFilesDir);
        final String[] list = soundList.toArray(new String[soundList.size()]);
        final MediaPlayer mp = new MediaPlayer();
        for (final String file: list) {

            Button myButton = new Button(this);
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mp.isPlaying()) {
                        mp.stop();
                    }
                    try {
                        mp.reset();
                        AssetFileDescriptor afd;
                        afd = getAssets().openFd("sounds/" + file + ".mp3");
                        mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
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
        for(final File myFile: mySoundsList) {
            Button myButton = new Button(this);
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mp.isPlaying()) {
                        mp.stop();
                    }
                    try {
                        mp.reset();
                        mp.setDataSource(myFile.getAbsolutePath());
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

    //Generate Just the Buttons that were searched
    public void generateButtons(String searchText){
        LinearLayout ll = (LinearLayout)findViewById(R.id.buttonlayout);
        ll.removeAllViews();
        ArrayList<String> soundList = listAssetFiles("sounds", searchText);
        final String[] list = soundList.toArray(new String[soundList.size()]);
        final MediaPlayer mp = new MediaPlayer();
        for (final String file: list) {

            Button myButton = new Button(this);
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mp.isPlaying()) {
                        mp.stop();
                    }
                    try {
                        mp.reset();
                        AssetFileDescriptor afd;
                        afd = getAssets().openFd("sounds/" + file + ".mp3");
                        mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
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



            ll.addView(myButton);
        }

    }

    //List Asset Files for all Buttons
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

    //List only Asset Files that are relevant for search
    private ArrayList listAssetFiles(String path, String searchTerm) {
        ArrayList<String> soundList = new ArrayList<>();
        String[] list = null;
        try {
            list = getAssets().list(path);

            soundList = new ArrayList<>();
            for (int i = 0; i< list.length; i++)
            {
                if(list[i].endsWith("mp3")&&list[i].contains(searchTerm.toUpperCase())) {
                    soundList.add(list[i].substring(0, list[i].lastIndexOf('.')));
                }
            }
        }
        catch (IOException e) {

        }
        return soundList;
    }

    //generate Buttons  for My Buttons Page
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


//list asset files for my Button Page
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

    public boolean addButton(MenuItem item) {
        Intent intent = new Intent(this, RecordAudioActivity.class);
        this.startActivity(intent);
        return true;
    }


}
