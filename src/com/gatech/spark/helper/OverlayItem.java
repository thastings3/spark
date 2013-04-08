/**
 * 
 */
package com.gatech.spark.helper;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * General class for displaying on map overlays
 * @author sam
 *
 */
public abstract class OverlayItem {
	private Marker marker;
	private LatLng loc;

	public OverlayItem() {		
	}

	public OverlayItem(LatLng loc) {
		this.loc = loc;
	}

	protected abstract MarkerOptions getMarkerOptions();

	public LatLng getLatLng() {
		return loc;
	}

	public void setLatLng(LatLng loc) {
		this.loc = loc;
	}

	public void addMarker(GoogleMap map) {
		marker = map.addMarker(getMarkerOptions().position(getLatLng()));
	}

	public void removeMarker() {
		if (marker != null) {
			marker.remove();
		}
	}
	
	public void show() {
		if (marker != null) {
			marker.setVisible(true);
		}
	}
	
	public void hide() {
		if (marker != null) {
			marker.setVisible(false);
		}
	}
}
