package com.gatech.spark.overlay;

import java.util.ArrayList;
import java.util.Collection;

import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;

import com.gatech.spark.R;
import com.gatech.spark.fragment.SparkMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class SubscriptionsOverlay extends MapOverlay {

	private static final LatLng TurnerField = new LatLng(33.734797, -84.389291);
	private static final LatLng LenoxMall = new LatLng(33.847109, -84.364207);
	private static final String PREFS_KEY_VISIBILITY =
	        "SubscriptionsOverlay.Visibility";
	private static final int MENU_ITEM_ID = R.id.subscriptions;

	private Collection<SubscriptionsOverlayItem> subscriptionsList;
	private boolean isVisible;
	
	public SubscriptionsOverlay(SparkMapFragment fragment, GoogleMap map) {
		super(fragment, map);
		subscriptionsList = new ArrayList<SubscriptionsOverlayItem>();
	}

	@Override
	public void updateMenuItem() {
		if (menuItem != null) {
			int iconId = isVisible() ?
			    R.drawable.ic_action_favorites_enabled :
			    R.drawable.ic_action_favorites_disabled;
			menuItem.setIcon(iconId);
		}
	}

	@Override
	public void populate() {
		clear();

		// TODO: get this from the server
		LatLng[] locList = { TurnerField, LenoxMall };
		for (LatLng loc : locList) {
			subscriptionsList.add(new SubscriptionsOverlayItem(loc));
		}
	}

	@Override
	public void clear() {
		for (OverlayItem item : subscriptionsList) {
			item.removeMarker();
			item.hide();
		}
		subscriptionsList.clear();
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
		for (OverlayItem item : subscriptionsList) {
			item.addMarker(map);
			item.show();
		}
	}

	@Override
	protected void hide() {
		for (OverlayItem item : subscriptionsList) {
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
		return WhatsHotOverlayItem.isMember(marker);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO
		return false;
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
