package com.gatech.spark.helper;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import android.content.SharedPreferences;

public abstract class MapOverlay {

	protected GoogleMap map;
	private boolean isVisible;
	
	public MapOverlay(GoogleMap map) {
		this.map = map;
		this.isVisible = false;
	}

	/**
	 * Populates overlay items from data source
	 */
	public abstract void populate();
	
	/**
	 * Clears overlay items
	 */
	public abstract void clear();
	

	/**
	 * Toggle overlay items visibility
	 */
	public void toggle() {
		setVisibility(!isVisible);
	}

	
	/**
	 * Gets the current visibility of the overlay
	 * @return the current visibility
	 */
	public boolean getVisibility() {
		return isVisible;
	}

	/**
	 * Sets the visibility of overlay items
	 * @param visibility
	 */
	public void setVisibility(boolean visibility) {
		if (visibility) {
			show();
		} else {
			hide();
		}
		isVisible = visibility;
	}

	/**
	 * Show overlay items on map
	 */
	protected abstract void show();

	/**
	 * Hide overlay items from map
	 */
	protected abstract void hide();

	/**
	 * Save preferences to settings file
	 * @param settings
	 */
	public abstract void save(SharedPreferences settings);

	/**
	 * Load settings from preferences file
	 * @param settings
	 */
	public abstract void load(SharedPreferences settings);
	
	/**
	 * Determines whether a particular marker is on this overlay
	 * @param marker
	 * @return true if `marker` is from this overlay, false otherwise
	 */
	public abstract boolean isMember(Marker marker);
}
