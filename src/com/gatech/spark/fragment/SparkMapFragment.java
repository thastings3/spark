package com.gatech.spark.fragment;

import com.gatech.spark.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SparkMapFragment extends SupportMapFragment {

	private GoogleMap map;
	private Marker marker;
	private static final LatLng GT = new LatLng(33.78102, -84.400363);
	private static final String TAG = "SparkMapFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.big_map, container, false);
		setupMapIfNeeded();
		return view;
	}

	public void setupMapIfNeeded() {
		if (map == null) {
			Log.d(TAG, "Getting map for first time");
			map = ((SupportMapFragment) getActivity()
					.getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			configMap();
		}
	}

	public void configMap() {
		map.moveCamera(CameraUpdateFactory.newLatLng(GT));
		map.animateCamera(CameraUpdateFactory.zoomTo(12));
		marker = addMarker(GT);
		marker.setDraggable(true);

		map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng pos) {
				addMarker(pos);
			}
		});

		map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

			@Override
			public void onMarkerDragStart(Marker arg0) {
			}

			@Override
			public void onMarkerDragEnd(Marker marker) {
				LatLng latlng = marker.getPosition();
				Toast.makeText(SparkMapFragment.this.getActivity(),
						"new position is " + latlng, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onMarkerDrag(Marker arg0) {
			}
		});
	}

	private Marker addMarker(LatLng position) {
		Marker marker = map.addMarker(new MarkerOptions().position(position)
				.title("Hello world").snippet("Load: 14%").draggable(true));
		return marker;
	}
}
