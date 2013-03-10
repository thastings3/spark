package com.gatech.spark.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gatech.spark.R;
import com.gatech.spark.helper.MarkerPlacer;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class WhatsHotFragment extends SupportMapFragment {
	private static final String TAG = "spark.WhatsHotFragment";
	protected int layout = R.layout.fragment_whats_hot;
	protected int id = R.id.whats_hot;
	private GoogleMap map;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(layout, container, false);
		getMapIfNeeded();
		setupMap();
		return view;
	}
	
	public void getMapIfNeeded() {
		if (map == null) {
			Log.d(TAG, "Getting map for first time");
			map = getMapFragment().getMap();
		}
	}

	protected SupportMapFragment getMapFragment() {
		FragmentActivity act = getActivity();
		FragmentManager mgr = act.getSupportFragmentManager();
		return (SupportMapFragment) mgr.findFragmentById(id);
//		return (SupportMapFragment) getActivity()
//				.getSupportFragmentManager().findFragmentById(id);
	}

    public void setupMap() {
        MarkerPlacer.addWhatsHotMarker(map, SparkMapFragment.GT);
    }
}
