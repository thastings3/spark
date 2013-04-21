package com.gatech.spark.helper;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * To change this template use File | Settings | File Templates.
 */
public class CommonHelper {

    /**
     * Displays a long toast message to the user. Pass in application context
     * and message to be displayed. Gravity is set to center.
     *
     * @param context
     * @param message
     */
    public static Toast showLongToast( Context context, String message )
    {
        return showLongToast( context, message, Gravity.CENTER );
    }

    /**
     * Displays a long toast message to the user. Pass in application context
     * and message to be displayed.
     *
     * @param context
     * @param message
     */
    public static Toast showLongToast( Context context, String message, int gravity )
    {
        return showLongToast(context, message, gravity, 0 , 0);
    }

    /**
     * Displays a long toast message to the user. Pass in application context
     * and message to be displayed.
     *
     * @param context
     * @param message
     */
    public static Toast showLongToast( Context context, String message, int gravity , int xOffset, int yOffset)
    {

        return showLongToast( context, true, message, gravity , xOffset, yOffset);
    }

    /**
     * Displays a long toast message to the user. Pass in application context
     * and message to be displayed.
     *
     * @param context
     * @param message
     */
    public static Toast showLongToast( Context context, boolean show, String message, int gravity , int xOffset, int yOffset)
    {
        Toast display_toast = Toast.makeText( context, message, Toast.LENGTH_LONG );
        display_toast.setGravity( gravity, xOffset, yOffset );

        display_toast.setText(message);
        if(show)
        {
            display_toast.show();
        }

        return display_toast;
    }





    /**
     * Tries to convert the string to an int. If there is an error it returns -1
     *
     * @param number
     * @return
     */
    public static int convertStringToInt( String number )
    {
        return convertStringToInt( number, -1 );
    }

    /**
     * Tries to convert the string to an Integer. If there is an error it
     * returns null
     *
     * @param number
     * @return
     */
    public static Integer convertStringToInteger( String number )
    {
        if ( number == null || number.isEmpty() )
        {
            return null;
        }

        try
        {
            return Integer.parseInt( number );
        }
        catch ( NumberFormatException e )
        {
            return null;
        }
    }

    /**
     *
     * @param number
     * @return
     */
    public static Double convertStringToDoubleObject( String number )
    {

        if ( number == null || number.isEmpty() )
        {
            return null;
        }
        try
        {
            return Double.parseDouble( number );
        }
        catch ( NumberFormatException e )
        {
            return null;
        }
    }

    /**
     * Returns a string representation of a boolean value.
     *
     * @param value
     * @return
     */
    public static String convertBooleanToString( boolean value )
    {
        return value ? "true" : "false";
    }

    /**
     * Returns a string representation of a int value.
     *
     * @param value
     * @return
     */
    public static String convertIntToBooleanString( int value )
    {
        return value == 1 ? "true" : "false";
    }

    /**
     * Tries to convert the string to an int. If there is an error it returns
     * defaultValue
     *
     * @param number
     * @param defaultValue
     * @return
     */
    public static int convertStringToInt( String number, int defaultValue )
    {
        try
        {
            return Integer.parseInt( number );
        }
        catch ( NumberFormatException e )
        {
            return (int) convertStringToDouble( number, defaultValue );
        }
    }

    /**
     *
     * @param number
     * @param defaultValue
     * @return
     */
    public static long convertStringToLong( String number, int defaultValue )
    {
        try
        {
            return Long.parseLong( number );
        }
        catch ( NumberFormatException e )
        {
            return (long) convertStringToDouble( number, defaultValue );
        }
    }

    /**
     *
     * @param number
     * @param defaultValue
     * @return
     */
    public static float convertStringToFloat( String number, float defaultValue )
    {
        try
        {
            return Float.parseFloat( number );
        }
        catch ( NumberFormatException e )
        {
            return defaultValue;
        }
    }

    /**
     *
     * @param number
     * @return
     */
    public static float convertStringToFloat( String number )
    {
        return convertStringToFloat( number, -1f );
    }

    /**
     *
     * @param number
     * @param defaultValue
     * @return
     */
    public static double convertStringToDouble( String number, double defaultValue )
    {
        try
        {
            return Double.parseDouble( number );
        }
        catch ( NumberFormatException e )
        {
            return defaultValue;
        }
    }

    /**
     *
     * @param number
     * @return
     */
    public static double convertStringToDouble( String number )
    {
        return convertStringToDouble( number, -1 );
    }

    /**
     *
     * @param bool
     * @return
     */
    public static boolean convertStringToBoolean( String bool )
    {
        bool = bool.toLowerCase().toString().trim();
        if ( bool.equalsIgnoreCase( "t" ) || bool.equalsIgnoreCase( "true" ) || bool.equalsIgnoreCase( "1" ) || bool.equalsIgnoreCase( "yes" ) || bool.equalsIgnoreCase( "y" ) || bool.equalsIgnoreCase( "ok" ) )
        {
            return true;
        }
        return false;
    }

    /**
     *
     * @param bool
     * @return
     */
    public static Boolean convertStringToBooleanObject( String bool )
    {
        bool = bool.toLowerCase().toString().trim();

        if ( bool.isEmpty() )
        {
            return null;
        }

        if ( bool.equalsIgnoreCase( "t" ) || bool.equalsIgnoreCase( "true" ) || bool.equalsIgnoreCase( "1" ) || bool.equalsIgnoreCase( "yes" ) || bool.equalsIgnoreCase( "y" ) )
        {
            return true;
        }
        return false;
    }

    /**
     * returns # of meters in given miles.
     * @param miles
     * @return
     */
    public static int convertMilesToMeters(int miles)
    {
        return (int)(miles * 1609.344);
    }

}
