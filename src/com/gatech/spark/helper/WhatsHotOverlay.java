/**
 * 
 */
package com.gatech.spark.helper;

import java.util.ArrayList;
import java.util.Collection;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import android.content.SharedPreferences;

/**
 * Shows and hides the "What's Hot" items
 * 
 * @author sam
 * 
 */
public class WhatsHotOverlay extends MapOverlay {

	private static final LatLng GT = new LatLng(33.78102, -84.400363);
	private static final LatLng SoNo = new LatLng(33.769872, -84.384527);
	private static final String PREFS_KEY_VISIBILITY =
	        "WhatsHotOverlay.Visibility";

	private Collection<WhatsHotOverlayItem> hotSpotList;

	public WhatsHotOverlay(GoogleMap map) {
		super(map);
		hotSpotList = new ArrayList<WhatsHotOverlayItem>();
	}

	@Override
	public void populate() {
		clear();

		// TODO: get this from the server
		LatLng[] locList = { GT, SoNo };
		for (LatLng loc : locList) {
			hotSpotList.add(new WhatsHotOverlayItem(loc));
		}
	}

	@Override
	public void clear() {
		hotSpotList.clear();
	}

	@Override
	protected void show() {
		populate();
		for (WhatsHotOverlayItem item : hotSpotList) {
			item.addMarker(map);
			item.show();
		}
	}

	@Override
	protected void hide() {
		for (WhatsHotOverlayItem item : hotSpotList) {
			item.hide();
		}
	}

	@Override
	public void save(SharedPreferences settings) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(PREFS_KEY_VISIBILITY, getVisibility());
		editor.commit();
	}

	@Override
	public void load(SharedPreferences settings) {
		boolean visibility = settings.getBoolean(PREFS_KEY_VISIBILITY, false);
		setVisibility(visibility);
	}

	@Override
	public boolean isMember(Marker marker) {
		return WhatsHotOverlayItem.isMember(marker);
	}
}
