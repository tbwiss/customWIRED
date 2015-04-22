package com.wiss.thom.wiredmobpro.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wiss.thom.wiredmobpro.model.PostORM;

/**
 * Created by Thomas on 15.04.2015.
 */
public class DatabaseWrapper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseWrapper";

    private static final String DATABASE_NAME = "MyDatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseWrapper sInstance;


    private DatabaseWrapper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseWrapper getInstance(Context context) {


        if (sInstance == null) {
            sInstance = new DatabaseWrapper(context);
        }
        return sInstance;
    }

    /**
     * Called if the database named DATABASE_NAME doesn't exist in order to create it.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "Creating database [" + DATABASE_NAME + " v." + DATABASE_VERSION + "]...");
        sqLiteDatabase.execSQL(PostORM.SQL_CREATE_TABLE);
       // sqLiteDatabase.close();
    }

    /**
     * Called when the DATABASE_VERSION is increased.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrading database ["+DATABASE_NAME+" v." + oldVersion+"] to ["+DATABASE_NAME+" v." + newVersion+"]...");
        sqLiteDatabase.execSQL(PostORM.SQL_DROP_TABLE);
        onCreate(sqLiteDatabase);
    }
}
