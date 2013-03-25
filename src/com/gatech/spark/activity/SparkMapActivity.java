package com.gatech.spark.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.gatech.spark.R;
import com.gatech.spark.helper.MarkerPlacer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 3/24/13
 * Time: 9:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class SparkMapActivity extends Activity {


    private static final String TAG = "SparkMapFragment";
    protected static final LatLng GT = new LatLng(33.78102, -84.400363);
    private GoogleMap map;
    private LatLng currentLoc;
    private boolean locationHasBeenSet = false;
    protected int layout = R.layout.spark_map_activity;
    protected int id = R.id.spark_map;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);
        setupMapIfNeeded();


        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

    }

    public void setupMapIfNeeded() {
        if (map == null) {
            Log.d(TAG, "Getting map for first time");
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.spark_map))
                    .getMap();
            setupMap();
            setupLocationListener();
        }
    }


    public void setupMap() {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng pos) {
                MarkerPlacer.addDraggableMarker(map, pos);
            }
        });

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker arg0) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng latlng = marker.getPosition();
                Toast.makeText(SparkMapActivity.this,
                        "new position is " + latlng, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
            }
        });
    }

    private void setupLocationListener() {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
            MarkerPlacer.addCurrentLocationMarker(map, currentLoc);
            locationHasBeenSet = true;
        }
    }

    private void setLocationView(LatLng loc) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 12));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.whats_hot:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this,SparkMapActivity.class );
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.my_map:
                // app icon in action bar clicked; go home
                Intent intent2 = new Intent(this, SparkMapActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                return true;
            case R.id.subscriptions:
                // app icon in action bar clicked; go home
                Intent intent3 = new Intent(this, SparkMapActivity.class);
                intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}