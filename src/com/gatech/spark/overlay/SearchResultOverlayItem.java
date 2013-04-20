package com.gatech.spark.overlay;

import com.gatech.spark.model.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SearchResultOverlayItem extends OverlayItem {
	private Place place;
	private static final String TAG = "SearchResult";
	private static int ID = 0;
	private int myID;

	public SearchResultOverlayItem(Place place) {
		super(new LatLng(place.getLocation().getLatitude(),
		                 place.getLocation().getLongitude()));
		this.place = place;
		myID = ID++;
	}

	public SearchResultOverlayItem() {
	}

	public Place getPlace() {
		return place;
	}

	@Override
	protected MarkerOptions getMarkerOptions() {
		return new MarkerOptions().title(place.getName())
			.snippet(createSnippet())
			.draggable(false);
	}

	@Override
	public String toString() {
		return "SearchResultOverlayItem" + getLatLng().toString() + ", " + place.getName();
	}

	@Override
	protected String getTag() {
		return TAG;
	}

	@Override
	protected int getID() {
		return myID;
	}
}
