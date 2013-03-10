package com.gatech.spark.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * To change this template use File | Settings | File Templates.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME    = "spark";
    private static final int    DATABASE_VERSION = 1;
    private static SqliteHelper dbHelper;


    private SqliteHelper( Context context )
    {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    public static synchronized SqliteHelper getDbHelper( Context context )
    {
        if ( dbHelper == null )
        {
            dbHelper = new SqliteHelper( context );
        }
        return dbHelper;
    }


    @Override
    public void onCreate( SQLiteDatabase db )
    {
        //SubscriptionTable.onCreate(db);
        //TODO create tables here
    }


    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
    {
        //TODO alter tables here if needed.
    }
}
