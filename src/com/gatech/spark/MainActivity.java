package com.gatech.spark;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity
{
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_map);
        setUpMapIfNeeded();
        addMarker(33.78102,-84.400363);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	setUpMapIfNeeded();
    }
    
    private void addMarker(double lat, double lng) {
        map.addMarker(new MarkerOptions()
        .position(new LatLng(lat, lng))
        .title("Hello world"));
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
