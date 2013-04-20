package com.gatech.spark.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 4/18/13
 * Time: 9:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class Subscription implements Parcelable
{
    private int pk;
    private String name;
    private double latitude;
    private double longitude;
    private String placeReference;

    public Subscription()
    {
        pk = -1;
        name = "";
        latitude = 0;
        longitude = 0;
        placeReference = "";
    }

    public Subscription(String name, double lat, double lon, String placeReference)
    {
        this.name = name;
        latitude = lat;
        longitude = lon;
        this.placeReference = placeReference;
    }

    private Subscription(Parcel in )
    {
        this();
        this.pk = in.readInt();
        this.name = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.placeReference = in.readString();
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPlaceReference() {
    	return placeReference;
    }

    public void setPlaceReference(String placeReference) {
    	this.placeReference = placeReference;
    }

    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel( Parcel dest, int flags ) {
        dest.writeInt(pk);
        dest.writeString(name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(placeReference);
    }

    public static final Parcelable.Creator<Subscription> CREATOR = new Parcelable.Creator<Subscription>() {
        public Subscription createFromParcel( Parcel in )
        {
            return new Subscription( in );
        }

        public Subscription[] newArray( int size )
        {
            return new Subscription[size];
        }
    };
}
