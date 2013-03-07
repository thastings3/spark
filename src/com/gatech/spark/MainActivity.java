package com.gatech.spark;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity
{
    private GoogleMap map;
    private Marker marker;
    private static final LatLng GT = new LatLng(33.78102,-84.400363);
    private static final String TAG = "Spark.Map";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_map);
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
				Toast.makeText(MainActivity.this, "new position is " + latlng,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onMarkerDrag(Marker arg0) {
				// TODO Auto-generated method stub
			}
		});
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	setUpMapIfNeeded();
    }
    
    private Marker addMarker(LatLng position) {
        Marker marker = map.addMarker(new MarkerOptions()
        .position(position)
        .title("Hello world")
        .snippet("Load: 14%")
        .draggable(true));
        return marker;
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }
    
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            if (map == null) {
            	Log.d(TAG, "Map is not inflated properly.");
            }
            Log.d(TAG, "Map is verified.");
        }
    }

}
