package com.gatech.spark.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * To change this template use File | Settings | File Templates.
 */
public class SubscriptionTable
{
    //TODO  time location name, id
    //FK to table_preferred lot location, id

    public static final String TB_SUBSCRIPTION         = "tb_subscription";
    public static final String PK_SUBSCRIPTION         = "subscription";
    public static final String SUBSCRIPTION_NAME       = "name";
    public static final String SUBSCRIPTION_LATITUDE   = "latitude";
    public static final String SUBSCRIPTION_LONGITUDE  = "longitude";
    public static final String SUBSCRIPTION_CREATED    = "created";

    /* @formatter:off */
    private static final String create_tb_subscription = "CREATE TABLE "
            + TB_SUBSCRIPTION
                + " ( "
                    + PK_SUBSCRIPTION        + " INTEGER NOT NULL , "
                    + SUBSCRIPTION_NAME      + " VARCHAR(128) NOT NULL , "
                    + SUBSCRIPTION_LATITUDE  + " REAL NOT NULL , "
                    + SUBSCRIPTION_LONGITUDE + " REAL NOT NULL ,"
                    + SUBSCRIPTION_CREATED   + " DATETIME NOT NULL DEFAULT ( DATETIME('now', 'localtime') ) , "
                    +   "PRIMARY KEY ( " + PK_SUBSCRIPTION + " )"
                + " ) ";

    /* @formatter:on */
    public static void onCreate( SQLiteDatabase db )
    {
        db.execSQL( create_tb_subscription );
    }

    public static void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
    {
        //db.alter if needed.
    }
}
