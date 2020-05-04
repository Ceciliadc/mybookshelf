package com.example.cecilia.mybookshelf;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.Calendar;

/* Library used for the Image Loader*/
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class BookOverviewActivity extends AppCompatActivity {

    private String idbook;
    private Book book;
    private ImageView iconImg;
    private TextView titleTxt;
    private TextView yearTxt;
    private TextView authorTxt;
    private TextView pagesTxt;
    private TextView aboutTxt;
    private TextView ratingTxt;
    private ProgressBar indicator;
    private RatingBar rating;
    private TextView date;
    DatePickerDialog datePickerDialog;

    DatabaseManager db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("BookOverview", "OnCreate()");
        setContentView(R.layout.activity_book_overview);

        //db
        db=new DatabaseManager(this);

        //popup read_activity
        ImageButton readButton = (ImageButton) findViewById(R.id.imageButton);
        readButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                AlertDialog.Builder readBuilder = new AlertDialog.Builder(BookOverviewActivity.this);
                View readView = getLayoutInflater().inflate(R.layout.add_rating, null);
                final RatingBar readRatingBar = (RatingBar) readView.findViewById(R.id.ratingBar2);
                final EditText readEditTxt = (EditText) readView.findViewById(R.id.editTxt);
                Button continuaButton = (Button) readView.findViewById(R.id.button2);

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
                        datePickerDialog = new DatePickerDialog(BookOverviewActivity.this,
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


                continuaButton.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        if (readRatingBar.getRating() != 0){
                            book.setPersonalRating(String.valueOf(readRatingBar.getRating()));
                            book.setNotes(readEditTxt.getText().toString());
                            db.saveRead(book.getTitle(), book.getAuthor(), book.getOriginal_publication_year(), book.getPages(), book.getImgUrl(), book.getAbout(), book.getRating(), book.getPersonalRating(), book.getNotes(), (String)date.getText().toString());
                            Toast.makeText(BookOverviewActivity.this, "Added to Read", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(BookOverviewActivity.this, "Set your rating", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                readBuilder.setView(readView);
                AlertDialog dialog = readBuilder.create();
                dialog.show();
            }
        });

        //popup reading_activity
        ImageButton readingButton = (ImageButton) findViewById(R.id.imageButton5);
        readingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final AlertDialog.Builder readingBuilder = new AlertDialog.Builder(BookOverviewActivity.this);
                final View readingView = getLayoutInflater().inflate(R.layout.add_inizio_lettura, null);
                Button continua2Button = (Button) readingView.findViewById(R.id.button3);

                date = (TextView) readingView.findViewById(R.id.setDate2);

                date.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // calender class's instance and get current date , month and year from calender
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR); // current year
                        int mMonth = c.get(Calendar.MONTH); // current month
                        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                        // date picker dialog
                        datePickerDialog = new DatePickerDialog(BookOverviewActivity.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        // set day of month , month and year value in the edit text
                                        date.setText(dayOfMonth + "/"
                                                + (monthOfYear + 1) + "/" + year);
                                        book.setDate_start(dayOfMonth + "/"
                                                + (monthOfYear + 1) + "/" + year);

                                    }
                                }, mYear, mMonth, mDay);

                        datePickerDialog.show();
                    }
                });

                continua2Button.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        db.saveReading(book.getTitle(), book.getAuthor(), book.getOriginal_publication_year(), book.getPages(), book.getImgUrl(), book.getAbout(), book.getRating(), book.getDate_start());
                        Toast.makeText(BookOverviewActivity.this, "Added to Reading", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                readingBuilder.setView(readingView);
                AlertDialog dialog = readingBuilder.create();
                dialog.show();
            }
        });


        //popup toread_activity

        ImageButton toreadButton = (ImageButton) findViewById(R.id.imageButton6);
        toreadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder toreadBuilder = new AlertDialog.Builder(BookOverviewActivity.this);
                View toreadView = getLayoutInflater().inflate(R.layout.add_priority, null);
                Button continua3Button = (Button) toreadView.findViewById(R.id.continue_priority);

                final RadioButton btnHigh = (RadioButton)toreadView.findViewById(R.id.ButtonHigh);
                final RadioButton btnMedium = (RadioButton)toreadView.findViewById(R.id.ButtonMedium);
                final RadioButton btnLow = (RadioButton)toreadView.findViewById(R.id.ButtonLow);

                book.setPriority(DatabaseStrings.PRIORITY_LOW);
                btnHigh.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if (btnHigh.isChecked()){
                            book.setPriority(DatabaseStrings.PRIORITY_HIGH);
                            btnMedium.setChecked(false);
                            btnLow.setChecked(false);
                        }
                    }
                });
                btnMedium.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        book.setPriority(DatabaseStrings.PRIORITY_MEDIUM);
                        if (btnMedium.isChecked()){
                            btnHigh.setChecked(false);
                            btnLow.setChecked(false);
                        }
                    }
                });
                btnLow.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        book.setPriority(DatabaseStrings.PRIORITY_LOW);
                        if (btnLow.isChecked()){
                            btnMedium.setChecked(false);
                            btnHigh.setChecked(false);
                        }
                    }
                });
                continua3Button.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                            db.saveToRead(book.getTitle(), book.getAuthor(), book.getOriginal_publication_year(), book.getPages(), book.getImgUrl(), book.getAbout(), book.getRating(), book.getPriority());
                            Toast.makeText(BookOverviewActivity.this, "Added to ToRead", Toast.LENGTH_SHORT).show();
                            finish();
                    }
                });

                toreadBuilder.setView(toreadView);
                AlertDialog dialog = toreadBuilder.create();
                dialog.show();
            }
        });

        //back to home
        ImageButton backButton = (ImageButton) findViewById(R.id.imageButton7);
        backButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent openMainActivity = new Intent(BookOverviewActivity.this, MainActivity.class);
                startActivity(openMainActivity);
            }
        });

        //using getIntent() to pass the data from SearchActivity to BookOverviewActivity
        idbook=getIntent().getStringExtra("EXTRA_ID");
        Log.i("BookId", idbook);

        //get references to the icons
        iconImg = (ImageView)findViewById(R.id.iconImg);
        titleTxt = (TextView)findViewById(R.id.titleTxt);
        yearTxt = (TextView)findViewById(R.id.yearTxt);
        authorTxt = (TextView)findViewById(R.id.authorTxt);
        pagesTxt = (TextView)findViewById(R.id.pagesTxt);
        aboutTxt = (TextView)findViewById(R.id.aboutTxt);
        ratingTxt = (TextView)findViewById(R.id.ratingTxt);

        indicator = (ProgressBar)findViewById(R.id.progress);
        rating = (RatingBar)findViewById(R.id.ratingBar);

        if(isNetworkAvailable()){
            //if network is available, I instantiate the object and call the BookDownloadTask method
            Log.i("Book", "starting download Task");
            book = new Book();
            book.setID(idbook);
            BookOverviewActivity.BookDownloadTask download = new BookOverviewActivity.BookDownloadTask();
            download.execute();
        } else {
            buildDialog(BookOverviewActivity.this).show();
        }


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public AlertDialog.Builder buildDialog(Context c){
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("FAILED CONNECTION");
        builder.setMessage("Please check again your connection");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        return builder;
    }


    private class BookDownloadTask extends AsyncTask<Void, Void, Void> {
        int check=0;

        @Override
        protected Void doInBackground(Void... arg0) {
            //Download the file
            try {
                Downloader.DownloadFromUrl("https://www.goodreads.com/book/show/"+ idbook +".xml?key=s6NJdnVHBcIa30pL6WF8Gg", openFileOutput("BookOverview.xml", Context.MODE_PRIVATE));
                BookXmlPullParser parser = new BookXmlPullParser();
                parser.getBookFromFile(BookOverviewActivity.this, book);
                if(book==null){
                    buildDialog(BookOverviewActivity.this).show();
                    return null;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){

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

            /*if ((book.getOriginal_publication_year().matches("-?//d+(//.//d+)?")) || !("".equals(book.getOriginal_publication_year()))) {
                yearTxt.setText("(" + book.getOriginal_publication_year() + ")");
            }
            else {
                yearTxt.setText("Publication Year Not Available");
            }

            if ((book.getPages().matches("-?//d+(//.//d+)?")) || !("".equals(book.getPages()))){
                pagesTxt.setText(book.getPages() + " pages");
            } else {
                pagesTxt.setText("Number of Pages Not Available");
            }

            aboutTxt.setText(book.getAbout());*/

            /*if (!("".equals(book.getOriginal_publication_year()))){
                yearTxt.setText("("+book.getOriginal_publication_year()+")");
            } else {
                yearTxt.setText("Publication Year Not Avaiable");
            }
            if (!("".equals(book.getPages()))){
                pagesTxt.setText(book.getPages() + " pages");
            } else {
                pagesTxt.setText("Number of Pages Not Avaiable");
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

            if (!("".equals(book.getAbout()))){
                aboutTxt.setText(book.getAbout());
            } else {
                aboutTxt.setText("Description Not Avaiable");
            }
            rating.setRating(Float.parseFloat(book.getRating()));
            ratingTxt.setText("Rating medio:\n" + book.getRating());

        }

    }

}
