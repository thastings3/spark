package com.gatech.spark.helper;

import com.gatech.spark.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Class to make customized markers
 */
public class MarkerPlacer {

	private GoogleMap map;

	public MarkerPlacer(GoogleMap map) {
		this.map = map;
	}

	public Marker addWhatsHotMarker(LatLng position, float radius) {
		MarkerOptions markerOptions = new MarkerOptions()
			.position(position)
			.anchor(0.5f, 0.5f)
			.title("this is hot hot hot!")
			.draggable(false)
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.whats_hot));
		return map.addMarker(markerOptions);
	}
}