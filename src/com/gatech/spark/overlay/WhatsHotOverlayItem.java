package com.gatech.spark.overlay;

import com.gatech.spark.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class WhatsHotOverlayItem extends OverlayItem {
	private static final String WHATS_HOT_SNIPPET = "whats_hot_marker";
	private static int ID = 0;
	private int myID;

	public WhatsHotOverlayItem() {
		super();
		myID = ID++;
	}

	public WhatsHotOverlayItem(LatLng loc) {
		super(loc);
	}

	@Override
	protected MarkerOptions getMarkerOptions() {
		return new MarkerOptions().title("this is hot hot hot!")
			.snippet(createSnippet())
			.draggable(false)
			.anchor(0.5f, 0.5f)
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.whats_hot));
	}

	@Override
	protected int getID() {
		return myID;
	}

	@Override
	protected String getTag() {
		return WHATS_HOT_SNIPPET;
	}
}
