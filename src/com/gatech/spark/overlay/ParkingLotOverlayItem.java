package com.gatech.spark.overlay;

import com.gatech.spark.model.SparkParkingLot;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ParkingLotOverlayItem extends OverlayItem {
	private static String TAG  = "spark.ParkingLotOverlayItem";
	private static int ID;
	private int myID;
	private SparkParkingLot parkingLot;
	
	public ParkingLotOverlayItem() {
    }

	public ParkingLotOverlayItem(SparkParkingLot lot) {
		super(new LatLng(lot.getLocation().getLatitude(),
		                 lot.getLocation().getLongitude()));
		parkingLot = lot;
	    myID = ID++;
	}
	
	public SparkParkingLot getParkingLot() {
		return parkingLot;
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
