package com.gatech.spark.fragment;

import com.gatech.spark.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SparkMapFragment extends SupportMapFragment {

	private static final String TAG = "SparkMapFragment";
	private GoogleMap map;
	private Marker marker;
	private static final LatLng GT = new LatLng(33.78102, -84.400363);
	private LatLng currentLoc;
	private boolean locationHasBeenSet = false;

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
			setupMap();
			setupLocationListener();
		}
	}

	public void setupMap() {
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
		return map.addMarker(new MarkerOptions().position(position).draggable(true));
	}

	private void setupLocationListener() {
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.
				updateCurrentLocation(location);
				setLocationViewIfNeeded();
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {}

			@Override
			public void onProviderEnabled(String provider) {}

			@Override
			public void onProviderDisabled(String provider) {}
		};

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}

	private void updateCurrentLocation(Location loc) {
		currentLoc = new LatLng(loc.getLatitude(), loc.getLongitude());
		setLocationViewIfNeeded();
	}

	private void setLocationViewIfNeeded() {
		if (map != null && !locationHasBeenSet) {
			setLocationView(currentLoc);
			addCurrentLocationMarker();
			locationHasBeenSet = true;
		}
	}

	private void setLocationView(LatLng loc) {
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 12));
	}

	private void addCurrentLocationMarker() {
		Marker marker = addMarker(currentLoc);
		marker.setTitle("Current Location");
		marker.setSnippet("a snippet");
	}
}
