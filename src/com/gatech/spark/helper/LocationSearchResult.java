package com.gatech.spark.helper;

import android.location.Address;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class LocationSearchResult {
	private Address addr;
	private Marker marker;
	
	public LocationSearchResult(Address addr) {
		this.addr = addr;
		this.marker = null;
	}
	
	public LatLng getLatLng() {
		return new LatLng(addr.getLatitude(), addr.getLongitude());
	}

	public void addMarker(GoogleMap map) {
		marker = MarkerPlacer.addSearchResultMarker(map, this);
	}
	
	public void clearMarker() {
		if (marker != null) {
			marker.remove();
		}
	}
	
	public Address getAddress() {
		return addr;
	}
}
