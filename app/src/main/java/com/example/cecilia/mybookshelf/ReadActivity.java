package com.example.cecilia.mybookshelf;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ReadActivity extends AppCompatActivity {

    private BookListAdapterDb mAdapter;
    private ListView listDb;
    public static String currentId;
    private CursorAdapter cAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        ImageButton back_read = (ImageButton) findViewById(R.id.imageButton9);
        back_read.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent openMainActivity = new Intent(ReadActivity.this, MainActivity.class);
                startActivity(openMainActivity);
            }
        });

        //Get reference to our ListView
        listDb = (ListView)findViewById(R.id.listDb1);

        //missing the List<Book> from the database--> I'm using an adapter for the view
        mAdapter = new BookListAdapterDb(getApplicationContext(), -1, getBooksFromDb(), DatabaseStrings.TABLE_NAME1);
        listDb.setAdapter(mAdapter);

        //THIS IS THE ONCLICK METHOD WHEN A ROW IS CLICKED
        //Set the click listener to launch the browser when a row is clicked.
        listDb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //this in the method called on the Click of a row
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                //currentId = mAdapter.getItem(pos).getID();
                //Log.i("GetIdBook",currentId);

                Intent openBookOverviewActivityRead = new Intent(ReadActivity.this, BookOverviewActivityRead.class);
                openBookOverviewActivityRead.putExtra("EXTRA_ID", mAdapter.getItem(pos).getID());
                openBookOverviewActivityRead.putExtra("EXTRA_TITLE", mAdapter.getItem(pos).getTitle());
                openBookOverviewActivityRead.putExtra("EXTRA_AUTHOR", mAdapter.getItem(pos).getAuthor());
                openBookOverviewActivityRead.putExtra("EXTRA_YEAR", mAdapter.getItem(pos).getOriginal_publication_year());
                openBookOverviewActivityRead.putExtra("EXTRA_PAGES", mAdapter.getItem(pos).getPages());
                openBookOverviewActivityRead.putExtra("EXTRA_IMG", mAdapter.getItem(pos).getImgUrl());
                openBookOverviewActivityRead.putExtra("EXTRA_ABOUT", mAdapter.getItem(pos).getAbout());
                openBookOverviewActivityRead.putExtra("EXTRA_RAT", mAdapter.getItem(pos).getRating());
                openBookOverviewActivityRead.putExtra("EXTRA_PRAT", mAdapter.getItem(pos).getPersonalRating());
                openBookOverviewActivityRead.putExtra("EXTRA_NOTES", mAdapter.getItem(pos).getNotes());
                openBookOverviewActivityRead.putExtra("EXTRA_DATEF", mAdapter.getItem(pos).getDate_finish());
                finish();
                startActivity(openBookOverviewActivityRead);
            }

        });


    }

    public ArrayList<Book> getBooksFromDb(){
        Cursor crs;
        DatabaseManager db = new DatabaseManager(this);
        crs = db.query(DatabaseStrings.TABLE_NAME1, DatabaseStrings.FIELD_TITLE);
        ArrayList<Book> list=new ArrayList<Book>();

        //ID(0), TITLE(1), AUTHOR(2), YEAR(3), PAGES(4), IMAGE(5), ABOUT(6), RATING_GEN(7), RATING_PERS(8), NOTES(9), DATE_END(10)
        while(crs.moveToNext()){
            Book row=new Book();
            row.setID(crs.getString(0));
            row.setTitle(crs.getString(1));
            row.setOriginal_publication_year(crs.getString(2));
            row.setAuthor(crs.getString(3));
            row.setPages(crs.getString(4));
            row.setImgUrl(crs.getString(5));
            row.setAbout(crs.getString(6));
            row.setRating(crs.getString(7));
            row.setPersonalRating(crs.getString(8));
            row.setNotes(crs.getString(9));
            row.setDate_finish(crs.getString(10));
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
