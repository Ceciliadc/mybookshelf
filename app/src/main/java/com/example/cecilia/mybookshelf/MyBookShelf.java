package com.example.cecilia.mybookshelf;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

public class MyBookShelf extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        SharedPreferences sharedPreferencesSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = sharedPreferencesSettings.edit();
        edit.putBoolean(getString(R.string.pref_previously_started), Boolean.FALSE);
        edit.commit();

        Thread mythread = new Thread(){
            @Override
            public void run(){
                try {
                    sleep(1500);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        mythread.start();
    }
}
