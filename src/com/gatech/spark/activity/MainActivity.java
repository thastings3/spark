package com.gatech.spark.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.widget.Toast;

import com.gatech.spark.R;
import com.gatech.spark.database.SqliteHelper;
import com.gatech.spark.fragment.SparkMapFragment;
import com.gatech.spark.fragment.SubscriptionsFragment;
import com.gatech.spark.helper.SaxParser;
import com.gatech.spark.model.Subscription;
import com.gatech.spark.overlay.SubscriptionsOverlayItem;

public class MainActivity extends Activity
{

	private static final String TAG = "spark.MainActivity";
	private static final String MAP_FRAGMENT_TAG = "map";
	private static final int MAP_FRAGMENT_INDEX = 0;
	private static final String SUBSCRIPTIONS_FRAGMENT_TAG = "subscriptions";
	private static final int SUBSCRIPTIONS_FRAGMENT_INDEX = 1;
    private SqliteHelper dbHelper;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Notice that setContentView() is not used, because we use the root
        // android.R.id.content as the container for each fragment
        // setContentView(R.layout.activity_main);

        dbHelper = SqliteHelper.getDbHelper( getApplicationContext() );
        actionBar = getActionBar();

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
                                                                        MAP_FRAGMENT_TAG,
                                                                        SparkMapFragment.class)),
             MAP_FRAGMENT_INDEX);
		actionBar.addTab(
	         actionBar.newTab()
	                  .setText("Subscriptions")
	                  .setTabListener(new TabListener<SubscriptionsFragment>(this,
	                                                                         SUBSCRIPTIONS_FRAGMENT_TAG,
	                                                                         SubscriptionsFragment.class)),
	         SUBSCRIPTIONS_FRAGMENT_INDEX);
        actionBar.setSelectedNavigationItem(SUBSCRIPTIONS_FRAGMENT_INDEX);

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
    	loadSparkMap();
    	SparkMapFragment sparkMap = (SparkMapFragment) getFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG);
    	sparkMap.searchForPlaces(query);
    }

    private void loadSparkMap() {
	    actionBar.setSelectedNavigationItem(MAP_FRAGMENT_INDEX);
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

    /**
     *
     * @param subscription
     * @param changeTab
     */
    public void searchForParkingLocations(Subscription subscription, boolean changeTab)
    {
        if(changeTab)
        {
            actionBar.setSelectedNavigationItem(MAP_FRAGMENT_INDEX);
        }
        SparkMapFragment sparkMap = (SparkMapFragment) getFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG);
        sparkMap.searchForParkingLocations(subscription);
    }
}
