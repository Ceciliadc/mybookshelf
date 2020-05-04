package com.example.cecilia.mybookshelf;

/**
 * Created by Cecilia on 01/09/2017.
 */

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/* Library used for the Image Loader*/
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class BookListAdapter extends ArrayAdapter<Book>{
    ImageLoader imageLoader;
    DisplayImageOptions options;

    private TextView titleTxt;
    private TextView yearTxt;
    private TextView authorTxt;

    //we are not using the parameter ID but we are bound to place it in the constructor
    public BookListAdapter(Context ctx, int textViewResourceId, List<Book> books) {
        super(ctx, textViewResourceId, books);

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
        RelativeLayout row = (RelativeLayout)convertView;
        Log.i("BookListAdapter", "getView pos = " + pos);
        if(null == row){
            //No recycled View, we have to inflate one(new one)
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = (RelativeLayout)inflater.inflate(R.layout.layout_row_book, null);
        }

        //Get our View References
        final ImageView iconImg = (ImageView)row.findViewById(R.id.iconImg);
        titleTxt = (TextView)row.findViewById(R.id.titleTxt);
        yearTxt = (TextView)row.findViewById(R.id.yearTxt);
        authorTxt = (TextView)row.findViewById(R.id.authorTxt);
        final ProgressBar indicator = (ProgressBar)row.findViewById(R.id.progress);

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

        /*if ((getItem(pos).getOriginal_publication_year().matches("-?//d+(//.//d+)?"))){
            yearTxt.setText("("+getItem(pos).getOriginal_publication_year()+")");
        } else {
            yearTxt.setText("NA");
        }*/
        yearTxt.setText(getItem(pos).getOriginal_publication_year());
        authorTxt.setText(getItem(pos).getAuthor());

        return row;

    }
}
