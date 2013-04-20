/**
 * 
 */
package com.gatech.spark.overlay;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gatech.spark.R;
import com.gatech.spark.activity.PlaceExpandedActivity;
import com.gatech.spark.adapter.GenericArrayAdapter;
import com.gatech.spark.fragment.SparkMapFragment;
import com.gatech.spark.helper.CommonHelper;
import com.gatech.spark.helper.HandlerReturnObject;
import com.gatech.spark.helper.HttpRestClient;
import com.gatech.spark.helper.SaxParser;
import com.gatech.spark.model.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.loopj.android.http.AsyncHttpResponseHandler;

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
	private static final int MENU_ITEM_ID = R.id.whats_hot;

	private Collection<WhatsHotOverlayItem> hotSpotList;
	private boolean isVisible;

	public WhatsHotOverlay(SparkMapFragment fragment, GoogleMap map) {
		super(fragment, map);
		hotSpotList = new ArrayList<WhatsHotOverlayItem>();
	}

	/**
	 * Sets menu title if the menu item is valid
	 */
	@Override
	public void updateMenuItem() {
		if (menuItem != null) {
			int iconId = isVisible() ?
				R.drawable.ic_action_whats_hot_enabled:
					R.drawable.ic_action_whats_hot_disabled;
			menuItem.setIcon(iconId);
		}
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
		for (OverlayItem item : hotSpotList) {
			item.removeMarker();
			item.hide();
		}
		hotSpotList.clear();
	}

	@Override
	public boolean isVisible() {
		return isVisible;
	}

	@Override
	public void setVisibility(boolean visibility) {
		this.isVisible = visibility;
		if (visibility) {
			show();
		} else {
			hide();
		}
		updateMenuItem();
	}

	@Override
	protected void show() {
		populate();
		for (OverlayItem item : hotSpotList) {
			item.addMarker(map);
			item.show();
		}
	}

	@Override
	protected void hide() {
		for (OverlayItem item : hotSpotList) {
			item.hide();
		}
	}

	@Override
	public void save(SharedPreferences settings) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(PREFS_KEY_VISIBILITY, isVisible());
		editor.commit();
	}

	@Override
	public void load(SharedPreferences settings) {
		boolean visibility = settings.getBoolean(PREFS_KEY_VISIBILITY, false);
		setVisibility(visibility);
	}

	@Override
	public boolean isMember(Marker marker) {
		return new WhatsHotOverlayItem().isMember(marker);
	}

	@Override
	public OverlayItem getOverlayItem(Marker marker) {
		if (isMember(marker)) {
			for (OverlayItem item : hotSpotList) {
				if (item.hasMarker(marker))
					return item;
			}
		}
		return null;
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if(isMember(marker))
		{
			HttpRestClient.getPlaces(marker.getPosition().latitude,
			                         marker.getPosition().longitude,
			                         500,
			                         new AsyncHttpResponseHandler(){
				ProgressDialog pDialog;

				@Override
				public void onStart() {
					pDialog = new ProgressDialog(getActivity());
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
						showListDialog(handlerObject.getObject(), getActivity());
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

	private void showListDialog(ArrayList<Place> places, final Activity activity)
	{
		Dialog dialog = new Dialog(activity);
		dialog.setTitle("Locations");
		LayoutInflater inflater = activity.getLayoutInflater();
		View v = inflater.inflate(R.layout.places_list_layout, null, false);
		GenericArrayAdapter<Place> adapter = new GenericArrayAdapter<Place>(activity,places);
		ListView list = (ListView)v.findViewById(R.id.placeList);

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Intent intent = new Intent( activity, PlaceExpandedActivity.class );
				intent.putExtra( PlaceExpandedActivity.PLACE,  ((Place)adapterView.getItemAtPosition(i)) );
				activity.startActivity( intent );
			}
		});

		list.setAdapter(adapter);
		dialog.setContentView(v);
		dialog.show();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu) {
		setMenuItem(menu.findItem(MENU_ITEM_ID));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == MENU_ITEM_ID) {
			toggle();
			return true;
		}
		return false;
	}
}
