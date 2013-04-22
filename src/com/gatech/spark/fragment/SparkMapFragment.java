package com.gatech.spark.fragment;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
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
import com.gatech.spark.activity.MainActivity;
import com.gatech.spark.helper.CommonHelper;
import com.gatech.spark.helper.HandlerReturnObject;
import com.gatech.spark.helper.HttpRestClient;
import com.gatech.spark.helper.SaxParser;
import com.gatech.spark.model.Place;
import com.gatech.spark.model.SparkParkingLot;
import com.gatech.spark.model.Subscription;
import com.gatech.spark.overlay.MapOverlay;
import com.gatech.spark.overlay.OverlayInfoWindowAdapter;
import com.gatech.spark.overlay.ParkingLotOverlayItem;
import com.gatech.spark.overlay.ParkingSearchOverlay;
import com.gatech.spark.overlay.SearchResultsOverlay;
import com.gatech.spark.overlay.SubscriptionsOverlay;
import com.gatech.spark.overlay.WhatsHotOverlay;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.loopj.android.http.AsyncHttpResponseHandler;

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
	private MapOverlay subscriptionsOverlay;
	private MapOverlay parkingSearchOverlay;
	private ArrayList<MapOverlay> allOverlays;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Log.d(TAG, "Creating View");
		View rootView =
			inflater.inflate(R.layout.fragment_spark_map, container, false);

		initMapFeatures();
		mapView = (MapView) rootView.findViewById(R.id.sparkMapView);
		mapView.onCreate(savedInstanceState);
		setupClickListeners();
		setupMap();
		initOverlays();

		MainActivity activity = (MainActivity) getActivity();
		if (activity.getShowParkingResultsOnLoad()) {
			searchForParkingLocations(activity.getShowParkingResultsOnLoadPlace());
			activity.setShowParkingReultsOnLoad(false);
		}

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(TAG, "Activity Created");
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * Initialize certain maps features, e.g., CameraUpdateFactory. See also
	 * https ://developers.google.com/maps/documentation/android/reference/com/
	 * google/android/gms/maps/MapsInitializer
	 * http://stackoverflow.com/a/13824917
	 */
	private void initMapFeatures() {
		try {
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			Log.d(TAG, "Google play services not available");
			throw (new RuntimeException("Unable to initialize map", e));
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

	private void initOverlays() {
		allOverlays = new ArrayList<MapOverlay>();
		whatsHotOverlay = new WhatsHotOverlay(this);
		allOverlays.add(whatsHotOverlay);
		searchResultsOverlay = new SearchResultsOverlay(this);
		allOverlays.add(searchResultsOverlay);
		subscriptionsOverlay = new SubscriptionsOverlay(this);
		allOverlays.add(subscriptionsOverlay);
		parkingSearchOverlay = new ParkingSearchOverlay(this);
		allOverlays.add(parkingSearchOverlay);
	}

	public Collection<MapOverlay> getOverlays() {
		return allOverlays;
	}

	public void setupClickListeners() {
		final GoogleMap map = getMap();

		// Let the overlays handle marker clicks
		map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				for (MapOverlay overlay : allOverlays) {
					if (overlay.onMarkerClick(marker))
						return true;
				}
				return false;
			}
		});


		// Let the overlays handle info windows
		OverlayInfoWindowAdapter infoWindowHandler = new OverlayInfoWindowAdapter(this);
		map.setInfoWindowAdapter(infoWindowHandler);
		map.setOnInfoWindowClickListener(infoWindowHandler);
	}

	/**
	 * Restores saved map view state from preferences file
	 */
	private void restorePreferences() {
		Log.d(TAG, "Restoring Preferences");
		SharedPreferences settings = getSharedPreferences();
		try {
			float mapLat =
				settings.getFloat(PREFS_KEY_MAP_LOC_LAT,
				                  (float) GT.latitude);
			float mapLng =
				settings.getFloat(PREFS_KEY_MAP_LOC_LNG,
				                  (float) GT.longitude);
			float mapZoom = settings.getFloat(PREFS_KEY_MAP_ZOOM, 13);
			getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapLat,
			                                                                 mapLng),
			                                                                 mapZoom));

			for (MapOverlay overlay : allOverlays) {
				overlay.load(settings);
			}
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

		for (MapOverlay overlay : allOverlays) {
			overlay.save(settings);
		}
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
		for (MapOverlay overlay : allOverlays) {
			overlay.onCreateOptionsMenu(menu);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean handled = false;
		for (MapOverlay overlay : allOverlays) {
			handled = overlay.onOptionsItemSelected(item) || handled;
		}
		return handled || super.onOptionsItemSelected(item);
	}

	/**
	 * Given a text query like "airport", populates the map with markers for the
	 * top results
	 * 
	 * @param query
	 */
	public void searchForPlaces(String query) {
		Log.d(TAG, "searching for " + query);
		((SearchResultsOverlay) searchResultsOverlay).setSearchParams(query);
		// When we search, we are actually in a saved, non-active state. The act
		// of searching has caused us to pause in order to send the SEARCH
		// intent to the searching activity. Therefore, store any relevant
		// queries now, so they are available when we resume.
		storePreferences();
	}
	
	public void searchForParkingLocations(Place place) {
		searchForParkingLocations(new Subscription(place));
	}

    public void searchForParkingLocations(Subscription subscription)
    {
		Log.d(TAG, "searching for lots around " + subscription.getName());
		for (MapOverlay overlay : allOverlays) {
	        overlay.setVisibility(false);
        }

		((ParkingSearchOverlay) parkingSearchOverlay).setSubscription(subscription);
		parkingSearchOverlay.populate();
		parkingSearchOverlay.setVisibility(true);
    }

	// Must forward lifecycle methods to MapView object. See
	// https://developers.google.com/maps/documentation/android/reference/com/google/android/gms/maps/MapView
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Creating fragment");
		initOverlays();
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
