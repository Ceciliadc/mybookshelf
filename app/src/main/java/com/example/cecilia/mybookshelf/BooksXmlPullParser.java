package com.example.cecilia.mybookshelf;

/**
 * Created by Cecilia on 01/09/2017.
 */


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;

public class BooksXmlPullParser {

    //xml params to search in the xml file
    static final String KEY_WORK = "work";
    static final String KEY_TITLE = "title";
    static final String KEY_YEAR = "original_publication_year";
    static final String KEY_ID = "id";
    static final String KEY_AUTHOR = "name";
    static final String KEY_IMAGE_URL = "image_url";


    public static List<Book> getBooksFromFile(Context ctx) {

        // List of Books that we will return
        List<Book> books;
        books = new ArrayList<Book>();

        // temp current Book while parsing (for each <work>)
        Book curBook = null;
        // temp current text value while parsing (between each tag)
        String curText = "";

        try {
            // Get our factory and PullParser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            // Open up InputStream and Reader of our file.
            FileInputStream fis = ctx.openFileInput("BookList.xml"); //the data are saved in the internal storage
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            // point the parser to our file.
            xpp.setInput(reader);

            // get initial eventType (to manage the events while parsing)
            int eventType = xpp.getEventType();
            boolean setID = false;

            /* We are using 3 tags: START_TAG (<name>), TEXT (title), END_TAG (</name>)*/

            // LOOP through pull events until we reach END_DOCUMENT
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Get the CURRENT TAG
                String tagname = xpp.getName();

                // React to different event types appropriately
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase(KEY_WORK)) {
                            // If we are starting a new <work> block we need
                            //a new Book object to represent it
                            curBook = new Book();
                            //we set ID to false to prevent it
                            //from considering the first ID
                            setID = false;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        //grab the current text so we can use it in END_TAG event
                        curText = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase(KEY_WORK)) {
                            // if </work> then we are done with current Book
                            // add it to the list.
                            books.add(curBook);
                        } else if (tagname.equalsIgnoreCase(KEY_YEAR)) { //altri casi di fine tag
                            // if </original_publication_year> use setOriginal_publication_year() on curSite
                            curBook.setOriginal_publication_year(curText);
                            //we set ID to true so that we
                            //can consider the next ID
                            setID=true;
                        } else if (tagname.equalsIgnoreCase(KEY_ID) && setID) {
                            // if </id> (second) use setID() on curBook
                            curBook.setID(curText);
                            //we set ID to false to prevent it
                            //from considering the other IDs
                            setID=false;
                        } else if (tagname.equalsIgnoreCase(KEY_TITLE)) {
                            // if </title> use setTitle() on curBook
                            curBook.setTitle(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_AUTHOR)) {
                            // if </about> use setAuthor() on curBook
                            curBook.setAuthor(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_IMAGE_URL)) {
                            // if </image_url> use setImgUrl() on curBook
                            curBook.setImgUrl(curText);
                        }
                        break;

                    default:
                        break;
                }
                //move on to next iteration
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // return the populated list.
        return books;
    }
}
