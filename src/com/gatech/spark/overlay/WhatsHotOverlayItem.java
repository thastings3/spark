package com.gatech.spark.overlay;

import com.gatech.spark.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class WhatsHotOverlayItem extends OverlayItem {
	private static final String WHATS_HOT_SNIPPET = "whats_hot_marker";

	public WhatsHotOverlayItem(LatLng loc) {
		super(loc);
	}

	@Override
	protected MarkerOptions getMarkerOptions() {
		return new MarkerOptions().title("this is hot hot hot!")
		                          .snippet(WHATS_HOT_SNIPPET)
		                          .draggable(false)
		                          .anchor(0.5f, 0.5f)
		                          .icon(BitmapDescriptorFactory.fromResource(R.drawable.whats_hot));
	}

	public static boolean isMember(Marker marker) {
		String snippet = marker.getSnippet();
		return (snippet != null) && snippet.equalsIgnoreCase(WHATS_HOT_SNIPPET);
	}
}
