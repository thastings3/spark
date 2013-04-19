package com.gatech.spark.overlay;

import com.gatech.spark.model.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SearchResultOverlayItem extends OverlayItem {
	private Place place;

	public SearchResultOverlayItem(Place place) {
		super(new LatLng(place.getLocation().getLatitude(),
                         place.getLocation().getLongitude()));
		this.place = place;
	}

	@Override
	protected MarkerOptions getMarkerOptions() {
		return new MarkerOptions().title(place.getName())
		                          .snippet(place.getFormattedAddress())
		                          .draggable(false);
	}

	@Override
	public String toString() {
		return "SearchResultOverlayItem" + getLatLng().toString() + ", " + place.getName();
	}
}
