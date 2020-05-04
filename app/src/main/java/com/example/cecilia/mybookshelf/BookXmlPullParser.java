package com.example.cecilia.mybookshelf;

/**
 * Created by Cecilia on 01/09/2017.
 */


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.text.Html;

public class BookXmlPullParser {

    //xml params to search in the xml file
    static final String KEY_TITLE = "title";
    static final String KEY_YEAR = "original_publication_year";
    static final String KEY_AUTHOR = "name";
    static final String KEY_IMAGE_URL = "image_url";
    static final String KEY_ABOUT = "description";
    static final String KEY_PAGES = "num_pages";
    static final String KEY_RATING = "average_rating";

    public static void getBookFromFile(Context ctx, Book book) {

        String curText = "";

        try {
            // Get our factory and PullParser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            // Open up InputStream and Reader of our file.
            FileInputStream fis = ctx.openFileInput("BookOverview.xml"); //the data are saved in the internal storage
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            // point the parser to our file.
            xpp.setInput(reader);

            // get initial eventType (to manage the events while parsing)
            int eventType = xpp.getEventType();

            /* We are using 3 tags: START_TAG (<name>), TEXT (title), END_TAG (</name>)*/

            // LOOP through pull events until we reach END_DOCUMENT
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Get the CURRENT TAG
                String tagname = xpp.getName();

                // React to different event types appropriately
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        //grab the current text so we can use it in END_TAG event
                        curText = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase(KEY_TITLE)) {
                            book.setTitle(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_IMAGE_URL)) {
                            book.setImgUrl(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_ABOUT)) {
                            curText = curText.replaceAll("<br />", "\n");
                            curText = curText.replaceAll("<p>", " ");
                            curText = curText.replaceAll("</p>", "");
                            curText = curText.replaceAll("<i>", "");
                            curText = curText.replaceAll("</i>", "");
                            curText = curText.replaceAll("<b>", "");
                            curText = curText.replaceAll("</b>", "");
                            curText = curText.replaceAll("\\[ACE\\]", "");
                            curText = curText.replaceAll("<a([^<]*)</a>", "Check www.goodreads.com for more info");
                            //Html.fromHtml(curText).toString();
                            book.setAbout(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_YEAR)) {
                            book.setOriginal_publication_year(curText);
                        }  else if (tagname.equalsIgnoreCase(KEY_RATING)) {
                            book.setRating(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_PAGES)) {
                            book.setPages(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_AUTHOR)) {
                            //stop the parsing to avoid waste of resources
                            book.setAuthor(curText);
                            return;
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
        return;
    }

}
