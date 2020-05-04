package com.example.cecilia.mybookshelf;

/**
 * Created by Cecilia on 01/09/2017.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by utente on 30/07/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DB_NAME="MyBooks";

    /*constructor*/
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }


    /*
    -- onCreate: viene invocato nel momento in cui non si trova nello spazio dell’applicazione un
    database con nome indicato nel costruttore. Da ricordare che onCreate verrà invocato una sola volta,
    quando il database non esiste ancora. Il parametro passato in input è un riferimento all’oggetto
    che astrae il database. La classe SQLiteDatabase è importantissima in quanto per suo tramite
    invieremo i comandi di gestione dei dati. Il metodo onCreate contiene la query SQL che serve a
    creare il contenuto del database. Questo è l’applicazione del primo step, enunciato prima.
    Notare che al suo interno non c’è alcun comando CREATE DATABASE in quanto il database stesso
    è già stato creato dal sistema. Il comando SQL di creazione verrà invocato mediante execSQL;
    -- onUpgrade: viene invocato nel momento in cui si richiede una versione del database più aggiornata
    di quella presente su disco. Questo metodo contiene solitamente alcune query che permettono di
    adeguare il database alla versione richiesta.
    */

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        /*method called ONCE */
        //TABLE 1: READ
        String q="CREATE TABLE "+DatabaseStrings.TABLE_NAME1 +
                " ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseStrings.FIELD_TITLE+" TEXT," +
                DatabaseStrings.FIELD_YEAR+" TEXT," +
                DatabaseStrings.FIELD_AUTHOR+" TEXT," +
                DatabaseStrings.FIELD_PAGES+" TEXT," +
                DatabaseStrings.FIELD_IMAGE+" TEXT," +
                DatabaseStrings.FIELD_ABOUT+" TEXT," +
                DatabaseStrings.FIELD_RATING_GEN+" TEXT," +
                DatabaseStrings.FIELD_RATING_PERS+" TEXT," +
                DatabaseStrings.FIELD_NOTES+" TEXT," +
                DatabaseStrings.FIELD_DATE_END+" TEXT)";
        db.execSQL(q);

        //TABLE 2: CURRENTLY READING
        String q2="CREATE TABLE "+DatabaseStrings.TABLE_NAME2 +
                " ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseStrings.FIELD_TITLE+" TEXT," +
                DatabaseStrings.FIELD_YEAR+" TEXT," +
                DatabaseStrings.FIELD_AUTHOR+" TEXT," +
                DatabaseStrings.FIELD_PAGES+" TEXT," +
                DatabaseStrings.FIELD_IMAGE+" TEXT," +
                DatabaseStrings.FIELD_ABOUT+" TEXT," +
                DatabaseStrings.FIELD_RATING_GEN+" TEXT," +
                DatabaseStrings.FIELD_DATE_START+" TEXT)";
        db.execSQL(q2);

        //TABLE 3: TO READ
        String q3="CREATE TABLE "+DatabaseStrings.TABLE_NAME3 +
                " ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseStrings.FIELD_TITLE+" TEXT," +
                DatabaseStrings.FIELD_YEAR+" TEXT," +
                DatabaseStrings.FIELD_AUTHOR+" TEXT," +
                DatabaseStrings.FIELD_PAGES+" TEXT," +
                DatabaseStrings.FIELD_IMAGE+" TEXT," +
                DatabaseStrings.FIELD_ABOUT+" TEXT," +
                DatabaseStrings.FIELD_RATING_GEN+" TEXT," +
                DatabaseStrings.FIELD_PRIORITY+" TEXT)";
        db.execSQL(q3);
    }

    /* It's called when a newer version of the db is requested. The method usually contains some
    queries which allow to adequate the db to the requested version */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseStrings.TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseStrings.TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseStrings.TABLE_NAME3);
        onCreate(db);
    }


}
