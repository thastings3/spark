package com.gatech.spark.model;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 4/17/13
 * Time: 9:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SparkLocation {
    private double latitude;
    private double longitude;
    private int id;
    private String name;
    private SparkAddress sparkAddress;

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
}
