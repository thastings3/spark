package com.gatech.spark.overlay;

import java.util.ArrayList;
import java.util.Collection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gatech.spark.R;
import com.gatech.spark.activity.PlaceExpandedActivity;
import com.gatech.spark.fragment.SparkMapFragment;
import com.gatech.spark.helper.CommonHelper;
import com.gatech.spark.helper.HandlerReturnObject;
import com.gatech.spark.helper.HttpRestClient;
import com.gatech.spark.helper.SaxParser;
import com.gatech.spark.model.Place;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class SearchResultsOverlay extends MapOverlay {
	private static final String TAG = "spark.SearchResultsOverlay";
	private static final String PREFS_KEY_SHOW_ON_LOAD =
		"SearchResultsOverlay.Visibility";
	private static final String PREFS_KEY_QUERY =
		"SearchResultsOverlay.Query";
	private static final int MENU_ITEM_ID = R.id.search_again;

	private Collection<SearchResultOverlayItem> searchResults;
	private String query;
	private boolean isVisible;	// currently visible
	private boolean showOnLoad;	// do the search the next time we load

	public SearchResultsOverlay(SparkMapFragment fragment) {
		super(fragment);
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
		updateMenuItem();
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
		if (query.isEmpty()) {
			menuItem.setVisible(false);
		} else {
			menuItem.setTitle(query);
			menuItem.setVisible(true);
		}
	}

	@Override
	public void populate() {
		Log.d(TAG, "populate: " + query);
		clear();
		search();
	}

	private void search() {
		PlaceSearcher searcher = new PlaceSearcher();
		searcher.search(query);
	}

	private void onSearchResultsUpdated() {
		show();
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
		for (SearchResultOverlayItem item : searchResults) {
			item.addMarker(getMap());
			item.show();
		}
		SearchResultOverlayItem first = searchResults.iterator().next();
		zoomOutTillVisible(first);
	}

	private void zoomOutTillVisible(SearchResultOverlayItem item) {
		if (item == null)
			return;
		LatLng target = item.getLatLng();
		while (!getBounds().contains(target)) {
			Log.d(TAG, "zooming");
			getMap().moveCamera(CameraUpdateFactory.zoomOut());
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
		editor.commit();
	}

	@Override
	public void load(SharedPreferences settings) {
		boolean showOnLoad = settings.getBoolean(PREFS_KEY_SHOW_ON_LOAD, false);
		String query = settings.getString(PREFS_KEY_QUERY, "");

		setQuery(query);
		setVisibility(showOnLoad); // current visibility
		if (showOnLoad) {
			populate();
		} else {
			hide();
		}
		this.showOnLoad = false; // reset for next load
	}

	@Override
	public boolean isMember(Marker marker) {
		return new SearchResultOverlayItem().isMember(marker);
	}

	@Override
	public OverlayItem getOverlayItem(Marker marker) {
		if (isMember(marker)) {
			for (OverlayItem item : searchResults) {
				if (item.hasMarker(marker))
					return item;
			}
		}
		return null;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu) {
		setMenuItem(menu.findItem(MENU_ITEM_ID));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == MENU_ITEM_ID) {
			populate();
			return true;
		}
		return false;
	}

	private LatLngBounds getBounds() {
		return getMap().getProjection().getVisibleRegion().latLngBounds;
	}

	private LatLng getCenter() {
		return getMap().getCameraPosition().target;
	}

	private Location latLngToLoc(LatLng latLng) {
		Location loc = new Location("");
		loc.setLatitude(latLng.latitude);
		loc.setLongitude(latLng.longitude);
		return loc;
	}

	private float distanceBetweenLatLngs(LatLng src, LatLng dest) {
		Location srcLoc = latLngToLoc(src);
		Location destLoc = latLngToLoc(dest);
		return srcLoc.distanceTo(destLoc);
	}

	private float getViewableRadiusInMeters() {
		LatLngBounds bounds = getBounds();
		LatLng center = getCenter();
		return distanceBetweenLatLngs(center, bounds.northeast);
	}

	@Override
	public View getInfoContents(LayoutInflater inflater, Marker marker) {
		if (!isMember(marker))
			return null;
		SearchResultOverlayItem item = (SearchResultOverlayItem) getOverlayItem(marker);
		Place place = item.getPlace();
		View popup = inflater.inflate(R.layout.info_window_subscription, null);
		TextView tv = (TextView)popup.findViewById(R.id.title);
		tv.setText(place.getName());
		tv = (TextView)popup.findViewById(R.id.snippet);
		tv.setText(place.getFormattedAddress());

		return(popup);
	}

	@Override
	public boolean onInfoWindowClick(Marker marker) {
		if (!isMember(marker))
			return false;
		SearchResultOverlayItem item = (SearchResultOverlayItem) getOverlayItem(marker);
		Intent intent = new Intent(getActivity(), PlaceExpandedActivity.class);
		intent.putExtra(PlaceExpandedActivity.PLACE, item.getPlace().getReference());
		getActivity().startActivity(intent);
		return true;
	}

	/**
	 * Class to search for address
	 * 
	 */
	public class PlaceSearcher {

		private static final String TAG =
			"spark.SearchResultsOverlay.PlaceSearcher";

		/**
		 * Searches for the top results for the `query`, and returns a list of
		 * LocationSearchResults as search results
		 * 
		 * @param query
		 *            text query like "airport"
		 * @return list of LocationSearchResults most closely matching the query
		 */
		public void search(String query) {
			if (!query.isEmpty()) {
				searchInViewableRegion(query);
			} else {
				Log.d(TAG, "Query empty");
			}
		}

		private void searchInViewableRegion(final String query) {
			HttpRestClient.getPlaces(query,
			                         getCenter().latitude,
			                         getCenter().longitude,
			                         (int) getViewableRadiusInMeters(),
			                         new PlacesResponseHandler(this));
		}

		private void onSearchCompleted(ArrayList<Place> placeList) {
			updateSearchResults(placeList);
		}

		private void updateSearchResults(ArrayList<Place> placeList) {
			Log.d(TAG, "Updating search results. Size of placeList=" + placeList.size());
			for (Place place : placeList) {
				searchResults.add(new SearchResultOverlayItem(place));
			}
			onSearchResultsUpdated();
		}

		private class PlacesResponseHandler extends AsyncHttpResponseHandler {
			ProgressDialog pDialog;
			PlaceSearcher searcher;

			public PlacesResponseHandler(PlaceSearcher searcher) {
				super();
				this.searcher = searcher;
			}

			@Override
			public void onStart() {
				pDialog = new ProgressDialog(getActivity());
				pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pDialog.setCancelable( false );
				pDialog.setTitle( "Searching for " + query + "..." );
				pDialog.show();
			}

			@Override
			public void onSuccess(String s) {
				SaxParser parser = new SaxParser();
				HandlerReturnObject<ArrayList<Place>> handlerObject = parser.parsePlacesXmlResponse(s);
				if (handlerObject.isValid())
				{
					searcher.onSearchCompleted(handlerObject.getObject());
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
		}
	}
}
