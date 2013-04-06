package com.gatech.spark.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 4/3/13
 * Time: 8:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Type implements Parcelable {
    private String name;


    public Type()
    {
        this.name = "";
    }

    private Type( Parcel in )
    {
        this();
        this.name = in.readString();
    }

    public Type(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel( Parcel dest, int flags )
    {
        dest.writeString( name );
    }

    public static final Parcelable.Creator<Type> CREATOR = new Parcelable.Creator<Type>() {
        public Type createFromParcel( Parcel in )
        {
            return new Type( in );
        }

        public Type[] newArray( int size )
        {
            return new Type[size];
        }
    };
}
