package com.gatech.spark.activity;

import java.io.IOException;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.widget.Toast;

import com.gatech.spark.R;
import com.gatech.spark.database.SqliteHelper;
import com.gatech.spark.fragment.SparkMapFragment;
import com.gatech.spark.fragment.SubscriptionsFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends Activity
{

	private static final String TAG = "spark.MainActivity";
    private SqliteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Notice that setContentView() is not used, because we use the root
        // android.R.id.content as the container for each fragment
        // setContentView(R.layout.activity_main);

        dbHelper = SqliteHelper.getDbHelper( getApplicationContext() );
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
		actionBar.addTab(
             actionBar.newTab()
                      .setText("Map")
                      .setTabListener(new TabListener<SparkMapFragment>(this,
                                                                        "map",
                                                                        SparkMapFragment.class)));
		actionBar.addTab(
	         actionBar.newTab()
	                  .setText("Subscriptions")
	                  .setTabListener(new TabListener<SubscriptionsFragment>(this,
	                                                                        "subscriptions",
	                                                                        SubscriptionsFragment.class)));
        actionBar.setSelectedNavigationItem(1);
        
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }

    private void doSearch(String query) {
    	Toast.makeText(this, "Searching for " + query, Toast.LENGTH_SHORT).show();
    	Geocoder geocoder = new Geocoder(this);
    	List<Address> addresses = null;

    	try {
	        addresses = geocoder.getFromLocationName(query,5);
        } catch (IOException e) {
	        e.printStackTrace();
        }

        if(addresses != null && addresses.size() > 0) {
        	Address addr = addresses.get(0);
        	LatLng loc = new LatLng(addr.getLatitude(), addr.getLongitude());
        	Toast.makeText(this, "add marker to " + addr + "(" + loc + ")", Toast.LENGTH_LONG).show();
        } else {
        	Toast.makeText(this, "invalid location", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager =
		        (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView =
		        (SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setSearchableInfo(
		          searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

	public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
		private final Activity activity;
		private final String tag;
		private final Class<T> cl;
		private Fragment fragment;

		public TabListener(Activity activity, String tag, Class<T> cl) {
            this.activity = activity;
            this.tag = tag;
            this.cl = cl;

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state.  If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            fragment = activity.getFragmentManager().findFragmentByTag(tag);
            if (fragment != null && !fragment.isDetached()) {
                FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
                ft.detach(fragment);
                ft.commit();
            }
		}

		@Override
		public void onTabUnselected(ActionBar.Tab tab,
		                            FragmentTransaction fragmentTransaction) {
			if (fragment != null) {
				fragmentTransaction.detach(fragment);
			}
		}

		@Override
		public void onTabSelected(ActionBar.Tab tab,
		                          FragmentTransaction fragmentTransaction) {
            if (fragment == null) {
                fragment = Fragment.instantiate(activity, cl.getName(), null);
                fragmentTransaction.add(android.R.id.content, fragment, tag);
            } else {
            	fragmentTransaction.attach(fragment);
            }
		}

		@Override
		public void onTabReselected(ActionBar.Tab tab,
		                            FragmentTransaction fragmentTransaction) {
		}
	}
}
