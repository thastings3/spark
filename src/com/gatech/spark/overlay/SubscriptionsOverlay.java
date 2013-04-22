package com.gatech.spark.overlay;

import java.util.ArrayList;
import java.util.Collection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gatech.spark.R;
import com.gatech.spark.activity.MainActivity;
import com.gatech.spark.activity.PlaceExpandedActivity;
import com.gatech.spark.database.SqliteHelper;
import com.gatech.spark.fragment.SparkMapFragment;
import com.gatech.spark.helper.HandlerReturnObject;
import com.gatech.spark.model.Subscription;
import com.google.android.gms.maps.model.Marker;

public class SubscriptionsOverlay extends MapOverlay {

	private static final String TAG = "spark.SubscriptionsOverlay";
	private static final String PREFS_KEY_VISIBILITY =
		"SubscriptionsOverlay.Visibility";
	private static final int MENU_ITEM_ID = R.id.subscriptions;

	private Collection<SubscriptionsOverlayItem> subscriptionsList;
	private boolean isVisible;

	public SubscriptionsOverlay(SparkMapFragment fragment) {
		super(fragment);
		subscriptionsList = new ArrayList<SubscriptionsOverlayItem>();
	}

	@Override
	public void updateMenuItem() {
		if (menuItem != null) {
			int iconId = isVisible() ?
				R.drawable.ic_action_favorites_enabled :
					R.drawable.ic_action_favorites_disabled;
			menuItem.setIcon(iconId);
		}
	}

	@Override
	public void populate() {
		clear();

		SqliteHelper dbHelper = SqliteHelper.getDbHelper(getActivity().getApplicationContext());
		HandlerReturnObject<ArrayList<Subscription>> handler = dbHelper.getSubscriptions();
		for (Subscription sub : handler.getObject()) {
			Log.d(TAG, "populating subscription " + sub.getName());
			subscriptionsList.add(new SubscriptionsOverlayItem(sub));
        }
	}

	@Override
	public void clear() {
		for (OverlayItem item : subscriptionsList) {
			item.removeMarker();
			item.hide();
		}
		subscriptionsList.clear();
	}

	@Override
	public boolean isVisible() {
		return isVisible;
	}

	@Override
	public void setVisibility(boolean visibility) {
		this.isVisible = visibility;
		if (visibility) {
			show();
		} else {
			hide();
		}
		updateMenuItem();
	}

	@Override
	protected void show() {
		populate();
		for (OverlayItem item : subscriptionsList) {
			item.addMarker(getMap());
			item.show();
		}
	}

	@Override
	protected void hide() {
		for (OverlayItem item : subscriptionsList) {
			item.hide();
		}
	}

	@Override
	public void save(SharedPreferences settings) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(PREFS_KEY_VISIBILITY, isVisible());
		editor.commit();
	}

	@Override
	public void load(SharedPreferences settings) {
		boolean visibility = settings.getBoolean(PREFS_KEY_VISIBILITY, false);
		setVisibility(visibility);
	}

	@Override
	public boolean isMember(Marker marker) {
		return new SubscriptionsOverlayItem().isMember(marker);
	}

	@Override
	public OverlayItem getOverlayItem(Marker marker) {
		if (isMember(marker)) {
			for (OverlayItem item : subscriptionsList) {
				if (item.hasMarker(marker))
					return item;
			}
		}
		return null;
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO
		return false;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu) {
		setMenuItem(menu.findItem(MENU_ITEM_ID));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == MENU_ITEM_ID) {
			toggle();
			return true;
		}
		return false;
	}

	@Override
	public View getInfoContents(LayoutInflater inflater, Marker marker) {
		if (!isMember(marker))
			return null;
		SubscriptionsOverlayItem item = (SubscriptionsOverlayItem) getOverlayItem(marker);
		View popup = inflater.inflate(R.layout.info_window_subscription, null);
		TextView tv = (TextView)popup.findViewById(R.id.title);
		tv.setText(item.getSubscription().getName());
		tv = (TextView)popup.findViewById(R.id.snippet);
		tv.setText(item.createSnippet());

		return(popup);
	}

    @Override
    public boolean onInfoWindowClick(Marker marker) {
        if (!isMember(marker))
            return false;
        final SubscriptionsOverlayItem item = (SubscriptionsOverlayItem) getOverlayItem(marker);
        new AlertDialog.Builder( getActivity() ).setTitle( "Find Parking?" ).setMessage( "Would you like to navigate to this location?" )
                .setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialog, int which )
                    {
                        ((MainActivity)getActivity()).searchForParkingLocations(item.getSubscription(), false);
                    }
                } ).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
        }).create().show();
        //Intent intent = new Intent(getActivity(), PlaceExpandedActivity.class);
        //intent.putExtra(PlaceExpandedActivity.PLACE, item.getPlace().getReference());
        //getActivity().startActivity(intent);

        return true;
    }
}
