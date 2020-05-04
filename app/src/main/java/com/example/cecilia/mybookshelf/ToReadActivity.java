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

public class ToReadActivity extends AppCompatActivity {

    private BookListAdapterDb mAdapter;
    private ListView listDb;
    DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_read);

        //Get reference to our ListView
        listDb = (ListView)findViewById(R.id.listDb1);

        //db = new DatabaseManager(this);

        ImageButton back_toread = (ImageButton) findViewById(R.id.imageButton11);
        back_toread.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent openMainActivity = new Intent(ToReadActivity.this, MainActivity.class);
                startActivity(openMainActivity);
            }
        });



        //missing the List<Book> from the database--> I'm using an adapter for the view
        mAdapter = new BookListAdapterDb(getApplicationContext(), -1, getBooksFromDb(), DatabaseStrings.TABLE_NAME3);
        listDb.setAdapter(mAdapter);

        listDb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //this in the method called on the Click of a row
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

                Intent openBookOverviewActivityToRead = new Intent(ToReadActivity.this, BookOverviewActivityToRead.class);
                openBookOverviewActivityToRead.putExtra("EXTRA_ID", mAdapter.getItem(pos).getID());
                openBookOverviewActivityToRead.putExtra("EXTRA_TITLE", mAdapter.getItem(pos).getTitle());
                openBookOverviewActivityToRead.putExtra("EXTRA_AUTHOR", mAdapter.getItem(pos).getAuthor());
                openBookOverviewActivityToRead.putExtra("EXTRA_YEAR", mAdapter.getItem(pos).getOriginal_publication_year());
                openBookOverviewActivityToRead.putExtra("EXTRA_PAGES", mAdapter.getItem(pos).getPages());
                openBookOverviewActivityToRead.putExtra("EXTRA_IMG", mAdapter.getItem(pos).getImgUrl());
                openBookOverviewActivityToRead.putExtra("EXTRA_ABOUT", mAdapter.getItem(pos).getAbout());
                openBookOverviewActivityToRead.putExtra("EXTRA_RAT", mAdapter.getItem(pos).getRating());
                finish();
                startActivity(openBookOverviewActivityToRead);
            }

        });

    }

    public ArrayList<Book> getBooksFromDb(){
        Cursor crs;
        DatabaseManager db = new DatabaseManager(this);
        crs = db.query(DatabaseStrings.TABLE_NAME3, DatabaseStrings.FIELD_PRIORITY);
        ArrayList<Book> list=new ArrayList<Book>();

        //ID(0), TITLE(1), AUTHOR(2), YEAR(3), PAGES(4), IMAGE(5), ABOUT(6), RATING_GEN(7), PRIORITY(8)
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
            row.setPriority(crs.getString(8));
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
