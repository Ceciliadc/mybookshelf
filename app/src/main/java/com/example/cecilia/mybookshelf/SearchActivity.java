package com.example.cecilia.mybookshelf;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.FileNotFoundException;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class SearchActivity extends AppCompatActivity {

    private BookListAdapter mAdapter;
    private ListView booksList;
    private Button mButton;
    private EditText mEdit;
    private String query; //String from the edittext
    public static String currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //creation of the class
        super.onCreate(savedInstanceState);
        Log.i("BookSearch", "OnCreate()");
        setContentView(R.layout.activity_search);

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setBackgroundDrawableResource(R.drawable.mybookshelf2_1);

        //get references to our Button and EditText
        mButton = (Button)findViewById(R.id.button);
        mEdit = (EditText)findViewById(R.id.txtsearch);

        //setOnClickListener for the Go Button (WHEN the button is pressed)
        mButton.setOnClickListener( new View.OnClickListener() {
            public void onClick(View view)
            {
                //when the button Go is clicked
                //initialize query with the text from mEdit(EditText)
                query = (String) mEdit.getText().toString();
                //Get reference to our ListView
                booksList = (ListView)findViewById(R.id.bookList);
                //Set the click listener to launch the browser when a row is clicked.
                booksList.setOnItemClickListener(new OnItemClickListener() {
                    //this in the method called on the Click of a row
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int pos,long id) {
                        currentId = mAdapter.getItem(pos).getID();
                        Log.i("GetIdBook",currentId);
                        Intent openBookOverviewActivity = new Intent(SearchActivity.this, BookOverviewActivity.class);
                        openBookOverviewActivity.putExtra("EXTRA_ID", currentId);
                        startActivity(openBookOverviewActivity);
                    }

                });
		/*
		 * If network is available download the xml from the Internet.
		 * If not then try to use the local file from last time.
		 */
                if(isNetworkAvailable()){
                    Log.i("BookList", "starting download Task");
                    BooksDownloadTask download = new BooksDownloadTask();
                    download.execute();
                }else{
                    AlertDialog.Builder alert_network = new AlertDialog.Builder(SearchActivity.this);
                    alert_network.setTitle("Connection failed")
                            .setMessage("Please, check your internet connection and try again")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent openMainactivity = new Intent(SearchActivity.this, MainActivity.class);
                                    startActivity(openMainactivity);
                                }
                            });
                    alert_network.show();
                    //mAdapter = new BookListAdapter(getApplicationContext(), -1, BooksXmlPullParser.getBooksFromFile(SearchActivity.this));
                    //booksList.setAdapter(mAdapter);
                }
            }
        });


    }

    //Helper method to determine if Internet connection is available.
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getCurrentId(){
        return currentId;
    }
    /*
     * AsyncTask that will download the xml file for us and store it locally.
     * After the download is done we'll parse the local file.
     */
    private class BooksDownloadTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... arg0) {
            //Download the file
            try {
                Downloader.DownloadFromUrl("https://www.goodreads.com/search/index.xml?key=s6NJdnVHBcIa30pL6WF8Gg&q=" + query, openFileOutput("BookList.xml", Context.MODE_PRIVATE));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            //setup our Adapter and set it to the ListView.
            mAdapter = new BookListAdapter(SearchActivity.this, -1, BooksXmlPullParser.getBooksFromFile(SearchActivity.this));
            booksList.setAdapter(mAdapter);
            Log.i("BookList", "adapter size = "+ mAdapter.getCount());
        }



    }

}
