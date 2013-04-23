package com.gatech.spark.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 4/20/13
 * Time: 2:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class SparkParkingLot implements Parcelable
{
    private int capacity;
    private boolean covered;
    private int parkingLotID;
    private double price;
    private int priceTypeID;
    private SparkLocation location;

    public SparkParkingLot()
    {
        this.capacity = 0;
        this.covered = false;
        this.parkingLotID = -1;
        this.price = 0.0;
        this.priceTypeID = -1;
        this.location = new SparkLocation();
    }

    public SparkParkingLot(Parcel in) {
        this.capacity = in.readInt();
        this.covered  = in.readByte() == 1;
        this.parkingLotID = in.readInt();
        this.price = in.readDouble();
        this.priceTypeID = in.readInt();
        this.location = in.readParcelable(SparkLocation.class.getClassLoader());

        //dest.writeByte( (byte) ( isFirstDateAttained ? 1 : 0 ) );
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isCovered() {
        return covered;
    }

    public void setCovered(boolean covered) {
        this.covered = covered;
    }

    public int getParkingLotID() {
        return parkingLotID;
    }

    public void setParkingLotID(int parkingLotID) {
        this.parkingLotID = parkingLotID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPriceTypeID() {
        return priceTypeID;
    }

    public void setPriceTypeID(int priceTypeID) {
        this.priceTypeID = priceTypeID;
    }

    public SparkLocation getLocation() {
        return location;
    }

    public void setLocation(SparkLocation location) {
        this.location = location;
    }

    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel( Parcel dest, int flags )
    {
        dest.writeInt(this.capacity);
        dest.writeByte( (byte) ( covered ? 1 : 0 ) );
        dest.writeInt(parkingLotID);
        dest.writeDouble(price);
        dest.writeInt(priceTypeID);
        dest.writeParcelable(location, flags);
    }

    public static final Parcelable.Creator<SparkParkingLot> CREATOR = new Parcelable.Creator<SparkParkingLot>() {
        public SparkParkingLot createFromParcel( Parcel in )
        {
            return new SparkParkingLot( in );
        }

        public SparkParkingLot[] newArray( int size )
        {
            return new SparkParkingLot[size];
        }
    };
}
