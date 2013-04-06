package com.gatech.spark.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 4/3/13
 * Time: 8:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class Event implements Parcelable {
    private String summary;
    private String eventId;
    private String url;

    public Event()
    {
        this.summary = "";
        this.eventId = "";
        this.url = "";
    }

    public Event(Parcel in)
    {
        this.summary = in.readString();
        this.eventId = in.readString();
        this.url = in.readString();
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel( Parcel dest, int flags )
    {
        dest.writeString(this.summary);
        dest.writeString(this.eventId);
        dest.writeString(this.url);
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        public Event createFromParcel( Parcel in )
        {
            return new Event( in );
        }

        public Event[] newArray( int size )
        {
            return new Event[size];
        }
    };
}
