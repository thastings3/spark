package com.gatech.spark.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gatech.spark.R;
import com.gatech.spark.helper.LocationSearchResult;
import com.gatech.spark.helper.MapOverlay;
import com.gatech.spark.helper.MarkerPlacer;
import com.gatech.spark.helper.WhatsHotOverlay;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.VisibleRegion;

public class SparkMapFragment extends Fragment {

	private static final String TAG = "SparkMapFragment";
	private static final String PREFS_NAME = "MapPreferences";
	private static final String PREFS_KEY_MAP_LOC_LAT = "mapLocLat";
	private static final String PREFS_KEY_MAP_LOC_LNG = "mapLocLng";
	private static final String PREFS_KEY_MAP_ZOOM = "mapZoom";
	protected static final LatLng GT = new LatLng(33.78102, -84.400363);
	protected static final LatLng SoNo = new LatLng(33.769872,-84.384527);
	private MapView mapView;
	private MapOverlay whatsHotOverlay;
	private List<LocationSearchResult> searchResults;


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
		
		whatsHotOverlay = new WhatsHotOverlay(getMap());

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

	private GoogleMap getMap() {
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
		map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng pos) {
				MarkerPlacer.addDraggableMarker(map, pos);
			}
		});

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
            	return whatsHotOverlay.onMarkerClick(marker, getActivity());
            }
        });

		map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

			@Override
			public void onMarkerDragStart(Marker arg0) {
			}

			@Override
			public void onMarkerDragEnd(Marker marker) {
				LatLng latlng = marker.getPosition();
				Toast.makeText(SparkMapFragment.this.getActivity(),
				               "new position is " + latlng, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onMarkerDrag(Marker arg0) {
			}
		});
	}


	/**
	 * Restores saved map view state from preferences file
	 */
	private void restorePreferences() {
		Log.d(TAG, "Restoring Preferences");
		Activity activity = getActivity();
		SharedPreferences settings =
		        activity.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
		
		whatsHotOverlay.load(settings);

		float mapLat = settings.getFloat(PREFS_KEY_MAP_LOC_LAT, (float) GT.latitude);
		float mapLng = settings.getFloat(PREFS_KEY_MAP_LOC_LNG, (float) GT.longitude);
		float mapZoom = settings.getFloat(PREFS_KEY_MAP_ZOOM, 13);
		getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapLat, mapLng), mapZoom));
    }

	/**
	 * Saves map view state to preferences file
	 */
	private void storePreferences() {
		Log.d(TAG, "Storing preferences");
		SharedPreferences settings =
				getActivity().getSharedPreferences(PREFS_NAME,
		                                           Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		CameraPosition position = getMap().getCameraPosition();
		editor.putFloat(PREFS_KEY_MAP_LOC_LAT, (float) position.target.latitude);
		editor.putFloat(PREFS_KEY_MAP_LOC_LNG, (float) position.target.longitude);
		editor.putFloat(PREFS_KEY_MAP_ZOOM, position.zoom);
		editor.commit();
		
		whatsHotOverlay.save(settings);
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

	// Must forward lifecycle methods to MapView object. See
	// https://developers.google.com/maps/documentation/android/reference/com/google/android/gms/maps/MapView
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Creating fragment");
		setHasOptionsMenu(true);
	}

	/**
	 * Given a text query like "airport", populates the map with markers for the top results
	 * @param query
	 */
	public void doSearch(String query) {
		Log.d(TAG, "searching for " + query);
		clearSearchResults();
		AddressSearcher searcher = new AddressSearcher();
		searchResults = searcher.search(query, getMap());
		addSearchResultsToMap();
    }

	/**
	 * Adds list of address to search results of map
	 * @param addresses
	 */
	private void addSearchResultsToMap() {
		if (searchResults != null) {
    	    for (LocationSearchResult res : searchResults) {
    	    	res.addMarker(getMap());
    	    }
		}
    }

	/**
	 * Clears the map and search results. Afterwards, `searchResults` is an
	 * empty list, ready to be populated
	 */
	private void clearSearchResults() {
		if (searchResults != null) {
			for (LocationSearchResult res : searchResults) {
				res.clearMarker();
			}
			searchResults.clear();
		}
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

	public class AddressSearcher {

		private static final String TAG = "spark.SparkMapFragment.AddressSearcher";
		/**
		 * Searches for the top results for the `query`, and returns a list of
		 * LocationSearchResults as search results
		 * 
		 * @param query
		 *            text query like "airport"
		 * @return list of LocationSearchResults most closely matching the query
		 */
		public List<LocationSearchResult> search(String query, GoogleMap map) {
			List<LocationSearchResult> searchResults = new ArrayList<LocationSearchResult>();
			List<Address> addresses = searchForAddresses(query, map);
			if (addresses != null) {
    			for (Address addr : addresses) {
    	            searchResults.add(new LocationSearchResult(addr));
                }
			}
			return searchResults;
		}

		/**
		 * Searches for the top results for the `query`, and returns a list of
		 * addresses as search results
		 * 
		 * @param query
		 *            text query like "airport"
		 * @return list of address most closely matching the query
		 */
		private List<Address> searchForAddresses(String query, GoogleMap map) {
			if (Geocoder.isPresent() && !query.isEmpty()) {
				Log.d(TAG, "Searching for " + query);
				VisibleRegion visibleRegion =
						map.getProjection().getVisibleRegion();
				return getAddressesInRegion(query, 5, visibleRegion);

			}
			return null;
		}
		
		private List<Address> getAddressesInRegion(String query,
		                                           int maxResults,
		                                           VisibleRegion visible) {
			List<Address> addresses = null;
			Geocoder geocoder = new Geocoder(getActivity());
			try {
				addresses =
				        geocoder.getFromLocationName(query,
				                                     maxResults,
				                                     visible.nearLeft.latitude,
				                                     visible.nearLeft.longitude,
				                                     visible.farRight.latitude,
				                                     visible.farRight.longitude);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return addresses;
		}
	}
}
