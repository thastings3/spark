package com.gatech.spark.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.gatech.spark.R;
import com.gatech.spark.activity.LotExpandedActivity;
import com.gatech.spark.adapter.SubscriptionsListViewAdapter;
import com.gatech.spark.database.SqliteHelper;
import com.gatech.spark.model.Subscription;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * To change this template use File | Settings | File Templates.22o
 */
public class SubscriptionsFragment extends Fragment {


    ListView list;
    private SqliteHelper                dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subscriptions, container, false);
        //((TextView) rootView.findViewById(android.R.id.text1)).setText( "Section" + "test");
        dbHelper = SqliteHelper.getDbHelper( getActivity().getApplicationContext() );

        //TODO REMOVE TEST CODE
        if(dbHelper.getSubscriptions().getObject().size() == 0)
        {
            //FIXME insert in fake data here
            dbHelper.insertSubscription(new Subscription("Turner Field"    , 33.73928,-84.389379));
            dbHelper.insertSubscription(new Subscription("Atlantic Station", 33.792723,-84.396524));
            dbHelper.insertSubscription(new Subscription("Georgia Tech"    , 33.775905,-84.394679));

        }


        list = (ListView)rootView.findViewById(R.id.listView);

        list.setAdapter(new SubscriptionsListViewAdapter(getActivity(), dbHelper.getSubscriptions().getObject() ));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO where should we redirect??? OR DO WE just show a dialog to start navigation??
                Intent intent = new Intent(getActivity(),LotExpandedActivity.class );
                intent.putExtra(LotExpandedActivity.SUBSCRIPTION, (Subscription)adapterView.getSelectedItem() );
                startActivity(intent);
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
