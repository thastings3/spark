package com.gatech.spark.overlay;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SubscriptionsOverlayItem extends OverlayItem {

	private static final String SNIPPET = "subscriptions_marker";

	public SubscriptionsOverlayItem(LatLng loc) {
		super(loc);
	}

	@Override
	protected MarkerOptions getMarkerOptions() {
		return new MarkerOptions().title("subscription")
		                          .snippet(SNIPPET)
		                          .draggable(true)
		                          .anchor(0.5f, 0.5f)
		                          .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
	}

	public static boolean isMember(Marker marker) {
		String snippet = marker.getSnippet();
		return (snippet != null) && snippet.equalsIgnoreCase(SNIPPET);
	}

}
