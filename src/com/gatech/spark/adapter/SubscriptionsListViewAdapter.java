package com.gatech.spark.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.gatech.spark.R;
import com.gatech.spark.model.Subscription;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 3/9/13
 * Time: 7:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class SubscriptionsListViewAdapter extends ArrayAdapter<Subscription>
{
    private ArrayList<Subscription> mData;
    private Context context;

    public SubscriptionsListViewAdapter( Context context, int textViewResourceId, ArrayList<Subscription> subscriptions )
    {
        super( context, textViewResourceId );
        this.context = context;
        mData = subscriptions;
    }

    public SubscriptionsListViewAdapter( Context context, ArrayList<Subscription> subscriptions)
    {
        super( context, R.layout.subscriptions_list_row_layout, subscriptions );
        this.context = context;
        mData = subscriptions;
    }

    static class ViewHolder
    {
        protected TextView text;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        ViewHolder viewHolder;
        if ( convertView == null )
        {
            convertView = View.inflate( context, R.layout.subscriptions_list_row_layout, null );
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById( R.id.text1 );
            convertView.setTag( viewHolder );
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.text.setText( mData.get( position ).getName() );

        return convertView;
    }

    @Override
    public int getCount()
    {
        return mData.size();
    }

    @Override
    public Subscription getItem( int index )
    {
        return mData.get( index );
    }
}
