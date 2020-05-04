package com.example.cecilia.mybookshelf;



import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;

public class BookOverviewActivityRead extends AppCompatActivity {


    private String idbook;
    private Book book;
    private ImageView iconImg;
    private TextView titleTxt;
    private TextView yearTxt;
    private TextView authorTxt;
    private TextView pagesTxt;
    private TextView aboutTxt;
    private ProgressBar indicator;
    private RatingBar rating;
    private RatingBar ratingp;
    private TextView date;
    private TextView notes;
    private AlertDialog dialog_notes;
    private EditText edit_notes;
    private DatabaseManager db;
    private DatePickerDialog datePickerDialog;
    public static final int WRAP_CONTENT_LENGTH = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("BookOverviewDb", "OnCreate()");
        setContentView(R.layout.activity_book_overview_read);

        db = new DatabaseManager(this);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setBackgroundDrawableResource(R.drawable.mybookshelf2_1);

        //using getIntent() to pass the data from SearchActivity to BookOverviewActivity
        idbook = getIntent().getStringExtra("EXTRA_ID");
        Log.i("BookId", idbook);

        book=new Book();

        book.setTitle(getIntent().getStringExtra("EXTRA_TITLE"));
        book.setOriginal_publication_year(getIntent().getStringExtra("EXTRA_YEAR"));
        book.setAuthor(getIntent().getStringExtra("EXTRA_AUTHOR"));
        book.setPages(getIntent().getStringExtra("EXTRA_PAGES"));
        book.setAbout(getIntent().getStringExtra("EXTRA_ABOUT"));
        book.setRating(getIntent().getStringExtra("EXTRA_RAT"));
        book.setImgUrl(getIntent().getStringExtra("EXTRA_IMG"));
        book.setPersonalRating(getIntent().getStringExtra("EXTRA_PRAT"));
        book.setNotes(getIntent().getStringExtra("EXTRA_NOTES"));
        book.setDate_finish(getIntent().getStringExtra("EXTRA_DATEF"));

        //get references to the icons
        iconImg = (ImageView) findViewById(R.id.iconImg_read);
        titleTxt = (TextView) findViewById(R.id.titleTxt_read);
        yearTxt = (TextView) findViewById(R.id.yearTxt_read);
        authorTxt = (TextView) findViewById(R.id.authorTxt_read);
        pagesTxt = (TextView) findViewById(R.id.pagesTxt_read);
        aboutTxt = (TextView) findViewById(R.id.aboutTxt_read);
        indicator = (ProgressBar) findViewById(R.id.progress_read);
        rating = (RatingBar) findViewById(R.id.ratingBar_read);
        ratingp = (RatingBar)findViewById(R.id.ratingBar_personal);
        date = (TextView)findViewById(R.id.setDateEnd);

        notes = (TextView) findViewById(R.id.notesTxt_read);
        dialog_notes = new AlertDialog.Builder(this).create();
        edit_notes = new EditText(this);

        dialog_notes.setView(edit_notes);
        dialog_notes.setButton(DialogInterface.BUTTON_POSITIVE, "Save comment", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int toWrap = WRAP_CONTENT_LENGTH;
                int lineBreakIndex = edit_notes.getText().toString().indexOf('\n');
                if(edit_notes.getText().length() > WRAP_CONTENT_LENGTH || lineBreakIndex > 0){
                    if(lineBreakIndex > 0){
                        toWrap = lineBreakIndex;
                    }
                    if(toWrap > 0){
                        notes.setText(edit_notes.getText().toString().substring(0, toWrap) + "...");
                    }else {
                        notes.setText(edit_notes.getText());
                    }
                }else {
                    notes.setText(edit_notes.getText());
                    db.update(Long.valueOf(idbook), DatabaseStrings.TABLE_NAME1, DatabaseStrings.FIELD_NOTES, edit_notes.getText().toString());
                    Toast.makeText(BookOverviewActivityRead.this, "Note added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_notes.setText(notes.getText());
                dialog_notes.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog_notes.show();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(BookOverviewActivityRead.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                date.setText("You finished this book the "+dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                book.setDate_finish(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                Toast.makeText(BookOverviewActivityRead.this, "Date " + book.getDate_finish(), Toast.LENGTH_LONG).show();
                                db.update(Long.valueOf(idbook), DatabaseStrings.TABLE_NAME1 ,DatabaseStrings.FIELD_DATE_END, book.getDate_finish());

                            }


                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });

        aboutTxt.setMovementMethod(new ScrollingMovementMethod());
        ImageLoader imageLoader;
        DisplayImageOptions options;

        //Setup the ImageLoader, we'll use this to display our images
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        //Setup options for ImageLoader so it will handle caching for us.
        options = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .build();


        indicator.setVisibility(View.VISIBLE);
        iconImg.setVisibility(View.INVISIBLE);

        //Setup a listener we can use to switch from the loading indicator to the Image once it's ready
        ImageLoadingListener listener = new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String arg0, View arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                // TODO Auto-generated method stub

            }

            //here we switch the VISIBILITY from the indicator to the image
            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                indicator.setVisibility(View.INVISIBLE);
                iconImg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                // TODO Auto-generated method stub
            }
        };

        imageLoader.displayImage(book.getImgUrl(), iconImg, options, listener);

//Set the relevant text in our TextViews
        titleTxt.setText(book.getTitle());
        authorTxt.setText(book.getAuthor());

        /*if (!("".equals(book.getOriginal_publication_year()))){
            yearTxt.setText("("+book.getOriginal_publication_year()+")");
        } else {
            yearTxt.setText("Publication Year Not Avaiable");
        }
        if (!("".equals(book.getPages()))){
            pagesTxt.setText(book.getPages() + " pages");
        } else {
            pagesTxt.setText("Number of Pages Not Avaiable");
        }

        if (!("".equals(book.getAbout()))){
            aboutTxt.setText(book.getAbout());
        } else {
            aboutTxt.setText("Description Not Avaiable");
        }*/


        if (book.getOriginal_publication_year().matches("-?\\d+(\\.\\d+)?")){
            yearTxt.setText("("+book.getOriginal_publication_year()+")");
        } else {
            yearTxt.setText("Publication Year Not Available");
        }
        if (book.getPages().matches("-?\\d+(\\.\\d+)?")){
            pagesTxt.setText(book.getPages() + " pages");
        } else {
            pagesTxt.setText("Number of Pages Not Available");
        }

        aboutTxt.setText(book.getAbout());
        rating.setRating(Float.parseFloat(book.getRating()));
        if (!("".equals(book.getPersonalRating()))){
            ratingp.setRating(Float.parseFloat(book.getPersonalRating()));
        }
        if (!("".equals(book.getNotes()))){
            notes.setText(book.getNotes());
        }
        if (!("".equals(book.getDate_finish()))){
            date.setText("You finished this book the "+book.getDate_finish());
        }


    }

    @Override
    public void onBackPressed(){
        finish();
        startActivity(new Intent(this, ReadActivity.class));
    }

}

