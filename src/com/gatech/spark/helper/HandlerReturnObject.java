package com.gatech.spark.helper;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 4/3/13
 * Time: 8:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class HandlerReturnObject<T>
{
    private Boolean   isValid;
    private String    message;
    private T         object;
    private long      epochTimeStamp;

    public HandlerReturnObject( Boolean isValid, String message, T object )
    {
        this( isValid, message, object, -1 );
    }

    public HandlerReturnObject( Boolean isValid, String message, T object, long epochTimeStamp )
    {
        this.isValid = isValid;
        this.message = message;
        this.object = object;
        this.epochTimeStamp = epochTimeStamp;
    }

    public Boolean isValid()
    {
        return isValid;
    }

    public String getMessage()
    {
        return message;
    }

    public T getObject()
    {
        return object;
    }


    public long getEpochTimeStamp()
    {
        return this.epochTimeStamp;
    }
}
