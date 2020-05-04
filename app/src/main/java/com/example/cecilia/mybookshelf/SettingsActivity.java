package com.example.cecilia.mybookshelf;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {


    TextView resetTxt, nameTxt;
    DatabaseManager db;
    EditText edit_name;
    AlertDialog dialog_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        db = new DatabaseManager(this);

        resetTxt = (TextView)findViewById(R.id.resetTxt);
        resetTxt.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                AlertDialog.Builder reset_dialog = new AlertDialog.Builder(SettingsActivity.this);
                reset_dialog.setMessage("Are you sure you want to reset the app?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.dropRead();
                                db.dropReading();
                                db.dropToRead();
                                Toast.makeText(SettingsActivity.this, "Application deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                reset_dialog.show();
            }
        });

        nameTxt = (TextView)findViewById(R.id.nameTxt);
        dialog_name = new AlertDialog.Builder(this).create();
        edit_name = new EditText(this);

        dialog_name.setView(edit_name);
        dialog_name.setButton(DialogInterface.BUTTON_POSITIVE, "Save name", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("username", edit_name.getText().toString());
                editor.apply();
                Toast.makeText(SettingsActivity.this, "Name saved", Toast.LENGTH_SHORT).show();
            }
        });

        nameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_name.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog_name.show();
            }
        });


    }
}

