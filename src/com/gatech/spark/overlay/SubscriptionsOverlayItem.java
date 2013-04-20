package com.gatech.spark.overlay;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SubscriptionsOverlayItem extends OverlayItem {

	private static final String TAG = "subscriptions_marker";
	private static int ID = 0;
	private int myID;

	public SubscriptionsOverlayItem(LatLng loc) {
		super(loc);
		myID = ID++;
	}

	public SubscriptionsOverlayItem() {}

	@Override
	protected MarkerOptions getMarkerOptions() {
		return new MarkerOptions().title("subscription")
			.snippet(createSnippet())
			.draggable(true)
			.anchor(0.5f, 0.5f)
			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
	}

	@Override
	protected String getTag() {
		return TAG;
	}

	@Override
	protected int getID() {
		return myID;
	}
}
