package com.gatech.spark.model;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 4/20/13
 * Time: 2:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class SparkParkingLot
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
}
