package com.gatech.spark.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 4/17/13
 * Time: 9:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class SparkAddress implements Parcelable {
    private int id;
    private String city;
    private String country;
    private String phoneNumber;
    private int postalCode;
    private String state;
    private String streetLine;
    private String streetLine2;
    private String county;

    public SparkAddress()
    {
        this.id = -1;
        this.city = "";
        this.country = "";
        this.phoneNumber = "";
        this.postalCode = 0;
        this.state = "";
        this.streetLine = "";
        this.streetLine2 = "";
        this.county = "";
    }

    public SparkAddress(Parcel in)
    {
        id = in.readInt();
        city = in.readString();
        country  = in.readString();
        phoneNumber = in.readString();
        postalCode = in.readInt();
        state  = in.readString();
        streetLine = in.readString();
        streetLine2 = in.readString();
        county = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreetLine() {
        return streetLine;
    }

    public void setStreetLine(String streetLine) {
        this.streetLine = streetLine;
    }

    public String getStreetLine2() {
        return streetLine2;
    }

    public void setStreetLine2(String streetLine2) {
        this.streetLine2 = streetLine2;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel( Parcel dest, int flags )
    {
        dest.writeInt(id);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(phoneNumber);
        dest.writeInt(postalCode);
        dest.writeString(state);
        dest.writeString(streetLine);
        dest.writeString(streetLine2);
        dest.writeString(county);
    }

    public static final Parcelable.Creator<SparkAddress> CREATOR = new Parcelable.Creator<SparkAddress>() {
        public SparkAddress createFromParcel( Parcel in )
        {
            return new SparkAddress( in );
        }

        public SparkAddress[] newArray( int size )
        {
            return new SparkAddress[size];
        }
    };
}
