package com.gatech.spark.helper;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.MenuItem;

public abstract class MapOverlay {

	protected GoogleMap map;
	protected MenuItem menuItem;
	protected boolean isVisible;

	public MapOverlay(GoogleMap map) {
		this.map = map;
		this.menuItem = null;
		this.isVisible = false;
	}

	/**
	 * Gets the current visibility of the overlay
	 * 
	 * @return the current visibility
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Sets the menu item for this overlay
	 * 
	 * @param item
	 */
	public final void setMenuItem(MenuItem item) {
		this.menuItem = item;
		updateMenuItem();
	}

	/**
	 * Prepares the overlays menu item before menu is displayed.
	 */
	public abstract void updateMenuItem();

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
	 * Sets the visibility of overlay items
	 * 
	 * @param visibility
	 */
	public final void setVisibility(boolean visibility) {
		if (visibility) {
			show();
		} else {
			hide();
		}
		isVisible = visibility;
		updateMenuItem();
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
	 * 
	 * @param settings
	 */
	public abstract void save(SharedPreferences settings);

	/**
	 * Load settings from preferences file
	 * 
	 * @param settings
	 */
	public abstract void load(SharedPreferences settings);

	/**
	 * Determines whether a particular marker is on this overlay
	 * 
	 * @param marker
	 * @return true if `marker` is from this overlay, false otherwise
	 */
	public abstract boolean isMember(Marker marker);

	/**
	 * Callback performed when a marker is clicked. Must check to make sure that
	 * the marker that was clicked is of this overlay type
	 * 
	 * @param marker	Marker that was clicked
	 * @param activity	Current active activity
	 * @return			True if the click event was handled by this overlay, else false
	 */
	public boolean onMarkerClick(Marker marker, Activity activity) {
		return false;
	}

}
