package com.gatech.spark.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.gatech.spark.R;
import com.gatech.spark.activity.PlaceExpandedActivity;
import com.gatech.spark.adapter.SubscriptionsListViewAdapter;
import com.gatech.spark.database.SqliteHelper;
import com.gatech.spark.helper.CommonHelper;
import com.gatech.spark.helper.HandlerReturnObject;
import com.gatech.spark.model.Subscription;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * To change this template use File | Settings | File Templates.22o
 */
public class SubscriptionsFragment extends Fragment {


    ListView list;
    private SqliteHelper                dbHelper;
    private SubscriptionsListViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subscriptions, container, false);
        dbHelper = SqliteHelper.getDbHelper( getActivity().getApplicationContext() );

        list = (ListView)rootView.findViewById(R.id.listView);
        adapter = new SubscriptionsListViewAdapter(getActivity(), dbHelper.getSubscriptions().getObject() );
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view,final int i, long l) {
				Intent intent = new Intent( getActivity(), PlaceExpandedActivity.class );
				String placeReference = ((Subscription)adapterView.getItemAtPosition(i)).getPlaceReference();
				intent.putExtra( PlaceExpandedActivity.PLACE,  placeReference );
				getActivity().startActivity( intent );
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view,final int i, long l) {

                new AlertDialog.Builder( SubscriptionsFragment.this.getActivity() ).setTitle( "Un-Subscribe" ).setMessage( "Would you like to un-subscribe from this location?" )
                        .setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick( DialogInterface dialog, int which )
                            {
                                HandlerReturnObject<Subscription> handlerObject = dbHelper.deleteSubscription((Subscription) adapterView.getItemAtPosition(i));
                                if(handlerObject.isValid())
                                {
                                    CommonHelper.showLongToast(SubscriptionsFragment.this.getActivity(), "Successfully un-subscribed");
                                    adapter = new SubscriptionsListViewAdapter(getActivity(), dbHelper.getSubscriptions().getObject() );
                                    list.setAdapter(adapter);
                                    list.invalidate();
                                }
                                else
                                {
                                    CommonHelper.showLongToast(SubscriptionsFragment.this.getActivity(), "Error un-subscribing.");
                                }
                            }
                        } ).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();

                return true;
            }
        });
        return rootView;
    }

	// Hide the search bar
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

    @Override
    public void onResume() {
        super.onResume();
        adapter = new SubscriptionsListViewAdapter(getActivity(), dbHelper.getSubscriptions().getObject() );
        list.setAdapter(adapter);
    }

    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().invalidateOptionsMenu();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		MenuItem search = menu.findItem(R.id.search);
		search.setVisible(false);
	}
}
