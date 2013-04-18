/**
 * 
 */
package com.gatech.spark.overlay;

import android.util.Log;

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
	private static final String TAG = "spark.OverlayItem";

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
			Log.d(TAG, "Seting marker at " + marker.getPosition() + " to visible");
			marker.setVisible(true);
		}
	}
	
	public void hide() {
		if (marker != null) {
			marker.setVisible(false);
		}
	}
}
