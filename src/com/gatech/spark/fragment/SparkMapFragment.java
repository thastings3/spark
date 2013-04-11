package com.gatech.spark.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gatech.spark.R;
import com.gatech.spark.activity.PlaceExpandedActivity;
import com.gatech.spark.adapter.GenericArrayAdapter;
import com.gatech.spark.helper.CommonHelper;
import com.gatech.spark.helper.HandlerReturnObject;
import com.gatech.spark.helper.HotSpot;
import com.gatech.spark.helper.HttpRestClient;
import com.gatech.spark.helper.LocationSearchResult;
import com.gatech.spark.helper.MarkerPlacer;
import com.gatech.spark.helper.SaxParser;
import com.gatech.spark.model.Place;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.VisibleRegion;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class SparkMapFragment extends Fragment {

	private static final String TAG = "SparkMapFragment";
	private static final String PREFS_NAME = "MapPreferences";
	private static final String PREFS_KEY_WHATS_HOT = "whatsHotIsShowing";
	private static final String PREFS_KEY_MAP_LOC_LAT = "mapLocLat";
	private static final String PREFS_KEY_MAP_LOC_LNG = "mapLocLng";
	private static final String PREFS_KEY_MAP_ZOOM = "mapZoom";
	protected static final LatLng GT = new LatLng(33.78102, -84.400363);
	protected static final LatLng SoNo = new LatLng(33.769872,-84.384527);
	private MapView mapView;
	private boolean whatsHotIsShowing = false;
	private MenuItem whatsHotItem;
	private Iterable<HotSpot> hotSpotList;
	private List<LocationSearchResult> searchResults;
    private ProgressDialog pDialog;


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

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		restorePreferences();
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
                if(MarkerPlacer.isWhatsHotMarker(marker))
                {
                    HttpRestClient.getPlaces(marker.getPosition().latitude, marker.getPosition().longitude, 500, new AsyncHttpResponseHandler(){

                        @Override
                        public void onStart() {
                            pDialog = new ProgressDialog( getActivity() );
                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            pDialog.setCancelable( false );
                            pDialog.setTitle( "Searching for data..." );
                            pDialog.show();
                        }

                        @Override
                        public void onSuccess(String s) {
                            SaxParser parser = new SaxParser();
                            HandlerReturnObject<ArrayList<Place>> handlerObject = parser.parsePlacesXmlResponse(s);
                            if (handlerObject.isValid())
                            {
                                showListDialog(handlerObject.getObject());
                            }
                            else
                            {
                                CommonHelper.showLongToast(getActivity(), "Failed to find locations.");
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable, String s) {
                            super.onFailure(throwable, s);
                        }

                        @Override
                        public void onFinish() {
                            pDialog.dismiss();
                        }
                    });
                    //return true to suppress showing the default dialog.
                    return true;
                }
                //return false to show the default behavior on the marker press.
                return false;
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

	private void toggleWhatsHot() {
		setWhatsHot(!whatsHotIsShowing);
	}

	private void setWhatsHot(boolean shouldShow) {
		if (shouldShow)
			showWhatsHot();
		else
			hideWhatsHot();
		setWhatsHotTitle();
	}

	private void showWhatsHot() {
		hotSpotList = getHotSpots();
		for (HotSpot spot : hotSpotList) {
			spot.addMarker(getMap());
		}
		whatsHotIsShowing = true;
	}
	
	private void hideWhatsHot() {
		if (hotSpotList != null) {
			for (HotSpot spot : hotSpotList) {
				spot.clearMarker();
			}
			hotSpotList = null;
		}
		whatsHotIsShowing = false;
	}
	
	public void setWhatsHotTitle() {
		if (whatsHotItem != null) {
			int titleRes =
			        whatsHotIsShowing ? R.string.hide_whats_hot : R.string.show_whats_hot;
			whatsHotItem.setTitle(titleRes);
		}
	}
	
	private Iterable<HotSpot> getHotSpots() {
		ArrayList<HotSpot> hotSpotList = new ArrayList<HotSpot>();
		
		// TODO: get this from the server
		LatLng[] locList = {GT, SoNo};
		for (LatLng loc : locList) {
			hotSpotList.add(new HotSpot(loc));
		}
		return hotSpotList;
	}

	/**
	 * Restores saved map view state from preferences file
	 */
	private void restorePreferences() {
		Log.d(TAG, "Restoring Preferences");
		Activity activity = getActivity();
		SharedPreferences settings =
		        activity.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
		boolean hotIsShowing = settings.getBoolean(PREFS_KEY_WHATS_HOT, false);
		setWhatsHot(hotIsShowing);

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
		editor.putBoolean(PREFS_KEY_WHATS_HOT, whatsHotIsShowing);

		editor.commit();
    }

	// Handling the menus
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_map, menu);
		whatsHotItem = menu.findItem(R.id.whats_hot);
		setWhatsHotTitle();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.whats_hot:
				toggleWhatsHot();
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


    public void showListDialog(ArrayList<Place> places)
    {
        Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Locations");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.places_list_layout, null, false);
        GenericArrayAdapter<Place> adapter = new GenericArrayAdapter<Place>(getActivity(),places);
        ListView list = (ListView)v.findViewById(R.id.placeList);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent( getActivity(), PlaceExpandedActivity.class );
                intent.putExtra( PlaceExpandedActivity.PLACE,  ((Place)adapterView.getItemAtPosition(i)) );
                startActivity( intent );
            }
        });

        list.setAdapter(adapter);
        dialog.setContentView(v);
        dialog.show();
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
