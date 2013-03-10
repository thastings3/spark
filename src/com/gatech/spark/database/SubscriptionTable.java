package com.gatech.spark.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * To change this template use File | Settings | File Templates.
 */
public class SubscriptionTable {
    //TODO  time location name, id
    //FK to table_preferred lot location, id

    public static final String TB_SUBSCRIPTION = "tb_subscription";
    public static final String SUBSCRIPTION_LOT = "lot";
    public static final String SUBSCRIPTION_LOCATION = "location";
    /* @formatter:off */
    private static final String create_tb_subscription = "CREATE TABLE "
            + TB_SUBSCRIPTION
                + " ( "
                    + SUBSCRIPTION_LOT       + " VARCHAR(128) NOT NULL , "
                    + SUBSCRIPTION_LOCATION  + " VARCHAR(128) NOT NULL "
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
