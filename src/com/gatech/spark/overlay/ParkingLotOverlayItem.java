package com.gatech.spark.overlay;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

public class ParkingLotOverlayItem extends OverlayItem {
	private static String TAG  = "spark.ParkingLotOverlayItem";
	private static int ID;
	private int myID;
	
	public ParkingLotOverlayItem() {
	    super();
	    myID = ID++;
    }

	@Override
	protected MarkerOptions getMarkerOptions() {
		return new MarkerOptions().title("Parking Lot")
			.snippet(createSnippet())
			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
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
