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

	public static Marker addDraggableMarker(GoogleMap map, LatLng position) {
		return map.addMarker(new MarkerOptions().position(position).draggable(true));
	}

	public static Marker addSearchResultMarker(GoogleMap map, LocationSearchResult res) {
		MarkerOptions markerOptions = new MarkerOptions()
            .position(res.getLatLng())
            .title(res.getAddress().getAddressLine(0))
            .snippet(res.getAddress().getAddressLine(1))
            .draggable(false);
		return map.addMarker(markerOptions);
    }

}