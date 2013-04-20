package com.gatech.spark.overlay;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.Marker;

public class OverlayInfoWindowAdapter implements InfoWindowAdapter,
OnInfoWindowClickListener {

	private LayoutInflater inflater;
	private List<MapOverlay> overlays;

	public OverlayInfoWindowAdapter(LayoutInflater inflater, List<MapOverlay> overlays) {
		this.inflater = inflater;
		this.overlays = overlays;
	}

	@Override
	public View getInfoContents(Marker marker) {
		for (MapOverlay overlay : overlays) {
			View view = overlay.getInfoContents(inflater, marker);
			if (view != null)
				return view;
		}
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		for (MapOverlay overlay : overlays) {
			View view = overlay.getInfoWindow(inflater, marker);
			if (view != null)
				return view;
		}
		return null;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		for (MapOverlay overlay : overlays) {
			if (overlay.onInfoWindowClick(marker))
				return;
		}
	}
}
