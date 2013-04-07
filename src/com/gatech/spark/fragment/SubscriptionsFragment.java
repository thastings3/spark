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
import com.gatech.spark.database.SqliteHelper;

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
        list = (ListView)rootView.findViewById(R.id.listView);
        dbHelper = SqliteHelper.getDbHelper( getActivity().getApplicationContext() );
        list.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, new String[]{"Lot 1", "Lot 2", "Lot 3","Lot 4"} ));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(),LotExpandedActivity.class );
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
