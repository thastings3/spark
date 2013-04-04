package com.gatech.spark.model;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 4/3/13
 * Time: 8:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Place {

    private String name;
    private ArrayList<Type> types;
    private String formattedAddress;
    private String vicinity;
    private Location location;
    private double rating;
    private String iconLink;
    private String reference;
    private String id;
    private ArrayList<Event> events;
    private Photo photo;
    private int priceLevel;
    private Boolean openNow;


    public Place()
    {
        name = "";
        types = new ArrayList<Type>();
        formattedAddress = "";
        location = new Location();
        rating = 0;
        iconLink = "";
        reference = "";
        id = "";
        events = new ArrayList<Event>();
        photo = new Photo();
        priceLevel = 0;
        vicinity = "";

    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Type> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<Type> types) {
        this.types = types;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public int getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(int priceLevel) {
        this.priceLevel = priceLevel;
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public void addType(String name)
    {
        this.types.add(new Type(name));
    }

    public void setLatitude(double latitude)
    {
        this.location.setLatitude(latitude);
    }

    public void setLongitude(double longitude)
    {
        this.location.setLongitude(longitude);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
