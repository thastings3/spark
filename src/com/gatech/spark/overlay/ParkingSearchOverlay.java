package com.gatech.spark.overlay;

import java.util.ArrayList;
import java.util.Collection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.gatech.spark.activity.LotExpandedActivity;
import com.gatech.spark.activity.MainActivity;
import com.gatech.spark.activity.PlaceExpandedActivity;
import com.gatech.spark.fragment.SparkMapFragment;
import com.gatech.spark.helper.CommonHelper;
import com.gatech.spark.helper.HandlerReturnObject;
import com.gatech.spark.helper.HttpRestClient;
import com.gatech.spark.helper.SaxParser;
import com.gatech.spark.model.SparkParkingLot;
import com.gatech.spark.model.Subscription;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ParkingSearchOverlay extends MapOverlay {

	private static final String TAG = "spark.ParkingSearchOverlay";

	private ParkingLotSubscriptionOverlayItem subscription;
	private Collection<ParkingLotOverlayItem> parkingLots;
	private boolean isVisible = false;

	public ParkingSearchOverlay(SparkMapFragment fragment) {
		super(fragment);
		parkingLots = new ArrayList<ParkingLotOverlayItem>();
		subscription = null;
		

	}

	public void setSubscription(Subscription sub) {
		this.subscription = new ParkingLotSubscriptionOverlayItem(sub);
	}

	@Override
	public boolean isVisible() {
		return isVisible;
	}

	@Override
	public void updateMenuItem() {
		return;
	}

	@Override
	public void populate() {
		if (subscription == null)
			return;
		Log.d(TAG, "poplulate: " + subscription.getSubscription().getName());
		clear();
		search();
	}

	private void search() {
		LotSearcher searcher = new LotSearcher(this);
		searcher.search(subscription.getLatLng());
	}

	public void addSearchResult(SparkParkingLot lot) {
		this.parkingLots.add(new ParkingLotOverlayItem(lot));
	}

	public void onSearchCompleted() {
		show();
	}

	@Override
	public void clear() {
		for (OverlayItem item : parkingLots) {
			item.removeMarker();
			item.hide();
		}
		parkingLots.clear();
		subscription.removeMarker();
		subscription.hide();
	}

	@Override
	public void setVisibility(boolean visibility) {
		isVisible = visibility;
		if (visibility)
			show();
		else
			hide();
	}

	@Override
	protected void show() {
		Log.d(TAG, "show");
		subscription.addMarker(getMap());
		subscription.show();
		for (OverlayItem item : parkingLots) {
			item.addMarker(getMap());
			item.show();
		}
		centerMapOnLatLng(subscription.getLatLng());
	}

	private void centerMapOnLatLng(LatLng center) {
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(center);
		getMap().animateCamera(cameraUpdate);
	}

	@Override
	protected void hide() {
		for (OverlayItem item : parkingLots) {
			item.hide();
		}

		if (subscription != null)
			subscription.hide();
	}

	@Override
	public void save(SharedPreferences settings) {
		// TODO Auto-generated method stub
	}

	@Override
	public void load(SharedPreferences settings) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isMember(Marker marker) {
		return subscription.isMember(marker) ||
		       new ParkingLotOverlayItem().isMember(marker);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu) {
		return;
	}

	@Override
	public OverlayItem getOverlayItem(Marker marker) {
		if (isMember(marker)) {
			for (OverlayItem item : parkingLots) {
				if (item.hasMarker(marker))
					return item;
			}
		}
		return null;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// just hide this view if any other overlay wants to show
		setVisibility(false);
		return false;
	}

	@Override
	public boolean onInfoWindowClick(Marker marker) {
		if (!isMember(marker))
			return false;
		ParkingLotOverlayItem item = (ParkingLotOverlayItem) getOverlayItem(marker);
		if (item == null) {
			Log.d(TAG, "item is null");
			return false;
		}
		if (item.getParkingLot() == null) {
			Log.d(TAG, "lot is null");
			return false;
		}
        Intent intent = new Intent(getActivity(), LotExpandedActivity.class );
        intent.putExtra(LotExpandedActivity.SUBSCRIPTION, item.getParkingLot().getParkingLotID());
        getActivity().startActivity(intent);
        return true;
	}

	/**
	 * Searcher for parking lots
	 */
	public class LotSearcher {
		private ParkingSearchOverlay overlay;
		private Collection<SparkParkingLot> lotList;

		public LotSearcher(ParkingSearchOverlay overlay) {
			this.overlay = overlay;
		}

		public void search(LatLng latLng) {
			if (latLng != null) {
				doSearch(latLng);
			} else {
				Log.d(TAG, "Query empty");
			}
		}

		private void doSearch(LatLng latLng) {
			HttpRestClient.getNearbyParkingLots(latLng.latitude,
			                                    latLng.longitude,
			                                    CommonHelper.convertMilesToMeters(2),
			                                    new LotResponseHandler(this));
		}

		public void setSearchResults(Collection<SparkParkingLot> lotList) {
			Log.d(TAG, "Setting search results");
			this.lotList = lotList;
		}

		private void onSearchCompleted() {
			Log.d(TAG, "Updating search results. Size of lotList=" + lotList.size());
			for (SparkParkingLot lot : lotList) {
				overlay.addSearchResult(lot);
			}
			overlay.onSearchCompleted();
		}

		private class LotResponseHandler extends AsyncHttpResponseHandler {
			ProgressDialog pDialog;
			LotSearcher searcher;

			public LotResponseHandler(LotSearcher searcher) {
				super();
				this.searcher = searcher;
			}

			@Override
			public void onStart() {
				pDialog = new ProgressDialog(getActivity());
				pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pDialog.setCancelable(false);
				pDialog.setTitle("Searching for parking...");
				pDialog.show();
			}

			@Override
			public void onSuccess(String s) {
				SaxParser parser = new SaxParser();
				HandlerReturnObject<ArrayList<SparkParkingLot>> handlerObject =
				    parser.parseParkingLotsXmlResponse(s);
				if (handlerObject.isValid())
				{
					searcher.setSearchResults(handlerObject.getObject());
					searcher.onSearchCompleted();
				}
				else
				{
					Log.d(TAG, "Failed to find parking locations");
					showFailureMessage();
				}
			}

			@Override
			public void onFailure(Throwable throwable, String s) {
				Log.d(TAG, "Failed to find results: " + s);
				showFailureMessage();
				super.onFailure(throwable, s);
			}

			@Override
			public void onFinish() {
				pDialog.dismiss();
			}
			
			private void showFailureMessage() {
				CommonHelper.showLongToast(getActivity(),
				                           "Failed to find parking locations. Please Try again later.");

			}
		}
	}

}
