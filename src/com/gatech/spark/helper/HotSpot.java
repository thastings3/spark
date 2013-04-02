package com.gatech.spark.helper;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class HotSpot {
	private LatLng loc;
	private Marker marker;
	
	public HotSpot(LatLng loc) {
		this.loc = loc;
	}

	public void addMarker(GoogleMap map) {
		marker = MarkerPlacer.addWhatsHotMarker(map, loc);
	}
	
	public void clearMarker() {
		if (marker != null) {
			marker.remove();
		}
	}
}
