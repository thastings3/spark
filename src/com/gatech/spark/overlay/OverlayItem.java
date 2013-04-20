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

	/**
	 * 
	 * @return A string unique to this OverlayItem type
	 */
	protected abstract String getTag();

	/**
	 * 
	 * @return An int unique to this OverlyItem instance
	 */
	protected abstract int getID();

	protected String createSnippet() {
		return getTag() + getID();
	}

	/**
	 * Returns true if the marker is of this OverlayItem type
	 * @param marker
	 * @return
	 */
	public boolean isMember(Marker marker) {
		String snippet = marker.getSnippet();
		return (snippet != null) && snippet.startsWith(getTag());
	}

	/**
	 * Returns true if the marker is associated with this OverlayItem instance
	 * @param marker
	 * @return
	 */
	public boolean hasMarker(Marker marker) {
		String snippet = marker.getSnippet();
		return isMember(marker) && snippet.endsWith(String.valueOf(getID()));
	}
}
