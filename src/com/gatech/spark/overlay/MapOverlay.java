package com.gatech.spark.overlay;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gatech.spark.fragment.SparkMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public abstract class MapOverlay {

	private static final String TAG = "spark.MapOverlay";
	private SparkMapFragment fragment;
	protected GoogleMap map;
	protected MenuItem menuItem;

	public MapOverlay(SparkMapFragment fragment, GoogleMap map) {
		this.fragment = fragment;
		this.map = map;
		this.menuItem = null;
	}

	/**
	 * Gets the current active activity.
	 */
	public final Activity getActivity() {
		return fragment.getActivity();
	}

	public final GoogleMap getMap() {
		return fragment.getMap();
	}

	public final SparkMapFragment getFragment() {
		return fragment;
	}

	/**
	 * Gets the current visibility of the overlay
	 * 
	 * @return the current visibility
	 */
	public abstract boolean isVisible();

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
		setVisibility(!isVisible());
	}

	/**
	 * Sets the visibility of overlay items
	 * 
	 * @param visibility
	 */
	public abstract void setVisibility(boolean visibility);

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
	 * Returns true if the marker is associated with this overlay
	 * 
	 * @param marker Marker to determine if it is from this overlay
	 * @return true if the `marker` is from this overlay, false otherwise
	 */
	public abstract boolean isMember(Marker marker);

	/**
	 * Returns the OverlayItem associated with the marker on this overlay.
	 * 
	 * @param marker Marker to find the associated OverlayItem from
	 * @return the associated OverlayItem if `marker` is from this overlay, null otherwise
	 */
	public OverlayItem getOverlayItem(Marker marker) {
		return null;
	}

	/**
	 * Callback performed when a marker is clicked. Must check to make sure that
	 * the marker that was clicked is of this overlay type.
	 * 
	 * @param marker	Marker that was clicked
	 * @return			True if the click event was handled by this overlay, else false
	 */
	public boolean onMarkerClick(Marker marker) {
		return false;
	}

	/**
	 * Call back performed when the options menu is created. Use to set the
	 * overlay's menu item.
	 * 
	 * @param menu
	 */
	public abstract void onCreateOptionsMenu(Menu menu);

	/**
	 * Callback performed when a menu item is clicked. Must check to make sure
	 * that the marker that was clicked is of this overlay type.
	 * 
	 * @param marker Marker that was clicked
	 * @return True if the click event was handled by this overlay, else false
	 * 
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}

	/**
	 * Custom info windows when markers are clicked.
	 * getInfoWindow() is called first. If it returns null,
	 * getInfoContents() is called second. If it returns null, the default
	 * info window is used.
	 */
	public View getInfoWindow(LayoutInflater inflator, Marker marker) {
		return null;
	}

	public View getInfoContents(LayoutInflater inflator, Marker marker) {
		return null;
	}

	/**
	 * Called when a marker's info window is clicked. Return true if
	 * handled by this overlay.
	 * @param marker The marker whose info window was clicked
	 * @return true if the click event was handled by this overlay, false otherwise.
	 */
	public boolean onInfoWindowClick(Marker marker) {
		return false;
	}

}
