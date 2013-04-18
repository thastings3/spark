package com.gatech.spark.overlay;

import android.content.SharedPreferences;

import com.gatech.spark.fragment.SparkMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class SubscriptionsOverlay extends MapOverlay {
	
	public SubscriptionsOverlay(SparkMapFragment fragment, GoogleMap map) {
		super(fragment, map);
	}

	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateMenuItem() {
		// TODO Auto-generated method stub

	}

	@Override
	public void populate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		return false;
	}

}
