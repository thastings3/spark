package com.gatech.spark.overlay;

import com.gatech.spark.model.Subscription;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Overlay item for the center subscription of the parking lot search
 * overlay. Mostly passes things along to a SubscriptionsOverlayItem, but 
 * has a unique tag.
 * @author sam
 *
 */
public class ParkingLotSubscriptionOverlayItem extends OverlayItem {
	private SubscriptionsOverlayItem subOverlayItem;
	
	public ParkingLotSubscriptionOverlayItem(Subscription sub) {
		super(new LatLng(sub.getLatitude(), sub.getLongitude()));
		this.subOverlayItem = new SubscriptionsOverlayItem(sub);
	}

	@Override
	protected MarkerOptions getMarkerOptions() {
		MarkerOptions options = subOverlayItem.getMarkerOptions();
		options.snippet(createSnippet());
		return options;
	}

	@Override
	protected String getTag() {
		// use a unique tag so our item doesn't get confused
		// with the subscriptions overlay.
		return "ParkingOverlay" + subOverlayItem.getTag();
	}

	@Override
	protected int getID() {
		return subOverlayItem.getID();
	}

	public Subscription getSubscription() {
		return subOverlayItem.getSubscription();
	}
}
