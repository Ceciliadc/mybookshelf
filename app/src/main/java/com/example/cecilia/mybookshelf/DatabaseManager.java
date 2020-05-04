package com.example.cecilia.mybookshelf;

/**
 * Created by Cecilia on 01/09/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.renderscript.RenderScript;

/**
 * Created by utente on 17/08/2017.
 */

public class DatabaseManager {

    private DatabaseHelper dbhelper;

    public DatabaseManager(Context ctx)
    {
        dbhelper = new DatabaseHelper(ctx);
    }

    /* I metodi che vengono implementati mostrano tre operazioni basilari da svolgere sulla tabella del db:
       save per salvare una nuova scadenza, delete per cancellarne una in base all’id, query per recuperarne
       l’intero contenuto.
       Da questi metodi, emerge un modus operandi comune. Infatti per lavorare su un oggetto SQLiteDatabase,
       la prima cosa da fare è recuperarne un riferimento. Lo si può fare con i metodi di SQLiteOpenHelper,
       getReadableDatabase() e getWriteableDatabase() che restituiscono, rispettivamente, un riferimento al
       database “in sola lettura” e uno che ne permette la modifica.
       Sull’oggetto SQliteDatabase recuperato, si svolge una delle quattro operazioni CRUD, le azioni fondamentali
       della persistenza (Create, Read, Update, Delete). */

    /* insert: per l’inserimento. Riceve in input una stringa che contiene il nome della tabella e la lista di valori di
    inizializzazione del nuovo record mediante la classe ContentValues. Questa è una struttura a mappa che accetta
    coppie chiave/valore dove la chiave rappresenta il nome del campo della tabella;*/
    public void saveRead(String title, String author, String year, String pages, String img, String about, String g_rat, String p_rat, String notes, String date_end) {

        //we get an instance to the dbhelper
        SQLiteDatabase db=dbhelper.getWritableDatabase();

        //ID, TITLE, AUTHOR, YEAR, PAGES, IMAGE, ABOUT, RATING_GEN, RATING_PERS, NOTES, DATE_END
        //we create a set of values
        ContentValues cv = new ContentValues();
        cv.put(DatabaseStrings.FIELD_TITLE, title);
        cv.put(DatabaseStrings.FIELD_AUTHOR, author);
        cv.put(DatabaseStrings.FIELD_YEAR, year);
        cv.put(DatabaseStrings.FIELD_PAGES, pages);
        cv.put(DatabaseStrings.FIELD_IMAGE, img);
        cv.put(DatabaseStrings.FIELD_ABOUT, about);
        cv.put(DatabaseStrings.FIELD_RATING_GEN, g_rat);
        cv.put(DatabaseStrings.FIELD_RATING_PERS, p_rat);
        cv.put(DatabaseStrings.FIELD_NOTES, notes);
        cv.put(DatabaseStrings.FIELD_DATE_END, date_end);

        try
        {
            db.insert(DatabaseStrings.TABLE_NAME1, null,cv);
        }
        catch (SQLiteException sqle)
        {
            // Gestione delle eccezioni
        }
    }

    public void saveReading(String title, String author, String year, String pages, String img, String about, String g_rat, String date_start) {

        //we get an instance to the dbhelper
        SQLiteDatabase db=dbhelper.getWritableDatabase();

        //ID, TITLE, AUTHOR, YEAR, PAGES, IMAGE, ABOUT, RATING_GEN, DATE_START
        //we create a set of values
        ContentValues cv = new ContentValues();
        cv.put(DatabaseStrings.FIELD_TITLE, title);
        cv.put(DatabaseStrings.FIELD_AUTHOR, author);
        cv.put(DatabaseStrings.FIELD_YEAR, year);
        cv.put(DatabaseStrings.FIELD_PAGES, pages);
        cv.put(DatabaseStrings.FIELD_IMAGE, img);
        cv.put(DatabaseStrings.FIELD_ABOUT, about);
        cv.put(DatabaseStrings.FIELD_RATING_GEN, g_rat);
        cv.put(DatabaseStrings.FIELD_DATE_START, date_start);

        try
        {
            db.insert(DatabaseStrings.TABLE_NAME2, null,cv);
        }
        catch (SQLiteException sqle)
        {
            // Gestione delle eccezioni
        }
    }

