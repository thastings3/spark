package com.gatech.spark.fragment;

import android.support.v4.app.Fragment;
import com.google.android.gms.maps.GoogleMap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gatech.spark.R;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * To change this template use File | Settings | File Templates.
 */
public class WhatsHotfragment extends SupportMapFragment {


    private GoogleMap map;
    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_whats_hot, container, false);
        //((TextView) rootView.findViewById(android.R.id.text1)).setText( "Section" + "test");
        mapView = (MapView)rootView.findViewById(R.id.mapView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)     {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {

            //map = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mapView)).getMap();
            // The Map is verified. It is now safe to manipulate the map.
        }
    }
}
