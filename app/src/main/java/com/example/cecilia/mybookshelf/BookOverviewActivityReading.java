package com.example.cecilia.mybookshelf;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Calendar;

public class BookOverviewActivityReading extends AppCompatActivity {

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
    private TextView date;
    DatePickerDialog datePickerDialog;
    DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_overview_reading);

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setBackgroundDrawableResource(R.drawable.mybookshelf2_1);

        //using getIntent() to pass the data from SearchActivity to BookOverviewActivity
        idbook = getIntent().getStringExtra("EXTRA_ID");
        Log.i("BookId", idbook);

        db = new DatabaseManager(this);
        book=new Book();

        book.setTitle(getIntent().getStringExtra("EXTRA_TITLE"));
        book.setOriginal_publication_year(getIntent().getStringExtra("EXTRA_YEAR"));
        book.setAuthor(getIntent().getStringExtra("EXTRA_AUTHOR"));
        book.setPages(getIntent().getStringExtra("EXTRA_PAGES"));
        book.setAbout(getIntent().getStringExtra("EXTRA_ABOUT"));
        book.setRating(getIntent().getStringExtra("EXTRA_RAT"));
        book.setImgUrl(getIntent().getStringExtra("EXTRA_IMG"));
        book.setDate_start(getIntent().getStringExtra("EXTRA_DATES"));

        //popup read_activity
        ImageButton readButton = (ImageButton) findViewById(R.id.imageButton3);
        readButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                AlertDialog.Builder readBuilder = new AlertDialog.Builder(BookOverviewActivityReading.this);
                View readView = getLayoutInflater().inflate(R.layout.add_rating, null);
                final RatingBar readRatingBar = (RatingBar) readView.findViewById(R.id.ratingBar2);
                final EditText readEditTxt = (EditText) readView.findViewById(R.id.editTxt);
                Button continueReadButton = (Button) readView.findViewById(R.id.button2);

                date = (TextView) readView.findViewById(R.id.setDate);
                date.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // calender class's instance and get current date , month and year from calender
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR); // current year
                        int mMonth = c.get(Calendar.MONTH); // current month
                        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                        // date picker dialog
                        datePickerDialog = new DatePickerDialog(BookOverviewActivityReading.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        // set day of month , month and year value in the edit text
                                        date.setText(dayOfMonth + "/"
                                                + (monthOfYear + 1) + "/" + year);
                                        book.setDate_finish(dayOfMonth + "/"
                                                + (monthOfYear + 1) + "/" + year);

                                    }
                                }, mYear, mMonth, mDay);

                        datePickerDialog.show();
                    }
                });


                continueReadButton.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        if (readRatingBar.getRating() != 0){
                            book.setPersonalRating(String.valueOf(readRatingBar.getRating()));
                            book.setNotes(readEditTxt.getText().toString());
                            db.saveRead(book.getTitle(), book.getAuthor(), book.getOriginal_publication_year(), book.getPages(), book.getImgUrl(), book.getAbout(), book.getRating(), book.getPersonalRating(), book.getNotes(), (String)date.getText().toString());
                            db.delete(DatabaseStrings.TABLE_NAME2, Long.valueOf(idbook));
                            Toast.makeText(BookOverviewActivityReading.this, "Moved to Read", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(BookOverviewActivityReading.this, "Set your rating", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                readBuilder.setView(readView);
                AlertDialog dialog = readBuilder.create();
                dialog.show();
            }
        });

        //get references to the icons
        iconImg = (ImageView) findViewById(R.id.iconImg_reading);
        titleTxt = (TextView) findViewById(R.id.titleTxt_reading);
        yearTxt = (TextView) findViewById(R.id.yearTxt_reading);
        authorTxt = (TextView) findViewById(R.id.authorTxt_reading);
        pagesTxt = (TextView) findViewById(R.id.pagesTxt_reading);
        aboutTxt = (TextView) findViewById(R.id.aboutTxt_reading);
        indicator = (ProgressBar) findViewById(R.id.progress_reading);
        rating = (RatingBar) findViewById(R.id.ratingBar_reading);
        date = (TextView) findViewById(R.id.setDateStart);

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(BookOverviewActivityReading.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                date.setText("You started this book the "+dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                book.setDate_start(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                Toast.makeText(BookOverviewActivityReading.this, "Date " + book.getDate_start(), Toast.LENGTH_LONG).show();
                                db.update(Long.valueOf(idbook), DatabaseStrings.TABLE_NAME2 ,DatabaseStrings.FIELD_DATE_START, book.getDate_start());

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

        if (!("".equals(book.getDate_finish()))){
            date.setText("You started this book the "+book.getDate_start());
        }

    }


    @Override
    public void onBackPressed(){
        finish();
        startActivity(new Intent(this, ReadingActivity.class));
    }

}
