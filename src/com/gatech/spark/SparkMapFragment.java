package com.gatech.spark;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SparkMapFragment extends Fragment {

    private GoogleMap map;
    private Marker marker;
    private static final LatLng GT = new LatLng(33.78102,-84.400363);
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.big_map, container, false);
    }
    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpMapIfNeeded();

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
				// TODO Auto-generated method stub
			}

			@Override
			public void onMarkerDragEnd(Marker marker) {
				LatLng latlng = marker.getPosition();
				Toast.makeText(SparkMapFragment.this.getActivity(), "new position is " + latlng,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onMarkerDrag(Marker arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

    private Marker addMarker(LatLng position) {
        Marker marker = map.addMarker(new MarkerOptions()
        .position(position)
        .title("Hello world")
        .snippet("Load: 14%")
        .draggable(true));
        return marker;
    }
    
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            if (map != null) {
            	// Something has gone wrong.
                assert(false);
            }
         // The Map is verified. It is now safe to manipulate the map.
        }
    }

}
