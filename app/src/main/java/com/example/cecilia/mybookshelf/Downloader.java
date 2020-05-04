package com.example.cecilia.mybookshelf;

/**
 * Created by Cecilia on 01/09/2017.
 */


import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import android.util.Log;
/**
 * Created by utente on 03/07/2017.
 */



/*
 * Helper class for downloading a file.
 */
public class Downloader {

    //Tag for Log statements
    private static String myTag = "Searching Books";
    //Handler msg that represents we are posting a progress update.
    //In case there is a problem, it will still show a past result
    static final int POST_PROGRESS = 1;

    //method to solve the HTML string
    public static String toHTML(String t){
        StringBuilder s = new StringBuilder(t.length());
        CharacterIterator i = new StringCharacterIterator(t);
        for (char c = i.first(); c != CharacterIterator.DONE; c = i.next()) {
            switch (c) {
                case ' ':
                    s.append("+");
                    break;
                case '\'':
                    s.append("%27");
                    break;
                default:
                    s.append(c);
                    break;
            }
        }
        t = s.toString();
        return t;
    }

    /************************************************
     * Download a file from the Internet and store it locally
     *
     * @param URL - the url of the file to download
     * @param fos - a FileOutputStream to save the downloaded file to.
     ************************************************/
    public static void DownloadFromUrl(String URL, FileOutputStream fos) {  //downloader method
        try {
            URL=toHTML(URL);
            //Create URL Connection
            URL url = new URL(URL); //URL of the file
            //keep the start time so we can display how long it took to the Log.
            long startTime = System.currentTimeMillis();
            Log.d(myTag, "download beginning");

            //Open connection to URL
            URLConnection uc = url.openConnection();
            // this will be useful so that you can show a typical 0-100% progress bar
            //int lenghtOfFile = ucon.getContentLength();
            Log.i(myTag, "Opened Connection");

            //Define InputStreams to read from the URLConnection.
            InputStream is = uc.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            Log.i(myTag, "Got InputStream and BufferedInputStream");

            //Define OutputStreams to write to our file.
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            Log.i(myTag, "Got FileOutputStream and BufferedOutputStream");

            //Start reading the and writing our file.
            byte data[] = new byte[1024];
            //long total = 0;
            int count;
            //loop and read the current chunk
            while ((count = bis.read(data)) != -1) {
                //keep track of size for progress.
                //total += count;
                //write this chunk
                bos.write(data, 0, count);
            }
            //Flush and Close so that the file won't get corrupted.
            bos.flush();
            bos.close();

            Log.d(myTag, "download ready in "
                    + ((System.currentTimeMillis() - startTime))
                    + " milisec");
        } catch (IOException e) {
            Log.d(myTag, "Error: " + e);
        }
    }
}