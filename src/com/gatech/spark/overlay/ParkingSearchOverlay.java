package com.gatech.spark.overlay;

import java.util.ArrayList;
import java.util.Collection;

import android.content.SharedPreferences;
import android.view.Menu;

import com.gatech.spark.fragment.SparkMapFragment;
import com.gatech.spark.model.Subscription;
import com.google.android.gms.maps.model.Marker;

public class ParkingSearchOverlay extends MapOverlay {

	private Subscription subscription;
	private Collection<ParkingLotOverlayItem> parkingLots;
	private boolean isVisible = false;

	public ParkingSearchOverlay(SparkMapFragment fragment) {
		super(fragment);
		parkingLots = new ArrayList<ParkingLotOverlayItem>();
		subscription = null;
	}
	
	public void setSubscription(Subscription sub) {
		this.subscription = sub;
	}

	@Override
	public boolean isVisible() {
		return isVisible;
	}

	@Override
	public void updateMenuItem() {
		return;
	}

	@Override
	public void populate() {
		// TODO Auto-generated method stub
	}

	@Override
	public void clear() {
		for (OverlayItem item : parkingLots) {
			item.removeMarker();
			item.hide();
		}
		parkingLots.clear();
	}

	@Override
	public void setVisibility(boolean visibility) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void show() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(SharedPreferences settings) {
		// TODO Auto-generated method stub
	}

	@Override
	public void load(SharedPreferences settings) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isMember(Marker marker) {
		return new ParkingLotOverlayItem().isMember(marker);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu) {
		return;
	}

}
