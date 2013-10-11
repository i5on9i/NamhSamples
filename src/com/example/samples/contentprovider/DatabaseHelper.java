package com.example.samples.contentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class DatabaseHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME_NOTELIST = "notelist";
    public static final String TABLE_NAME_DOWNLOAD_ID = "download_id";

    public static final String _ID = BaseColumns._ID;
    public static final String DATE = Contract.Note.DATE;
    public static final String NAME = Contract.Note.NAME;
    public static final String NOTE = Contract.Note.NOTE;

    public static final String FILENAME = Contract.DownloadIds.FILENAME;
    public static final String DOWNLOAD_ID = Contract.DownloadIds.DOWNLOAD_ID;


    private static class OpenHelper extends SQLiteOpenHelper {


        /**
         * Sql example :
         * CREATE TABLE downloading
         * (
         * 		id INTEGER PRIMARY KEY AUTOINCREMENT,
         * 		filename TEXT NOT NULL,
         *		download_id INTEGER
         *	);
         */
        private static final String CREATE_TABLE_NOTELIST =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_NOTELIST + " ("
                        + _ID + " INTEGER PRIMARY KEY ASC, "
                        + DATE + " DATETIME NOT NULL, "
                        + NAME + " TEXT NOT NULL, "
                        + NOTE + " TEXT"
                        + ");";

        private static final String CREATE_TABLE_DOWNLOAD_ID =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_DOWNLOAD_ID + " ("
                        + _ID + " INTEGER PRIMARY KEY ASC, "
                        + FILENAME + " TEXT NOT NULL, "
                        + DOWNLOAD_ID + " INTEGER"
                        + ");";


        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {


            db.execSQL(CREATE_TABLE_NOTELIST);
            db.execSQL(CREATE_TABLE_DOWNLOAD_ID);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTELIST);
            onCreate(db);
        }
    }


    private SQLiteDatabase mWritableDb;
    private SQLiteDatabase mReadableDb;

    public SQLiteDatabase getWritableDatabase() {
        return mWritableDb;
    }

    public SQLiteDatabase getReadableDatabase() {
        return mReadableDb;
    }


    public DatabaseHelper(Context context) {

        OpenHelper openHelper = new OpenHelper(context);

        // getWritableDatabase()
        // : open or create depending on the DATABASE_VERSION
        mWritableDb = openHelper.getWritableDatabase();
        mReadableDb = openHelper.getReadableDatabase();
    }




}
