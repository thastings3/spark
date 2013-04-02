package com.gatech.spark.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.gatech.spark.R;
import com.gatech.spark.database.SqliteHelper;
import com.gatech.spark.fragment.SparkMapFragment;
import com.gatech.spark.fragment.SubscriptionsFragment;

public class MainActivity extends Activity
{

	private static final String TAG = "spark.MainActivity";
    private SqliteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