    public void saveToRead(String title, String author, String year, String pages, String img, String about, String g_rat, String priority) {

        //we get an instance to the dbhelper
        SQLiteDatabase db=dbhelper.getWritableDatabase();

        //ID, TITLE, AUTHOR, YEAR, PAGES, IMAGE, ABOUT, RATING_GEN, PRIORITY
        //we create a set of values
        ContentValues cv = new ContentValues();
        cv.put(DatabaseStrings.FIELD_TITLE, title);
        cv.put(DatabaseStrings.FIELD_AUTHOR, author);
        cv.put(DatabaseStrings.FIELD_YEAR, year);
        cv.put(DatabaseStrings.FIELD_PAGES, pages);
        cv.put(DatabaseStrings.FIELD_IMAGE, img);
        cv.put(DatabaseStrings.FIELD_ABOUT, about);
        cv.put(DatabaseStrings.FIELD_RATING_GEN, g_rat);
        cv.put(DatabaseStrings.FIELD_PRIORITY, priority);

        try
        {
            db.insert(DatabaseStrings.TABLE_NAME3, null,cv);
        }
        catch (SQLiteException sqle)
        {
            // Gestione delle eccezioni
        }
    }

    /* delete: per la cancellazione di uno o più record della tabella;*/


    public boolean delete(String table, long id)
    {
        SQLiteDatabase db=dbhelper.getWritableDatabase();

        try
        {
            if (db.delete(table, DatabaseStrings.FIELD_ID+"=?", new String[]{Long.toString(id)})>0)
                return true;
            return true;
        }
        catch (SQLiteException sqle)
        {
            return false;
        }
    }

    public boolean update(long id, String table, String field, String val) {

        SQLiteDatabase db=dbhelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(field, val);

        try
        {
            if (db.update(table, cv,  DatabaseStrings.FIELD_ID+"=?", new String[]{Long.toString(id)})>0)
                return true;
            return true;
        }
        catch (SQLiteException sqle)
        {
            return false;
        }
    }

    public void dropRead(){
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        try
        {
            String q="DROP TABLE "+DatabaseStrings.TABLE_NAME1;
            db.execSQL(q);
            String q2="CREATE TABLE "+DatabaseStrings.TABLE_NAME1 +
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
            db.execSQL(q2);
        }
        catch (SQLiteException sqle)
        {

        }

    }
    public void dropReading(){
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        try
        {
            String q="DROP TABLE "+DatabaseStrings.TABLE_NAME2;
            db.execSQL(q);
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
        }
        catch (SQLiteException sqle)
        {

        }

    }

    public void dropToRead(){
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        try
        {
            String q="DROP TABLE "+DatabaseStrings.TABLE_NAME3;
            db.execSQL(q);
            String q2="CREATE TABLE "+DatabaseStrings.TABLE_NAME3 +
                    " ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DatabaseStrings.FIELD_TITLE+" TEXT," +
                    DatabaseStrings.FIELD_YEAR+" TEXT," +
                    DatabaseStrings.FIELD_AUTHOR+" TEXT," +
                    DatabaseStrings.FIELD_PAGES+" TEXT," +
                    DatabaseStrings.FIELD_IMAGE+" TEXT," +
                    DatabaseStrings.FIELD_ABOUT+" TEXT," +
                    DatabaseStrings.FIELD_RATING_GEN+" TEXT," +
                    DatabaseStrings.FIELD_PRIORITY+" TEXT)";
            db.execSQL(q2);
        }
        catch (SQLiteException sqle)
        {

        }

    }
    /* update: esegue modifiche. Il metodo associa i parametri usati nell’insert e nel delete.*/

    /* query: esegue la lettura sulle tabelle: mette in pratica il SELECT sui dati. I suoi svariati overload predispongono
    argomenti per ogni parametro che può essere inserito in una interrogazione di questo tipo (selezione, ordinamento,
    numero massimo di record, raggruppamento, etc.); */
    public Cursor query(String table, String order)
    {
        Cursor crs=null;
        try
        {
            //tramite crs posso usare il metodo getstrings(indice colonna)
            SQLiteDatabase db=dbhelper.getReadableDatabase();
            crs=db.query(table, null, null, null, null, null, order, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }
        return crs;
    }


}
