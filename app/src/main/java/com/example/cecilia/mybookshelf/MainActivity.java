package com.example.cecilia.mybookshelf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //set button tag
        Button btnHome = (Button)findViewById(R.id.button);
        btnHome.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent openSearchActivity = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(openSearchActivity);
            }
        });

        TextView txtRead = (TextView)findViewById(R.id.textView);
        txtRead.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent openReadActivity = new Intent(MainActivity.this, ReadActivity.class);
                startActivity(openReadActivity);
            }
        });


        TextView txtReading = (TextView)findViewById(R.id.textView2);
        txtReading.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent openReadingActivity = new Intent(MainActivity.this, ReadingActivity.class);
                startActivity(openReadingActivity);
            }
        });

        TextView txtToRead = (TextView)findViewById(R.id.textView3);
        txtToRead.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent openToReadActivity = new Intent(MainActivity.this, ToReadActivity.class);
                startActivity(openToReadActivity);
            }
        });

        TextView txtSettings = (TextView)findViewById(R.id.textView4);
        txtSettings.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent openSettingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(openSettingsActivity);
            }
        });


    }


    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences sharedPreferencesSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = sharedPreferencesSettings.getBoolean(getString(R.string.pref_previously_started), false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = sharedPreferencesSettings.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.commit();

            SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String name = sharedPref.getString("username", "John Doe");
            Toast.makeText(this, "Welcome back, " + name, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed(){
        Intent exit = new Intent(Intent.ACTION_MAIN);
        exit.addCategory(Intent.CATEGORY_HOME);
        exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(exit);
    }
}
