package com.gatech.spark.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.gatech.spark.helper.HandlerReturnObject;
import com.gatech.spark.model.Subscription;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * To change this template use File | Settings | File Templates.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME    = "spark";
    private static final int    DATABASE_VERSION = 4;
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
        SubscriptionTable.onCreate(db);
    }


    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
    {
        //TODO alter tables here if needed.
    	SubscriptionTable.onUpgrade(db, oldVersion, newVersion);
    }

    private void closeCursor( Cursor cursor )
    {
        if ( !cursor.isClosed() )
        {
            cursor.close();
        }
    }

    private String getStringByColumnName( Cursor cursor, String columnName )
    {
        return getStringByColumnIndex( cursor, cursor.getColumnIndex( columnName ) );
    }

    private String getStringByColumnIndex( Cursor cursor, int columnIndex )
    {
        if ( cursor.isNull( columnIndex ) )
        {
            return "";
        }
        return cursor.getString( columnIndex );
    }

    private Double getDoubleByColumnName( Cursor cursor, String columnName )
    {
        return getDoubleByColumnName( cursor, columnName, -1.0 );
    }

    private Double getDoubleByColumnName( Cursor cursor, String columnName, Double defaultValue )
    {
        return getDoubleByColumnIndex( cursor, cursor.getColumnIndex( columnName ), defaultValue );
    }

    private Double getDoubleByColumnIndex( Cursor cursor, int columnIndex, Double defaultValue )
    {
        if ( cursor.isNull( columnIndex ) )
        {
            return defaultValue;
        }
        return cursor.getDouble( columnIndex );
    }

    private Integer getIntegerByColumnName( Cursor cursor, String columnName )
    {
        return getIntegerByColumnName( cursor, columnName, -1 );
    }

    private Integer getIntegerByColumnName( Cursor cursor, String columnName, Integer defaultValue )
    {
        return getIntegerByColumnIndex( cursor, cursor.getColumnIndex( columnName ), defaultValue );
    }

    private Integer getIntegerByColumnIndex( Cursor cursor, int columnIndex, Integer defaultValue )
    {
        if ( cursor.isNull( columnIndex ) )
        {
            return defaultValue;
        }
        return cursor.getInt( columnIndex );
    }

    /**
     * Inserts a new subscription. Returns a handler object with the object
     * @param subscription
     * @return
     */
    public HandlerReturnObject<Subscription> insertSubscription( Subscription subscription)
    {
        SQLiteDatabase db = getWritableDatabase();
        long pk = -1;

        ContentValues cv = new ContentValues( 4 );
        cv.put( SubscriptionTable.SUBSCRIPTION_NAME, subscription.getName() );
        cv.put( SubscriptionTable.SUBSCRIPTION_LATITUDE, subscription.getLatitude() );
        cv.put( SubscriptionTable.SUBSCRIPTION_LONGITUDE, subscription.getLongitude() );
        cv.put( SubscriptionTable.SUBSCRIPTION_PLACE_REF, subscription.getPlaceReference() );
        try
        {
            pk = db.insert( SubscriptionTable.TB_SUBSCRIPTION, null, cv );
        }
        catch ( Exception e )
        {
            return new HandlerReturnObject<Subscription>(false, "Database Error", new Subscription());
        }

        if(pk == -1)
        {
            return new HandlerReturnObject<Subscription>(false, "Error Inserting Row.", new Subscription());
        }
        subscription.setPk((int)pk);
        return new HandlerReturnObject<Subscription>(true, "Success", subscription);
    }

    /**
     * Deletes the subscription from the database.
     * @param subscription
     * @return
     */
    public HandlerReturnObject<Subscription> deleteSubscription(Subscription subscription)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        if(subscription.getPk() == -1) {
            return new HandlerReturnObject<Subscription>(false, "Invalid Subscription", new Subscription());
        }

        int rowCount = db.delete( SubscriptionTable.TB_SUBSCRIPTION, SubscriptionTable.PK_SUBSCRIPTION + " = ?", new String[]{subscription.getPk() + ""} );

        if(rowCount > 0)
        {
            return new HandlerReturnObject<Subscription>(true, "Success", subscription);
        }
        return new HandlerReturnObject<Subscription>(false, "Error: Could not locate subscription in DB", new Subscription());
    }

    public HandlerReturnObject<ArrayList<Subscription>> getSubscriptions()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Subscription> subscriptions = new ArrayList<Subscription>();
        /* @formatter:off */

        String query = "SELECT * FROM " + SubscriptionTable.TB_SUBSCRIPTION + " ORDER BY " + SubscriptionTable.SUBSCRIPTION_CREATED;
        /* @formatter:on */
        Cursor cursor = db.rawQuery( query, new String[]{} );

        if ( cursor.moveToFirst() )
        {
            do
            {
                /* @formatter:off */
                Subscription subscription = new Subscription();
                subscription.setPk( getIntegerByColumnName( cursor, SubscriptionTable.PK_SUBSCRIPTION ) );
                subscription.setName( getStringByColumnName( cursor, SubscriptionTable.SUBSCRIPTION_NAME ) );
                subscription.setLatitude( getDoubleByColumnName( cursor, SubscriptionTable.SUBSCRIPTION_LATITUDE ) );
                subscription.setLongitude( getDoubleByColumnName( cursor, SubscriptionTable.SUBSCRIPTION_LONGITUDE ) );
                subscription.setPlaceReference( getStringByColumnName( cursor, SubscriptionTable.SUBSCRIPTION_PLACE_REF ) );
                subscriptions.add( subscription );
                /* @formatter:on */

            }
            while ( cursor.moveToNext() );
        }
        if ( !cursor.isClosed() )
        {
            cursor.close();
        }

        return new HandlerReturnObject<ArrayList<Subscription>>(true, "Success", subscriptions);
    }

}
