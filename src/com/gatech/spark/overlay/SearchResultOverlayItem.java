package com.gatech.spark.overlay;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SearchResultOverlayItem extends OverlayItem {
	private Address addr;

	public SearchResultOverlayItem(Address addr) {
		super(new LatLng(addr.getLatitude(), addr.getLongitude()));
		this.addr = addr;
	}

	@Override
	protected MarkerOptions getMarkerOptions() {
		return new MarkerOptions().title(addr.getAddressLine(0))
		                          .snippet(addr.getAddressLine(1))
		                          .draggable(false);
	}

	public Address getAddress() {
		return addr;
	}
	
	/**
	 * @return the first two address lines
	 */
	private String getShortAddress() {
		String line0 = getAddressLineOrEmpty(0);
		String line1 = getAddressLineOrEmpty(1);
		return line0 + ", " + line1;
	}
	
	/**
	 * Returns the address line at `index`, or the empty string if not available 
	 * @param index
	 * @return
	 */
	private String getAddressLineOrEmpty(int index) {
		if (addr == null)
			return "";

		String line = addr.getAddressLine(index);
		if (line == null)
			line = "";
		return line;
	}
	
	@Override
	public String toString() {
		return "SearchResultOverlayItem" + getLatLng().toString() + ", " + getShortAddress();
	}
}
