package com.gatech.spark.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.gatech.spark.R;
import com.gatech.spark.overlay.MapOverlay;
import com.gatech.spark.overlay.SearchResultsOverlay;
import com.gatech.spark.overlay.WhatsHotOverlay;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class SparkMapFragment extends Fragment {

	private static final String TAG = "spark.SparkMapFragment";
	private static final String PREFS_NAME = "MapPreferences";
	private static final String PREFS_KEY_MAP_LOC_LAT = "mapLocLat";
	private static final String PREFS_KEY_MAP_LOC_LNG = "mapLocLng";
	private static final String PREFS_KEY_MAP_ZOOM = "mapZoom";
	protected static final LatLng GT = new LatLng(33.78102, -84.400363);
	private MapView mapView;
	private MapOverlay whatsHotOverlay;
	private MapOverlay searchResultsOverlay;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Log.d(TAG, "Creating View");
		View rootView = inflater.inflate(R.layout.fragment_spark_map, container, false);
		
		initMapFeatures();
		mapView = (MapView) rootView.findViewById(R.id.sparkMapView);
		mapView.onCreate(savedInstanceState);
		setupClickListeners();
		setupMap();
		
		whatsHotOverlay = new WhatsHotOverlay(this, getMap());
		searchResultsOverlay = new SearchResultsOverlay(this, getMap());

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(TAG, "Activity Created");
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * Initialize certain maps features, e.g., CameraUpdateFactory. See also
	 * https://developers.google.com/maps/documentation/android/reference/com/google/android/gms/maps/MapsInitializer
	 * http://stackoverflow.com/a/13824917
	 */
    private void initMapFeatures() {
        try {
            MapsInitializer.initialize(getActivity());
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.d(TAG, "Google play services not available");
            throw(new RuntimeException("Unable to initialize map", e));
        }
    }

	private MapView getMapView() {
		return mapView;
	}

	public GoogleMap getMap() {
		return getMapView().getMap();
	}

	private void setupMap() {
		GoogleMap map = getMap();
		if (map != null) {
			getMap().setMyLocationEnabled(true);
		}
	}

	public void setupClickListeners() {
		final GoogleMap map = getMap();

		map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				return whatsHotOverlay.onMarkerClick(marker) ||
				       searchResultsOverlay.onMarkerClick(marker);
			}
		});
	}

	/**
	 * Restores saved map view state from preferences file
	 */
	private void restorePreferences() {
		Log.d(TAG, "Restoring Preferences");
		SharedPreferences settings = getSharedPreferences();
		try {
			float mapLat = settings.getFloat(PREFS_KEY_MAP_LOC_LAT, (float) GT.latitude);
			float mapLng = settings.getFloat(PREFS_KEY_MAP_LOC_LNG, (float) GT.longitude);
			float mapZoom = settings.getFloat(PREFS_KEY_MAP_ZOOM, 13);
			getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapLat, mapLng), mapZoom));
			
			whatsHotOverlay.load(settings);
			searchResultsOverlay.load(settings);
        } catch (Exception e) {
        	clearPreferences();
        }
    }

	/**
	 * Saves map view state to preferences file
	 */
	private void storePreferences() {
		Log.d(TAG, "Storing preferences");
		
		SharedPreferences settings = getSharedPreferences();
		SharedPreferences.Editor editor = settings.edit();
		CameraPosition position = getMap().getCameraPosition();
		editor.putFloat(PREFS_KEY_MAP_LOC_LAT, (float) position.target.latitude);
		editor.putFloat(PREFS_KEY_MAP_LOC_LNG,
		                (float) position.target.longitude);
		editor.putFloat(PREFS_KEY_MAP_ZOOM, position.zoom);
		editor.commit();

		whatsHotOverlay.save(settings);
		searchResultsOverlay.save(settings);
	}

	private void clearPreferences() {
		SharedPreferences settings = getSharedPreferences();
    	SharedPreferences.Editor editor = settings.edit();
    	editor.clear();
    	editor.commit();
	}

	private SharedPreferences getSharedPreferences() {
		return getActivity().getSharedPreferences(PREFS_NAME,
		                                           Activity.MODE_PRIVATE);
	}

	// Handling the menus
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		Log.d(TAG, "Creating options menu");
		inflater.inflate(R.menu.fragment_map, menu);
		whatsHotOverlay.setMenuItem(menu.findItem(R.id.whats_hot));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.whats_hot:
				whatsHotOverlay.toggle();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Given a text query like "airport", populates the map with markers for the top results
	 * @param query
	 */
	public void doSearch(String query) {
		Log.d(TAG, "searching for " + query);
		((SearchResultsOverlay) searchResultsOverlay).setSearchParams(query);
		// When we search, we are actually in a saved, non-active state. The act of searching
		// has caused us to pause in order to send the SEARCH intent to the searching activity.
		// Therefore, store any relevant queries now, so they are available when we resume
		storePreferences();
    }

	// Must forward lifecycle methods to MapView object. See
	// https://developers.google.com/maps/documentation/android/reference/com/google/android/gms/maps/MapView
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Creating fragment");
		setHasOptionsMenu(true);
	}

	@Override
	public void onResume() {
		Log.d(TAG, "...resuming");
		super.onResume();
		getMapView().onResume();
		restorePreferences();
		getMap().setMyLocationEnabled(true);
	}
	
	@Override
	public void onPause() {
		Log.d(TAG, "pausing...");
		super.onPause();
		getMap().setMyLocationEnabled(false);
		storePreferences();
		getMapView().onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		getMapView().onDestroy();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getMapView().onSaveInstanceState(outState);
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		getMapView().onLowMemory();
	}
}
