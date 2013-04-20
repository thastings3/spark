package com.gatech.spark.overlay;

import java.util.Collection;

import android.view.LayoutInflater;
import android.view.View;

import com.gatech.spark.fragment.SparkMapFragment;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.Marker;

public class OverlayInfoWindowAdapter implements InfoWindowAdapter,
OnInfoWindowClickListener {

	private SparkMapFragment fragment;

	public OverlayInfoWindowAdapter(SparkMapFragment fragment) {
		this.fragment = fragment;
	}

	private LayoutInflater getInflater() {
		return fragment.getActivity().getLayoutInflater();
	}

	private Collection<MapOverlay> getOverlays() {
		return fragment.getOverlays();
	}

	@Override
	public View getInfoContents(Marker marker) {
		LayoutInflater inflater = getInflater();
		for (MapOverlay overlay : getOverlays()) {
			View view = overlay.getInfoContents(inflater, marker);
			if (view != null)
				return view;
		}
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		LayoutInflater inflater = getInflater();
		for (MapOverlay overlay : getOverlays()) {
			View view = overlay.getInfoWindow(inflater, marker);
			if (view != null)
				return view;
		}
		return null;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		for (MapOverlay overlay : getOverlays()) {
			if (overlay.onInfoWindowClick(marker))
				return;
		}
	}
}
