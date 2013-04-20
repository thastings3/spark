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


    public SubscriptionsOverlayItem( Subscription subscription) {
        super(subscription.getLatLong() );
        myID = ID++;
        this.subscription = subscription;
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

    public Subscription getSubscription()
    {
        return this.subscription;
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
