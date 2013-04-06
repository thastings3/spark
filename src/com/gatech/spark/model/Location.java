package com.gatech.spark.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 4/3/13
 * Time: 8:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class Location implements Parcelable {
    private double latitude;
    private double longitude;

    public Location()
    {
        this.latitude = -1;
        this.longitude = -1;
    }

    public Location(Parcel in)
    {
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel( Parcel dest, int flags )
    {
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        public Location createFromParcel( Parcel in )
        {
            return new Location( in );
        }

        public Location[] newArray( int size )
        {
            return new Location[size];
        }
    };

}
