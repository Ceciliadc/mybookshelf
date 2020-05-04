package com.example.cecilia.mybookshelf;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class ReadingActivity extends AppCompatActivity {

    private BookListAdapterDb mAdapter;
    private ListView listDb;
    DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        db = new DatabaseManager(this);

        ImageButton back_reading = (ImageButton) findViewById(R.id.imageButton10);
        back_reading.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent openMainActivity = new Intent(ReadingActivity.this, MainActivity.class);
                startActivity(openMainActivity);
            }
        });

        //Get reference to our ListView
        listDb = (ListView)findViewById(R.id.listDb1);

        //missing the List<Book> from the database--> I'm using an adapter for the view
        mAdapter = new BookListAdapterDb(getApplicationContext(), -1, getBooksFromDb(), DatabaseStrings.TABLE_NAME2);
        listDb.setAdapter(mAdapter);

        //THIS IS THE ONCLICK METHOD WHEN A ROW IS CLICKED
        //Set the click listener to launch the browser when a row is clicked.
        listDb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //this in the method called on the Click of a row
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

                Intent openBookOverviewActivityReading = new Intent(ReadingActivity.this, BookOverviewActivityReading.class);
                openBookOverviewActivityReading.putExtra("EXTRA_ID", mAdapter.getItem(pos).getID());
                openBookOverviewActivityReading.putExtra("EXTRA_TITLE", mAdapter.getItem(pos).getTitle());
                openBookOverviewActivityReading.putExtra("EXTRA_AUTHOR", mAdapter.getItem(pos).getAuthor());
                openBookOverviewActivityReading.putExtra("EXTRA_YEAR", mAdapter.getItem(pos).getOriginal_publication_year());
                openBookOverviewActivityReading.putExtra("EXTRA_PAGES", mAdapter.getItem(pos).getPages());
                openBookOverviewActivityReading.putExtra("EXTRA_IMG", mAdapter.getItem(pos).getImgUrl());
                openBookOverviewActivityReading.putExtra("EXTRA_ABOUT", mAdapter.getItem(pos).getAbout());
                openBookOverviewActivityReading.putExtra("EXTRA_RAT", mAdapter.getItem(pos).getRating());
                openBookOverviewActivityReading.putExtra("EXTRA_DATES", mAdapter.getItem(pos).getDate_start());
                finish();
                startActivity(openBookOverviewActivityReading);
            }

        });

    }


    public ArrayList<Book> getBooksFromDb(){
        Cursor crs;
        DatabaseManager db = new DatabaseManager(this);
        crs = db.query(DatabaseStrings.TABLE_NAME2, DatabaseStrings.FIELD_TITLE);
        ArrayList<Book> list=new ArrayList<Book>();

        //ID(0), TITLE(1), AUTHOR(2), YEAR(3), PAGES(4), IMAGE(5), ABOUT(6), RATING_GEN(7), DATE_START(8)
        while(crs.moveToNext()){
            Book row=new Book();
            row.setID(crs.getString(0));
            row.setTitle(crs.getString(1));
            row.setAuthor(crs.getString(3));
            row.setOriginal_publication_year(crs.getString(2));
            row.setPages(crs.getString(4));
            row.setImgUrl(crs.getString(5));
            row.setAbout(crs.getString(6));
            row.setRating(crs.getString(7));
            row.setDate_start(crs.getString(8));
            list.add(row);
        }

        crs.close();
        return list;
    }

    @Override
    public void onBackPressed(){
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}
