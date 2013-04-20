package com.gatech.spark.overlay;

import com.gatech.spark.model.Subscription;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SubscriptionsOverlayItem extends OverlayItem {

	private static final String TAG = "subscriptions_marker";
	private static int ID = 0;
	private int myID;
	private Subscription subscription;

	public SubscriptionsOverlayItem() {}
	
	public SubscriptionsOverlayItem(Subscription sub) {
		super(new LatLng(sub.getLatitude(), sub.getLongitude()));
		this.subscription = sub;
		this.myID = ID++;
	}

	@Override
	protected MarkerOptions getMarkerOptions() {
		return new MarkerOptions().title(subscription.getName())
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
