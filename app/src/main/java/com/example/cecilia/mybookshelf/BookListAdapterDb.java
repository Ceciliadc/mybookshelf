package com.example.cecilia.mybookshelf;

/**
 * Created by Cecilia on 01/09/2017.
 */
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/* Library used for the Image Loader*/
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class BookListAdapterDb extends ArrayAdapter<Book>{
    ImageLoader imageLoader;
    DisplayImageOptions options;
    private Context context;
    private String table;


    //we are not using the parameter ID but we are bound to place it in the constructor
    public BookListAdapterDb(Context ctx, int textViewResourceId, List<Book> books, String tbl) {
        super(ctx, textViewResourceId, books);
        //I'm saving it in order to call an activity in the Adapter
        this.context=ctx;
        //in order to get the Table
        this.table=tbl;

        //Setup the ImageLoader, we'll use this to display our images
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ctx).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        //Setup options for ImageLoader so it will handle caching for us.
        options = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .build();

    }


    /*
     * (non-Javadoc)
     * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
     *
     * This method is responsible for creating row views out of a StackSite object that can be put
     * into our ListView
     */
    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        //position accessible from the inner class (delete button onclick())
        final int position = pos;

        RelativeLayout row = (RelativeLayout)convertView;
        Log.i("BookListAdapter", "getView pos = " + pos);
        if(null == row){
            //No recycled View, we have to inflate one(new one)
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = (RelativeLayout)inflater.inflate(R.layout.layout_row_book_db, null);
        }

        //Get our View References
        final ImageView iconImg = (ImageView)row.findViewById(R.id.iconImg_read);
        TextView titleTxt = (TextView)row.findViewById(R.id.titleTxt_read);
        TextView yearTxt = (TextView)row.findViewById(R.id.yearTxt_read);
        TextView authorTxt = (TextView)row.findViewById(R.id.authorTxt_read);
        final ProgressBar indicator = (ProgressBar)row.findViewById(R.id.progress_read);

        //Initially we want the progress indicator visible, and the image invisible
        indicator.setVisibility(View.VISIBLE);
        iconImg.setVisibility(View.INVISIBLE);

        //Setup a listener we can use to switch from the loading indicator to the Image once it's ready
        ImageLoadingListener listener = new ImageLoadingListener(){

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

        //Load the image and use our options so caching is handled.
        //pos is the 1* parameter passed
        imageLoader.displayImage(getItem(pos).getImgUrl(), iconImg,options, listener);

        //Set the relevant text in our TextViews
        titleTxt.setText(getItem(pos).getTitle());
        yearTxt.setText(getItem(pos).getOriginal_publication_year());
        authorTxt.setText(getItem(pos).getAuthor());

        ImageButton deleteImageView = (ImageButton)row.findViewById(R.id.imageButton8);
        deleteImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert_delete = new AlertDialog.Builder((Activity) v.getContext());
                alert_delete.setMessage("Are you sure you want to delete this book?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseManager db=new DatabaseManager(context);
                                db.delete(table, Long.valueOf(getItem(position).getID()).longValue());
                                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                Intent refresh;
                                if (table == DatabaseStrings.TABLE_NAME1){
                                    refresh = new Intent(context, ReadActivity.class);
                                } else if(table == DatabaseStrings.TABLE_NAME2){
                                    refresh = new Intent(context, ReadingActivity.class);
                                } else if (table == DatabaseStrings.TABLE_NAME3){
                                    refresh = new Intent(context, ToReadActivity.class);
                                } else refresh = new Intent(context, ReadActivity.class);
                                context.startActivity(refresh);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alert_delete.show();

            }
        });

        return row;
    }
}

