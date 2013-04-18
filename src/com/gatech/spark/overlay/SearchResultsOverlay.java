package com.gatech.spark.overlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.gatech.spark.fragment.SparkMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

public class SearchResultsOverlay extends MapOverlay {
	private static final String TAG = "spark.SearchResultsOverlay";
	private static final String PREFS_KEY_SHOW_ON_LOAD =
	        "SearchResultsOverlay.Visibility";
	private static final String PREFS_KEY_QUERY =
			"SearchResultsOverlay.Query";
	private static final String PREFS_KEY_BOUNDS =
			"SearchResultsOverlay.Bounds";

	private Collection<SearchResultOverlayItem> searchResults;
	private String query;
	private LatLngBounds storedBounds;
	private boolean isVisible;	// currently visible
	private boolean showOnLoad;	// do the search the next time we load

	public SearchResultsOverlay(SparkMapFragment fragment, GoogleMap map) {
		super(fragment, map);
		searchResults = new ArrayList<SearchResultOverlayItem>();
		query = "";
		isVisible = false;
		showOnLoad = false;
	}

	public void setSearchParams(String query) {
		// don't actually search here. Just set the parameters to be
		// saved to the preference's file. We'll do the searching when
		// we wake up.
		setQuery(query);
		showOnLoad = true;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Override
    public void setVisibility(boolean visibility) {
		this.isVisible = visibility;
		this.showOnLoad = visibility;
    }

	@Override
    public boolean isVisible() {
	    return this.isVisible;
    }


	@Override
	public void updateMenuItem() {
		// TODO Auto-generated method stub
	}

	@Override
	public void populate() {
		Log.d(TAG, "populate: " + query);
		clear();

		AddressSearcher searcher = new AddressSearcher();
		searchResults = searcher.search(query, storedBounds);

		for (OverlayItem item : searchResults) {
	        Log.d(TAG, item.toString());
        }
	}

	@Override
	public void clear() {
		for (SearchResultOverlayItem item : searchResults) {
			item.removeMarker();
			item.hide();
		}
		searchResults.clear();
	}

	@Override
	protected void show() {
		Log.d(TAG, "show");
		populate();
		for (SearchResultOverlayItem item : searchResults) {
			Log.d(TAG, "Adding marker to " + item.toString());
			item.addMarker(getMap());
			item.show();
		}
	}

	@Override
	protected void hide() {
		for (SearchResultOverlayItem item : searchResults) {
			item.hide();
		}
	}

	@Override
	public void save(SharedPreferences settings) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(PREFS_KEY_SHOW_ON_LOAD, showOnLoad);
		editor.putString(PREFS_KEY_QUERY, query);
		editor.putString(PREFS_KEY_BOUNDS, getBoundsStr());
		editor.commit();
	}

	@Override
	public void load(SharedPreferences settings) {
		boolean showOnLoad = settings.getBoolean(PREFS_KEY_SHOW_ON_LOAD, false);
		String query = settings.getString(PREFS_KEY_QUERY, "");
		String boundStr = settings.getString(PREFS_KEY_BOUNDS, "");
		this.storedBounds = LatLngMarshaller.unmarshallBounds(boundStr);

		setQuery(query);
		setVisibility(showOnLoad);
		
		if (showOnLoad)
			show();
		else
			hide();
		this.showOnLoad = false; // reset for next load
	}

	@Override
	public boolean isMember(Marker marker) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private String getBoundsStr() {
		LatLngBounds bounds = getMap().getProjection().getVisibleRegion().latLngBounds;
		return LatLngMarshaller.marshallBounds(bounds);
	}


	/**
	 * Class to search for address
	 * 
	 */
	public class AddressSearcher {

		private static final String TAG =
		        "spark.SearchResultsOverlay.AddressSearcher";

		/**
		 * Searches for the top results for the `query`, and returns a list of
		 * LocationSearchResults as search results
		 * 
		 * @param query
		 *            text query like "airport"
		 * @return list of LocationSearchResults most closely matching the query
		 */
		public List<SearchResultOverlayItem> search(String query, LatLngBounds bounds) {
			List<SearchResultOverlayItem> searchResults =
			        new ArrayList<SearchResultOverlayItem>();
			if (!query.isEmpty() && bounds != null) {
				List<Address> addresses = searchForAddresses(query, bounds);
				if (addresses != null) {
					for (Address addr : addresses) {
						searchResults.add(new SearchResultOverlayItem(addr));
					}
				}
			} else {
				Log.d(TAG, "Query empty or bounds is null");
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
		private List<Address> searchForAddresses(String query, LatLngBounds bounds) {
			if (Geocoder.isPresent() && !query.isEmpty()) {
				Log.d(TAG, "Searching for " + query);
				return getAddressesInRegion(query, 15, bounds);

			}
			return null;
		}

		private List<Address> getAddressesInRegion(String query,
		                                           int maxResults,
		                                           LatLngBounds bounds) {
			List<Address> addresses = null;
			Geocoder geocoder = new Geocoder(getActivity());
			try {
				addresses =
				        geocoder.getFromLocationName(query,
				                                     maxResults,
				                                     bounds.southwest.latitude,
				                                     bounds.southwest.longitude,
				                                     bounds.northeast.latitude,
				                                     bounds.northeast.longitude);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return addresses;
		}
	}
	
	/**
	 * Marshalls and Unmarshals LatLng and LatLngBound objects to/from Strings
	 *
	 */
	public static class LatLngMarshaller {
		private static final String BOUNDS_DELIM = "|";
		private static final String BOUNDS_REGEX = "\\|";
		private static final String LAT_LNG_DELIM = ",";
		private static final String LAT_LNG_REGEX = ",";

		public static String marshallBounds(LatLngBounds bounds) {
			if (bounds == null)
				return "";
			return  marshallLatLng(bounds.southwest) + BOUNDS_DELIM + marshallLatLng(bounds.northeast);
		}

		public static LatLngBounds unmarshallBounds(String str) {
			if (str.isEmpty()) {
	            return null;
            }

			String[] vals = str.split(BOUNDS_REGEX);
			LatLng southwest = unmarshallLatLng(vals[0]);
			LatLng northeast = unmarshallLatLng(vals[1]);
			return new LatLngBounds(southwest, northeast);
		}

		public static String marshallLatLng(LatLng latLng) {
			if (latLng == null)
				return "";
			return latLng.latitude + LAT_LNG_DELIM + latLng.longitude;
		}

		public static LatLng unmarshallLatLng(String str) {
			if (str.isEmpty()) {
	            return null;
            }

			String[] vals = str.split(LAT_LNG_REGEX);
			double lat = Double.parseDouble(vals[0]);
			double lng = Double.parseDouble(vals[1]);
			return new LatLng(lat, lng);
		}
	}

}
