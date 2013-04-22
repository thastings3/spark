package com.gatech.spark.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 4/17/13
 * Time: 9:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SparkLocation implements Parcelable {
    private double latitude;
    private double longitude;
    private int id;
    private String name;
    private SparkAddress sparkAddress;

    public SparkLocation()
    {
        this.latitude = 0;
        this.longitude = 0;
        this.id = -1;
        this.name = "";
        this.sparkAddress = new SparkAddress();
    }

    public SparkLocation(Parcel in)
    {
        latitude = in.readDouble();
        longitude = in.readDouble();
        id = in.readInt();
        name = in.readString();
        sparkAddress = in.readParcelable(SparkAddress.class.getClassLoader());
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SparkAddress getSparkAddress() {
        return sparkAddress;
    }

    public void setSparkAddress(SparkAddress sparkAddress) {
        this.sparkAddress = sparkAddress;
    }

    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel( Parcel dest, int flags )
    {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeParcelable( this.sparkAddress, flags );
    }

    public static final Parcelable.Creator<SparkLocation> CREATOR = new Parcelable.Creator<SparkLocation>() {
        public SparkLocation createFromParcel( Parcel in )
        {
            return new SparkLocation( in );
        }

        public SparkLocation[] newArray( int size )
        {
            return new SparkLocation[size];
        }
    };


}
