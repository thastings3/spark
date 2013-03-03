package com.gatech.spark;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity
{
    private GoogleMap map;
    private Marker marker;
    private static final LatLng GT = new LatLng(33.78102,-84.400363);

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
            // Check if we were successful in obtaining the map.
            if (map != null) {
            	// Something has gone wrong.
                assert(false);
            }
         // The Map is verified. It is now safe to manipulate the map.
        }
    }

}
