package com.gatech.spark.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 4/3/13
 * Time: 8:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class Photo implements Parcelable {
    private String photoReference;
    private double width;
    private double height;
    private String htmlLink;

    public Photo()
    {
        this.photoReference = "";
        this.width = -1;
        this.height = -1;
        this.htmlLink = "";
    }

    private Photo( Parcel in )
    {
        this();
        this.photoReference = in.readString();
        this.width = in.readDouble();
        this.height = in.readDouble();
        this.htmlLink = in.readString();
    }


    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getHtmlLink() {
        return htmlLink;
    }

    public void setHtmlLink(String htmlLink) {
        this.htmlLink = htmlLink;
    }
    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel( Parcel dest, int flags )
    {
        dest.writeString(this.photoReference);
        dest.writeDouble(this.width);
        dest.writeDouble(this.height);
        dest.writeString(this.htmlLink);
    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        public Photo createFromParcel( Parcel in )
        {
            return new Photo( in );
        }

        public Photo[] newArray( int size )
        {
            return new Photo[size];
        }
    };

}
